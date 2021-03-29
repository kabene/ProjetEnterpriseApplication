package be.vinci.pae.business.ucc;

import be.vinci.pae.business.dto.FurnitureDTO;
import java.util.List;

public interface FurnitureUCC {

  FurnitureDTO getOne(int id);

  List<FurnitureDTO> getAll();

  FurnitureDTO toRestoration(int furnitureId);

  FurnitureDTO toAvailable(int id, double sellingPrice);

  FurnitureDTO withdraw(int furnitureId);
}
