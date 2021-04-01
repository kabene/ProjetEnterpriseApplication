package be.vinci.pae.business.pojos;

import be.vinci.pae.business.dto.OptionDTO;
import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.utils.Views;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonView;

@JsonInclude(Include.NON_NULL)
public class OptionImpl implements OptionDTO {

  @JsonView(Views.Public.class)
  private int optionId;
  @JsonView(Views.Public.class)
  private int duration;
  @JsonView(Views.Public.class)
  private String dateOption;
  @JsonView(Views.Public.class)
  private int clientId;
  @JsonView(Views.Public.class)
  private int furnitureId;
  @JsonView(Views.Public.class)
  private boolean isCanceled;
  @JsonView(Views.AdminOnly.class)
  private UserDTO user;

  @Override
  public int getOptionId() {
    return optionId;
  }

  @Override
  public void setOptionId(int optionId) {
    this.optionId = optionId;
  }

  @Override
  public int getDuration() {
    return duration;
  }

  @Override
  public void setDuration(int duration) {
    this.duration = duration;
  }

  @Override
  public String getDateOption() {
    return dateOption;
  }

  @Override
  public void setDateOption(String dateOption) {
    this.dateOption = dateOption;
  }

  @Override
  public int getUserId() {
    return clientId;
  }

  @Override
  public void setUserId(int clientId) {
    this.clientId = clientId;
  }

  @Override
  public UserDTO getUser() {
    return user;
  }

  @Override
  public void setUser(UserDTO user) {
    this.user = user;
  }

  @Override
  public int getFurnitureId() {
    return furnitureId;
  }

  @Override
  public void setFurnitureId(int furnitureId) {
    this.furnitureId = furnitureId;
  }

  @Override
  public boolean isCanceled() {
    return isCanceled;
  }

  @Override
  public void setCanceled(boolean isCanceled) {
    this.isCanceled = isCanceled;
  }
}
