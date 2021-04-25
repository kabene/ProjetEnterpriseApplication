package be.vinci.pae.business.pojos;

public enum RequestStatus {
  WAITING("waiting"),
  CANCELED("canceled"),
  CONFIRMED("confirmed");

  private final String value;

  RequestStatus(String s) {
    this.value = s;
  }

  public String getValue() {
    return this.value;
  }

  /**
   * convert a string to a Enum type.
   *
   * @param s string to convert
   * @return the status in a Enum type
   */
  public static RequestStatus toEnum(String s) {
    switch (s) {
      case "waiting" :
        return RequestStatus.WAITING;
      case "canceled" :
        return RequestStatus.CANCELED;
      case "confirmed" :
        return RequestStatus.CONFIRMED;
      default:
        return null;
    }
  }
}
