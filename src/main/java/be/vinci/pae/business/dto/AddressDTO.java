package be.vinci.pae.business.dto;


import be.vinci.pae.business.pojos.AddressImpl;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = AddressImpl.class)
public interface AddressDTO {

  Integer getId();

  void setId(Integer id);

  String getStreet();

  void setStreet(String street);

  String getBuildingNumber();

  void setBuildingNumber(String buildingNumber);

  String getUnitNumber();

  void setUnitNumber(String unitNumber);

  Integer getPostcode();

  void setPostcode(Integer postcode);

  String getCommune();

  void setCommune(String commune);

  String getCountry();

  void setCountry(String country);

}
