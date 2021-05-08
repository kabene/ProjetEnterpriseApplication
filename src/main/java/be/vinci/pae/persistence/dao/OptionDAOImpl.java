package be.vinci.pae.persistence.dao;

import be.vinci.pae.business.dto.OptionDTO;
import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.business.factories.OptionFactory;
import be.vinci.pae.exceptions.NotFoundException;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class OptionDAOImpl extends AbstractDAO implements OptionDAO {

  @Inject
  private OptionFactory optionFactory;


  /**
   * Create an option.
   *
   * @param user        user.
   * @param furnitureId furnitureId.
   * @param duration    duration
   * @return OptionDTO.
   */
  @Override
  public OptionDTO introduceOption(UserDTO user, int furnitureId, int duration) {
    String query = "INSERT INTO satchoFurniture.options "
        + "VALUES(DEFAULT,?,NOW(),?,?,'false') RETURNING *";
    PreparedStatement ps = dalServices.makeStatement(query);
    OptionDTO optionFound;
    try {
      ps.setInt(1, duration);
      ps.setInt(2, user.getId());
      ps.setInt(3, furnitureId);
      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
        optionFound = toDTO(rs);
      } else {
        throw new NotFoundException("Error: option not found");
      }
      rs.close();
      ps.close();
    } catch (SQLException e) {
      throw new InternalError(e);
    }
    return optionFound;
  }


  /**
   * cancel an option.
   *
   * @param idOption id of the option to cancel.
   */
  @Override
  public void cancelOption(int idOption) {
    updateById("options",
        new QueryParameter("option_id", idOption),
        new QueryParameter("is_canceled", true));
  }

  /**
   * search an option.
   *
   * @param id id of the option.
   * @return OptionDTO that represent the option
   */
  @Override
  public OptionDTO findById(int id) {
    return findById(id, "options", "option_id");
  }

  /**
   * Finds all options.
   *
   * @return list of all the options.
   */
  @Override
  public List<OptionDTO> findAll() {
    return findAll("options");
  }

  /**
   * search an option by furniture id.
   *
   * @param furnitureId furniture id.
   * @return found option.
   */
  @Override
  public OptionDTO findByFurnitureId(int furnitureId) {
    return findOneByConditions("options",
        new QueryParameter("furniture_id", furnitureId),
        new QueryParameter("is_canceled", false));
  }

  /**
   * list all user's options.
   *
   * @param userId user id.
   * @return list of all user's options.
   */
  @Override
  public List<OptionDTO> findByUserId(int userId) {
    return findByConditions("options",
        new QueryParameter("user_id", userId),
        new QueryParameter("is_canceled", false));
  }

  /**
   * Creates and fills a OptionDTO object using a ResultSet.
   *
   * @param rs : the ResultSet containing the information
   * @return a dto containing the information from the result set
   * @throws SQLException in case of problem during access to the ResultSet
   */
  @Override
  protected OptionDTO toDTO(ResultSet rs) throws SQLException {
    OptionDTO optionFound = optionFactory.getOptionDTO();
    optionFound.setOptionId(rs.getInt("option_id"));
    optionFound.setDuration(rs.getInt("duration"));
    optionFound.setDateOption(rs.getDate("date_option").toString());
    optionFound.setUserId(rs.getInt("user_id"));
    optionFound.setFurnitureId(rs.getInt("furniture_id"));
    optionFound.setCanceled(rs.getBoolean("is_canceled"));
    return optionFound;
  }
}
