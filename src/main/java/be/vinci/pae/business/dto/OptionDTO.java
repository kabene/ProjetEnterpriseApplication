package be.vinci.pae.business.dto;

import be.vinci.pae.business.pojos.OptionImpl;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;


@JsonDeserialize(as = OptionImpl.class)
public interface OptionDTO {

  Integer getOptionId();

  void setOptionId(Integer optionId);

  Integer getDuration();

  void setDuration(Integer duration);

  String getDateOption();

  void setDateOption(String dateOption);

  Integer getUserId();

  void setUserId(Integer clientId);

  UserDTO getUser();

  void setUser(UserDTO user);

  Integer getFurnitureId();

  void setFurnitureId(Integer furnitureId);

  Boolean isCanceled();

  void setCanceled(Boolean isCanceled);

}
