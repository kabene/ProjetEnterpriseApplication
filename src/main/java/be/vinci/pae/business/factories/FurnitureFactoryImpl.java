package be.vinci.pae.business.factories;

import be.vinci.pae.business.dto.FurnitureDTO;
import be.vinci.pae.business.pojos.FurnitureImpl;

public class FurnitureFactoryImpl implements FurnitureFactory {

  @Override
  public FurnitureDTO getFurnitureDTO() {
    return new FurnitureImpl();
  }
}
