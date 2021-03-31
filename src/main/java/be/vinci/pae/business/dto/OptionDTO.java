package be.vinci.pae.business.dto;

import be.vinci.pae.business.pojos.OptionImpl;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;


@JsonDeserialize(as = OptionImpl.class)
public interface OptionDTO {

  int getOptionId();

  void setOptionId(int optionId);

  int getDuration();

  void setDuration(int duration);

  String getDateOption();

  void setDateOption(String dateOption);

  int getUserId();

  void setUserId(int clientId);

  int getFurnitureId();

  void setFurnitureId(int furnitureId);

  boolean isCanceled();

  void setCanceled(boolean isCanceled);

}
