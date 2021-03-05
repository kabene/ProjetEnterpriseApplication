package be.vinci.pae.main;

//import jakarta.inject.Singleton;
import jakarta.ws.rs.ext.Provider;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

@Provider
public class ApplicationBinder extends AbstractBinder {

  @Override
  protected void configure() {
    //bind(implClassname.class).to(factoryname.class).in(Singleton.class);
  }
}
