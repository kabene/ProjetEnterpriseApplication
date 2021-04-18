package be.vinci.pae.business.pojos;

public enum Status {
  REQUEST_FOR_VISIT("requested_for_visit"),
  REFUSED("refused"),
  ACCEPTED("accepted"),
  IN_RESTORATION("in_restoration"),
  AVAILABLE_FOR_SALE("available_for_sale"),
  UNDER_OPTION("under_option"),
  SOLD("sold"),
  RESERVED("reserved"),
  DELIVERED("delivered"),
  COLLECTED("collected"),
  WITHDRAWN("withdrawn");

  private final String value;

  Status(String s) {
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
  public static Status toEnum(String s) {
    switch (s) {
      case "requested_for_visit" :
        return Status.REQUEST_FOR_VISIT;
      case "refused" :
        return Status.REFUSED;
      case "accepted" :
        return Status.ACCEPTED;
      case "in_restoration" :
        return Status.IN_RESTORATION;
      case "available_for_sale" :
        return Status.AVAILABLE_FOR_SALE;
      case "under_option" :
        return Status.UNDER_OPTION;
      case "sold" :
        return Status.SOLD;
      case "reserved" :
        return Status.RESERVED;
      case "delivered" :
        return Status.DELIVERED;
      case "collected" :
        return Status.COLLECTED;
      case "withdrawn" :
        return Status.WITHDRAWN;
      default:
        return null;
    }
  }
}
