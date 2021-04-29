package be.vinci.pae.persistence.dao;

import be.vinci.pae.business.dto.FurnitureDTO;
import be.vinci.pae.business.factories.FurnitureFactory;
import be.vinci.pae.business.pojos.FurnitureStatus;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class FurnitureDAOImpl extends AbstractDAO implements FurnitureDAO {

  @Inject
  private FurnitureFactory furnitureFactory;

  /**
   * Finds one piece of furniture in the db having a specific id.
   *
   * @param id : the id of the piece of furniture
   * @return The piece of furniture as a FurnitureDTO
   */
  @Override
  public FurnitureDTO findById(int id) {
    return findById(id, "furniture", "furniture_id");
  }

  /**
   * Finds all pieces of furniture having a specific request id.
   *
   * @param requestId : request id.
   * @return List of FurnitureDTO
   */
  @Override
  public List<FurnitureDTO> findByRequestId(int requestId) {
    List<FurnitureDTO> lst = new ArrayList<>();
    String query = "SELECT f.* FROM satchofurniture.furniture f WHERE f.request_id = ?";
    try {
      PreparedStatement ps = dalServices.makeStatement(query);
      ps.setInt(1, requestId);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        lst.add(toDTO(rs));
      }
      rs.close();
    } catch (SQLException e) {
      throw new InternalError(e);
    }
    return lst;
  }

  /**
   * Finds all entries of furniture in the DB.
   *
   * @return a list of furnitureDTO
   */
  @Override
  public List<FurnitureDTO> findAll() {
    return super.findAll("furniture");
  }

  /**
   * updates the status of a piece of furniture into the database.
   *
   * @param furnitureDTO the furnitureDTO to modify
   * @return the furniture modified.
   */
  @Override
  public FurnitureDTO updateStatusOnly(FurnitureDTO furnitureDTO) {
    String query = "UPDATE satchofurniture.furniture "
        + "SET status = ? "
        + "WHERE furniture_id = ? ";
    PreparedStatement ps = dalServices.makeStatement(query);
    try {
      ps.setString(1, furnitureDTO.getStatus().getValue());
      ps.setInt(2, furnitureDTO.getFurnitureId());
      ps.execute();
      ps.close();
    } catch (SQLException e) {
      throw new InternalError(e);
    }
    return furnitureDTO;
  }

  /**
   * updates the status of the piece of furniture to AVAILABLE_FOR_SALE and sets the selling price
   * into the database.
   *
   * @param furnitureDTO the furnitureDTO to modify
   * @return the furniture modified.
   */
  @Override
  public FurnitureDTO updateToAvailable(FurnitureDTO furnitureDTO) {
    String query = "UPDATE satchofurniture.furniture "
        + "SET status = ?, "
        + "selling_price = ? "
        + "WHERE furniture_id = ?";
    PreparedStatement ps = dalServices.makeStatement(query);
    try {
      ps.setString(1, furnitureDTO.getStatus().getValue());
      ps.setDouble(2, furnitureDTO.getSellingPrice());
      ps.setInt(3, furnitureDTO.getFurnitureId());
      ps.execute();
      ps.close();
    } catch (SQLException e) {
      throw new InternalError(e);
    }
    return furnitureDTO;
  }

  /**
   * updates the status of the piece of furniture to WITHDRAWN and sets the sale_withdrawal_date
   * into the database.
   *
   * @param furnitureDTO the furnitureDTO to modify
   * @return the furniture modified.
   */
  @Override
  public FurnitureDTO updateToWithdrawn(FurnitureDTO furnitureDTO) {
    String query = "UPDATE satchofurniture.furniture "
        + "SET status = ?, "
        + "sale_withdrawal_date = ? "
        + "WHERE furniture_id = ?";
    PreparedStatement ps = dalServices.makeStatement(query);
    Date saleWithdrawalDate = new Date(new java.util.Date().getTime()); //now
    try {
      ps.setString(1, furnitureDTO.getStatus().getValue());
      ps.setDate(2, saleWithdrawalDate);
      ps.setInt(3, furnitureDTO.getFurnitureId());
      ps.execute();
      ps.close();
      furnitureDTO.setSaleWithdrawalDate(saleWithdrawalDate.toString());
    } catch (SQLException e) {
      throw new InternalError(e);
    }
    return furnitureDTO;
  }

  /**
   * updates the status of the furniture to SOLD and updates its buyerId.
   *
   * @param furnitureDTO : the furnitureDTO containing the new information
   * @return the modified dto.
   */
  @Override
  public FurnitureDTO updateToSold(FurnitureDTO furnitureDTO) {
    String query = "UPDATE satchofurniture.furniture "
        + "SET status = ?, "
        + "buyer_id = ? "
        + "WHERE furniture_id = ?";
    PreparedStatement ps = dalServices.makeStatement(query);
    try {
      ps.setString(1, furnitureDTO.getStatus().getValue());
      ps.setInt(2, furnitureDTO.getBuyerId());
      ps.setInt(3, furnitureDTO.getFurnitureId());
      ps.execute();
      ps.close();
    } catch (SQLException e) {
      throw new InternalError(e);
    }
    return furnitureDTO;
  }

  /**
   * updates the status of the furniture to SOLD and updates its buyerId and specialSalePrice.
   *
   * @param furnitureDTO : the furnitureDTO containing the new information
   * @return the modified dto.
   */
  @Override
  public FurnitureDTO updateToSoldWithSpecialSale(FurnitureDTO furnitureDTO) {
    String query = "UPDATE satchofurniture.furniture "
        + "SET status = ?, "
        + "buyer_id = ?, "
        + "special_sale_price = ? "
        + "WHERE furniture_id = ?";
    PreparedStatement ps = dalServices.makeStatement(query);
    try {
      ps.setString(1, furnitureDTO.getStatus().getValue());
      ps.setInt(2, furnitureDTO.getBuyerId());
      ps.setDouble(3, furnitureDTO.getSpecialSalePrice());
      ps.setInt(4, furnitureDTO.getFurnitureId());
      ps.execute();
      ps.close();
    } catch (SQLException e) {
      throw new InternalError(e);
    }
    return furnitureDTO;
  }

  /**
   * updates the favourite photo of a specific entry in the furniture table.
   *
   * @param furnitureDTO : the furnitureDTO to modify (containing new favourite photo id)
   * @return the modified furniture.
   */
  @Override
  public FurnitureDTO updateFavouritePhoto(FurnitureDTO furnitureDTO) {
    String query = "UPDATE satchofurniture.furniture "
        + "SET favourite_photo_id = ? "
        + "WHERE furniture_id = ?";
    PreparedStatement ps = dalServices.makeStatement(query);
    try {
      ps.setInt(1, furnitureDTO.getFavouritePhotoId());
      ps.setInt(2, furnitureDTO.getFurnitureId());
      ps.execute();
      ps.close();
    } catch (SQLException e) {
      throw new InternalError(e);
    }
    return furnitureDTO;
  }

  /**
   * transfers ONE furniture entry from a result set to a dto.
   *
   * @param rs the result set containing the information.
   * @return a dto containing the information from the result set
   * @throws SQLException if an error occurs while reading the result set
   */
  @Override
  protected FurnitureDTO toDTO(ResultSet rs) throws SQLException {
    FurnitureDTO res = furnitureFactory.getFurnitureDTO();
    res.setFurnitureId(rs.getInt("furniture_id"));

    int buyerId = rs.getInt("buyer_id");
    if (buyerId != 0) {
      res.setBuyerId(buyerId);
    }

    int sellerId = rs.getInt("seller_id");
    if (sellerId != 0) {
      res.setSellerId(sellerId);
    }

    res.setStatus(FurnitureStatus.toEnum(rs.getString("status")));

    Date saleWithdrawalDate = rs.getDate("sale_withdrawal_date");
    if (saleWithdrawalDate != null) {
      res.setSaleWithdrawalDate(saleWithdrawalDate.toString());
    }

    res.setDescription(rs.getString("description"));

    int typeId = rs.getInt("type_id");
    if (typeId != 0) {
      res.setTypeId(typeId);
    }

    int favouritePhotoId = rs.getInt("favourite_photo_id");
    if (favouritePhotoId != 0) {
      res.setFavouritePhotoId(favouritePhotoId);
    }

    double sellingPrice = rs.getInt("selling_price");
    if (sellingPrice != 0) {
      res.setSellingPrice(sellingPrice);
    }

    double specialSalePrice = rs.getInt("special_sale_price");
    if (specialSalePrice != 0) {
      res.setSpecialSalePrice(specialSalePrice);
    }

    Date dateOfSale = rs.getDate("date_of_sale");
    if (dateOfSale != null) {
      res.setDateOfSale(dateOfSale.toString());
    }

    res.setToPickUp(rs.getBoolean("is_to_pick_up"));

    Date pickUpDate = rs.getDate("pick_up_date");
    if (pickUpDate != null) {
      res.setPickUpDate(pickUpDate.toString());
    }

    int requestId = rs.getInt("request_id");
    if (requestId != 0) {
      res.setRequestId(requestId);
    }

    double purchasePrice = rs.getDouble("purchase_price");
    if (purchasePrice != 0) {
      res.setPurchasePrice(purchasePrice);
    }

    String customerWithdrawalDate = rs.getString("customer_withdrawal_date");
    if (customerWithdrawalDate != null) {
      res.setCustomerWithdrawalDate(customerWithdrawalDate);
    }

    String depositDate = rs.getString("deposit_date");
    if (depositDate != null) {
      res.setDepositDate(depositDate);
    }

    Boolean suitable = rs.getBoolean("suitable");
    res.setSuitable(suitable);

    Boolean availableForSale = rs.getBoolean("available_for_sale");
    res.setAvailableForSale(availableForSale);

    return res;
  }
}
