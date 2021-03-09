package be.vinci.pae.persistence.dao;

import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.business.pojos.User;

public class MockUserDAO implements UserDAO {

  @Override
  public User findByUsername(String username) {
    return null; // generated stub
  }

  @Override
  public UserDTO findById(int id) {
    return null; // generated stub
  }
}
