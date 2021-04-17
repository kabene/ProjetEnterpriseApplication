package be.vinci.pae.business.ucc;

import be.vinci.pae.business.dto.FurnitureDTO;
import be.vinci.pae.business.dto.OptionDTO;
import be.vinci.pae.business.dto.PhotoDTO;
import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.business.pojos.Status;
import be.vinci.pae.exceptions.ConflictException;
import be.vinci.pae.persistence.dal.ConnectionDalServices;
import be.vinci.pae.persistence.dao.FurnitureDAO;
import be.vinci.pae.persistence.dao.FurnitureTypeDAO;
import be.vinci.pae.persistence.dao.OptionDAO;
import be.vinci.pae.persistence.dao.PhotoDAO;
import be.vinci.pae.persistence.dao.UserDAO;
import jakarta.inject.Inject;
import java.util.List;

public class FurnitureUCCImpl implements FurnitureUCC {

  @Inject
  private FurnitureDAO furnitureDAO;
  @Inject
  private UserDAO userDAO;
  @Inject
  private PhotoDAO photoDAO;
  @Inject
  private FurnitureTypeDAO furnitureTypeDAO;
  @Inject
  private OptionDAO optionDAO;
  @Inject
  private ConnectionDalServices dalServices;


  @Override
  public FurnitureDTO getOne(int id) {
    FurnitureDTO res;

    try {
      dalServices.startTransaction();
      res = furnitureDAO.findById(id);
      completeFurnitureDTO(res);
      dalServices.commitTransaction();
    } catch (Throwable e) {
      dalServices.rollbackTransaction();
      throw e;
    }
    return res;
  }

  @Override
  public List<FurnitureDTO> getAll() {
    List<FurnitureDTO> dtos;
    try {
      dalServices.startTransaction();
      dtos = furnitureDAO.findAll();
      for (FurnitureDTO dto : dtos) {
        completeFurnitureDTO(dto);
      }
      dalServices.commitTransaction();
    } catch (Throwable e) {
      dalServices.rollbackTransaction();
      throw e;
    }
    return dtos;
  }

  @Override
  public FurnitureDTO toRestoration(int furnitureId) {
    FurnitureDTO furnitureDTO;
    try {
      dalServices.startTransaction();
      furnitureDTO = furnitureDAO.findById(furnitureId);
      if (!furnitureDTO.getStatus().equals(Status.ACCEPTED)) {
        throw new ConflictException(
            "The resource cannot change from its current state to the 'in_restoration' state");
      }
      furnitureDTO.setStatus(Status.IN_RESTORATION);
      furnitureDTO = furnitureDAO.updateStatusOnly(furnitureDTO);
      completeFurnitureDTO(furnitureDTO);
      dalServices.commitTransaction();
    } catch (Throwable e) {
      dalServices.rollbackTransaction();
      throw e;
    }
    return furnitureDTO;
  }

  @Override
  public FurnitureDTO toAvailable(int furnitureId, double sellingPrice) {
    FurnitureDTO furnitureDTO;
    try {
      dalServices.startTransaction();
      furnitureDTO = furnitureDAO.findById(furnitureId);
      if (!furnitureDTO.getStatus().equals(Status.ACCEPTED) && !furnitureDTO.getStatus()
          .equals(Status.IN_RESTORATION)) {
        throw new ConflictException(
            "The resource cannot change from its current state to the 'available_for_sale' state");
      }
      furnitureDTO.setStatus(Status.AVAILABLE_FOR_SALE);
      furnitureDTO.setSellingPrice(sellingPrice);
      furnitureDTO = furnitureDAO.updateToAvailable(furnitureDTO);
      completeFurnitureDTO(furnitureDTO);
      dalServices.commitTransaction();
    } catch (Throwable e) {
      dalServices.rollbackTransaction();
      throw e;
    }
    return furnitureDTO;
  }

  @Override
  public FurnitureDTO withdraw(int furnitureId) {
    FurnitureDTO furnitureDTO;
    try {
      dalServices.startTransaction();
      furnitureDTO = furnitureDAO.findById(furnitureId);
      if (!furnitureDTO.getStatus().equals(Status.AVAILABLE_FOR_SALE) && !furnitureDTO.getStatus()
          .equals(Status.IN_RESTORATION)) {
        throw new ConflictException(
            "The resource isn't in a withdrawable state");
      }
      furnitureDTO.setStatus(Status.WITHDRAWN);
      furnitureDTO = furnitureDAO.updateToWithdrawn(furnitureDTO);
      completeFurnitureDTO(furnitureDTO);
      dalServices.commitTransaction();
    } catch (Throwable e) {
      dalServices.rollbackTransaction();
      throw e;
    }
    return furnitureDTO;
  }

  /**
   * Completes the FurnitureDTO given as an argument with it's references in the db.
   *
   * @param dto : the FurnitureDTO to complete
   */
  private void completeFurnitureDTO(FurnitureDTO dto) {
    if (dto.getBuyerId() != null) {
      UserDTO u = userDAO.findById(dto.getBuyerId());
      dto.setBuyer(u);
    }
    if (dto.getSellerId() != null) {
      UserDTO u = userDAO.findById(dto.getSellerId());
      dto.setSeller(u);
    }
    if (dto.getFavouritePhotoId() != null) {
      PhotoDTO favPhoto = photoDAO.getPhotoById(dto.getFavouritePhotoId());
      dto.setFavouritePhoto(favPhoto);
    }
    if (dto.getStatus().equals(Status.UNDER_OPTION)) {
      OptionDTO opt = optionDAO.findByFurnitureId(dto.getFurnitureId());
      opt.setUser(userDAO.findById(opt.getUserId()));
      dto.setOption(opt);
    }
    List<PhotoDTO> photos = photoDAO.findAllByFurnitureId(dto.getFurnitureId());
    dto.setPhotos(photos);
    String type = furnitureTypeDAO.findById(dto.getTypeId());
    dto.setType(type);
  }
}
