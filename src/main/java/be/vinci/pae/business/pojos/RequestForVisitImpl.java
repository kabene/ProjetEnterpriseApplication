package be.vinci.pae.business.pojos;

import be.vinci.pae.business.dto.AddressDTO;
import be.vinci.pae.business.dto.FurnitureDTO;
import be.vinci.pae.business.dto.RequestForVisitDTO;
import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.utils.Views;
import com.fasterxml.jackson.annotation.JsonView;
import java.util.List;

public class RequestForVisitImpl implements RequestForVisitDTO {

  @JsonView(Views.AdminOnly.class)
  Integer requestId;
  @JsonView(Views.AdminOnly.class)
  String requestDate;
  @JsonView(Views.AdminOnly.class)
  String timeSlot;
  @JsonView(Views.AdminOnly.class)
  Integer addressId;
  @JsonView(Views.AdminOnly.class)
  AddressDTO address;
  @JsonView(Views.AdminOnly.class)
  String visitDateTime;
  @JsonView(Views.AdminOnly.class)
  String explanatoryNote;
  @JsonView(Views.AdminOnly.class)
  RequestStatus requestStatus;
  @JsonView(Views.AdminOnly.class)
  Integer userId;
  @JsonView(Views.AdminOnly.class)
  UserDTO user;
  @JsonView(Views.AdminOnly.class)
  List<FurnitureDTO> furnitureList;


  @Override
  public Integer getRequestId() {
    return this.requestId;
  }

  @Override
  public void setRequestId(Integer requestId) {
    this.requestId = requestId;
  }

  @Override
  public String getRequestDate() {
    return this.requestDate;
  }

  @Override
  public void setRequestDate(String requestDate) {
    this.requestDate = requestDate;
  }

  @Override
  public String getTimeSlot() {
    return this.timeSlot;
  }

  @Override
  public void setTimeSlot(String timeSlot) {
    this.timeSlot = timeSlot;
  }

  @Override
  public Integer getAddressId() {
    return this.addressId;
  }

  @Override
  public void setAddressId(Integer addressId) {
    this.addressId = addressId;
  }

  @Override
  public AddressDTO getAddress() {
    return this.address;
  }

  @Override
  public void setAddress(AddressDTO address) {
    this.address = address;
  }

  @Override
  public String getVisitDateTime() {
    return this.visitDateTime;
  }

  @Override
  public void setVisitDateTime(String visitDateTime) {
    this.visitDateTime = visitDateTime;
  }

  @Override
  public String getExplanatoryNote() {
    return this.explanatoryNote;
  }

  @Override
  public void setExplanatoryNote(String explanatoryNote) {
    this.explanatoryNote = explanatoryNote;
  }

  @Override
  public RequestStatus getRequestStatus() {
    return this.requestStatus;
  }

  @Override
  public void setRequestStatus(RequestStatus requestStatus) {
    this.requestStatus = requestStatus;
  }

  @Override
  public Integer getUserId() {
    return this.userId;
  }

  @Override
  public void setUserId(Integer userId) {
    this.userId = userId;
  }

  @Override
  public UserDTO getUser() {
    return this.user;
  }

  @Override
  public void setUser(UserDTO user) {
    this.user = user;
  }

  @Override
  public List<FurnitureDTO> getFurnitureList() {
    return furnitureList;
  }

  @Override
  public void setFurnitureList(List<FurnitureDTO> lst) {
    this.furnitureList = lst;
  }
}
