package be.vinci.pae.persistence.dao;

import be.vinci.pae.business.dto.OptionDTO;
import be.vinci.pae.business.factories.OptionFactory;
import be.vinci.pae.persistence.dal.ConnectionBackendDalServices;
import jakarta.inject.Inject;

public class OptionDAOImpl implements OptionDAO {

  @Inject
  private OptionFactory optionFactory;
  @Inject
  private ConnectionBackendDalServices dalServices;

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
  public void cancelOption(int idOption) {

  }

  /**
   * search an option.
   *
   * @param id id of the option.
   * @return OptionDTO that represent the option
   */
  @Override
  public OptionDTO getOption(int id) {
    return null;
  }
}
