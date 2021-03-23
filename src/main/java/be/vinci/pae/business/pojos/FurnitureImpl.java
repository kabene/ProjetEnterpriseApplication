package be.vinci.pae.business.pojos;

import be.vinci.pae.business.dto.FurnitureDTO;
import be.vinci.pae.business.dto.PhotoDTO;
import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.utils.Views;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonView;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(Include.NON_NULL)
public class FurnitureImpl implements FurnitureDTO {

  @JsonView(Views.Public.class)
  private Integer furnitureId;
  @JsonView(Views.AdminOnly.class)
  private Integer buyerId;
  @JsonView(Views.AdminOnly.class)
  private UserDTO buyer;
  @JsonView(Views.AdminOnly.class)
  private Integer sellerId;
  @JsonView(Views.AdminOnly.class)
  private UserDTO seller;
  @JsonView(Views.AdminOnly.class)
  private String condition;
  @JsonView(Views.AdminOnly.class)
  private String saleWithdrawalDate;
  @JsonView(Views.Public.class)
  private String description;
  @JsonView(Views.Public.class)
  private Integer typeId;
  @JsonView(Views.Public.class)
  private String type;
  @JsonView(Views.Public.class)
  private Integer favouritePhotoId;
  @JsonView(Views.Public.class)
  private PhotoDTO favouritePhoto;
  @JsonView(Views.Public.class)
  private List<PhotoDTO> photos;
  @JsonView(Views.Public.class)
  private Double sellingPrice;
  @JsonView(Views.AdminOnly.class)
  private Double specialSalePrice;
  @JsonView(Views.AdminOnly.class)
  private String dateOfSale;
  @JsonView(Views.AdminOnly.class)
  private Boolean toPickUp;
  @JsonView(Views.AdminOnly.class)
  private String pickUpDate;

  @Override
  public Integer getFurnitureId() {
    return furnitureId;
  }

  @Override
  public void setFurnitureId(Integer id) {
    this.furnitureId = id;
  }

  @Override
  public Integer getBuyerId() {
    return buyerId;
  }

  @Override
  public void setBuyerId(Integer id) {
    this.buyerId = id;
  }

  @Override
  public UserDTO getBuyer() {
    return buyer;
  }

  @Override
  public void setBuyer(UserDTO buyer) {
    this.buyer = buyer;
  }

  @Override
  public Integer getSellerId() {
    return sellerId;
  }

  @Override
  public void setSellerId(Integer id) {
    this.sellerId = id;
  }

  @Override
  public UserDTO getSeller() {
    return seller;
  }

  @Override
  public void setSeller(UserDTO seller) {
    this.seller = seller;
  }

  @Override
  public String getCondition() {
    return condition;
  }

  @Override
  public void setCondition(String cond) {
    this.condition = cond;
  }

  @Override
  public String getSaleWithdrawalDate() {
    return saleWithdrawalDate;
  }

  @Override
  public void setSaleWithdrawalDate(String date) {
    this.saleWithdrawalDate = date;
  }

  @Override
  public String getDescription() {
    return description;
  }

  @Override
  public void setDescription(String desc) {
    this.description = desc;
  }

  @Override
  public Integer getTypeId() {
    return typeId;
  }

  @Override
  public void setTypeId(Integer id) {
    this.typeId = id;
  }

  @Override
  public String getType() {
    return type;
  }

  @Override
  public void setType(String type) {
    this.type = type;
  }

  @Override
  public Integer getFavouritePhotoId() {
    return favouritePhotoId;
  }

  @Override
  public void setFavouritePhotoId(Integer id) {
    this.favouritePhotoId = id;
  }

  @Override
  public PhotoDTO getFavouritePhoto() {
    return this.favouritePhoto;
  }

  @Override
  public void setFavouritePhoto(PhotoDTO favPhoto) {
    this.favouritePhoto = favPhoto;
  }

  @Override
  public List<PhotoDTO> getPhotos() {
    return new ArrayList<>(photos);
  }

  @Override
  public void setPhotos(List<PhotoDTO> photos) {
    this.photos = new ArrayList<>(photos);
  }

  @Override
  public Double getSellingPrice() {
    return sellingPrice;
  }

  @Override
  public void setSellingPrice(Double price) {
    this.sellingPrice = price;
  }

  @Override
  public Double getSpecialSalePrice() {
    return specialSalePrice;
  }

  @Override
  public void setSpecialSalePrice(Double price) {
    this.specialSalePrice = price;
  }

  @Override
  public String getDateOfSale() {
    return dateOfSale;
  }

  @Override
  public void setDateOfSale(String date) {
    this.dateOfSale = date;
  }

  @Override
  public Boolean isToPickUp() {
    return toPickUp;
  }

  @Override
  public void setToPickUp(Boolean b) {
    this.toPickUp = b;
  }

  @Override
  public String getPickUpDate() {
    return pickUpDate;
  }

  @Override
  public void setPickUpDate(String date) {
    this.pickUpDate = date;
  }
}
