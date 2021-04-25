package be.vinci.pae.persistence.dao;

import be.vinci.pae.business.dto.RequestForVisitDTO;

import java.util.List;

public interface RequestForVisitDAO {

  /**
   * finds all entries of requests_for_visit.
   *
   * @return a list of RequestForVisitDTO
   */
  List<RequestForVisitDTO> findAll();

  /**
   * finds all entries of requests_for_visit belonging to the user.
   *
   * @param userId, the id of the user.
   * @return a list of RequestForVisitDTO .
   */
  List<RequestForVisitDTO> findByUserId(int userId);

  /**
   * find a request_for_visit.
   *
   * @param idRequest, the id of the request for visit.
   * @return RequestForVisitDTO that represent the request for visit.
   */
  RequestForVisitDTO findByRequestId(int idRequest);

  /**
   * cancel a request for visit.
   *
   * @param idRequest, the id of the request for visit to cancel.
   */
  void cancelRequest(int idRequest);

  /**
   * accept a request for visit.
   *
   * @param idRequest, the id of the request for visit to accept.
   */
  void acceptRequest(int idRequest);


}
