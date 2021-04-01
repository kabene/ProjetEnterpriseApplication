package be.vinci.pae.business.ucc;

import be.vinci.pae.business.dto.FurnitureDTO;
import be.vinci.pae.business.dto.OptionDTO;
import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.exceptions.ConflictException;
import be.vinci.pae.exceptions.UnauthorizedException;
import be.vinci.pae.persistence.dal.ConnectionDalServices;
import be.vinci.pae.persistence.dao.FurnitureDAO;
import be.vinci.pae.persistence.dao.OptionDAO;
import jakarta.inject.Inject;
import java.util.List;

public class OptionUCCImpl implements OptionUCC {

  @Inject
  private ConnectionDalServices dalServices;
  @Inject
  private OptionDAO optionDAO;
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
      if (!furnitureDTO.getCondition().equals("available_for_sale")) {
        throw new ConflictException("The resource isn't in a the 'available for sale' state");
      }
      furnitureDTO.setCondition("under_option");
      furnitureDAO.updateConditionOnly(furnitureDTO);
      opt = optionDAO.introduceOption(user, furnitureId, duration);
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
      opt = optionDAO.getOption(optionId);
      if (opt.isCanceled()) {
        throw new ConflictException("The resource is already canceled");
      }
      if (opt.getUserId() != user.getId()) {
        throw new UnauthorizedException("not allowed to cancel the option");
      }
      FurnitureDTO furnitureDTO = furnitureDAO.findById(opt.getFurnitureId());
      if (!furnitureDTO.getCondition().equals("under_option")) {
        throw new ConflictException("The resource is not under option");
      }
      furnitureDTO.setCondition("available_for_sale");
      furnitureDAO.updateConditionOnly(furnitureDTO);

      optionDAO.cancelOption(optionId);
      opt = optionDAO.getOption(optionId);
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
    dalServices.startTransaction();
    try {
      dtos = optionDAO.findAll();
      dalServices.commitTransaction();
    } catch (Throwable e) {
      dalServices.rollbackTransaction();
      throw e;
    }
    return dtos;
  }

}
