package be.vinci.pae.main;

import be.vinci.pae.business.dto.FurnitureDTO;
import be.vinci.pae.business.dto.PhotoDTO;
import be.vinci.pae.business.factories.AddressFactory;
import be.vinci.pae.business.pojos.FurnitureImpl;
import be.vinci.pae.business.pojos.PhotoImpl;
import be.vinci.pae.business.ucc.FurnitureUCC;
import be.vinci.pae.business.ucc.PhotoUCC;
import be.vinci.pae.persistence.dal.DalServicesImpl;
import be.vinci.pae.persistence.dao.AddressDAO;
import be.vinci.pae.persistence.dao.FurnitureDAO;
import be.vinci.pae.persistence.dao.FurnitureDAOImpl;
import be.vinci.pae.persistence.dao.FurnitureTypeDAO;
import be.vinci.pae.persistence.dao.FurnitureTypeDAOImpl;
import be.vinci.pae.persistence.dao.PhotoDAO;
import be.vinci.pae.persistence.dao.PhotoDAOImpl;
import be.vinci.pae.presentation.authentication.Authentication;
import be.vinci.pae.business.factories.UserFactory;
import be.vinci.pae.business.ucc.UserUCC;
import be.vinci.pae.persistence.dal.ConnectionDalServices;
import be.vinci.pae.persistence.dal.ConnectionBackendDalServices;
import be.vinci.pae.persistence.dao.UserDAO;
import be.vinci.pae.persistence.dao.UserDAOImpl;
import be.vinci.pae.utils.Configurate;
import jakarta.inject.Singleton;
import jakarta.ws.rs.ext.Provider;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.mockito.Mockito;

@Provider
public class TestBinder extends AbstractBinder {

  @Override
  protected void configure() {
    try {
      //addresses
      bind(Class.forName(Configurate.getConfiguration("AddressFactory"))).to(AddressFactory.class)
          .in(Singleton.class);
      bind(Mockito.mock(AddressDAO.class)).to(AddressDAO.class);

      //users
      bind(Class.forName(Configurate.getConfiguration("UserFactory"))).to(UserFactory.class)
          .in(Singleton.class);
      bind(Class.forName(Configurate.getConfiguration("UserUCC"))).to(UserUCC.class)
          .in(Singleton.class);
      bind(Mockito.mock(UserDAOImpl.class)).to(UserDAO.class);

      //furniture
      bind(Class.forName(Configurate.getConfiguration("FurnitureUCC"))).to(FurnitureUCC.class)
          .in(Singleton.class);
      bind(Mockito.mock(FurnitureDAOImpl.class)).to(FurnitureDAO.class);

      //furniture types
      bind(Mockito.mock(FurnitureTypeDAOImpl.class)).to(FurnitureTypeDAO.class);

      //photos
      bind(Class.forName(Configurate.getConfiguration("PhotoUCC"))).to(PhotoUCC.class)
          .in(Singleton.class);
      bind(Mockito.mock(PhotoDAOImpl.class)).to(PhotoDAO.class);

      //other services
      bind(Mockito.mock(DalServicesImpl.class)).to(ConnectionDalServices.class);
      bind(Class.forName(Configurate.getConfiguration("ConnectionBackendDalServices")))
          .to(ConnectionBackendDalServices.class).in(Singleton.class);
      bind(Class.forName(Configurate.getConfiguration("Authentication"))).to(Authentication.class)
          .in(Singleton.class);

    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
  }
}
