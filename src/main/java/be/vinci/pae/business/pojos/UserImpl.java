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
  private int id;
  @JsonView(Views.Public.class)
  private String username;
  @JsonView(Views.Internal.class)
  private String password;
  @JsonView(Views.Internal.class)
  private String lastName;
  @JsonView(Views.Internal.class)
  private String firstName;
  @JsonView(Views.Internal.class)
  private String email;
  @JsonView(Views.Internal.class)
  private String role;
  @JsonView(Views.Internal.class)
  private AddressDTO address;

  public UserImpl() {
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
  public int getID() {
    return id;
  }

  @Override
  public void setID(int id) {
    this.id = id;
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
  public boolean checkPassword(String plainText) {
    return BCrypt.checkpw(plainText, this.password);
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
  public String hashPassword(String password) {
    return BCrypt.hashpw(password, BCrypt.gensalt());
  }
}
