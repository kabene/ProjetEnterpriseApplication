package be.vinci.pae.business.pojos;

import be.vinci.pae.business.dto.PhotoDTO;
import be.vinci.pae.utils.Views;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonView;

@JsonInclude(Include.NON_NULL)
public class PhotoImpl implements PhotoDTO {

  @JsonView(Views.Public.class)
  private int photoId;
  @JsonView(Views.Public.class)
  private int furnitureId;
  @JsonView(Views.Public.class)
  private boolean onHomePage;
  @JsonView(Views.Public.class)
  private boolean visible;
  @JsonView(Views.Public.class)
  private String source;

  @Override
  public int getPhotoId() {
    return photoId;
  }

  @Override
  public void setPhotoId(int id) {
    this.photoId = id;
  }

  @Override
  public int getFurnitureId() {
    return furnitureId;
  }

  @Override
  public void setFurnitureId(int id) {
    furnitureId = id;
  }

  @Override
  public boolean isOnHomepage() {
    return onHomePage;
  }

  @Override
  public void setOnHomePage(boolean onHomePage) {
    this.onHomePage = onHomePage;
  }

  @Override
  public boolean isVisible() {
    return visible;
  }

  @Override
  public void setVisible(boolean visible) {
    this.visible = visible;
  }

  @Override
  public String getSource() {
    return source;
  }

  @Override
  public void setSource(String source) {
    this.source = source;
  }
}
