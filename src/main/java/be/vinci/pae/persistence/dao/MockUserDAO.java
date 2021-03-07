package be.vinci.pae.persistence.dao;

import be.vinci.pae.business.pojos.User;

public class MockUserDAO implements UserDAO {

  @Override
  public User findByUsername(String username) {
    return null; // generated stub
  }
}
