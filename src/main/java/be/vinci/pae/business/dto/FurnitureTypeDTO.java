package be.vinci.pae.business.dto;

import be.vinci.pae.business.pojos.FurnitureTypeImpl;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = FurnitureTypeImpl.class)
public interface FurnitureTypeDTO {

  Integer getTypeId();

  void setTypeId(Integer typeId);

  String getTypeName();

  void setTypeName(String typeName);
}
