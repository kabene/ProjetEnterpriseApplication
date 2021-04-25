package be.vinci.pae.business.pojos;

import be.vinci.pae.business.dto.AddressDTO;
import be.vinci.pae.business.dto.RequestForVisitDTO;
import be.vinci.pae.business.dto.UserDTO;

public class RequestForVisitImpl implements RequestForVisitDTO {

  Integer requestId;
  String requestDate;
  String timeSlot;
  Integer addressId;
  AddressDTO address;
  String visitDateTime;
  String explanatoryNote;
  RequestStatus requestStatus;
  Integer userId;
  UserDTO user;


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
}
