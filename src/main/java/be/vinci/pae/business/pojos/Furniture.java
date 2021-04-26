package be.vinci.pae.business.pojos;

import be.vinci.pae.business.dto.FurnitureDTO;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = FurnitureImpl.class)
public interface Furniture extends FurnitureDTO {

  /**
   * Removes all photos that doesn't have the isVisible flag set to true from its 'photos' list.
   */
  void removeInvisiblePhotos();
}
