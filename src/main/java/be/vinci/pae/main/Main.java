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

  /**
   * Starts the http server.
   *
   * @return the new server.
   */
  public static HttpServer startServer() {
    final ResourceConfig rc = new ResourceConfig().packages("be.vinci.pae")
        .register(JacksonFeature.class)
        .register(ApplicationBinder.class).property("jersey.config.server.wadl.disableWadl", true);

    return GrizzlyHttpServerFactory
        .createHttpServer(URI.create(Configurate.getConfiguration("baseUri")), rc);
  }

  /**
   * Main method.
   *
   * @param args command line arguments
   */
  public static void main(String[] args) throws IOException {
    //load properties file
    if (args.length != 1) {
      System.exit(1);
    }
    Configurate.load(args[0]);
    //start server
    final HttpServer server = startServer();
    System.out.println("Jersey app started at " + Configurate.getConfiguration("baseUri"));
    //Listen to key press and shut down
    System.out.println("Hit a key to stop it...");
    System.in.read();
    server.shutdown();

  }

}
