package be.vinci.pae.persistence.dao;


import be.vinci.pae.business.dto.AddressDTO;
import java.util.List;

public interface AddressDAO {

  /**
   * Create a newAddress.
   *
   * @param address AddressDTO describe the address.
   */
  void addAddress(AddressDTO address);

  /**
   * get the id of the address.
   *
   * @param address AddressDTO describe the address.
   */
  int getId(AddressDTO address);

  /**
   * Finds an address with its id.
   *
   * @param addressId : the address' id.
   * @return the address as an AddressDTO
   */
  AddressDTO findById(int addressId);

  /**
   * Finds all entries of addresses in the DB.
   *
   * @return a list of addressDTO
   */
  List<AddressDTO> findAll();
}
