package be.vinci.pae.business.ucc;

import be.vinci.pae.business.dto.FurnitureDTO;
import java.util.List;

public interface FurnitureUCC {
  FurnitureDTO getOne(int id);

  List<FurnitureDTO> getDetailAll();
}
