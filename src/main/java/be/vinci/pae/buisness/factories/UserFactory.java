package be.vinci.pae.buisness.factories;

import be.vinci.pae.buisness.dto.UserDTO;
import be.vinci.pae.buisness.pojos.User;

public interface UserFactory {

    User getUser();

    UserDTO getUserDTO();

}
