package be.vinci.pae.business.ucc;

import be.vinci.pae.business.dto.OptionDTO;

import be.vinci.pae.persistence.dal.ConnectionDalServices;
import be.vinci.pae.persistence.dao.OptionDAO;
import jakarta.inject.Inject;

public class OptionUCCImpl implements OptionUCC {

  @Inject
  private ConnectionDalServices dalServices;
  @Inject
  private OptionDAO optionDAO;


  /**
   * Create  an Option and insert it.
   *
   * @param option is an OptionDTO.
   * @return the optionDTO insered.
   */
  @Override
  public OptionDTO introduceOption(OptionDTO option) {
    OptionDTO opt = null;
    try {
      dalServices.startTransaction();
      int id = optionDAO.introduceOption(option);
      opt = optionDAO.getOption(id);
      dalServices.commitTransaction();
    } catch (Exception e) {
      e.printStackTrace();
      dalServices.rollbackTransaction();
    }
    return opt;
  }

  /**
   * cancel an option.
   *
   * @param idOption id of the option to cancel.
   * @return an OptionDTO that represent the canceled one.
   */
  @Override
  public OptionDTO cancelOption(
      int idOption) { // MAYBE more a deleat because after we will never reuse  the option
    OptionDTO opt = null;
    try {
      dalServices.startTransaction();
      optionDAO.cancelOption(idOption);
      opt=optionDAO.getOption(idOption);
      dalServices.commitTransaction();
    } catch (Exception e) {
      e.printStackTrace();
      dalServices.rollbackTransaction();
    }
    return opt;
  }
}
