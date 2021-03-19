package be.vinci.pae.persistence.dal;


public interface ConnectionDalServices {


  /**
   * Start the transaction.
   */
  void startTransaction();

  /**
   * Commit the transaction.
   */
  void commitTransaction();

  /**
   * RollbackThe Transaction.
   */
  void rollbackTransaction();

}
