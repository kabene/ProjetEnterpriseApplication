package be.vinci.pae.persistence.dao;

import be.vinci.pae.business.dto.OptionDTO;
import be.vinci.pae.business.dto.UserDTO;
import java.util.List;

public interface OptionDAO {


  /**
   * Create an option.
   *
   * @param user        user.
   * @param furnitureId furnitureId.
   * @param duration    duration
   * @return OptionDTO.
   */
  OptionDTO introduceOption(UserDTO user, int furnitureId, int duration);

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

  /**
   * list all the options of the user.
   *
   * @param userId userId.
   * @return list of all the option tha the user made.
   */
  List<OptionDTO> findAll(int userId);
}
