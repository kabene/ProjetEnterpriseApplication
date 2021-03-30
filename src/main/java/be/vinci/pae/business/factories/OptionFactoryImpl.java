package be.vinci.pae.business.factories;

import be.vinci.pae.business.dto.OptionDTO;
import be.vinci.pae.business.pojos.OptionImpl;

public class OptionFactoryImpl implements OptionFactory{

  @Override
  public OptionDTO getOptionDTO() {
    return new OptionImpl();
  }
}
