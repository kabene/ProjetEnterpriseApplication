package be.vinci.pae.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Json {

  private static final ObjectMapper jsonMapper = new ObjectMapper();

  /**
   * Filters a pojo to send as a response using public json view.
   * @param item : pojo to filter
   * @param targetClass : class to filter as.
   * @param <T> : class to filter as
   * @return the filtered pojo.
   */
  public static <T> T filterPublicJsonView(T item, Class<T> targetClass) {
    return filterUsingView(item, targetClass, Views.Public.class);
  }

  /**
   * Filters a pojo to send as a response using admin only json view.
   * @param item : pojo to filter
   * @param targetClass : class to filter as.
   * @param <T> : class to filter as
   * @return the filtered pojo.
   */
  public static <T> T filterAdminOnlyJsonView(T item, Class<T> targetClass) {
    return filterUsingView(item, targetClass, Views.AdminOnly.class);
  }


  private static <T> T filterUsingView(T item, Class<T> targetClass, Class view) {
    try {
      String adminOnlyItemAsString = jsonMapper.writerWithView(view)
          .writeValueAsString(item);
      T res =  jsonMapper.readerWithView(view).forType(targetClass)
          .readValue(adminOnlyItemAsString);
      return res;
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      return null;
    }
  }
}
