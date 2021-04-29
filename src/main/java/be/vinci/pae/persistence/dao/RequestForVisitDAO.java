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
   * @param userId the id of the user.
   * @return a list of RequestForVisitDTO .
   */
  List<RequestForVisitDTO> findByUserId(int userId);

  /**
   * find a request_for_visit.
   *
   * @param idRequest the id of the request for visit.
   * @return RequestForVisitDTO that represent the request for visit.
   */
  RequestForVisitDTO findById(int idRequest);

  /**
   * modify the status of a waiting request for visit.
   *
   * @param requestForVisitDTO : dto containing the new information
   * @return RequestForVisitDTO containing modified resource
   */
  RequestForVisitDTO modifyStatusWaitingRequest(RequestForVisitDTO requestForVisitDTO);


}
