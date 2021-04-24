package be.vinci.pae.business.ucc;

import be.vinci.pae.business.dto.PhotoDTO;
import be.vinci.pae.exceptions.BadRequestException;
import be.vinci.pae.exceptions.ConflictException;
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
   * updates one photo's isVisible by id.
   *
   * @param id         : the photo's id
   * @param isVisible : the photo's new isVisible flag.
   * @return the modified resource as PhotoDTO
   */
  @Override
  public PhotoDTO patchDisplayFlags(int id, boolean isVisible, boolean isOnHomePage) {
    PhotoDTO res;
    try {
      dalServices.startTransaction();
      PhotoDTO foundDto = photoDAO.getPhotoById(id);
      if (!isVisible && isOnHomePage) {
        throw new ConflictException(
            "Error: impossible flag configuration "
                + "(non-visible photos cannot be displayed on the homepage)");
      }
      //TODO: verify origin
      foundDto.setVisible(isVisible);
      foundDto.setOnHomePage(isOnHomePage);

      res = photoDAO.updateDisplayFlags(foundDto);
      dalServices.commitTransaction();
    } catch (Throwable e) {
      dalServices.rollbackTransaction();
      throw e;
    }
    return res;
  }

}
