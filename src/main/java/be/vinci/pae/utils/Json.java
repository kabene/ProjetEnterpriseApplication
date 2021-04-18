package be.vinci.pae.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Json {

  private static final ObjectMapper jsonMapper = new ObjectMapper();

  /**
   * Filters a pojo to send as a response using public json view.
   * @param item : pojo to filter
   * @param targetClass : class to filter as.
   * @param <T> : type of item parameter.
   * @return the filtered pojo.
   */
  public static <T> T filterPublicJsonView(T item, Class<T> targetClass) {
    return filterUsingView(item, targetClass, Views.Public.class);
  }

  /**
   * Filters a pojo to send as a response using admin only json view.
   * @param item : pojo to filter
   * @param targetClass : class to filter as.
   * @param <T> : type of item parameter.
   * @return the filtered pojo.
   */
  public static <T> T filterAdminOnlyJsonView(T item, Class<T> targetClass) {
    return filterUsingView(item, targetClass, Views.AdminOnly.class);
  }


  /**
   * Filters a pojo to send as a response using json view.
   * @param item : pojo to filter
   * @param targetClass : class to filter as.
   * @param view : view that determines how to filter the item.
   * @param <T> : type of item parameter.
   * @return the filtered pojo.
   */
  private static <T> T filterUsingView(T item, Class<T> targetClass, Class view) {
    try {
      String adminOnlyItemAsString = jsonMapper.writerWithView(view)
          .writeValueAsString(item);
      return jsonMapper.readerWithView(view).forType(targetClass)
          .readValue(adminOnlyItemAsString);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      return null;
    }
  }
}
