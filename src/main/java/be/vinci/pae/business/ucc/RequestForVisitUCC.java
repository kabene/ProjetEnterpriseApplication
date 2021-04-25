package be.vinci.pae.business.ucc;

import be.vinci.pae.business.dto.RequestForVisitDTO;

import java.util.List;

public interface RequestForVisitUCC {

  /**
   * list all the requests for visit.
   *
   * @return a list of all the requests for visit.
   */
  List<RequestForVisitDTO> listRequest();

  /**
   * list all the requests_for_visit belonging to the user.
   *
   * @param userId, the id of the user.
   * @return a list of all the requests for visit.
   */
  List<RequestForVisitDTO> listRequestByUserId(int userId);

  /**
   * cancel a request for visit.
   *
   * @param idRequest id of the request for visit to cancel.
   * @return an RequestForVisitDTO that represent the canceled one.
   */
  RequestForVisitDTO cancelRequest(int idRequest);

  /**
   * accept a request for visit.
   *
   * @param idRequest id of the request for visit to accept.
   * @return an RequestForVisitDTO that represent the accepted one.
   */
  RequestForVisitDTO acceptRequest(int idRequest);
}
