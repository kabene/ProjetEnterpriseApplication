package be.vinci.pae.main;

import be.vinci.pae.business.ucc.*;
import be.vinci.pae.persistence.dal.DalServicesImpl;
import be.vinci.pae.persistence.dao.*;
import be.vinci.pae.persistence.dal.ConnectionDalServices;
import jakarta.inject.Singleton;
import jakarta.ws.rs.ext.Provider;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.mockito.Mockito;

@Provider
public class TestBinder extends AbstractBinder {

  @Override
  protected void configure() {
    //addresses
    bind(Mockito.mock(AddressDAO.class)).to(AddressDAO.class);

    //users
    bind(UserUCCImpl.class).to(UserUCC.class).in(Singleton.class);
    bind(Mockito.mock(UserDAOImpl.class)).to(UserDAO.class);

    //furniture
    bind(FurnitureUCCImpl.class).to(FurnitureUCC.class).in(Singleton.class);
    bind(Mockito.mock(FurnitureDAOImpl.class)).to(FurnitureDAO.class);

    //furniture types
    bind(Mockito.mock(FurnitureTypeDAOImpl.class)).to(FurnitureTypeDAO.class);

    //photos
    bind(PhotoUCCImpl.class).to(PhotoUCC.class).in(Singleton.class);
    bind(Mockito.mock(PhotoDAOImpl.class)).to(PhotoDAO.class);

    //options
    bind(OptionUCCImpl.class).to(OptionUCC.class).in(Singleton.class);
    bind(Mockito.mock(OptionDAOImpl.class)).to(OptionDAO.class);

    //request_for_visit
    bind(RequestForVisitUCCImpl.class).to(RequestForVisitUCC.class).in(Singleton.class);
    bind(Mockito.mock(RequestForVisitDAOImpl.class)).to(RequestForVisitDAO.class);

    //other services
    bind(Mockito.mock(DalServicesImpl.class)).to(ConnectionDalServices.class);
  }
}
