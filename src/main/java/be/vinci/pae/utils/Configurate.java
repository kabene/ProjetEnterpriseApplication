package be.vinci.pae.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Configurate {

  private static Properties props = new Properties();
  private static Map<String, Object> dependencies = new HashMap<>();

  /**
   * Finds a value in the loaded .properties file (given a specific key).
   *
   * @param key : Key value
   * @return the corresponding value as a String
   */
  public static String getConfiguration(String key) {
    if (dependencies.containsKey(key)) {
      return (String) dependencies.get(key);
    }
    String value = props.getProperty(key);
    dependencies.put(key, value);
    return value;
  }

  /**
   * Loads a properties file.
   *
   * @param pathname to the .properties.
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