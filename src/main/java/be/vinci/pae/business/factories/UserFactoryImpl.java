package be.vinci.pae.business.factories;

import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.business.pojos.User;
import be.vinci.pae.business.pojos.UserImpl;

public class UserFactoryImpl implements UserFactory {

  @Override
  public UserDTO getUserDTO() {
    return new UserImpl();
  }
}
