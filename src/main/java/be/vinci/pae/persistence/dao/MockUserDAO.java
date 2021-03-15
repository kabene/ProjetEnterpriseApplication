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
  public List<UserDTO> getAllClients() {
    return null;
  }

  @Override
  public List<UserDTO> findWithFilters(String filter) {
    return null;
  }
}