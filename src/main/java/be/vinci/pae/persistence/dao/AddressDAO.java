package be.vinci.pae.persistence.dao;


import be.vinci.pae.business.dto.AddressDTO;
import java.util.List;

public interface AddressDAO {

  /**
   * Create a newAdress.
   *
   * @param address AdressDTO describe the adress.
   */
  void addAddress(AddressDTO address);

  /**
   * get the id of the address.
   *
   * @param address AdressDTO describe the adress.
   */
  int getId(AddressDTO address);

  /**
   * Finds an address with its id.
   *
   * @param addressId : the address' id.
   * @return the address as an AddressDTO
   */
  AddressDTO findById(int addressId);

  List<AddressDTO> findAll();
}
