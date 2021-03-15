package be.vinci.pae.persistence.dao;


import be.vinci.pae.business.dto.AddresseDTO;

public interface AddresseDAO {

  /**
   * Create a newAdress
   * @param adress AdressDTO describe the adress.
   */
  void newAdresse(AddresseDTO adress);



}
