package be.vinci.pae.buisness.pojos;

import be.vinci.pae.buisness.dto.UserDTO;

public interface User extends UserDTO {

    String getPassword();

    void setPassword(String password);

    boolean checkPassword(String password);

    String hashPassword(String password);

}
