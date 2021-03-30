package be.vinci.pae.persistence.dao;

import be.vinci.pae.business.dto.OptionDTO;
import be.vinci.pae.business.dto.UserDTO;

public interface OptionDAO {

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
  void cancelOption(int idOption);

  /**
   *  search an option.
   * @param id id of the option.
   * @return OptionDTO that represent the option
   */
  OptionDTO getOption(int id);


}
