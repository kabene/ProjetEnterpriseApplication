package be.vinci.pae.main;


import be.vinci.pae.utils.Configurate;
import be.vinci.pae.business.ucc.OptionUCC;
import java.io.IOException;
import java.net.URI;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.logging.Handler;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * Main class.
 */
public class Main {

  private static final String FILE_LOGGER_NAME = "be.vinci.pae-file";
  public static final String CONSOLE_LOGGER_NAME = "be.vinci.pae-console";

  private static ApplicationBinder applicationBinder = new ApplicationBinder();

  private static OptionUCC optionUCC;


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
    Logger fileLogger = Logger.getLogger(FILE_LOGGER_NAME);
    Logger consoleLogger = Logger.getLogger(CONSOLE_LOGGER_NAME);
    fileLogger.setLevel(Level.WARNING);
    consoleLogger.setLevel(Level.ALL);

    Handler fileHandler = new FileHandler("logs/log%g.xml", 1000000, 3, true);
    Handler consoleHandler = new ConsoleHandler();
    fileHandler.setLevel(Level.WARNING);
    consoleHandler.setLevel(Level.ALL);

    fileLogger.addHandler(fileHandler);
    fileLogger.setUseParentHandlers(false);

    consoleLogger.addHandler(consoleHandler);
    consoleLogger.setUseParentHandlers(true);

    consoleLogger.setParent(fileLogger);

    //Option's automatic transition (every 24h = 86400000ms)
    ServiceLocator locator = ServiceLocatorUtilities.bind(applicationBinder);
    optionUCC = locator.getService(OptionUCC.class);

    Timer t = new Timer();
    TimerTask tt = new TimerTask() {

      @Override
      public void run() {
        Logger.getLogger(Main.CONSOLE_LOGGER_NAME)
            .log(Level.INFO, "Option's automatic transition.");
        optionUCC.updateExpiredOptions();
      }
    };

    Date startSchedulerDate = Date
        .from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());

    t.scheduleAtFixedRate(tt, startSchedulerDate, 86400000);

    try {
      //start server
      final HttpServer server = startServer();
      System.out.println("Jersey app started at " + Configurate.getConfiguration("baseUri"));
      //Listen to key press and shut down
      System.out.println("Hit a key to stop it...");
      System.in.read();
      server.shutdown();
    } finally {
      fileHandler.flush();
      fileHandler.close();
    }
  }

}
