package be.vinci.pae.business.ucc;

import be.vinci.pae.business.dto.OptionDTO;

import be.vinci.pae.exceptions.UnauthorizedException;
import be.vinci.pae.persistence.dal.ConnectionDalServices;
import be.vinci.pae.persistence.dao.OptionDAO;
import jakarta.inject.Inject;

public class OptionUCCImpl implements OptionUCC {

  @Inject
  private ConnectionDalServices dalServices;
  @Inject
  private OptionDAO optionDAO;


  /**
   * @param clientId    clientId.
   * @param furnitureId furnitureId.
   * @return OptionDTO  introduced.
   */
  @Override
  public OptionDTO introduceOption(int clientId, int furnitureId) {
    OptionDTO opt = null;
    try {
      dalServices.startTransaction();
      opt = optionDAO.introduceOption(clientId, furnitureId);
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
   * @param idUser   id of the user that want to cancel the option.
   * @return an OptionDTO that represent the canceled one.
   */
  @Override
  public OptionDTO cancelOption(int idUser,
      int idOption) { // MAYBE more a deleat because after we will never reuse  the option
    OptionDTO opt = null;
    try {
      dalServices.startTransaction();
      opt = optionDAO.getOption(idOption);
      if(opt.getClientId()!=idUser){
        throw new UnauthorizedException("not allowed to cancel the option");
      }
      optionDAO.cancelOption(idOption);
      opt = optionDAO.getOption(idOption);
      dalServices.commitTransaction();
    } catch (Exception e) {
      e.printStackTrace();
      dalServices.rollbackTransaction();
    }
    return opt;
  }
}
