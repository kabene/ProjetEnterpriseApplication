package be.vinci.pae.business.ucc;

import be.vinci.pae.business.dto.FurnitureDTO;
import be.vinci.pae.business.dto.RequestForVisitDTO;
import be.vinci.pae.business.pojos.RequestStatus;
import be.vinci.pae.exceptions.ConflictException;
import be.vinci.pae.exceptions.UnauthorizedException;
import be.vinci.pae.persistence.dal.ConnectionDalServices;
import be.vinci.pae.persistence.dao.AddressDAO;
import be.vinci.pae.persistence.dao.FurnitureDAO;
import be.vinci.pae.persistence.dao.PhotoDAO;
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
  @Inject
  private FurnitureDAO furnitureDAO;
  @Inject
  private PhotoDAO photoDAO;

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
   * change the status of a waiting request for visit.
   *
   * @param idRequest     the id of the request for visit to change.
   * @param currentUserId the id of the user asking for the change.
   * @param requestStatus the status in which the request should be changed.
   * @return an RequestForVisitDTO that represent the changed one.
   */
  @Override
  public RequestForVisitDTO changeWaitingRequestStatus(int idRequest, int currentUserId,
                                                       RequestStatus requestStatus) {
    RequestForVisitDTO request;
    try {
      dalServices.startTransaction();
      request = requestForVisitDAO.findByRequestId(idRequest);
      if (request.getRequestStatus() != RequestStatus.WAITING) {
        throw new ConflictException("The request status can not be modified");
      }
      if (requestStatus.equals(RequestStatus.WAITING)) {
        throw new ConflictException("Can not set a request to waiting");
      }
      if (request.getUserId() != currentUserId) {
        throw new UnauthorizedException("The requests do not belong to the user "
            + "that called the request");
      }
      requestForVisitDAO.modifyStatusWaitingRequest(idRequest, requestStatus);
      request.setRequestStatus(requestStatus);
      completeFurnitureDTO(request);
      dalServices.commitTransaction();
    } catch (Throwable e) {
      dalServices.rollbackTransaction();
      throw e;
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
    dto.setFurnitureList(furnitureDAO.findByRequestId(dto.getRequestId()));
    for(FurnitureDTO furnitureDTO : dto.getFurnitureList()) {
      furnitureDTO.setPhotos(photoDAO.findAllByFurnitureId(furnitureDTO.getFurnitureId()));
    }
  }
}
