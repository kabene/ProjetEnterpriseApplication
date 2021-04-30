package be.vinci.pae.business.ucc;

import be.vinci.pae.business.dto.OptionDTO;
import be.vinci.pae.business.dto.UserDTO;
import java.util.List;

public interface OptionUCC {


  /**
   * create an option.
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
   * @param user     id of the user that want to cancel the option
   * @param idOption id of the option to cancel.
   * @return an OptionDTO that represent the canceled one.
   */
  OptionDTO cancelOption(UserDTO user, int idOption);


  /**
   * list all the options.
   *
   * @return list of all the Option.
   */
  List<OptionDTO> listOption();

  /**
   * list all options of the current user.
   *
   * @param currentUser user.
   * @return list of all currentUser's option.
   */
  List<OptionDTO> myOptions(UserDTO currentUser);
}
