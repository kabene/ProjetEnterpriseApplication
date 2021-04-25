package be.vinci.pae.business.dto;

import be.vinci.pae.business.pojos.FurnitureImpl;
import be.vinci.pae.business.pojos.FurnitureStatus;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;

@JsonDeserialize(as = FurnitureImpl.class)
public interface FurnitureDTO {

  Integer getFurnitureId();

  void setFurnitureId(Integer id);

  Integer getBuyerId();

  void setBuyerId(Integer id);

  UserDTO getBuyer();

  void setBuyer(UserDTO buyer);

  Integer getSellerId();

  void setSellerId(Integer id);

  UserDTO getSeller();

  void setSeller(UserDTO buyer);

  FurnitureStatus getStatus();

  void setStatus(FurnitureStatus status);

  String getSaleWithdrawalDate();

  void setSaleWithdrawalDate(String date);

  String getDescription();

  void setDescription(String desc);

  Integer getTypeId();

  void setTypeId(Integer id);

  String getType();

  void setType(String type);

  Integer getFavouritePhotoId();

  void setFavouritePhotoId(Integer id);

  PhotoDTO getFavouritePhoto();

  void setFavouritePhoto(PhotoDTO favPhoto);

  List<PhotoDTO> getPhotos();

  void setPhotos(List<PhotoDTO> photos);

  Double getSellingPrice();

  void setSellingPrice(Double price);

  Double getSpecialSalePrice();

  void setSpecialSalePrice(Double price);

  String getDateOfSale();

  void setDateOfSale(String date);

  Boolean isToPickUp();

  void setToPickUp(Boolean b);

  String getPickUpDate();

  void setPickUpDate(String date);

  OptionDTO getOption();

  void setOption(OptionDTO option);

  Integer getRequestId();

  void setRequestId(Integer requestId);

  Double getPurchasePrice();

  void setPurchasePrice(Double purchasePrice);

  String getCustomerWithdrawalDate();

  void setCustomerWithdrawalDate(String customerWithdrawalDate);

  String getDepositDate();

  void setDepositDate(String depositDate);

  Boolean isSuitable();

  void setSuitable(Boolean isSuitable);

  Boolean isAvailableForSale();

  void setAvailableForSale(Boolean isAvailableForSale);
}
