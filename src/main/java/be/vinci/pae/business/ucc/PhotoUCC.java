package be.vinci.pae.business.ucc;

import be.vinci.pae.business.dto.PhotoDTO;

import be.vinci.pae.business.dto.UserDTO;
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
   *
   * @param furnitureId id of the furniture linked to the picture.
   * @param source      the base64 img.
   * @return the photoDTO.
   */
  PhotoDTO add(Integer furnitureId, String source);

  /**
   * updates one photo's display flags (isVisible & isOnHomePage) by id.
   *
   * @param id           : the photo's id
   * @param isVisible    : the photo's new visibility flag.
   * @param isOnHomePage : the photo's new isOnHomePage flag.
   * @return the modified resource as PhotoDTO
   */
  PhotoDTO patchDisplayFlags(int id, boolean isVisible, boolean isOnHomePage);

  /**
   * Finds the favourite photo for a specific furniture id.
   *
   * @param furnitureId : furniture id
   * @return favourite photo as PhotoDTO
   */
  PhotoDTO getFavourite(int furnitureId);

  /**
   * Finds all photos for a specific furniture id.
   *
   * @param furnitureId : furniture id
   * @return list of PhotoDTO
   */
  List<PhotoDTO> getAllForFurniture(int furnitureId);

  /**
   * Finds all request photos for a specific furniture id. (verifies if the current user is the
   * owner of the request)
   *
   * @param currentUserDTO : dto containing current user information.
   * @param furnitureId    : furniture id.
   * @return PhotoDTO list
   */
  List<PhotoDTO> getRequestPhotos(UserDTO currentUserDTO, int furnitureId);
}
