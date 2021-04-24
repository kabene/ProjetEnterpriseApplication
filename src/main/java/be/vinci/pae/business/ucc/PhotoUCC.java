package be.vinci.pae.business.ucc;

import be.vinci.pae.business.dto.PhotoDTO;

import java.util.List;

public interface PhotoUCC {

  /**
   * get all the photos that are visible on the home page's carousel.
   *
   * @return a list containing all the photos that are visible on the home page's carousel.
   */
  List<PhotoDTO> getAllHomePageVisiblePhotos();

  /**
   * updates one photo's display flags (isVisible & isOnHomePage) by id.
   *
   * @param id : the photo's id
   * @param isVisible : the photo's new visibility flag.
   * @param isOnHomePage : the photo's new isOnHomePage flag.
   * @return the modified resource as PhotoDTO
   */
  PhotoDTO patchDisplayFlags(int id, boolean isVisible, boolean isOnHomePage);
}
