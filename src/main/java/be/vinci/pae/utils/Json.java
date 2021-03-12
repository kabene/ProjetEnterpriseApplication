package be.vinci.pae.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Json {

  private static final ObjectMapper jsonMapper = new ObjectMapper();

  /**
   * Filters a pojo to send as a response using views.
   * @param item : pojo to filter
   * @param targetClass : class to filter as.
   * @param <T> : class to filter as
   * @return the filtered pojo.
   */
  public static <T> T filterPublicJsonView(T item, Class<T> targetClass) {
    try {
      String publicItemAsString = jsonMapper.writerWithView(Views.Public.class)
          .writeValueAsString(item);
      return jsonMapper.readerWithView(Views.Public.class).forType(targetClass)
          .readValue(publicItemAsString);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      return null;
    }
  }
}
