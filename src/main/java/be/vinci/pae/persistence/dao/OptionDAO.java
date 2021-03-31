package be.vinci.pae.persistence.dao;

import be.vinci.pae.business.dto.OptionDTO;
import be.vinci.pae.business.dto.UserDTO;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface OptionDAO {

  /**
   * Create  an Option and insert it.
   *
   * @param option is an OptionDTO.
   * @return the id of the option.
   */
   int introduceOption(OptionDTO option);

  /**
   * cancel an option.
   *
   * @param idOption id of the option to cancel.
   */
  void cancelOption(int idOption);

  /**
   *  search an option.
   * @param id id of the option.
   * @return OptionDTO that represent the option
   */
  OptionDTO getOption(int id);


}
