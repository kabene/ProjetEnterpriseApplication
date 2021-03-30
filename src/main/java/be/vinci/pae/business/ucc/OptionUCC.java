package be.vinci.pae.business.ucc;

import be.vinci.pae.business.dto.OptionDTO;
import be.vinci.pae.business.dto.UserDTO;

public interface OptionUCC {

  /**
   * Create  an Option and insert it.
   *
   * @param option is an OptionDTO.
   * @return the optionDTO insered.
   */
  OptionDTO introduceOption(OptionDTO option);

  /**
   * cancel an option.
   *
   * @param idOption id of the option to cancel.
   * @return an OptionDTO that represent the canceled one.
   */
  OptionDTO cancelOption(int idOption);

}
