package be.vinci.pae.persistence.dao;

import be.vinci.pae.business.dto.PhotoDTO;
import be.vinci.pae.business.factories.PhotoFactory;
import be.vinci.pae.persistence.dal.ConnectionBackendDalServices;
import jakarta.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class PhotoDAOImpl implements PhotoDAO{

  @Inject
  private ConnectionBackendDalServices dalServices;
  @Inject
  private PhotoFactory photoFactory;

  //TODO: implement functions
  @Override
  public List<PhotoDTO> getPhotosByFurnitureId(int FurnitureId) {
    List<PhotoDTO> res = new ArrayList<>();
    return res;
  }

  @Override
  public PhotoDTO getPhotoById(int photoId) {
    return null;
  }
}
