package be.vinci.pae.persistence.dao;

import be.vinci.pae.business.dto.AddressDTO;
import be.vinci.pae.persistence.dal.ConnectionBackendDalServices;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.commons.text.StringEscapeUtils;

public class AddressDAOImpl implements AddressDAO {

  @Inject
  private ConnectionBackendDalServices dalServices;


  /**
   * Create a newAdress.
   *
   * @param address AdressDTO describe the address.
   */
  @Override
  public void addAddress(AddressDTO address) {
    String query =
        " INSERT INTO satchoFurniture.addresses VALUES(DEFAULT,?,?,?,?,?,?)";
    PreparedStatement ps = dalServices.makeStatement(query);
    try {
      ps.setString(1, StringEscapeUtils.escapeHtml4(address.getStreet()));
      ps.setString(2, StringEscapeUtils.escapeHtml4(address.getBuildingNumber()));
      ps.setString(3, StringEscapeUtils.escapeHtml4(address.getUnitNumber()));
      ps.setString(4, String.valueOf(address.getPostcode()));
      ps.setString(5, StringEscapeUtils.escapeHtml4(address.getCommune()));
      ps.setString(6, StringEscapeUtils.escapeHtml4(address.getCountry()));
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
   * @param address AdressDTO describe the address.
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
      ps.setString(1, StringEscapeUtils.escapeHtml4(address.getStreet()));
      ps.setString(2, StringEscapeUtils.escapeHtml4(address.getBuildingNumber()));
      ps.setString(3, StringEscapeUtils.escapeHtml4(address.getUnitNumber()));
      ps.setString(4, String.valueOf(address.getPostcode()));
      ps.setString(5, StringEscapeUtils.escapeHtml4(address.getCommune()));
      ps.setString(6, StringEscapeUtils.escapeHtml4(address.getCountry()));
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
