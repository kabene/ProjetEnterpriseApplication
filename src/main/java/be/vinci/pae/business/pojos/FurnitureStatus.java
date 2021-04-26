package be.vinci.pae.business.pojos;

public enum FurnitureStatus {
  REQUESTED_FOR_VISIT("requested_for_visit"),
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

  FurnitureStatus(String s) {
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
  public static FurnitureStatus toEnum(String s) {
    switch (s) {
      case "requested_for_visit" :
        return FurnitureStatus.REQUESTED_FOR_VISIT;
      case "refused" :
        return FurnitureStatus.REFUSED;
      case "accepted" :
        return FurnitureStatus.ACCEPTED;
      case "in_restoration" :
        return FurnitureStatus.IN_RESTORATION;
      case "available_for_sale" :
        return FurnitureStatus.AVAILABLE_FOR_SALE;
      case "under_option" :
        return FurnitureStatus.UNDER_OPTION;
      case "sold" :
        return FurnitureStatus.SOLD;
      case "reserved" :
        return FurnitureStatus.RESERVED;
      case "delivered" :
        return FurnitureStatus.DELIVERED;
      case "collected" :
        return FurnitureStatus.COLLECTED;
      case "withdrawn" :
        return FurnitureStatus.WITHDRAWN;
      default:
        return null;
    }
  }

  /**
   * Verifies if the current Status instance is publicly available for non-admin users.
   *
   * @return whether the current Status instance is publicly available
   */
  public boolean isPubliclyAvailable() {
    return this.equals(FurnitureStatus.AVAILABLE_FOR_SALE)
        || this.equals(FurnitureStatus.SOLD)
        || this.equals(FurnitureStatus.UNDER_OPTION);
  }
}
