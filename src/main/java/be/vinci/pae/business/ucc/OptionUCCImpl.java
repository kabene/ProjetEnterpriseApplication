package be.vinci.pae.business.ucc;

import be.vinci.pae.business.dto.OptionDTO;
import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.persistence.dal.ConnectionDalServices;
import be.vinci.pae.persistence.dao.UserDAO;
import jakarta.inject.Inject;

public class OptionUCCImpl implements OptionUCC {

  @Inject
  private ConnectionDalServices dalServices;
  @Inject
  private UserDAO userDAO;


  /**
   * Create  an Option and insert it.
   *
   * @param option is an OptionDTO.
   * @return the optionDTO insered.
   */
  @Override
  public OptionDTO introduceOption(OptionDTO option) {
    return null;
  }

  /**
   * cancel an option.
   *
   * @param idOption id of the option to cancel.
   * @return an OptionDTO that represent the canceled one.
   */
  @Override
  public OptionDTO cancelOption(int idOption) {
    return null;
  }
}
