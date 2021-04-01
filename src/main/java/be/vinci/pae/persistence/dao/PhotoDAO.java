package be.vinci.pae.persistence.dao;

import be.vinci.pae.business.dto.PhotoDTO;
import java.util.List;

public interface PhotoDAO {

  /**
   * Finds all photos for a given furniture id.
   *
   * @param furnitureId : the furniture id.
   * @return a list of photoDTO
   */
  List<PhotoDTO> findAllByFurnitureId(int furnitureId);

  /**
   * Finds one photo by id.
   *
   * @param photoId : the researched photoId.
   * @return one instance of PhotoDTO
   */
  PhotoDTO getPhotoById(int photoId);


  /**
   * Finds all entries of photos in the DB.
   *
   * @return a list of photoDTO
   */
  List<PhotoDTO> findAll();

  /**
   * Finds all visible photos to display on the homepage
   *
   * @return a list containing all the photos visible on the home page's carousel.
   */
  List<PhotoDTO> getAllHomePageVisiblePhotos();
}
