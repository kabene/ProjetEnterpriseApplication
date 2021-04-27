package be.vinci.pae.business.dto;

import be.vinci.pae.business.pojos.RequestForVisitImpl;
import be.vinci.pae.business.pojos.RequestStatus;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;

@JsonDeserialize(as = RequestForVisitImpl.class)
public interface RequestForVisitDTO {

  Integer getRequestId();

  void setRequestId(Integer requestId);

  String getRequestDate();

  void setRequestDate(String requestDate);

  String getTimeSlot();

  void setTimeSlot(String timeSlot);

  Integer getAddressId();

  void setAddressId(Integer addressId);

  AddressDTO getAddress();

  void setAddress(AddressDTO address);

  String getVisitDateTime();

  void setVisitDateTime(String visitDateTime);

  String getExplanatoryNote();

  void setExplanatoryNote(String explanatoryNote);

  RequestStatus getRequestStatus();

  void setRequestStatus(RequestStatus requestStatus);

  Integer getUserId();

  void setUserId(Integer userId);

  UserDTO getUser();

  void setUser(UserDTO user);

  List<FurnitureDTO> getFurnitureList();

  void setFurnitureList(List<FurnitureDTO> lst);
}
