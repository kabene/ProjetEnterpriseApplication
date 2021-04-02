package be.vinci.pae.main;

import be.vinci.pae.business.ucc.FurnitureUCC;
import be.vinci.pae.business.ucc.FurnitureUCCImpl;
import be.vinci.pae.business.ucc.OptionUCC;
import be.vinci.pae.business.ucc.OptionUCCImpl;
import be.vinci.pae.business.ucc.PhotoUCC;
import be.vinci.pae.business.ucc.PhotoUCCImpl;
import be.vinci.pae.business.ucc.UserUCCImpl;
import be.vinci.pae.persistence.dal.DalServicesImpl;
import be.vinci.pae.persistence.dao.AddressDAO;
import be.vinci.pae.persistence.dao.FurnitureDAO;
import be.vinci.pae.persistence.dao.FurnitureDAOImpl;
import be.vinci.pae.persistence.dao.FurnitureTypeDAO;
import be.vinci.pae.persistence.dao.FurnitureTypeDAOImpl;
import be.vinci.pae.persistence.dao.OptionDAO;
import be.vinci.pae.persistence.dao.OptionDAOImpl;
import be.vinci.pae.persistence.dao.PhotoDAO;
import be.vinci.pae.persistence.dao.PhotoDAOImpl;
import be.vinci.pae.business.ucc.UserUCC;
import be.vinci.pae.persistence.dal.ConnectionDalServices;
import be.vinci.pae.persistence.dao.UserDAO;
import be.vinci.pae.persistence.dao.UserDAOImpl;
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

    //other services
    bind(Mockito.mock(DalServicesImpl.class)).to(ConnectionDalServices.class);
  }
}
