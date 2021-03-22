package be.vinci.pae.business.dto;

import be.vinci.pae.business.pojos.PhotoImpl;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = PhotoImpl.class)
public interface PhotoDTO {
  int getPhotoId();
  void setPhotoId(int id);

  int getFurnitureId();
  void setFurnitureId(int id);

  boolean isOnHomepage();
  void setOnHomePage(boolean onHomePage);

  boolean isVisible();
  void setVisible(boolean visible);

  String getSource();
  void setSource(String source);
}
