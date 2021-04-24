package be.vinci.pae.business.ucc;

import be.vinci.pae.business.dto.FurnitureDTO;
import java.util.List;

public interface FurnitureUCC {

  /**
   * get the piece of furniture searched by its id.
   *
   * @param id the furniture id.
   * @return Furniture represented bu FurnitureDTO.
   */
  FurnitureDTO getOne(int id);

  /**
   * get all the furniture.
   *
   * @return list contains furniture.
   */
  List<FurnitureDTO> getAll();

  /**
   * set the status of the furniture to IN_RESTORATION.
   *
   * @param furnitureId the furniture id.
   * @return the furniture modified.
   */
  FurnitureDTO toRestoration(int furnitureId);

  /**
   * set the status of the furniture to AVAILABLE_FOR_SALE and set its selling price.
   *
   * @param furnitureId the furniture id.
   * @param sellingPrice the selling price of the furniture.
   * @return the furniture modified.
   */
  FurnitureDTO toAvailable(int furnitureId, double sellingPrice);

  /**
   * set the status of the furniture to WITHDRAWN.
   *
   * @param furnitureId the furniture id.
   * @return the furniture modified.
   */
  FurnitureDTO withdraw(int furnitureId);

  /**
   * updates the favourite photo id of a specific piece of furniture.
   * 
   * @param furnitureId : the furniture id
   * @param photoId : the new favourite photo id
   * @return the updated furniture
   */
  FurnitureDTO updateFavouritePhoto(int furnitureId, int photoId);
}
