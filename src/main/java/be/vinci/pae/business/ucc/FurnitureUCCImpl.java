package be.vinci.pae.business.ucc;

import be.vinci.pae.business.dto.FurnitureDTO;
import be.vinci.pae.business.dto.PhotoDTO;
import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.exceptions.ConflictException;
import be.vinci.pae.persistence.dal.ConnectionDalServices;
import be.vinci.pae.persistence.dao.FurnitureDAO;
import be.vinci.pae.persistence.dao.FurnitureTypeDAO;
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
  private ConnectionDalServices dalServices;

  @Override
  public FurnitureDTO getOne(int id) {
    FurnitureDTO res;
    dalServices.startTransaction();
    try {
      res = furnitureDAO.findById(id);
      completeFurnitureDTO(res);
      dalServices.commitTransaction();
    } catch (Exception e) {
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
    } catch (Exception e) {
      dalServices.rollbackTransaction();
      throw e;
    }
    return dtos;
  }

  @Override
  public FurnitureDTO toRestoration(int furnitureId) {
    FurnitureDTO furnitureDTO;
    dalServices.startTransaction();
    try {
      furnitureDTO = furnitureDAO.findById(furnitureId);
      if (!furnitureDTO.getCondition().equals("accepted")) {
        throw new ConflictException(
            "The resource cannot change from its current state to the 'in_restoration' state");
      }
      furnitureDTO.setCondition("in_restoration");
      furnitureDTO = furnitureDAO.updateToRestoration(furnitureDTO);
      completeFurnitureDTO(furnitureDTO);
      dalServices.commitTransaction();
    } catch (Exception e) {
      dalServices.rollbackTransaction();
      throw e;
    }
    return furnitureDTO;
  }

  @Override
  public FurnitureDTO toAvailable(int furnitureId, double sellingPrice) {
    FurnitureDTO furnitureDTO;
    dalServices.startTransaction();
    try {
      furnitureDTO = furnitureDAO.findById(furnitureId);
      if (!furnitureDTO.getCondition().equals("accepted") && !furnitureDTO.getCondition()
          .equals("in_restoration")) {
        throw new ConflictException(
            "The resource cannot change from its current state to the 'available_for_sale' state");
      }
      furnitureDTO.setCondition("available_for_sale");
      furnitureDTO.setSellingPrice(sellingPrice);
      furnitureDTO = furnitureDAO.updateToAvailable(furnitureDTO);
      completeFurnitureDTO(furnitureDTO);
      dalServices.commitTransaction();
    } catch (Exception e) {
      dalServices.rollbackTransaction();
      throw e;
    }
    return furnitureDTO;
  }

  @Override
  public FurnitureDTO withdraw(int furnitureId) {
    FurnitureDTO furnitureDTO;
    dalServices.startTransaction();
    try {
      furnitureDTO = furnitureDAO.findById(furnitureId);
      if (!furnitureDTO.getCondition().equals("available_for_sale") && !furnitureDTO.getCondition()
          .equals("in_restoration")) {
        throw new ConflictException(
            "The resource isn't in a withdrawable state");
      }
      furnitureDTO.setCondition("withdrawn");
      furnitureDTO = furnitureDAO.updateToWithdrawn(furnitureDTO);
      completeFurnitureDTO(furnitureDTO);
      dalServices.commitTransaction();
    } catch (Exception e) {
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
    if (dto.getBuyerId() != null && dto.getBuyerId() != 0) {
      UserDTO u = userDAO.findById(dto.getBuyerId());
      dto.setBuyer(u);
    }
    if (dto.getSellerId() != null && dto.getSellerId() != 0) {
      UserDTO u = userDAO.findById(dto.getSellerId());
      dto.setSeller(u);
    }
    if (dto.getFavouritePhotoId() != null && dto.getFavouritePhotoId() != 0) {
      PhotoDTO favPhoto = photoDAO.getPhotoById(dto.getFavouritePhotoId());
      dto.setFavouritePhoto(favPhoto);
    }
    List<PhotoDTO> photos = photoDAO.getPhotosByFurnitureId(dto.getFurnitureId());
    dto.setPhotos(photos);
    String type = furnitureTypeDAO.findById(dto.getTypeId());
    dto.setType(type);
  }
}
