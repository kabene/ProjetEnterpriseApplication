package be.vinci.pae.main;


import be.vinci.pae.presentation.ExceptionHandler;
import be.vinci.pae.utils.Configurate;
import java.io.IOException;
import java.net.URI;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * Main class.
 */
public class Main {

  public static final String LOGGER_NAME = "be.vinci.pae";

  /**
   * Starts the http server.
   *
   * @return the new server.
   */
  public static HttpServer startServer() {
    final ResourceConfig rc = new ResourceConfig().packages("be.vinci.pae.presentation")
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
      System.out
          .println("put the path to a .properties file in parameter of your main");
      System.exit(1);
    }
    Configurate.load(args[0]);
    //create & setup loggers
    Logger fileLogger = Logger.getLogger(LOGGER_NAME);
    //Logger consoleLogger = Logger.getLogger()
    Handler fileHandler = new FileHandler("logs/log%g.xml", 1000000, 3, true);
    fileLogger.addHandler(fileHandler);
    fileLogger.setUseParentHandlers(false);

    //start server
    final HttpServer server = startServer();
    System.out.println("Jersey app started at " + Configurate.getConfiguration("baseUri"));
    //Listen to key press and shut down
    System.out.println("Hit a key to stop it...");
    System.in.read();

    fileHandler.flush();
    fileHandler.close();
    server.shutdown();

  }

}
