package be.vinci.pae.business.pojos;

import be.vinci.pae.business.dto.AddressDTO;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = AddressesImpl.class)
public interface Addresses extends AddressDTO {


}
