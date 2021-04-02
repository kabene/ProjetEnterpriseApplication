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
  @JsonView(Views.AdminOnly.class)
  private String street;
  @JsonView(Views.AdminOnly.class)
  private String buildingNumber;
  @JsonView(Views.AdminOnly.class)
  private String unitNumber;
  @JsonView(Views.AdminOnly.class)
  private int postcode;
  @JsonView(Views.AdminOnly.class)
  private String commune;
  @JsonView(Views.AdminOnly.class)
  private String country;


  @Override
  public Integer getId() {
    return id;
  }

  @Override
  public void setId(Integer id) {
    this.id = id;
  }

  @Override
  public String getStreet() {
    return street;
  }

  @Override
  public void setStreet(String street) {
    this.street = street;
  }

  @Override
  public String getBuildingNumber() {
    return buildingNumber;
  }

  @Override
  public void setBuildingNumber(String buildingNumber) {
    this.buildingNumber = buildingNumber;
  }

  @Override
  public String getUnitNumber() {
    return unitNumber;
  }

  @Override
  public void setUnitNumber(String unitNumber) {
    this.unitNumber = unitNumber;
  }

  @Override
  public Integer getPostcode() {
    return postcode;
  }

  @Override
  public void setPostcode(Integer postcode) {
    this.postcode = postcode;
  }

  @Override
  public String getCommune() {
    return commune;
  }

  @Override
  public void setCommune(String commune) {
    this.commune = commune;
  }

  @Override
  public String getCountry() {
    return country;
  }

  @Override
  public void setCountry(String country) {
    this.country = country;
  }


}
