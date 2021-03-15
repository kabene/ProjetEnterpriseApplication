package be.vinci.pae.business.dto;


import be.vinci.pae.business.pojos.AddressesImpl;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = AddressesImpl.class)
public interface AddressDTO {

  int getId();

  void setId(int id);

  String getStreet();

  void setStreet(String street);

  String getBuilding_number();

  void setBuilding_number(String building_number);

  String getUnit_number();

  void setUnit_number(String unit_number);

  int getPostcode();

  void setPostcode(int postcode);

  String getCommune();

  void setCommune(String commune);

  String getCountry();

  void setCountry(String country);

}
