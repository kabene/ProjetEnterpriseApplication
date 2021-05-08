package be.vinci.pae.persistence.dao;

import be.vinci.pae.business.dto.RequestForVisitDTO;
import be.vinci.pae.business.factories.RequestForVisitFactory;
import be.vinci.pae.business.pojos.RequestStatus;
import jakarta.inject.Inject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RequestForVisitDAOImpl extends AbstractDAO implements RequestForVisitDAO {

  @Inject
  RequestForVisitFactory requestForVisitFactory;

  /**
   * Finds all entries of request_for_visit in the DB.
   *
   * @return a list of RequestForVisitDTO
   */
  @Override
  public List<RequestForVisitDTO> findAll() {
    return findAll("requests_for_visit");
  }

  /**
   * Finds all entries of request_for_visit belonging to the user.
   *
   * @param userId the id of the user.
   * @return a list of RequestForVisitDTO .
   */
  @Override
  public List<RequestForVisitDTO> findByUserId(int userId) {
    return findByFK("requests_for_visit", "user_id", userId);
  }

  /**
   * find a request_for_visit.
   *
   * @param idRequest the id of the request for visit.
   * @return RequestForVisitDTO that represent the request for visit.
   */
  @Override
  public RequestForVisitDTO findById(int idRequest) {
    return findById(idRequest, "requests_for_visit", "request_id");
  }

  /**
   * modify the status of a waiting request for visit.
   *
   * @param requestForVisitDTO : dto containing the new information
   * @return RequestForVisitDTO containing modified resource
   */
  @Override
  public RequestForVisitDTO modifyStatusWaitingRequest(RequestForVisitDTO requestForVisitDTO) {
    String query;
    String info;
    if (requestForVisitDTO.getRequestStatus().equals(RequestStatus.CANCELED)) {
      query = "UPDATE satchoFurniture.requests_for_visit SET status=?, "
          + "explanatory_note=? WHERE request_id=?";
      info = requestForVisitDTO.getExplanatoryNote();
    } else {
      query = "UPDATE satchoFurniture.requests_for_visit SET status=?, "
          + "visit_date_time=TO_TIMESTAMP(?, 'YYYY-MM-DD HH24-MI') WHERE request_id=?";
      info = requestForVisitDTO.getVisitDateTime();
    }
    try {
      PreparedStatement ps = dalServices.makeStatement(query);
      ps.setString(1, requestForVisitDTO.getRequestStatus().getValue());
      ps.setString(2, info);
      ps.setInt(3, requestForVisitDTO.getRequestId());
      ps.executeUpdate();
      ps.close();
    } catch (SQLException e) {
      throw new InternalError(e);
    }
    return requestForVisitDTO;
  }

  /**
   * Instantiates and fills a DTO object using an entry from a ResultSet.
   *
   * @param rs A ResultSet.
   * @return A DTO instance filled with information from the result set.
   * @throws SQLException if a problem occurs while reading the result set.
   */
  @Override
  protected RequestForVisitDTO toDTO(ResultSet rs) throws SQLException {
    RequestForVisitDTO requestFound = requestForVisitFactory.getRequestForVisitDTO();
    requestFound.setRequestId(rs.getInt("request_id"));
    requestFound.setRequestDate(rs.getString("request_date"));
    requestFound.setTimeSlot(rs.getString("time_slot"));
    requestFound.setAddressId(rs.getInt("address_id"));
    requestFound.setVisitDateTime(rs.getString("visit_date_time"));
    requestFound.setExplanatoryNote(rs.getString("explanatory_note"));
    requestFound.setRequestStatus(RequestStatus.toEnum(rs.getString("status")));
    requestFound.setUserId(rs.getInt("user_id"));
    return requestFound;
  }
}
