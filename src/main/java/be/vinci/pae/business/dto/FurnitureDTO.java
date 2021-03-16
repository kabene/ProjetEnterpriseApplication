package be.vinci.pae.business.dto;

import java.time.LocalDate;

public interface FurnitureDTO {
  int getFurnitureId();
  void setFurnitureId(int id);

  int getBuyerId();
  void setBuyerId(int id);

  int getSellerId();
  void setSellerId(int id);

  String getCondition();
  void setCondition(String cond);

  LocalDate getSaleWithdrawalDate();
  void setSaleWithdrawalDate(LocalDate date);

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

  LocalDate getDateOfSale();
  void setDateOfSale(LocalDate date);

  boolean isToPickUp();
  void setToPickUp(boolean b);

  LocalDate getPickUpDate();
  void setPickUpDate(LocalDate date);
}
