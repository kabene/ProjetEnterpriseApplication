package be.vinci.pae.main;

import be.vinci.pae.business.authentication.Authentication;
import be.vinci.pae.business.authentication.AuthenticationImpl;
import be.vinci.pae.business.factories.UserFactory;
import be.vinci.pae.business.ucc.UserUCC;
import be.vinci.pae.persistence.dal.ConnectionDalServices;
import be.vinci.pae.persistence.dao.UserDAO;
import jakarta.inject.Singleton;
import jakarta.ws.rs.ext.Provider;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

@Provider
public class ApplicationBinder extends AbstractBinder {

  @Override
  protected void configure() {
    try {
      bind(Class.forName(Configurate.getConfiguration("UserFactory"))).to(UserFactory.class)
          .in(Singleton.class);
      bind(Class.forName(Configurate.getConfiguration("UserUCC"))).to(UserUCC.class)
          .in(Singleton.class);
      bind(Class.forName(Configurate.getConfiguration("UserDAO"))).to(UserDAO.class)
          .in(Singleton.class);
      bind(Class.forName(Configurate.getConfiguration("ConnectionDalServices")))
          .to(ConnectionDalServices.class).in(Singleton.class);
      bind(AuthenticationImpl.class).to(Authentication.class).in(Singleton.class);
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }

  }
}
