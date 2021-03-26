package be.vinci.pae.business.dto;

import be.vinci.pae.business.pojos.UserImpl;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = UserImpl.class)
public interface UserDTO {

  Integer getId();

  void setId(Integer id);

  String getUsername();

  void setUsername(String username);

  String getPassword();

  void setPassword(String password);

  String getLastName();

  void setLastName(String lastName);

  String getFirstName();

  void setFirstName(String firstName);

  String getEmail();

  void setEmail(String email);

  String getRole();

  void setRole(String role);

  Integer getAddressId();

  void setAddressId(Integer addressId);

  AddressDTO getAddress();

  void setAddress(AddressDTO addressDTO);

  String getRegistrationDate();

  void setRegistrationDate(String registrationDate);

  Integer getPurchasedFurnitureNbr();

  void setPurchasedFurnitureNbr(Integer purchasedFurnitureNbr);

  Integer getSoldFurnitureNbr();

  void setSoldFurnitureNbr(Integer soldFurnitureNbr);

  Boolean isWaiting();

  void setWaiting(Boolean waiting);
}
