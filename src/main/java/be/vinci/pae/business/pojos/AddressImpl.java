package be.vinci.pae.business.pojos;

import be.vinci.pae.business.dto.AddressDTO;
import be.vinci.pae.utils.Views;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonView;

@JsonInclude(Include.NON_NULL)
public class AddressImpl implements AddressDTO {

  @JsonView(Views.Public.class)
  private int id;
  @JsonView(Views.Internal.class)
  private String  street;
  @JsonView(Views.Internal.class)
  private String buildingNumber;
  @JsonView(Views.Internal.class)
  private String unitNumber;
  @JsonView(Views.Internal.class)
  private int postcode;
  @JsonView(Views.Internal.class)
  private String commune;
  @JsonView(Views.Internal.class)
  private String country;

  public AddressImpl() {
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getStreet() {
    return street;
  }

  public void setStreet(String street) {
    this.street = street;
  }

  public String getBuildingNumber() {
    return buildingNumber;
  }

  public void setBuildingNumber(String buildingNumber) {
    this.buildingNumber = buildingNumber;
  }

  public String getUnitNumber() {
    return unitNumber;
  }

  public void setUnitNumber(String unitNumber) {
    this.unitNumber = unitNumber;
  }

  public int getPostcode() {
    return postcode;
  }

  public void setPostcode(int postcode) {
    this.postcode = postcode;
  }

  public String getCommune() {
    return commune;
  }

  public void setCommune(String commune) {
    this.commune = commune;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }


}
