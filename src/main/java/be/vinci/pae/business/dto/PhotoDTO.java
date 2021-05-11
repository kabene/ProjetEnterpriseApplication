package be.vinci.pae.business.dto;

import be.vinci.pae.business.pojos.PhotoImpl;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = PhotoImpl.class)
public interface PhotoDTO {

  Integer getPhotoId();

  void setPhotoId(Integer id);

  Integer getFurnitureId();

  void setFurnitureId(Integer id);

  FurnitureDTO getFurniture();

  void setFurniture(FurnitureDTO furniture);

  Boolean isOnHomePage();

  void setOnHomePage(Boolean onHomePage);

  Boolean isVisible();

  void setVisible(Boolean isVisible);

  Boolean isFromRequest();

  void setFromRequest(Boolean isFromRequest);

  String getSource();

  void setSource(String source);
}
