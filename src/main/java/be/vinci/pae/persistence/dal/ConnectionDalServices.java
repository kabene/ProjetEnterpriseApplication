package be.vinci.pae.persistence.dal;

import java.sql.PreparedStatement;

public interface ConnectionDalServices {

  /**
   * @param query String represt the query.
   * @return send preparedStatement filled first.
   */
  PreparedStatement makeStatement(String query);
}
