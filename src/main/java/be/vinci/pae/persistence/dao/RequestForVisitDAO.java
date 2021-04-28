package be.vinci.pae.persistence.dao;

import be.vinci.pae.business.dto.RequestForVisitDTO;
import be.vinci.pae.business.pojos.RequestStatus;

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
  RequestForVisitDTO findByRequestId(int idRequest);

  /**
   * modify the status of a waiting request for visit.
   *
   * @param idRequest the id of the request for visit to modify.
   * @param requestStatus the status in which the request should be modified.
   * @param info          the info (explanatory note or visit date time) to add.
   */
  void modifyStatusWaitingRequest(int idRequest, RequestStatus requestStatus, String info);


}
