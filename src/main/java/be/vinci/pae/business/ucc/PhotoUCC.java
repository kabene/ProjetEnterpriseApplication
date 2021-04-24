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
   * Add a photo.
   * @param furnitureId id of the furniture linked to the picture.
   * @param source the base64 img.
   * @return the photoDTO.
   */
  PhotoDTO add(Integer furnitureId, String source);
}
