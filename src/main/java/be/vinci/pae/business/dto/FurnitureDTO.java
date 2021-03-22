package be.vinci.pae.business.dto;

import be.vinci.pae.business.pojos.FurnitureImpl;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = FurnitureImpl.class)
public interface FurnitureDTO {

  int getFurnitureId();

  void setFurnitureId(int id);

  int getBuyerId();

  void setBuyerId(int id);

  UserDTO getBuyer();

  void setBuyer(UserDTO buyer);

  int getSellerId();

  void setSellerId(int id);

  UserDTO getSeller();

  void setSeller(UserDTO buyer);

  String getCondition();

  void setCondition(String cond);

  String getSaleWithdrawalDate();

  void setSaleWithdrawalDate(String date);

  String getDescription();

  void setDescription(String desc);

  int getTypeId();

  void setTypeId(int id);

  int getFavouritePhotoId();

  void setFavouritePhotoId(int id);

  double getSellingPrice();

  void setSellingPrice(double price);

  double getSpecialSalePrice();

  void setSpecialSalePrice(double price);

  String getDateOfSale();

  void setDateOfSale(String date);

  boolean isToPickUp();

  void setToPickUp(boolean b);

  String getPickUpDate();

  void setPickUpDate(String date);
}
