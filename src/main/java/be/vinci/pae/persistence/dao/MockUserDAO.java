package be.vinci.pae.persistence.dao;

import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.business.factories.UserFactory;
import jakarta.inject.Inject;
import java.util.List;

public class MockUserDAO implements UserDAO {

  @Inject
  private UserFactory uf;

  @Override
  public UserDTO findByUsername(String username) {
    UserDTO res = uf.getUserDTO();
    res.setUsername("ex");
    res.setID(1);
    res.setPassword("$2a$04$62XdSoqyDOBZWQCk/cuh1.OY/x3mnPi2wjcmDC0HCCzc7MVcj/VmW");
    return res;
  }

  @Override
  public UserDTO findById(int id) {
    UserDTO res = uf.getUserDTO();
    res.setUsername("ex");
    res.setID(1);
    res.setPassword("$2a$04$62XdSoqyDOBZWQCk/cuh1.OY/x3mnPi2wjcmDC0HCCzc7MVcj/VmW");
    return res;
  }

  @Override
  public boolean isAdmin(int id) {
    return false;
  }

  @Override
  public List<UserDTO> getAllCustomers() {
    return null;
  }

  @Override
  public List<UserDTO> findBySearch(String filter) {
    return null;
  }



  /**
   * used to register a new user.
   *
   * @param user   UserDTO that describe the user.
   * @param addressId id of the address.
   */
  @Override
  public void register(UserDTO user, int addressId) {

  }

  /**
   * verify if email is already taken.
   *
   * @param email string email.
   * @return a boolean true if email is already taken and false in other case.
   */
  @Override
  public boolean emailAlreadyTaken(String email) {
    return false;
  }

  /**
   * verify if the username is already taken.
   *
   * @param username string username.
   * @return a boolean true if username is already taken and false in other case.
   */
  @Override
  public boolean usernameAlreadyTaken(String username) {
    return false;
  }


}