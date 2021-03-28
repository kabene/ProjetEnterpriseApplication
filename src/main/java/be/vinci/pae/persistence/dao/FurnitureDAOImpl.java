package be.vinci.pae.persistence.dao;

import be.vinci.pae.business.dto.FurnitureDTO;
import be.vinci.pae.business.factories.FurnitureFactory;
import be.vinci.pae.exceptions.NotFoundException;
import be.vinci.pae.persistence.dal.ConnectionBackendDalServices;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class FurnitureDAOImpl implements FurnitureDAO {

  @Inject
  private FurnitureFactory furnitureFactory;
  @Inject
  private ConnectionBackendDalServices dalServices;

  /**
   * Finds one piece of furniture in the db having a specific id.
   *
   * @param id : the id of the piece of furniture
   * @return The piece of furniture as a FurnitureDTO
   */
  @Override
  public FurnitureDTO findById(int id) {
    FurnitureDTO res = null;
    String query = "SELECT f.* FROM satchofurniture.furniture f WHERE f.furniture_id = ?";

    try {
      PreparedStatement ps = dalServices.makeStatement(query);
      ps.setInt(1, id);
      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
        res = toDTO(rs);
      } else {
        throw new NotFoundException("Error: piece of furniture not found");
      }
      rs.close();
      ps.close();
    } catch (SQLException e) {
      throw new InternalError(e.getMessage());
    }

    return res;
  }

  @Override
  public List<FurnitureDTO> findAll() {
    List<FurnitureDTO> res = new ArrayList<>();
    String query = "SELECT f.* FROM satchofurniture.furniture f";
    try {
      PreparedStatement ps = dalServices.makeStatement(query);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        res.add(toDTO(rs));
      }
      rs.close();
      ps.close();
    } catch (SQLException e) {
      throw new InternalError(e.getMessage());
    }
    return res;
  }

  @Override
  public void updateToRestoration(FurnitureDTO furnitureDTO) {
    String query = "UPDATE satchofurniture.furniture "
        + "SET condition = ? "
        + "WHERE furniture_id = ? ";
    PreparedStatement ps = dalServices.makeStatement(query);
    try {
      ps.setString(1, furnitureDTO.getCondition());
      ps.setInt(2, furnitureDTO.getFurnitureId());
      ps.execute();
      ps.close();
    } catch (SQLException e) {
      throw new InternalError(e);
    }


  }

  @Override
  public void updateToAvailable(FurnitureDTO furnitureDTO) {
    String query = "UPDATE satchofurniture.furniture "
        + "SET condition = ?, "
        + "selling_price = ? "
        + "WHERE furniture_id = ?";
    PreparedStatement ps = dalServices.makeStatement(query);
    try {
      ps.setString(1, furnitureDTO.getCondition());
      ps.setDouble(2, furnitureDTO.getSellingPrice());
      ps.setInt(3, furnitureDTO.getFurnitureId());
      ps.execute();
      ps.close();
    } catch (SQLException e) {
      throw new InternalError(e);
    }
  }

  /**
   * transfers ONE furniture entry from a result set to a dto.
   *
   * @param rs the result set containing the information.
   * @return a dto containing the information from the result set
   * @throws SQLException if an error occurs while reading the result set
   */
  private FurnitureDTO toDTO(ResultSet rs) throws SQLException {
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

    res.setCondition(rs.getString("condition"));

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
      res.setSellingPrice(specialSalePrice);
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
    return res;
  }
}
