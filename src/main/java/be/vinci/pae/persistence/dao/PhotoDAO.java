package be.vinci.pae.persistence.dao;

import be.vinci.pae.business.dto.PhotoDTO;
import java.util.List;

public interface PhotoDAO {

  List<PhotoDTO> getPhotosByFurnitureId(int furnitureId);

  PhotoDTO getPhotoById(int photoId);

  /**
   * Make a query to the database to get all the photos visible on the home page's carousel.
   *
   * @return a list containing all the photos visible on the home page's carousel.
   */
  List<PhotoDTO> getAllHomePageVisiblePhotos();
}
