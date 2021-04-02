package be.vinci.pae.business.factories;

import be.vinci.pae.business.dto.PhotoDTO;
import be.vinci.pae.business.pojos.PhotoImpl;

public class PhotoFactoryImpl implements PhotoFactory {

  @Override
  public PhotoDTO getPhotoDTO() {
    return new PhotoImpl();
  }
}
