package be.vinci.pae.persistence.dao;

import be.vinci.pae.business.dto.FurnitureDTO;
import be.vinci.pae.business.factories.FurnitureFactory;
import be.vinci.pae.persistence.dal.ConnectionBackendDalServices;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;

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
    FurnitureDTO res = furnitureFactory.getFurnitureDTO();
    String query = "SELECT f.* FROM furniture f WHERE f.furniture_id = ?";

    try {
      PreparedStatement ps = dalServices.makeStatement(query);
      ps.setInt(1, id);
      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
        res.setFurnitureId(rs.getInt("furniture_id"));
        res.setBuyerId(rs.getInt("buyer_id"));
        res.setSellerId(rs.getInt("seller_id"));
        res.setCondition(rs.getString("condition"));
        res.setSaleWithdrawalDate(rs.getDate("sale_withdrawal_date")
            .toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        res.setDescription(rs.getString("description"));
        res.setTypeId(rs.getInt("type_id"));
        res.setFavouritePhotoId(rs.getInt("favourite_photo_id"));
        res.setSellingPrice(rs.getDouble("selling_price"));
        res.setSpecialSalePrice(rs.getDouble("special_price"));
        res.setDateOfSale(rs.getDate("date_of_sale")
            .toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        res.setToPickUp(rs.getBoolean("is_to_pick_up"));
        res.setPickUpDate(rs.getDate("pick_up_date")
            .toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return res;
  }
}
