package be.vinci.pae.business.ucc;

import be.vinci.pae.business.dto.FurnitureDTO;
import be.vinci.pae.business.dto.OptionDTO;
import be.vinci.pae.business.dto.PhotoDTO;
import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.business.pojos.FurnitureStatus;
import be.vinci.pae.exceptions.ConflictException;
import be.vinci.pae.persistence.dal.ConnectionDalServices;
import be.vinci.pae.persistence.dao.FurnitureDAO;
import be.vinci.pae.persistence.dao.UserDAO;
import be.vinci.pae.persistence.dao.PhotoDAO;
import be.vinci.pae.persistence.dao.FurnitureTypeDAO;
import be.vinci.pae.persistence.dao.OptionDAO;
import be.vinci.pae.persistence.dao.RequestForVisitDAO;
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
  private RequestForVisitDAO requestForVisitDAO;
  @Inject
  private ConnectionDalServices dalServices;


  /**
   * get the piece of furniture searched by its id.
   *
   * @param id the furniture id.
   * @return Furniture represented bu FurnitureDTO.
   */
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

  /**
   * get all the furniture.
   *
   * @return list contains furniture.
   */
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

  /**
   * Sets the status of the piece of furniture to ACCEPTED.
   *
   * @param furnitureId : the furniture id
   * @return modified resource as a FurnitureDTO
   */
  @Override
  public FurnitureDTO toAccepted(int furnitureId) {
    FurnitureDTO res;
    try {
      dalServices.startTransaction();
      res = updateAfterVisit(furnitureId, FurnitureStatus.ACCEPTED);
      dalServices.commitTransaction();
    } catch (Throwable e) {
      dalServices.rollbackTransaction();
      throw e;
    }
    return res;
  }

  /**
   * Sets the status of the piece of furniture to REFUSED.
   *
   * @param furnitureId : the furniture id
   * @return modified resource as a FurnitureDTO
   */
  @Override
  public FurnitureDTO toRefused(int furnitureId) {
    FurnitureDTO res;
    try {
      dalServices.startTransaction();
      res = updateAfterVisit(furnitureId, FurnitureStatus.REFUSED);
      dalServices.commitTransaction();
    } catch (Throwable e) {
      dalServices.rollbackTransaction();
      throw e;
    }
    return res;
  }

  /**
   * Updates furniture status from REQUESTED_FOR_VISIT to status. Requirements: status is either
   * ACCEPTED or REFUSED.
   *
   * @param furnitureId : furniture id
   * @param status      : new status
   * @return modified resource as a FurnitureDTO
   */
  private FurnitureDTO updateAfterVisit(int furnitureId, FurnitureStatus status) {
    FurnitureDTO foundFurnitureDTO = furnitureDAO.findById(furnitureId);
    if (!foundFurnitureDTO.getStatus().equals(FurnitureStatus.REQUESTED_FOR_VISIT)) {
      throw new ConflictException("Error: invalid furniture status");
    }
    foundFurnitureDTO.setStatus(status);
    return furnitureDAO.updateStatusOnly(foundFurnitureDTO);
  }

  /**
   * set the status of the furniture to IN_RESTORATION.
   *
   * @param furnitureId the furniture id.
   * @return the furniture modified.
   */
  @Override
  public FurnitureDTO toRestoration(int furnitureId) {
    FurnitureDTO furnitureDTO;
    try {
      dalServices.startTransaction();
      furnitureDTO = furnitureDAO.findById(furnitureId);
      if (!furnitureDTO.getStatus().equals(FurnitureStatus.ACCEPTED)) {
        throw new ConflictException(
            "The resource cannot change from its current state to the 'in_restoration' state");
      }
      furnitureDTO.setStatus(FurnitureStatus.IN_RESTORATION);
      furnitureDTO = furnitureDAO.updateStatusOnly(furnitureDTO);
      completeFurnitureDTO(furnitureDTO);
      dalServices.commitTransaction();
    } catch (Throwable e) {
      dalServices.rollbackTransaction();
      throw e;
    }
    return furnitureDTO;
  }

  /**
   * set the status of the furniture to AVAILABLE_FOR_SALE and set its selling price.
   *
   * @param furnitureId  the furniture id.
   * @param sellingPrice the selling price of the furniture.
   * @return the furniture modified.
   */
  @Override
  public FurnitureDTO toAvailable(int furnitureId, double sellingPrice) {
    FurnitureDTO furnitureDTO;
    try {
      dalServices.startTransaction();
      furnitureDTO = furnitureDAO.findById(furnitureId);
      if (!furnitureDTO.getStatus().equals(FurnitureStatus.ACCEPTED) && !furnitureDTO.getStatus()
          .equals(FurnitureStatus.IN_RESTORATION)) {
        throw new ConflictException(
            "The resource cannot change from its current state to the 'available_for_sale' state");
      }
      furnitureDTO.setStatus(FurnitureStatus.AVAILABLE_FOR_SALE);
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

  /**
   * set the status of the furniture to WITHDRAWN.
   *
   * @param furnitureId the furniture id.
   * @return the furniture modified.
   */
  @Override
  public FurnitureDTO withdraw(int furnitureId) {
    FurnitureDTO furnitureDTO;
    try {
      dalServices.startTransaction();
      furnitureDTO = furnitureDAO.findById(furnitureId);
      if (!furnitureDTO.getStatus().equals(FurnitureStatus.AVAILABLE_FOR_SALE)
          && !furnitureDTO.getStatus().equals(FurnitureStatus.IN_RESTORATION)) {
        throw new ConflictException(
            "The resource isn't in a withdrawable state");
      }
      furnitureDTO.setStatus(FurnitureStatus.WITHDRAWN);
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
   * Sets the status of the furniture to SOLD and set its buyer + specialSellingPrice.
   *
   * @param furnitureId      : the furniture id
   * @param buyerUsername    : the username of the buyer
   * @param specialSalePrice : the special selling price (or null if there isn't one)
   * @return the modified resource
   */
  @Override
  public FurnitureDTO toSold(int furnitureId, String buyerUsername, Double specialSalePrice) {
    FurnitureDTO furnitureDTO;
    try {
      dalServices.startTransaction();
      UserDTO buyer = userDAO.findByUsername(buyerUsername);
      FurnitureDTO foundFurnitureDTO = furnitureDAO.findById(furnitureId);
      if (specialSalePrice != null && !buyer.getRole().equals("antique_dealer")) {
        throw new ConflictException("Error: only antique dealers can make special sales");
      }
      if (foundFurnitureDTO.getStatus().equals(FurnitureStatus.UNDER_OPTION)
          && specialSalePrice != null) {
        throw new ConflictException(
            "Error: pieces of furniture under option cannot be sold with a special sale price");
      }
      if (!foundFurnitureDTO.getStatus().equals(FurnitureStatus.AVAILABLE_FOR_SALE)
          && !foundFurnitureDTO.getStatus().equals(FurnitureStatus.UNDER_OPTION)) {
        throw new ConflictException("Error: invalid furniture status");
      }
      if (foundFurnitureDTO.getStatus().equals(FurnitureStatus.UNDER_OPTION)) {
        OptionDTO optionDTO = optionDAO.findByFurnitureId(foundFurnitureDTO.getFurnitureId());
        if (optionDTO.getUserId() != buyer.getId()) {
          throw new ConflictException(
              "Error: piece of furniture already under option, only one user allowed to buy it");
        }
      }
      foundFurnitureDTO.setBuyerId(buyer.getId());
      foundFurnitureDTO.setStatus(FurnitureStatus.SOLD);
      if (specialSalePrice == null) {
        furnitureDAO.updateToSold(foundFurnitureDTO);
      } else {
        foundFurnitureDTO.setSpecialSalePrice(specialSalePrice);
        furnitureDAO.updateToSoldWithSpecialSale(foundFurnitureDTO);
      }
      furnitureDTO = furnitureDAO.findById(foundFurnitureDTO.getFurnitureId());
      completeFurnitureDTO(furnitureDTO);
      dalServices.commitTransaction();
    } catch (Throwable e) {
      dalServices.rollbackTransaction();
      throw e;
    }
    return furnitureDTO;
  }

  /**
   * updates the favourite photo id of a specific piece of furniture.
   *
   * @param furnitureId : the furniture id
   * @param photoId     : the new favourite photo id
   * @return the updated furniture
   */
  @Override
  public FurnitureDTO updateFavouritePhoto(int furnitureId, int photoId) {
    FurnitureDTO furnitureDTO;
    try {
      dalServices.startTransaction();
      FurnitureDTO foundFurnitureDTO = furnitureDAO.findById(furnitureId);
      PhotoDTO foundPhotoDTO = photoDAO.findById(photoId);
      if (!foundPhotoDTO.getFurnitureId().equals(foundFurnitureDTO.getFurnitureId())) {
        throw new ConflictException(
            "Error: The photo doesn't belong to the specified piece of furniture");
      }
      foundFurnitureDTO.setFavouritePhoto(foundPhotoDTO);
      foundFurnitureDTO.setFavouritePhotoId(foundPhotoDTO.getPhotoId());

      furnitureDTO = furnitureDAO.updateFavouritePhoto(foundFurnitureDTO);
      completeFurnitureDTO(furnitureDTO);
      dalServices.commitTransaction();
    } catch (Throwable e) {
      dalServices.rollbackTransaction();
      throw e;
    }
    return furnitureDTO;
  }

  /**
   * Updates the a furniture resource with the information contained in the bodyDTO (works for:
   * description, typeId, sellingPrice).
   *
   * @param bodyDTO : request body as FurnitureDTO
   * @return the modified resource as FurnitureDTO
   */
  @Override
  public FurnitureDTO updateInfos(FurnitureDTO bodyDTO) {
    FurnitureDTO res;
    try {
      dalServices.startTransaction();
      FurnitureDTO foundFurnitureDTO = furnitureDAO.findById(bodyDTO.getFurnitureId());
      if (bodyDTO.getDescription() != null) {
        foundFurnitureDTO.setDescription(bodyDTO.getDescription());
      }
      if (bodyDTO.getTypeId() != null) {
        foundFurnitureDTO.setTypeId(bodyDTO.getTypeId());
      }
      if (bodyDTO.getSellingPrice() != null) {
        if (!foundFurnitureDTO.getStatus().equals(FurnitureStatus.AVAILABLE_FOR_SALE)) {
          throw new ConflictException(
              "Error: cannot update the selling price on a sold piece of furniture");
        }
        foundFurnitureDTO.setSellingPrice(bodyDTO.getSellingPrice());
      }
      res = furnitureDAO.updateDescription(foundFurnitureDTO);
      res = furnitureDAO.updateTypeId(res);
      if (foundFurnitureDTO.getSellingPrice() != null) {
        res = furnitureDAO.updateSellingPrice(res);
      }
      completeFurnitureDTO(res);
      dalServices.commitTransaction();
    } catch (Throwable e) {
      dalServices.rollbackTransaction();
      throw e;
    }
    return res;
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
      PhotoDTO favPhoto = photoDAO.findById(dto.getFavouritePhotoId());
      dto.setFavouritePhoto(favPhoto);
    }
    if (dto.getStatus().equals(FurnitureStatus.UNDER_OPTION)) {
      OptionDTO opt = optionDAO.findByFurnitureId(dto.getFurnitureId());
      opt.setUser(userDAO.findById(opt.getUserId()));
      dto.setOption(opt);
    }
    List<PhotoDTO> photos = photoDAO.findAllByFurnitureId(dto.getFurnitureId());
    dto.setPhotos(photos);
    String type = furnitureTypeDAO.findById(dto.getTypeId()).getTypeName();
    dto.setType(type);
    if (dto.getRequestId() != null) {
      dto.setRequest(requestForVisitDAO.findById(dto.getRequestId()));
    }
  }
}
