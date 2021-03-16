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
  private String last_name;
  @JsonView(Views.Internal.class)
  private String first_name;
  @JsonView(Views.Internal.class)
  private String email;
  @JsonView(Views.Internal.class)
  private String role;
  @JsonView(Views.Internal.class)
  private AddressDTO address;

  public UserImpl() {
  }

  public UserImpl(String username, String password, String last_name, String first_name,
      String email, String role, AddressDTO add) {
    this.username = username;
    this.password = password;
    this.last_name = last_name;
    this.first_name = first_name;
    this.email = email;
    this.role = role;
    this.address = add;
  }

  @Override
  public String getLast_name() {
    return last_name;
  }

  @Override
  public void setLast_name(String last_name) {
    this.last_name = last_name;
  }

  @Override
  public String getFirst_name() {
    return first_name;
  }

  @Override
  public void setFirst_name(String first_name) {
    this.first_name = first_name;
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
