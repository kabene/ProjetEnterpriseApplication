package be.vinci.pae.persistence.dao;

import be.vinci.pae.business.dto.FurnitureDTO;
import be.vinci.pae.business.factories.FurnitureFactory;
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
      }
      rs.close();
      ps.close();
    } catch (SQLException e) {
      e.printStackTrace();
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
      while(rs.next()) {
        res.add(toDTO(rs));
      }
      rs.close();
      ps.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return res;
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
    res.setBuyerId(rs.getInt("buyer_id"));
    res.setSellerId(rs.getInt("seller_id"));
    res.setCondition(rs.getString("condition"));

    Date saleWithdrawalDate = rs.getDate("sale_withdrawal_date");
    if (saleWithdrawalDate != null) {
      res.setSaleWithdrawalDate(saleWithdrawalDate.toString());
    }

    res.setDescription(rs.getString("description"));
    res.setTypeId(rs.getInt("type_id"));
    res.setFavouritePhotoId(rs.getInt("favourite_photo_id"));
    res.setSellingPrice(rs.getDouble("selling_price"));
    res.setSpecialSalePrice(rs.getDouble("special_sale_price"));

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
