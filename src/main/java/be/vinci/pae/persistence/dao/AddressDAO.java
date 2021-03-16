package be.vinci.pae.persistence.dao;


import be.vinci.pae.business.dto.AddressDTO;

public interface AddressDAO {

  /**
   * Create a newAdress
   * @param address AdressDTO describe the adress.
   */
  void newAdresse(AddressDTO address);

  /**
   * get the id of the address
   * @param address AdressDTO describe the adress.
   */
  int getId(AddressDTO address);



}
