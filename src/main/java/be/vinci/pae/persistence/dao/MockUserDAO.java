package be.vinci.pae.persistence.dao;

import be.vinci.pae.business.dto.UserDTO;

public class MockUserDAO implements UserDAO {

  @Override
  public UserDTO findByUsername(String username) {
    return null; // generated stub
  }
}
