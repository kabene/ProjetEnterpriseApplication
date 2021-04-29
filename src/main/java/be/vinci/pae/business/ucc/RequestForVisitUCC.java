package be.vinci.pae.business.ucc;

import be.vinci.pae.business.dto.RequestForVisitDTO;
import be.vinci.pae.business.pojos.RequestStatus;

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
   * @param currentUserId the id of the current user.
   * @return a list of all the requests for visit.
   */
  List<RequestForVisitDTO> listRequestByUserId(int currentUserId);

  /**
   * change the status of a waiting request for visit.
   *
   * @param idRequest     the id of the request for visit to change.
   * @param currentUserId the id of the user asking for the change.
   * @param requestStatus the status in which the request should be changed.
   * @param info          the info (explanatory note or visit date time) to put in the DTO.
   * @return an RequestForVisitDTO that represent the changed one.
   */
  RequestForVisitDTO changeWaitingRequestStatus(int idRequest, int currentUserId,
                                                RequestStatus requestStatus, String info);
}
