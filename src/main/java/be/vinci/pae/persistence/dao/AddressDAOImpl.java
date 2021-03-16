package be.vinci.pae.persistence.dao;

import be.vinci.pae.business.dto.AddressDTO;
import be.vinci.pae.business.factories.AddressFactory;
import be.vinci.pae.persistence.dal.ConnectionDalServices;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddressDAOImpl implements AddressDAO {

  @Inject
  private ConnectionDalServices dalServices;
  @Inject
  private AddressFactory addressFactory;

  /**
   * Create a newAdress
   *
   * @param adress AdressDTO describe the adress.
   */
  @Override
  public void newAdresse(AddressDTO adress) {
    String query =
        " INSERT INTO satchoFurniture.addresses VALUES(DEFAULT,?,?,?,?,?,?)";
    PreparedStatement ps = dalServices.makeStatement(query);
    try {
      ps.setString(1, adress.getStreet());
      ps.setString(2, adress.getBuilding_number());
      ps.setString(3, adress.getUnit_number());
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
}
