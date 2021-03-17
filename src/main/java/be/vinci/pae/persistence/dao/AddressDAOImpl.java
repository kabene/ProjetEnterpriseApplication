package be.vinci.pae.persistence.dao;

import be.vinci.pae.business.dto.AddressDTO;
import be.vinci.pae.persistence.dal.ConnectionDalServices;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AddressDAOImpl implements AddressDAO {

  @Inject
  private ConnectionDalServices dalServices;


  /**
   * Create a newAdress.
   *
   * @param adress AdressDTO describe the adress.
   */
  @Override
  public void addAddress(AddressDTO adress) {
    String query =
        " INSERT INTO satchoFurniture.addresses VALUES(DEFAULT,?,?,?,?,?,?)";
    PreparedStatement ps = dalServices.makeStatement(query);
    try {
      ps.setString(1, adress.getStreet());
      ps.setString(2, adress.getBuildingNumber());
      ps.setString(3, adress.getUnitNumber());
      ps.setInt(4, adress.getPostcode());
      ps.setString(5, adress.getCommune());
      ps.setString(6, adress.getCountry());
      ps.execute();
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
    try {
      ps.close();
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }

  /**
   * get the id of the address.
   *
   * @param address AdressDTO describe the adress.
   */
  @Override
  public int getId(AddressDTO address) {
    int id = 0;
    try {
      String query = "SELECT a.address_id FROM satchofurniture.addresses a WHERE "
          + "a.street = ? "
          + "AND a.building_number=? "
          + "AND a.unit_number=? "
          + "AND a.postcode=? "
          + "AND a.commune=? "
          + "AND a.country=? ";
      PreparedStatement ps = dalServices.makeStatement(query);
      ps.setString(1, address.getStreet());
      ps.setString(2, address.getBuildingNumber());
      ps.setString(3, address.getUnitNumber());
      ps.setInt(4, address.getPostcode());
      ps.setString(5, address.getCommune());
      ps.setString(6, address.getCountry());
      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
        id = rs.getInt(1);
      }
      rs.close();
      ps.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return id;
  }


}
