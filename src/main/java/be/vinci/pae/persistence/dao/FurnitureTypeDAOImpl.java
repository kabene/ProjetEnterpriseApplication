package be.vinci.pae.persistence.dao;

import be.vinci.pae.persistence.dal.ConnectionBackendDalServices;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FurnitureTypeDAOImpl implements FurnitureTypeDAO{

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
      if(rs.next()) {
        res = rs.getString("type_name");
      }
      rs.close();
      ps.close();
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
    return res;
  }
}
