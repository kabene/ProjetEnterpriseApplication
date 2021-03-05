package be.vinci.pae.persistence.dao;

import be.vinci.pae.buisness.dto.UserDTO;

public interface UserDAO {
    UserDTO findByUsername(String username);
}
