package be.vinci.pae.business.factories;

import be.vinci.pae.business.dto.FurnitureTypeDTO;
import be.vinci.pae.business.pojos.FurnitureTypeImpl;

public class FurnitureTypeFactoryImpl implements FurnitureTypeFactory {

  @Override
  public FurnitureTypeDTO getFurnitureTypeDTO() {
    return new FurnitureTypeImpl();
  }
}
