package be.vinci.pae.business.ucc;

import be.vinci.pae.business.dto.OptionDTO;
import be.vinci.pae.business.dto.UserDTO;

public interface OptionUCC {


  /**
   *  create an option.
   * @param clientId clientId.
   * @param furnitureId furnitureId.
   * @return OptionDTO.
   */
  OptionDTO introduceOption(int clientId, int furnitureId);

  /**
   * cancel an option.
   *
   * @param idOption id of the option to cancel.
   * @return an OptionDTO that represent the canceled one.
   */
  OptionDTO cancelOption(int idOption);

}
