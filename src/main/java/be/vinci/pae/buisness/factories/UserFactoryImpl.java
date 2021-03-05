package be.vinci.pae.buisness.factories;

import be.vinci.pae.buisness.dto.UserDTO;
import be.vinci.pae.buisness.pojos.User;
import be.vinci.pae.buisness.pojos.UserImpl;

public class UserFactoryImpl implements UserFactory {

    @Override
    public User getUser() {
        return new UserImpl();
    }

    @Override
    public UserDTO getUserDTO() {
        return new UserImpl();
    }

}
