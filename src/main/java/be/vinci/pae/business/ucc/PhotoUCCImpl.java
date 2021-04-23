package be.vinci.pae.business.ucc;

import be.vinci.pae.business.dto.PhotoDTO;
import be.vinci.pae.persistence.dal.ConnectionDalServices;
import be.vinci.pae.persistence.dao.PhotoDAO;
import jakarta.inject.Inject;
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
    List<PhotoDTO> res;
    try {
      dalServices.startTransaction();
      res = photoDAO.getAllHomePageVisiblePhotos();
      dalServices.commitTransaction();
    } catch (Throwable e) {
      dalServices.rollbackTransaction();
      throw e;
    }
    return res;
  }

  /**
   * @param furnitureId
   * @param source
   * @return
   */
  @Override
  public PhotoDTO add(Integer furnitureId, String source) {
    PhotoDTO res;
    try {
      dalServices.startTransaction();
      int id = photoDAO.insert(furnitureId, source);
      res = photoDAO.getPhotoById(id);
      dalServices.commitTransaction();
    }catch (Throwable e) {
      dalServices.rollbackTransaction();
      throw e;
    }
    return res;
  }
}
