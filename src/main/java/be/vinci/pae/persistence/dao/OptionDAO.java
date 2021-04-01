package be.vinci.pae.persistence.dao;

import be.vinci.pae.business.dto.OptionDTO;
import be.vinci.pae.business.dto.UserDTO;

public interface OptionDAO {


  /**
   * Create an option.
   *
   * @param user        user.
   * @param furnitureId furnitureId.
   * @param duration  duration
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
   * @param optionId optionId of the option.
   * @return OptionDTO that represent the option
   */
  OptionDTO getOption(int optionId);

  /**
   * search an option by furniture id.
   *
   * @param furnitureId furniture id.
   * @return found option.
   */
  OptionDTO findByFurnitureId(int furnitureId);

}
