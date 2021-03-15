package be.vinci.pae.business.pojos;

import be.vinci.pae.business.dto.AddresseDTO;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = AddressesImpl.class)
public interface Addresses extends AddresseDTO {

  @Override
  int getId();

  @Override
  void setId(int id);

  @Override
  String getStreet();

  @Override
  void setStreet(String street);

  @Override
  String getBuilding_number();

  @Override
  void setBuilding_number(String building_number);

  @Override
  String getUnit_number();

  @Override
  void setUnit_number(String unit_number);

  @Override
  int getPostcode();

  @Override
  void setPostcode(int postcode);

  @Override
  String getCommune();

  @Override
  void setCommune(String commune);

  @Override
  String getCountry();

  @Override
  void setCountry(String country);
}
