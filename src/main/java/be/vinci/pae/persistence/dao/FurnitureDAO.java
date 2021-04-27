package be.vinci.pae.persistence.dao;

import be.vinci.pae.business.dto.FurnitureDTO;
import java.util.List;

public interface FurnitureDAO {

  /**
   * Finds one piece of furniture in the db having a specific id.
   *
   * @param id : the id of the piece of furniture
   * @return The piece of furniture as a FurnitureDTO
   */
  FurnitureDTO findById(int id);

  /**
   * Finds all pieces of furniture having a specific request id.
   *
   * @param requestId : request id.
   * @return List of FurnitureDTO
   */
  List<FurnitureDTO> findByRequestId(int requestId);

  /**
   * Finds all entries of furniture in the DB.
   *
   * @return a list of furnitureDTO
   */
  List<FurnitureDTO> findAll();

  /**
   * updates the status of a piece of furniture into the database.
   *
   * @param furnitureDTO the furnitureDTO to modify
   * @return the furniture modified.
   */
  FurnitureDTO updateStatusOnly(FurnitureDTO furnitureDTO);

  /**
   * updates the status of the piece of furniture to AVAILABLE_FOR_SALE and sets the selling price
   * into the database.
   *
   * @param furnitureDTO the furnitureDTO to modify
   * @return the furniture modified.
   */
  FurnitureDTO updateToAvailable(FurnitureDTO furnitureDTO);

  /**
   * updates the status of the piece of furniture to WITHDRAWN and sets the sale_withdrawal_date
   * into the database.
   *
   * @param furnitureDTO the furnitureDTO to modify
   * @return the modified furniture.
   */
  FurnitureDTO updateToWithdrawn(FurnitureDTO furnitureDTO);

  /**
   * updates the status of the furniture to SOLD and updates its buyerId.
   *
   * @param furnitureDTO : the furnitureDTO containing the new information
   * @return the modified dto.
   */
  FurnitureDTO updateToSold(FurnitureDTO furnitureDTO);

  /**
   * updates the status of the furniture to SOLD and updates its buyerId and specialSalePrice.
   *
   * @param furnitureDTO : the furnitureDTO containing the new information
   * @return the modified dto.
   */
  FurnitureDTO updateToSoldWithSpecialSale(FurnitureDTO furnitureDTO);

  /**
   * updates the favourite photo of a specific entry in the furniture table.
   *
   * @param furnitureDTO : the furnitureDTO to modify (containing new favourite photo id)
   * @return the modified furniture.
   */
  FurnitureDTO updateFavouritePhoto(FurnitureDTO furnitureDTO);

}
