package be.vinci.pae.business.pojos;

import be.vinci.pae.business.dto.FurnitureDTO;
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
  private FurnitureDTO furniture;
  @JsonView(Views.Public.class)
  private Boolean onHomePage;
  @JsonView(Views.Public.class)
  private Boolean isVisible;
  @JsonView(Views.AdminOnly.class)
  private Boolean isFromRequest;
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
  public FurnitureDTO getFurniture() {
    return this.furniture;
  }

  @Override
  public void setFurniture(FurnitureDTO furniture) {
    this.furniture = furniture;
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
    return isVisible;
  }

  @Override
  public void setVisible(Boolean isVisible) {
    this.isVisible = isVisible;
  }

  @Override
  public Boolean isFromRequest() {
    return this.isFromRequest;
  }

  @Override
  public void setFromRequest(Boolean isFromRequest) {
    this.isFromRequest = isFromRequest;
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
