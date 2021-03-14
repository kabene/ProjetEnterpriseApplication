package be.vinci.pae.persistence.dao;

import be.vinci.pae.business.dto.AdresseDTO;
import be.vinci.pae.persistence.dal.ConnectionDalServices;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AdressDAOImpl implements AdresseDAO {

  @Inject
  private ConnectionDalServices dalServices;

  /**
   * Create a newAdress
   *
   * @param adress AdressDTO describe the adress.
   */
  @Override
  public void newAdresse(AdresseDTO adress) {
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
      ps.executeQuery();
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }
}
