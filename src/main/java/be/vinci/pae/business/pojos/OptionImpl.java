package be.vinci.pae.business.pojos;

import be.vinci.pae.business.dto.OptionDTO;
import be.vinci.pae.utils.Views;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonView;
import java.sql.Timestamp;

@JsonInclude(Include.NON_NULL)
public class OptionImpl implements OptionDTO {

  @JsonView(Views.Public.class)
  private int optionId;
  @JsonView(Views.AdminOnly.class)
  private int duree;
  @JsonView(Views.AdminOnly.class)
  private Timestamp dateOption;
  @JsonView(Views.AdminOnly.class)
  private int clientId;
  @JsonView(Views.AdminOnly.class)
  private int furnitureId;
  @JsonView(Views.AdminOnly.class)
  private boolean isCanceled;

  @Override
  public int getOptionId() {
    return optionId;
  }

  @Override
  public void setOptionId(int optionId) {
    this.optionId = optionId;
  }

  @Override
  public int getDuree() {
    return duree;
  }

  @Override
  public void setDuree(int duree) {
    this.duree = duree;
  }

  @Override
  public Timestamp getDateOption() {
    return dateOption;
  }

  @Override
  public void setDateOption(Timestamp dateOption) {
    this.dateOption = dateOption;
  }

  @Override
  public int getClientId() {
    return clientId;
  }

  @Override
  public void setClientId(int clientId) {
    this.clientId = clientId;
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
  public void setCanceled(boolean canceled) {
    isCanceled = canceled;
  }
}
