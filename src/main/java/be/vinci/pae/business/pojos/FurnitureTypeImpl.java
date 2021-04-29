package be.vinci.pae.business.pojos;

import be.vinci.pae.business.dto.FurnitureTypeDTO;
import be.vinci.pae.utils.Views;
import com.fasterxml.jackson.annotation.JsonView;

public class FurnitureTypeImpl implements FurnitureTypeDTO {

  @JsonView(Views.Public.class)
  private Integer typeId;

  @JsonView(Views.Public.class)
  private String typeName;

  @Override
  public Integer getTypeId() {
    return typeId;
  }

  @Override
  public void setTypeId(Integer typeId) {
    this.typeId = typeId;
  }

  @Override
  public String getTypeName() {
    return typeName;
  }

  @Override
  public void setTypeName(String typeName) {
    this.typeName = typeName;
  }
}
