package be.vinci.pae.main;

import be.vinci.pae.business.factories.UserFactory;
import be.vinci.pae.business.factories.UserFactoryImpl;
import be.vinci.pae.business.ucc.UserUCC;
import be.vinci.pae.business.ucc.UserUCCImpl;
import jakarta.inject.Singleton;
import jakarta.ws.rs.ext.Provider;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

@Provider
public class ApplicationBinder extends AbstractBinder {

  @Override
  protected void configure() {
    bind(UserFactoryImpl.class).to(UserFactory.class)
        .in(Singleton.class); //TODO extract injection info into .properties
    bind(UserUCCImpl.class).to(UserUCC.class).in(Singleton.class);
  }
}
