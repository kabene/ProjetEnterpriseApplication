package be.vinci.pae.main;


import java.io.IOException;
import java.net.URI;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * Main class.
 */
public class Main {

  public static HttpServer startServer() {
    final ResourceConfig rc = new ResourceConfig().packages("be.vinci.pae.presentation")
        .register(JacksonFeature.class)
        .register(ApplicationBinder.class).property("jersey.config.server.wadl.disableWadl", true);

    return GrizzlyHttpServerFactory.createHttpServer(URI.create(Configurate.getConfiguration("BaseUri")), rc);
  }

  /**
   * Main method.
   *
   * @param args command line arguments
   */
  public static void main(String[] args) throws IOException {
    //load properties file
    Configurate.load("src.main.resources.prod.properties");
    //start server
    final HttpServer server = startServer();
    System.out.println("Jersey app started at " + Configurate.getConfiguration("BaseUri"));
    //Listen to key press and shut down
    System.out.println("Hit a key to stop it...");
    System.in.read();
    server.shutdown();

  }

}
