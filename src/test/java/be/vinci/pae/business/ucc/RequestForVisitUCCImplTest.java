package be.vinci.pae.business.ucc;

import be.vinci.pae.business.dto.RequestForVisitDTO;
import be.vinci.pae.business.pojos.RequestStatus;
import be.vinci.pae.exceptions.ConflictException;
import be.vinci.pae.exceptions.NotFoundException;
import be.vinci.pae.exceptions.UnauthorizedException;
import be.vinci.pae.main.TestBinder;
import be.vinci.pae.persistence.dal.ConnectionDalServices;
import be.vinci.pae.persistence.dao.RequestForVisitDAO;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RequestForVisitUCCImplTest {

  private static RequestForVisitUCC requestUCC;
  private static ConnectionDalServices mockDal;

  private static RequestForVisitDAO mockRequestForVisitDAO;

  private static RequestForVisitDTO mockRequestDTO1;
  private static RequestForVisitDTO mockRequestDTO2;
  private static RequestForVisitDTO mockRequestDTO3;

  private static final int defaultRequestId = 1;
  private static final int defaultUserId = 2;
  private static final int defaultBadUserId = 3;
  private static final String info = "my info";

  @BeforeAll
  public static void init() {
    ServiceLocator locator = ServiceLocatorUtilities.bind(new TestBinder());

    requestUCC = locator.getService(RequestForVisitUCC.class);
    mockDal = locator.getService(ConnectionDalServices.class);

    mockRequestForVisitDAO = locator.getService(RequestForVisitDAO.class);

    mockRequestDTO1 = Mockito.mock(RequestForVisitDTO.class);
    mockRequestDTO2 = Mockito.mock(RequestForVisitDTO.class);
    mockRequestDTO3 = Mockito.mock(RequestForVisitDTO.class);
  }

  @BeforeEach
  void setUp() {
    Mockito.reset(mockDal);
    Mockito.reset(mockRequestForVisitDAO);
    Mockito.reset(mockRequestDTO1);
    Mockito.reset(mockRequestDTO2);
    Mockito.reset(mockRequestDTO3);

    Mockito.when(mockRequestDTO1.getRequestId()).thenReturn(1);
    Mockito.when(mockRequestDTO2.getRequestId()).thenReturn(2);
    Mockito.when(mockRequestDTO3.getRequestId()).thenReturn(3);

    Mockito.when(mockRequestForVisitDAO.findByRequestId(defaultRequestId))
        .thenReturn(mockRequestDTO1);
    Mockito.when(mockRequestDTO1.getRequestStatus()).thenReturn(RequestStatus.WAITING);
    Mockito.when(mockRequestDTO1.getUserId()).thenReturn(defaultUserId);
  }

  //TEST listRequest

  @DisplayName("TEST listRequest() should have return all requests")
  @Test
  void test_listRequest_shouldReturnDTOList() {
    List<RequestForVisitDTO> list = Arrays.asList(mockRequestDTO1, mockRequestDTO2,
        mockRequestDTO3);
    Mockito.when(mockRequestForVisitDAO.findAll()).thenReturn(list);

    assertEquals(list, requestUCC.listRequest(),
        "called listRequest should have return all requests");

    Mockito.verify(mockRequestForVisitDAO).findAll();
    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal, Mockito.never()).rollbackTransaction();
    Mockito.verify(mockDal).commitTransaction();
  }

  @DisplayName("TEST listRequest() without data should have return an empty list")
  @Test
  void test_listRequestWithEmptyDB_shouldReturnEmptyDTOList() {
    List<RequestForVisitDTO> list = new ArrayList<>();
    Mockito.when(mockRequestForVisitDAO.findAll()).thenReturn(list);

    assertEquals(list, requestUCC.listRequest(),
        "called listRequest without data in the db should return an empty list");

    Mockito.verify(mockRequestForVisitDAO).findAll();
    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal, Mockito.never()).rollbackTransaction();
    Mockito.verify(mockDal).commitTransaction();
  }

  @DisplayName("TEST listRequest() DAO throws an Error, should rollback and throw Error")
  @Test
  void test_listRequest_InternalErrorThrown_shouldThrowInternalErrorAndRollback() {
    Mockito.when(mockRequestForVisitDAO.findAll()).thenThrow(new InternalError());

    assertThrows(InternalError.class, () -> requestUCC.listRequest(),
        "DAO throws an Error, should rollback and throw InternalError");

    Mockito.verify(mockRequestForVisitDAO).findAll();
    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal).rollbackTransaction();
    Mockito.verify(mockDal, Mockito.never()).commitTransaction();
  }

  //TEST listRequestByUserId

  @DisplayName("TEST listRequestByUserId() should have return correct requests")
  @Test
  void test_listRequestByUserId_shouldReturnCorrectRequests() {
    List<RequestForVisitDTO> list = Arrays.asList(mockRequestDTO1, mockRequestDTO2);
    Mockito.when(mockRequestForVisitDAO.findByUserId(defaultUserId)).thenReturn(list);

    assertEquals(list, requestUCC.listRequestByUserId(defaultUserId),
        "called listRequestByUserId should have return all requests from client");

    Mockito.verify(mockRequestForVisitDAO).findByUserId(defaultUserId);
    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal, Mockito.never()).rollbackTransaction();
    Mockito.verify(mockDal).commitTransaction();
  }

  @DisplayName("TEST listRequestByUserId() should have return correct requests")
  @Test
  void test_listRequestByUserIdWithEmptyDb_shouldReturnCorrectRequests() {
    List<RequestForVisitDTO> list = new ArrayList<>();
    Mockito.when(mockRequestForVisitDAO.findByUserId(defaultUserId)).thenReturn(list);

    assertEquals(list, requestUCC.listRequestByUserId(defaultUserId),
        "called listRequestByUserId with empty db should have return an empty list");

    Mockito.verify(mockRequestForVisitDAO).findByUserId(defaultUserId);
    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal, Mockito.never()).rollbackTransaction();
    Mockito.verify(mockDal).commitTransaction();
  }

  @DisplayName("TEST listRequestByUserId() DAO throws an Error, should rollback and throw Error")
  @Test
  void test_listRequestByUser_ErrorThrown_shouldThrowInternalErrorAndRollback() {
    Mockito.when(mockRequestForVisitDAO.findByUserId(defaultUserId)).thenThrow(new InternalError());

    assertThrows(InternalError.class, () -> requestUCC.listRequestByUserId(defaultUserId),
        "DAO throws an Error, should rollback and throw InternalError");

    Mockito.verify(mockRequestForVisitDAO).findByUserId(defaultUserId);
    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal).rollbackTransaction();
    Mockito.verify(mockDal, Mockito.never()).commitTransaction();
  }

  //TEST modifyStatusWaitingRequest

  @DisplayName("TEST changeWaitingRequestStatus() with the good userId and a good request "
      + "should return the good request")
  @ParameterizedTest
  @EnumSource(value = RequestStatus.class, names = {"CANCELED", "CONFIRMED"})
  void test_modifyStatusWaitingRequest_shouldReturnRequest(RequestStatus requestStatus) {
    Mockito.when(mockRequestForVisitDAO.modifyStatusWaitingRequest(mockRequestDTO1))
        .thenReturn(mockRequestDTO1);
    assertEquals(mockRequestDTO1, requestUCC
            .changeWaitingRequestStatus(defaultRequestId, defaultUserId, requestStatus, info),
        "called changeWaitingRequestStatus() with the good userId and a good request "
            + "should return the good request");

    Mockito.verify(mockRequestForVisitDAO)
        .modifyStatusWaitingRequest(mockRequestDTO1);
    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal, Mockito.never()).rollbackTransaction();
    Mockito.verify(mockDal).commitTransaction();
  }

  @DisplayName("TEST changeWaitingRequestStatus() with a invalid request id"
      + "should throw NotFoundException")
  @ParameterizedTest
  @EnumSource(value = RequestStatus.class, names = {"CANCELED", "CONFIRMED"})
  void test_changeWaitingRequestStatus_invalidRequestId_shouldThrowNotFound(RequestStatus requestStatus) {
    Mockito.when(mockRequestDTO1.getRequestStatus()).thenReturn(requestStatus);
    Mockito.when(mockRequestForVisitDAO.findByRequestId(defaultRequestId)).thenThrow(new NotFoundException());

    assertThrows(NotFoundException.class, () -> requestUCC
            .changeWaitingRequestStatus(defaultRequestId, defaultUserId, requestStatus, info),
        "called changeWaitingRequestStatus() with a not waiting request"
            + "should have thrown Conflict Exception");

    Mockito.verify(mockRequestForVisitDAO, Mockito.never())
        .modifyStatusWaitingRequest(mockRequestDTO1);
    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockRequestForVisitDAO).findByRequestId(defaultRequestId);
    Mockito.verify(mockDal).rollbackTransaction();
    Mockito.verify(mockDal, Mockito.never()).commitTransaction();
  }

  @DisplayName("TEST changeWaitingRequestStatus() with a not waiting request"
      + "should have thrown Conflict Exception")
  @ParameterizedTest
  @EnumSource(value = RequestStatus.class, names = {"CANCELED", "CONFIRMED"})
  void test_changeWaitingRequestStatus_withNoRequest_shouldThrowExc(RequestStatus requestStatus) {
    Mockito.when(mockRequestDTO1.getRequestStatus()).thenReturn(requestStatus);

    assertThrows(ConflictException.class, () -> requestUCC
            .changeWaitingRequestStatus(defaultRequestId, defaultUserId, requestStatus, info),
        "called changeWaitingRequestStatus() with a not waiting request"
            + "should have thrown Conflict Exception");

    Mockito.verify(mockRequestForVisitDAO, Mockito.never())
        .modifyStatusWaitingRequest(mockRequestDTO1);
    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal).rollbackTransaction();
    Mockito.verify(mockDal, Mockito.never()).commitTransaction();
  }

  @DisplayName("TEST changeWaitingRequestStatus() from waiting to waiting "
      + "should have thrown Conflict Exception")
  @Test
  void test_changeWaitingRequestStatus_intoWaiting_shouldThrowConflictException() {

    assertThrows(ConflictException.class, () -> requestUCC
            .changeWaitingRequestStatus(defaultRequestId, defaultUserId, RequestStatus.WAITING
                , info),
        "called changeWaitingRequestStatus() to change it into waiting"
            + "should have thrown Conflict Exception");

    Mockito.verify(mockRequestForVisitDAO, Mockito.never())
        .modifyStatusWaitingRequest(mockRequestDTO1);
    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal).rollbackTransaction();
    Mockito.verify(mockDal, Mockito.never()).commitTransaction();
  }

  @DisplayName("TEST changeWaitingRequestStatus() DAO throws err, should rollback and throw err")
  @ParameterizedTest
  @EnumSource(value = RequestStatus.class, names = {"CANCELED", "CONFIRMED"})
  void test_changeWaitingRequestStatus_ErrorThrown_shouldThrowError(RequestStatus requestStatus) {
    Mockito.doThrow(new InternalError()).when(mockRequestForVisitDAO)
        .modifyStatusWaitingRequest(mockRequestDTO1);

    assertThrows(InternalError.class, () -> requestUCC
            .changeWaitingRequestStatus(defaultRequestId, defaultUserId, requestStatus, info),
        "DAO throws an Error, should rollback and throw InternalError");

    Mockito.verify(mockRequestForVisitDAO)
        .modifyStatusWaitingRequest(mockRequestDTO1);
    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal).rollbackTransaction();
    Mockito.verify(mockDal, Mockito.never()).commitTransaction();
  }
}