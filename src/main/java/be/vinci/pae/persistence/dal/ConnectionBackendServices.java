package be.vinci.pae.persistence.dal;

import java.sql.PreparedStatement;

public interface ConnectionBackendServices {

  /**
   * Create a  PreparedStatement
   *
   * @pre query : to execute
   * @post send preparedStatement filled first @author: karim
   */
  PreparedStatement makeStatement(String query);
}
