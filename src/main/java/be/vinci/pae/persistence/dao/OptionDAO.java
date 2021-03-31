package be.vinci.pae.persistence.dao;

import be.vinci.pae.business.dto.OptionDTO;
import be.vinci.pae.business.dto.UserDTO;

public interface OptionDAO {


  /**
   * Create an option.
   *
   * @param user        user.
   * @param furnitureId furnitureId.
   * @return OptionDTO.
   */
  OptionDTO introduceOption(UserDTO user, int furnitureId);

  /**
   * cancel an option.
   *
   * @param idOption id of the option to cancel.
   */
  void cancelOption(int idOption);

  /**
   * search an option.
   *
   * @param id id of the option.
   * @return OptionDTO that represent the option
   */
  OptionDTO getOption(int id);


}
