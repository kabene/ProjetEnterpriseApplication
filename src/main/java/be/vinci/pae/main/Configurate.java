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
   * get configuration should give a string that contains the config file.
   * @param key string .properties.
   * @return a string who represent the value.
   */
  public static String getConfiguration(String key) {
    if (dependencies.containsKey(key)) {
      return (String) dependencies.get(key);
    }
    String value = (String) props.get(key);
    dependencies.put(key, value);
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