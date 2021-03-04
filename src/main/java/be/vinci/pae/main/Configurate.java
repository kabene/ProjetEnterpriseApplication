package be.vinci.pae.main;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Configurate {

  private static Properties props = new Properties();

  public static String getConfiguration(String prop) {
    String val = (String) new Properties().get(prop);
    return val;
  }

  /**
   * load properties file
   *
   * @param pathname properties file path
   * @post successfull loading of the file any question ? https://crunchify.com/java-properties-file-how-to-read-config-properties-values-in-java/
   * first @author: karim
   */
  public static void load(String pathname) {
    FileInputStream file;
    try {
      file = new FileInputStream(pathname);
      props.load(file);
      file.close();
    } catch (IOException exception) {
      exception.printStackTrace();
      throw new RuntimeException();
    }
  }
}
