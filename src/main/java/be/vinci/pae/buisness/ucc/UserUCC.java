package be.vinci.pae.buisness.ucc;

import be.vinci.pae.buisness.dto.UserDTO;

public interface UserUCC {
    UserDTO login(String login, String password);
}
