package be.vinci.pae.business.pojos;

import be.vinci.pae.business.dto.PhotoDTO;
import be.vinci.pae.utils.Views;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonView;

@JsonInclude(Include.NON_NULL)
public class PhotoImpl implements PhotoDTO {

  @JsonView(Views.Public.class)
  private Integer photoId;
  @JsonView(Views.Public.class)
  private Integer furnitureId;
  @JsonView(Views.Public.class)
  private Boolean onHomePage;
  @JsonView(Views.Public.class)
  private Boolean visible;
  @JsonView(Views.Public.class)
  private String source;

  @Override
  public Integer getPhotoId() {
    return photoId;
  }

  @Override
  public void setPhotoId(Integer id) {
    this.photoId = id;
  }

  @Override
  public Integer getFurnitureId() {
    return furnitureId;
  }

  @Override
  public void setFurnitureId(Integer id) {
    furnitureId = id;
  }

  @Override
  public Boolean isOnHomePage() {
    return onHomePage;
  }

  @Override
  public void setOnHomePage(Boolean onHomePage) {
    this.onHomePage = onHomePage;
  }

  @Override
  public Boolean isVisible() {
    return visible;
  }

  @Override
  public void setVisible(Boolean visible) {
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
