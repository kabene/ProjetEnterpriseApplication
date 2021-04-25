package be.vinci.pae.business.ucc;

import be.vinci.pae.business.dto.RequestForVisitDTO;
import be.vinci.pae.business.pojos.RequestStatus;
import be.vinci.pae.exceptions.ConflictException;
import be.vinci.pae.exceptions.UnauthorizedException;
import be.vinci.pae.persistence.dal.ConnectionDalServices;
import be.vinci.pae.persistence.dao.AddressDAO;
import be.vinci.pae.persistence.dao.RequestForVisitDAO;
import be.vinci.pae.persistence.dao.UserDAO;
import jakarta.inject.Inject;

import java.util.List;

public class RequestForVisitUCCImpl implements RequestForVisitUCC {

  @Inject
  private ConnectionDalServices dalServices;
  @Inject
  private RequestForVisitDAO requestForVisitDAO;
  @Inject
  private AddressDAO addressDAO;
  @Inject
  private UserDAO userDAO;

  /**
   * list all the requests for visit.
   *
   * @return a list of all the requests for visit.
   */
  @Override
  public List<RequestForVisitDTO> listRequest() {
    List<RequestForVisitDTO> dtos;
    try {
      dalServices.startTransaction();
      dtos = requestForVisitDAO.findAll();
      for (RequestForVisitDTO dto : dtos) {
        completeFurnitureDTO(dto);
      }
      dalServices.commitTransaction();
    } catch (Throwable e) {
      dalServices.rollbackTransaction();
      throw e;
    }
    return dtos;
  }

  /**
   * list all the requests_for_visit belonging to the user.
   *
   * @param currentUserId the id of the current user.
   * @return a list of all the requests for visit.
   */
  @Override
  public List<RequestForVisitDTO> listRequestByUserId(int currentUserId) {
    List<RequestForVisitDTO> dtos;
    try {
      dalServices.startTransaction();
      dtos = requestForVisitDAO.findByUserId(currentUserId);
      for (RequestForVisitDTO dto : dtos) {
        completeFurnitureDTO(dto);
      }
      dalServices.commitTransaction();
    } catch (Throwable e) {
      dalServices.rollbackTransaction();
      throw e;
    }
    return dtos;
  }

  /**
   * cancel a request for visit.
   *
   * @param idRequest id of the request for visit to cancel.
   * @return an RequestForVisitDTO that represent the canceled one.
   */
  @Override
  public RequestForVisitDTO cancelRequest(int idRequest, int idCurrentUser) {
    RequestForVisitDTO request;
    try {
      dalServices.startTransaction();
      request = requestForVisitDAO.findByRequestId(idRequest);
      if (request.getRequestStatus() != RequestStatus.WAITING) {
        throw new ConflictException("The request status can not be modified");
      }
      if (request.getUserId() != idCurrentUser) {
        throw new UnauthorizedException(
            "The requests do not belong to the user that called the request"
        );
      }
      requestForVisitDAO.cancelRequest(idRequest);
      completeFurnitureDTO(request);
      dalServices.commitTransaction();
    } catch (Throwable e) {
      dalServices.rollbackTransaction();
      throw (e);
    }
    return request;
  }

  /**
   * accept a request for visit.
   *
   * @param idRequest id of the request for visit to accept.
   * @return an RequestForVisitDTO that represent the accepted one.
   */
  @Override
  public RequestForVisitDTO acceptRequest(int idRequest, int idCurrentUser) {
    RequestForVisitDTO request;
    try {
      dalServices.startTransaction();
      request = requestForVisitDAO.findByRequestId(idRequest);
      if (request.getRequestStatus() != RequestStatus.WAITING) {
        throw new ConflictException("The request status can not be modified");
      }
      if (request.getUserId() != idCurrentUser) {
        throw new UnauthorizedException("The requests do not belong to the user that called the request");
      }
      requestForVisitDAO.acceptRequest(idRequest);
      completeFurnitureDTO(request);
      dalServices.commitTransaction();
    } catch (Throwable e) {
      dalServices.rollbackTransaction();
      throw (e);
    }
    return request;
  }

  /**
   * complete the RequestForVisitDTO with the DTO objects missing.
   *
   * @param dto the RequestForVisitDTO to complete.
   */
  private void completeFurnitureDTO(RequestForVisitDTO dto) {
    dto.setAddress(addressDAO.findById(dto.getAddressId()));
    dto.setUser(userDAO.findById(dto.getUserId()));
  }
}
