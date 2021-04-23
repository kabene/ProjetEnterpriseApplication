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
   *
   * @param furnitureId
   * @param source
   * @return
   */
  PhotoDTO add(Integer furnitureId, String source);
}
