package be.vinci.pae.business.ucc;

import be.vinci.pae.business.dto.FurnitureDTO;
import be.vinci.pae.business.dto.OptionDTO;
import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.business.pojos.FurnitureStatus;
import be.vinci.pae.exceptions.ConflictException;
import be.vinci.pae.exceptions.UnauthorizedException;
import be.vinci.pae.persistence.dal.ConnectionDalServices;
import be.vinci.pae.persistence.dao.FurnitureDAO;
import be.vinci.pae.persistence.dao.OptionDAO;
import be.vinci.pae.persistence.dao.UserDAO;
import jakarta.inject.Inject;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class OptionUCCImpl implements OptionUCC {

  @Inject
  private ConnectionDalServices dalServices;
  @Inject
  private OptionDAO optionDAO;
  @Inject
  private UserDAO userDAO;
  @Inject
  private FurnitureDAO furnitureDAO;


  /**
   * create an option.
   *
   * @param user        user.
   * @param furnitureId furnitureId.
   * @return OptionDTO.
   */
  @Override
  public OptionDTO introduceOption(UserDTO user, int furnitureId, int duration) {
    OptionDTO opt;
    try {
      dalServices.startTransaction();
      FurnitureDTO furnitureDTO = furnitureDAO.findById(furnitureId);
      if (!furnitureDTO.getStatus().equals(FurnitureStatus.AVAILABLE_FOR_SALE)) {
        throw new ConflictException("The resource isn't in a the 'available for sale' state");
      }
      furnitureDTO.setStatus(FurnitureStatus.UNDER_OPTION);
      furnitureDAO.updateStatusOnly(furnitureDTO);
      opt = optionDAO.introduceOption(user, furnitureId, duration);
      completeOptionDTO(opt);
      dalServices.commitTransaction();
    } catch (Throwable e) {
      dalServices.rollbackTransaction();
      throw e;
    }
    return opt;
  }

  /**
   * cancel an option.
   *
   * @param user     id of the user that want to cancel the option.
   * @param optionId id of the option to cancel.
   * @return an OptionDTO that represent the canceled one.
   */
  @Override
  public OptionDTO cancelOption(UserDTO user,
      int optionId) {
    OptionDTO opt;
    try {
      dalServices.startTransaction();
      opt = optionDAO.findById(optionId);
      if (opt.isCanceled()) {
        throw new ConflictException("The resource is already canceled");
      }
      if (opt.getUserId() != user.getId()) {
        throw new UnauthorizedException("not allowed to cancel the option");
      }
      FurnitureDTO furnitureDTO = furnitureDAO.findById(opt.getFurnitureId());
      if (!furnitureDTO.getStatus().equals(FurnitureStatus.UNDER_OPTION)) {
        throw new ConflictException("The resource is not under option");
      }
      furnitureDTO.setStatus(FurnitureStatus.AVAILABLE_FOR_SALE);
      furnitureDAO.updateStatusOnly(furnitureDTO);

      optionDAO.cancelOption(optionId);
      opt = optionDAO.findById(optionId);
      completeOptionDTO(opt);
      dalServices.commitTransaction();
    } catch (Throwable e) {
      dalServices.rollbackTransaction();
      throw e;
    }
    return opt;
  }


  /**
   * list all the options.
   *
   * @return list of all the Options.
   */
  @Override
  public List<OptionDTO> listOption() {
    List<OptionDTO> dtos;
    try {
      dalServices.startTransaction();
      dtos = optionDAO.findAll();
      for (OptionDTO optionDTO : dtos) {
        completeOptionDTO(optionDTO);
      }
      dalServices.commitTransaction();
    } catch (Throwable e) {
      dalServices.rollbackTransaction();
      throw e;
    }
    return dtos;
  }

  /**
   * list all active options of the current user.
   *
   * @param currentUser user.
   * @return list of all currentUser's option.
   */
  @Override
  public List<OptionDTO> myOptions(UserDTO currentUser) {
    List<OptionDTO> dtos;
    try {
      dalServices.startTransaction();
      dtos = optionDAO.findByUserId(currentUser.getId()).stream()
          .filter((o) -> !o.isCanceled())
          .collect(Collectors.toList());
      for (OptionDTO optionDTO : dtos) {
        completeOptionDTO(optionDTO);
      }
      dalServices.commitTransaction();
    } catch (Throwable e) {
      dalServices.rollbackTransaction();
      throw e;
    }
    return dtos;
  }

  /**
   * cancels expired options.
   */
  @Override
  public void updateExpiredOptions() {
    try {
      dalServices.startTransaction();
      Date today = new Date();
      int oneDayInMs = 86400000;

      List<OptionDTO> optionList = optionDAO.findAll();
      optionList = optionList.stream()
          .filter((o) -> !o.isCanceled())
          .collect(Collectors.toList());

      for (OptionDTO option : optionList) {
        String[] dateTable = option.getDateOption().split("-");
        LocalDate optionLocalDate = LocalDate
            .of(Integer.parseInt(dateTable[0]),
                Integer.parseInt(dateTable[1]),
                Integer.parseInt(dateTable[2]));
        Date optionDate = Date
            .from(optionLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        int optionDelay = (int) Math.floor(
            (optionDate.getTime() / oneDayInMs)
                + option.getDuration()
                - (today.getTime() / oneDayInMs));

        if (optionDelay < 0) { //expired
          FurnitureDTO furnitureDTO = furnitureDAO.findById(option.getFurnitureId());
          furnitureDTO.setStatus(FurnitureStatus.AVAILABLE_FOR_SALE);
          furnitureDAO.updateStatusOnly(furnitureDTO);
          optionDAO.cancelOption(option.getOptionId());
        }
      }
      dalServices.commitTransaction();
    } catch (Throwable e) {
      dalServices.rollbackTransaction();
      throw e;
    }
  }

  /**
   * Completes the OptionDTO given as an argument with it's references in the db.
   *
   * @param dto : the FurnitureDTO to complete
   */
  private void completeOptionDTO(OptionDTO dto) {
    if (dto.getUserId() != null) {
      dto.setUser(userDAO.findById(dto.getUserId()));
    }
  }
}
