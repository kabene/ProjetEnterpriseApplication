package be.vinci.pae.persistence.dao;

import be.vinci.pae.buisness.dto.UserDTO;

public interface UserDAO {

    /**
     * Executes a query to find a user having a specific username.
     * @param username : the username to look for
     * @return a UserDTO corresponding to the user found or null if no user has the given username.
     */
    UserDTO findByUsername(String username);
}
