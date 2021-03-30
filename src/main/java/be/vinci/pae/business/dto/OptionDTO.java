package be.vinci.pae.business.dto;

import be.vinci.pae.business.pojos.OptionImpl;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.sql.Timestamp;

@JsonDeserialize(as = OptionImpl.class)
public interface OptionDTO {

  int getOptionId();

  void setOptionId(int optionId);

  int getDuree();

  void setDuree(int duree);

  Timestamp getDateOption();

  void setDateOption(Timestamp dateOption);

  int getClientId();

  void setClientId(int clientId);

  int getFurnitureId();

  void setFurnitureId(int furnitureId);

  boolean isCanceled();

  void setCanceled(boolean canceled);

}
