package be.vinci.pae.persistence.dao;

import be.vinci.pae.business.dto.AddressDTO;
import be.vinci.pae.business.factories.AddressFactory;
import be.vinci.pae.exceptions.NotFoundException;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.apache.commons.text.StringEscapeUtils;

public class AddressDAOImpl extends AbstractDAO implements AddressDAO {

  @Inject
  private AddressFactory addressFactory;


  /**
   * Create a newAddress.
   *
   * @param address AddressDTO describe the address.
   */
  @Override
  public void addAddress(AddressDTO address) {
    String query =
        " INSERT INTO satchoFurniture.addresses VALUES(DEFAULT,?,?,?,?,?,?)";
    PreparedStatement ps = dalServices.makeStatement(query);
    try {
      addressToRequest(address, ps);
      ps.execute();
    } catch (SQLException e) {
      throw new InternalError(e);
    }
    try {
      ps.close();
    } catch (SQLException e) {
      throw new InternalError(e);
    }
  }

  /**
   * get the id of the address.
   *
   * @param address AddressDTO describe the address.
   */
  @Override
  public int getId(AddressDTO address) {
    int id;
    try {
      String query = "SELECT a.address_id FROM satchofurniture.addresses a WHERE "
          + "a.street = ? "
          + "AND a.building_number=? "
          + "AND a.unit_number=? "
          + "AND a.postcode=? "
          + "AND a.commune=? "
          + "AND a.country=? ";
      PreparedStatement ps = dalServices.makeStatement(query);
      addressToRequest(address, ps);
      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
        id = rs.getInt(1);
      } else {
        throw new NotFoundException("Error: address not found");
      }
      rs.close();
      ps.close();
    } catch (SQLException e) {
      throw new InternalError(e.getMessage());
    }
    return id;
  }

  /**
   * Finds an address with its id.
   *
   * @param addressId : the address' id.
   * @return the address as an AddressDTO
   */
  @Override
  public AddressDTO findById(int addressId) {
    return findOneByConditions("addresses",
        new QueryParameter("address_id", addressId));
  }

  /**
   * Finds all entries of addresses in the DB.
   *
   * @return a list of addressDTO
   */
  @Override
  public List<AddressDTO> findAll() {
    return findAll("addresses");
  }

  private void addressToRequest(AddressDTO address, PreparedStatement ps) throws SQLException {
    ps.setString(1, StringEscapeUtils.escapeHtml4(address.getStreet()));
    ps.setString(2, StringEscapeUtils.escapeHtml4(address.getBuildingNumber()));
    ps.setString(3, StringEscapeUtils.escapeHtml4(address.getUnitNumber()));
    ps.setString(4, String.valueOf(address.getPostcode()));
    ps.setString(5, StringEscapeUtils.escapeHtml4(address.getCommune()));
    ps.setString(6, StringEscapeUtils.escapeHtml4(address.getCountry()));
  }



  /**
   * Creates and fills an AddressDTO object using a ResultSet.
   *
   * @param rs : the ResultSet containing the information.
   * @return a dto containing the information from the result set
   * @throws SQLException in case of problem during access to the ResultSet.
   */
  @Override
  protected AddressDTO toDTO(ResultSet rs) throws SQLException {
    AddressDTO res = addressFactory.getAddressDTO();
    res.setId(rs.getInt("address_id"));
    res.setStreet(rs.getString("street"));
    res.setBuildingNumber(rs.getString("building_number"));
    res.setUnitNumber(rs.getString("unit_number"));
    res.setPostcode(rs.getInt("postcode"));
    res.setCommune(rs.getString("commune"));
    res.setCountry(rs.getString("country"));
    return res;
  }

}
