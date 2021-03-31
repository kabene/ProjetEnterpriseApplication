package be.vinci.pae.business.pojos;

import be.vinci.pae.business.dto.AddressDTO;
import be.vinci.pae.utils.Views;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonView;
import org.mindrot.jbcrypt.BCrypt;

@JsonInclude(Include.NON_NULL)
public class UserImpl implements User {

  @JsonView(Views.Public.class)
  private Integer id;
  @JsonView(Views.Public.class)
  private String username;
  @JsonView(Views.Internal.class)
  private String password;
  @JsonView(Views.AdminOnly.class)
  private String lastName;
  @JsonView(Views.AdminOnly.class)
  private String firstName;
  @JsonView(Views.AdminOnly.class)
  private String email;
  @JsonView(Views.AdminOnly.class)
  private String role;
  @JsonView(Views.AdminOnly.class)
  private Integer addressId;
  @JsonView(Views.AdminOnly.class)
  private AddressDTO address;
  @JsonView(Views.AdminOnly.class)
  private String registrationDate;
  @JsonView(Views.AdminOnly.class)
  private Integer purchasedFurnitureNbr;
  @JsonView(Views.AdminOnly.class)
  private Integer soldFurnitureNbr;
  @JsonView(Views.AdminOnly.class)
  private Boolean isWaiting;

  @Override
  public Integer getId() {
    return id;
  }

  @Override
  public void setId(Integer id) {
    this.id = id;
  }

  @Override
  public String getLastName() {
    return lastName;
  }

  @Override
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  @Override
  public String getFirstName() {
    return firstName;
  }

  @Override
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  @Override
  public String getEmail() {
    return email;
  }

  @Override
  public void setEmail(String email) {
    this.email = email;
  }

  @Override
  public String getRole() {
    return role;
  }

  @Override
  public void setRole(String role) {
    this.role = role;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public void setUsername(String username) {
    this.username = username;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public void setPassword(String password) {
    this.password = password;
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
  public void setAddress(AddressDTO addressDTO) {
    this.address = addressDTO;
  }


  @Override
  public String getRegistrationDate() {
    return registrationDate;
  }

  @Override
  public void setRegistrationDate(String registrationDate) {
    this.registrationDate = registrationDate;
  }

  @Override
  public Integer getPurchasedFurnitureNbr() {
    return purchasedFurnitureNbr;
  }

  @Override
  public void setPurchasedFurnitureNbr(Integer purchasedFurnitureNbr) {
    this.purchasedFurnitureNbr = purchasedFurnitureNbr;
  }

  @Override
  public Integer getSoldFurnitureNbr() {
    return soldFurnitureNbr;
  }

  @Override
  public void setSoldFurnitureNbr(Integer soldFurnitureNbr) {
    this.soldFurnitureNbr = soldFurnitureNbr;
  }

  @Override
  public Boolean isWaiting() {
    return isWaiting;
  }

  @Override
  public void setWaiting(Boolean waiting) {
    this.isWaiting = waiting;
  }


  @Override
  public boolean checkPassword(String plainText) {
    return BCrypt.checkpw(plainText, this.password);
  }

  @Override
  public String hashPassword(String password) {
    return BCrypt.hashpw(password, BCrypt.gensalt());
  }
}
