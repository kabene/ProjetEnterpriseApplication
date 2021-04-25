package be.vinci.pae.business.factories;

import be.vinci.pae.business.dto.RequestForVisitDTO;
import be.vinci.pae.business.pojos.RequestForVisitImpl;

public class RequestForVisitFactoryImpl implements RequestForVisitFactory {
  @Override
  public RequestForVisitDTO getRequestForVisitDTO() {
    return new RequestForVisitImpl();
  }
}
