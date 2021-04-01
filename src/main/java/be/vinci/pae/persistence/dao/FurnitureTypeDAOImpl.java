package be.vinci.pae.persistence.dao;

import be.vinci.pae.exceptions.NotFoundException;
import be.vinci.pae.persistence.dal.ConnectionBackendDalServices;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class FurnitureTypeDAOImpl extends AbstractDAO implements FurnitureTypeDAO {

  @Inject
  ConnectionBackendDalServices dalServices;

  @Override
  public String findById(int id) {
    String res = "";
    String query = "SELECT t.* FROM satchofurniture.furniture_types t WHERE t.type_id = ?";
    try {
      PreparedStatement ps = dalServices.makeStatement(query);
      ps.setInt(1, id);
      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
        res = rs.getString("type_name");
      } else {
        throw new NotFoundException("Error: furniture type not found");
      }
      rs.close();
      ps.close();
    } catch (SQLException e) {
      throw new InternalError(e.getMessage());
    }
    return res;
  }

  @Override
  public List<String> findAll() {
    return findAll("furniture_type");
  }


  protected String toDTO(ResultSet rs) throws SQLException {
    return rs.getString("type_name");
  }
}
