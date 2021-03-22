package be.vinci.pae.business.dto;

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
