package be.vinci.pae.business.ucc;

import be.vinci.pae.business.dto.FurnitureDTO;
import java.util.List;

public interface FurnitureUCC {

  /**
   * Gets one piece of furniture by id.
   *
   * @param id the furniture id.
   * @return Furniture represented bu FurnitureDTO.
   */
  FurnitureDTO getOne(int id);

  /**
   * Gets all pieces of furniture.
   *
   * @return list contains furniture.
   */
  List<FurnitureDTO> getAll();

  /**
   * Sets the status of the piece of furniture to ACCEPTED.
   *
   * @param furnitureId : the furniture id
   * @return modified resource as a FurnitureDTO
   */
  FurnitureDTO toAccepted(int furnitureId);

  /**
   * Sets the status of the piece of furniture to REFUSED.
   *
   * @param furnitureId : the furniture id
   * @return modified resource as a FurnitureDTO
   */
  FurnitureDTO toRefused(int furnitureId);

  /**
   * Sets the status of the furniture to IN_RESTORATION.
   *
   * @param furnitureId the furniture id.
   * @return the furniture modified.
   */
  FurnitureDTO toRestoration(int furnitureId);

  /**
   * Sets the status of the furniture to AVAILABLE_FOR_SALE and set its selling price.
   *
   * @param furnitureId  the furniture id.
   * @param sellingPrice the selling price of the furniture.
   * @return the furniture modified.
   */
  FurnitureDTO toAvailable(int furnitureId, double sellingPrice);

  /**
   * Sets the status of the furniture to WITHDRAWN.
   *
   * @param furnitureId the furniture id.
   * @return the furniture modified.
   */
  FurnitureDTO withdraw(int furnitureId);

  /**
   * Sets the status of the furniture to SOLD and set its buyer + specialSellingPrice.
   *
   * @param furnitureId      : the furniture id
   * @param buyerUsername    : the username of the buyer
   * @param specialSalePrice : the special selling price (or null if there isn't one)
   * @return the modified resource
   */
  FurnitureDTO toSold(int furnitureId, String buyerUsername, Double specialSalePrice);

  /**
   * Updates the favourite photo id of a specific piece of furniture.
   *
   * @param furnitureId : the furniture id
   * @param photoId     : the new favourite photo id
   * @return the updated furniture
   */
  FurnitureDTO updateFavouritePhoto(int furnitureId, int photoId);

  /**
   * Updates the a furniture resource with the information contained in the bodyDTO
   * (works for: description, typeId, sellingPrice).
   *
   * @param bodyDTO : request body as FurnitureDTO
   * @return the modified resource as FurnitureDTO
   */
  FurnitureDTO updateInfos(FurnitureDTO bodyDTO);
}
