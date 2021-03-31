package be.vinci.pae.persistence.dao;

import be.vinci.pae.business.dto.OptionDTO;
import be.vinci.pae.business.factories.OptionFactory;
import be.vinci.pae.exceptions.NotFoundException;
import be.vinci.pae.persistence.dal.ConnectionBackendDalServices;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OptionDAOImpl implements OptionDAO {

  @Inject
  private OptionFactory optionFactory;
  @Inject
  private ConnectionBackendDalServices dalServices;


  /**
   * Create an option.
   * @param clientId clientId.
   * @param furnitureId furnitureId.
   * @return OptionDTO.
   */
  @Override
  public OptionDTO introduceOption(int clientId,int furnitureId) {
    String query= "INSERT INTO satchoFurniture.options VALUES(DEFAULT,0,NOW(),?,?,'false') RETURNING *";
    PreparedStatement ps= dalServices.makeStatement(query);
    OptionDTO optionFound;
    try{
      ps.setInt(1,clientId);
      ps.setInt(2,furnitureId);
      ResultSet rs=ps.executeQuery();
      if(rs.next()){
          optionFound=toDTO(rs);
      }else{
        throw new NotFoundException("Error: option not found");
      }
      rs.close();
      ps.close();
    }catch (SQLException e){
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
  public void cancelOption(int idOption)  {
    String query="UPDATE  satchoFurniture.options o SET canceled=true WHERE option_id=?";
    PreparedStatement ps=dalServices.makeStatement(query);
    try {
      ps.setInt(1,idOption);
      ps.executeUpdate();
      ps.close();
    } catch (SQLException throwables) {
      throw new InternalError(throwables);
    }
  }

  /**
   * search an option.
   *
   * @param id id of the option.
   * @return OptionDTO that represent the option
   */
  @Override
  public OptionDTO getOption(int id) {
    OptionDTO optionFound=null;
    String query="SELECT o.* FROM satchofurniture.options o WHERE o.option_id=? ";
    PreparedStatement ps=dalServices.makeStatement(query);
    try {
      ps.setInt(1,id);
      ResultSet rs=ps.executeQuery();
      if(rs.next()){
        optionFound=toDTO(rs);
      }else {
        throw new NotFoundException("Error: option not found");
      }
      rs.close();
      ps.close();
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
    return optionFound;
  }


  /**
   * Creates and fills a OptionDTO object using a ResultSet.
   *
   * @param rs : the ResultSet containing the information
   * @throws SQLException in case of problem during access to the ResultSet
   */
  private OptionDTO toDTO(ResultSet rs) throws SQLException {
    OptionDTO optionFound = optionFactory.getOptionDTO();
    optionFound.setOptionId(rs.getInt("option_id"));
    optionFound.setDuree(rs.getInt("duration"));
    optionFound.setDateOption(rs.getDate("date_option").toString());
    optionFound.setClientId(rs.getInt("client_id"));
    optionFound.setFurnitureId(rs.getInt("furniture_id"));
    optionFound.setCanceled(rs.getBoolean("canceled"));
    return optionFound;
  }
}
