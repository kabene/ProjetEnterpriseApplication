package be.vinci.pae.main;


import be.vinci.pae.business.factories.FurnitureFactory;
import be.vinci.pae.business.factories.PhotoFactory;
import be.vinci.pae.business.ucc.FurnitureUCC;
import be.vinci.pae.business.ucc.PhotoUCC;
import be.vinci.pae.persistence.dal.ConnectionBackendDalServices;
import be.vinci.pae.persistence.dao.FurnitureDAO;
import be.vinci.pae.persistence.dao.FurnitureTypeDAO;
import be.vinci.pae.persistence.dao.PhotoDAO;
import be.vinci.pae.presentation.authentication.Authentication;
import be.vinci.pae.business.factories.AddressFactory;
import be.vinci.pae.business.factories.UserFactory;
import be.vinci.pae.business.ucc.UserUCC;
import be.vinci.pae.persistence.dal.ConnectionDalServices;
import be.vinci.pae.persistence.dao.AddressDAO;
import be.vinci.pae.persistence.dao.UserDAO;
import be.vinci.pae.utils.Configurate;
import jakarta.inject.Singleton;
import jakarta.ws.rs.ext.Provider;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

@Provider
public class ApplicationBinder extends AbstractBinder {

  @Override
  protected void configure() {
    try {
      //DAO
      bind(Class.forName(Configurate.getConfiguration("UserDAO"))).to(UserDAO.class)
          .in(Singleton.class);
      bind(Class.forName(Configurate.getConfiguration("AddressDAO"))).to(AddressDAO.class)
          .in(Singleton.class);
      bind(Class.forName(Configurate.getConfiguration("FurnitureDAO")))
          .to(FurnitureDAO.class)
          .in(Singleton.class);
      bind(Class.forName(Configurate.getConfiguration("FurnitureTypeDAO")))
          .to(FurnitureTypeDAO.class)
          .in(Singleton.class);
      bind(Class.forName(Configurate.getConfiguration("PhotoDAO"))).to(PhotoDAO.class)
          .in(Singleton.class);

      //FACTORY
      bind(Class.forName(Configurate.getConfiguration("UserFactory"))).to(UserFactory.class)
          .in(Singleton.class);
      bind(Class.forName(Configurate.getConfiguration("AddressFactory")))
          .to(AddressFactory.class)
          .in(Singleton.class);
      bind(Class.forName(Configurate.getConfiguration("FurnitureFactory")))
          .to(FurnitureFactory.class)
          .in(Singleton.class);
      bind(Class.forName(Configurate.getConfiguration("PhotoFactory"))).to(PhotoFactory.class)
          .in(Singleton.class);

      //UCC
      bind(Class.forName(Configurate.getConfiguration("UserUCC"))).to(UserUCC.class)
          .in(Singleton.class);
      bind(Class.forName(Configurate.getConfiguration("FurnitureUCC"))).to(FurnitureUCC.class)
          .in(Singleton.class);
      bind(Class.forName(Configurate.getConfiguration("PhotoUCC"))).to(PhotoUCC.class)
          .in(Singleton.class);

      //OTHERS
      bind(Class.forName(Configurate.getConfiguration("Authentication"))).to(Authentication.class)
          .in(Singleton.class);
      bind(Class.forName(Configurate.getConfiguration("ConnectionDalServices")))
          .to(ConnectionDalServices.class)
          .to(ConnectionBackendDalServices.class)
          .in(Singleton.class);

    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }

  }
}
