package be.vinci.pae.business.ucc;

import be.vinci.pae.business.dto.FurnitureDTO;
import be.vinci.pae.business.factories.FurnitureFactory;
import jakarta.inject.Inject;

public class FurnitureUCCImpl implements FurnitureUCC{

  @Inject
  FurnitureFactory furnitureFactory;

  @Override
  public FurnitureDTO getOne(int id) {
    return furnitureFactory.getFurnitureDTO();
  }
}
