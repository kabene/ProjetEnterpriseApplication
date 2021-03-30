package be.vinci.pae.business.ucc;

import be.vinci.pae.business.dto.PhotoDTO;
import be.vinci.pae.persistence.dal.ConnectionDalServices;
import be.vinci.pae.persistence.dao.PhotoDAO;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.List;

public class PhotoUCCImpl implements PhotoUCC {

  @Inject
  private PhotoDAO photoDAO;

  @Inject
  private ConnectionDalServices dalServices;

  /**
   * get all the photos that are visible on the home page's carousel.
   *
   * @return a list containing all the photos that are visible on the home page's carousel.
   */
  @Override
  public List<PhotoDTO> getAllHomePageVisiblePhotos() {
    List<PhotoDTO> res = new ArrayList<>();
    try {
      dalServices.startTransaction();
      res = photoDAO.getAllHomePageVisiblePhotos();
      dalServices.commitTransaction();
    } catch (Exception e) {
      e.printStackTrace();
      dalServices.rollbackTransaction();
    }
    return res;
  }
}
