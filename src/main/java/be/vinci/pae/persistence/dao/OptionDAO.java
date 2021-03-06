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
  OptionDTO findById(int id);

  /**
   * list all the options .
   *
   * @return list of all the option.
   */
  List<OptionDTO> findAll();

  /**
   * search an option by furniture id.
   *
   * @param furnitureId furniture id.
   * @return found option.
   */
  OptionDTO findByFurnitureId(int furnitureId);


  /**
   * list all user's options.
   *
   * @param userId user id.
   * @return list of all user's options.
   */
  List<OptionDTO> findByUserId(int userId);
}
