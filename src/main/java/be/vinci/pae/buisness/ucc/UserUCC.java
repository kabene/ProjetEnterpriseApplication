package be.vinci.pae.buisness.ucc;

import be.vinci.pae.buisness.dto.UserDTO;

public interface UserUCC {

    /**
     * Logs in after checking the given credentials.
     * @param username : the given username
     * @param password : the given password
     * @return the corresponding UserUCC or null if invalid credentials
     */
    UserDTO login(String username, String password);
}
