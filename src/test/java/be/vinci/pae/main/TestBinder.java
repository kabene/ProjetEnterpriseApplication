package be.vinci.pae.main;

import be.vinci.pae.business.factories.AddressFactory;
import be.vinci.pae.persistence.dao.AddressDAO;
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
      bind(Class.forName(Configurate.getConfiguration("AddressFactory"))).to(AddressFactory.class)
          .in(Singleton.class);
      bind(Mockito.mock(AddressDAO.class)).to(AddressDAO.class);
      bind(Class.forName(Configurate.getConfiguration("UserFactory"))).to(UserFactory.class)
          .in(Singleton.class);
      bind(Class.forName(Configurate.getConfiguration("UserUCC"))).to(UserUCC.class)
          .in(Singleton.class);
      bind(Mockito.mock(UserDAOImpl.class)).to(UserDAO.class);
      bind(Class.forName(Configurate.getConfiguration("ConnectionDalServices")))
          .to(ConnectionDalServices.class).in(Singleton.class);
      bind(Class.forName(Configurate.getConfiguration("ConnectionBackendDalServices")))
          .to(ConnectionBackendDalServices.class).in(Singleton.class);
      bind(Class.forName(Configurate.getConfiguration("Authentication"))).to(Authentication.class)
          .in(Singleton.class);
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
  }
}
