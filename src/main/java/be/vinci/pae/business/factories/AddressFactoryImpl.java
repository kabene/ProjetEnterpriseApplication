package be.vinci.pae.business.factories;

import be.vinci.pae.business.dto.AddressDTO;
import be.vinci.pae.business.pojos.AddressImpl;


public class AddressFactoryImpl implements AddressFactory {

  @Override
  public AddressDTO getAddressDTO() {
    return new AddressImpl();
  }
}
