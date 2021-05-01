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
  private Integer optionId;
  @JsonView(Views.Public.class)
  private Integer duration;
  @JsonView(Views.Public.class)
  private String dateOption;
  @JsonView(Views.AdminOnly.class)
  private Integer userId;
  @JsonView(Views.Public.class)
  private Integer furnitureId;
  @JsonView(Views.Public.class)
  private Boolean isCanceled;
  @JsonView(Views.AdminOnly.class)
  private UserDTO user;

  @Override
  public Integer getOptionId() {
    return optionId;
  }

  @Override
  public void setOptionId(Integer optionId) {
    this.optionId = optionId;
  }

  @Override
  public Integer getDuration() {
    return duration;
  }

  @Override
  public void setDuration(Integer duration) {
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
  public Integer getUserId() {
    return userId;
  }

  @Override
  public void setUserId(Integer clientId) {
    this.userId = clientId;
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
  public Integer getFurnitureId() {
    return furnitureId;
  }

  @Override
  public void setFurnitureId(Integer furnitureId) {
    this.furnitureId = furnitureId;
  }

  @Override
  public Boolean isCanceled() {
    return isCanceled;
  }

  @Override
  public void setCanceled(Boolean isCanceled) {
    this.isCanceled = isCanceled;
  }
}
