package be.vinci.pae.business.pojos;

import be.vinci.pae.business.dto.FurnitureDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.time.LocalDate;

@JsonInclude(Include.NON_NULL)
public class FurnitureImpl implements FurnitureDTO {

  private int furnitureId;
  private int buyerId;
  private int sellerId;
  private String condition;
  private LocalDate saleWithdrawalDate;
  private String description;
  private int typeId;
  private int favouritePhotoId;
  private double sellingPrice;
  private double specialSalePrice;
  private LocalDate dateOfSale;
  private boolean toPickUp;
  private LocalDate pickUpDate;

  @Override
  public int getFurnitureId() {
    return furnitureId;
  }

  @Override
  public void setFurnitureId(int id) {
    this.furnitureId = id;
  }

  @Override
  public int getBuyerId() {
    return buyerId;
  }

  @Override
  public void setBuyerId(int id) {
    this.buyerId = id;
  }

  @Override
  public int getSellerId() {
    return sellerId;
  }

  @Override
  public void setSellerId(int id) {
    this.sellerId = id;
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
  public LocalDate getSaleWithdrawalDate() {
    return saleWithdrawalDate;
  }

  @Override
  public void setSaleWithdrawalDate(LocalDate date) {
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
  public int getTypeId() {
    return typeId;
  }

  @Override
  public void setTypeId(int id) {
    this.typeId = id;
  }

  @Override
  public int getFavouritePhotoId() {
    return favouritePhotoId;
  }

  @Override
  public void setFavouritePhotoId(int id) {
    this.favouritePhotoId = id;
  }

  @Override
  public double getSellingPrice() {
    return sellingPrice;
  }

  @Override
  public void setSellingPrice(double price) {
    this.sellingPrice = price;
  }

  @Override
  public double getSpecialSalePrice() {
    return specialSalePrice;
  }

  @Override
  public void setSpecialSalePrice(double price) {
    this.specialSalePrice = price;
  }

  @Override
  public LocalDate getDateOfSale() {
    return dateOfSale;
  }

  @Override
  public void setDateOfSale(LocalDate date) {
    this.dateOfSale = date;
  }

  @Override
  public boolean isToPickUp() {
    return toPickUp;
  }

  @Override
  public void setToPickUp(boolean b) {
    this.toPickUp = b;
  }

  @Override
  public LocalDate getPickUpDate() {
    return pickUpDate;
  }

  @Override
  public void setPickUpDate(LocalDate date) {
    this.pickUpDate = date;
  }
}
