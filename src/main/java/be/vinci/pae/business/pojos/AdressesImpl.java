package be.vinci.pae.business.pojos;

import be.vinci.pae.utils.Views;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonView;

@JsonInclude(Include.NON_NULL)
public class AdressesImpl implements Adresses {

  @JsonView(Views.Public.class)
  private int id;
  @JsonView(Views.Public.class)
  private String  street;
  @JsonView(Views.Public.class)
  private String building_number;
  @JsonView(Views.Public.class)
  private String unit_number;
  @JsonView(Views.Public.class)
  private int postcode;
  @JsonView(Views.Public.class)
  private String commune;
  @JsonView(Views.Public.class)
  private String country;

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

  public String getBuilding_number() {
    return building_number;
  }

  public void setBuilding_number(String building_number) {
    this.building_number = building_number;
  }

  public String getUnit_number() {
    return unit_number;
  }

  public void setUnit_number(String unit_number) {
    this.unit_number = unit_number;
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
