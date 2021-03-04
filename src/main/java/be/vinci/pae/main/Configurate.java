package be.vinci.pae.main;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Configurate {

  private static Properties props = new Properties();
  private static Map<String, Object> dependencies = new HashMap<>();

  /**
   * @param prop string .properties.
   * @return a string who represent the value.
   */
  public static String getConfiguration(String prop) {
    if (dependencies.containsKey(prop)) {
      return (String) dependencies.get(prop);
    }
    String value = (String) props.get(prop);
    dependencies.put(prop, value);
    return value;
  }

  /**
   * successfull loading of the file any question ? https://crunchify.com/java-properties-file-how-to-read-config-properties-values-in-java/
   *
   * @param pathname to the .propreperties.
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