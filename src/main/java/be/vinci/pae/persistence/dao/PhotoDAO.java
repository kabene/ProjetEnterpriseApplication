package be.vinci.pae.persistence.dao;

import be.vinci.pae.business.dto.PhotoDTO;
import java.util.List;

public interface PhotoDAO {

  List<PhotoDTO> getPhotosByFurnitureId(int furnitureId);

  PhotoDTO getPhotoById(int photoId);
}
