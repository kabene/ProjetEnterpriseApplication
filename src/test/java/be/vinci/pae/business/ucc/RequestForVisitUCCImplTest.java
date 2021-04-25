package be.vinci.pae.business.ucc;

import be.vinci.pae.business.dto.RequestForVisitDTO;
import be.vinci.pae.business.pojos.RequestStatus;
import be.vinci.pae.exceptions.ConflictException;
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

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RequestForVisitUCCImplTest {

  private static RequestForVisitUCC requestUCC;
  private static ConnectionDalServices mockDal;

  private static RequestForVisitDAO requestForVisitDAO;

  private static RequestForVisitDTO mockRequestDTO1;
  private static RequestForVisitDTO mockRequestDTO2;
  private static RequestForVisitDTO mockRequestDTO3;

  @BeforeAll
  public static void init() {
    ServiceLocator locator = ServiceLocatorUtilities.bind(new TestBinder());

    requestUCC = locator.getService(RequestForVisitUCC.class);
    mockDal = locator.getService(ConnectionDalServices.class);

    requestForVisitDAO = locator.getService(RequestForVisitDAO.class);

    mockRequestDTO1 = Mockito.mock(RequestForVisitDTO.class);
    mockRequestDTO2 = Mockito.mock(RequestForVisitDTO.class);
    mockRequestDTO3 = Mockito.mock(RequestForVisitDTO.class);
  }

  @BeforeEach
  void setUp() {
    Mockito.reset(mockDal);
    Mockito.reset(requestForVisitDAO);
    Mockito.reset(mockRequestDTO1);
    Mockito.reset(mockRequestDTO2);
    Mockito.reset(mockRequestDTO3);

    Mockito.when(mockRequestDTO1.getRequestId()).thenReturn(1);
    Mockito.when(mockRequestDTO2.getRequestId()).thenReturn(2);
    Mockito.when(mockRequestDTO3.getRequestId()).thenReturn(3);
  }

  @DisplayName("TEST listRequest() should have return all requests")
  @Test
  void test_listRequest_shouldReturnDTOList() {
    List<RequestForVisitDTO> list = Arrays.asList(mockRequestDTO1, mockRequestDTO2,
        mockRequestDTO3);
    Mockito.when(requestForVisitDAO.findAll()).thenReturn(list);

    assertEquals(list, requestUCC.listRequest()
        ,"called listRequest should have return all requests");

    Mockito.verify(requestForVisitDAO).findAll();
    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal, Mockito.never()).rollbackTransaction();
    Mockito.verify(mockDal).commitTransaction();
  }

  @DisplayName("TEST listRequest() without data should have return an empty list")
  @Test
  void test_listRequestWithEmptyDB_shouldReturnEmptyDTOList() {
    List<RequestForVisitDTO> list = Arrays.asList();
    Mockito.when(requestForVisitDAO.findAll()).thenReturn(list);

    assertEquals(list, requestUCC.listRequest()
        ,"called listRequest without data in the db should return an empty list");

    Mockito.verify(requestForVisitDAO).findAll();
    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal, Mockito.never()).rollbackTransaction();
    Mockito.verify(mockDal).commitTransaction();
  }

  @DisplayName("TEST listRequest() DAO throws an Error, should rollback and throw Error")
  @Test
  void test_listRequest_InternalErrorThrown_shouldThrowInternalErrorAndRollback() {
    Mockito.when(requestForVisitDAO.findAll()).thenThrow(new InternalError());

    assertThrows(InternalError.class, () -> requestUCC.listRequest(),
        "DAO throws an Error, should rollback and throw InternalError");

    Mockito.verify(requestForVisitDAO).findAll();
    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal).rollbackTransaction();
    Mockito.verify(mockDal, Mockito.never()).commitTransaction();
  }

  @DisplayName("TEST listRequestByUserId() should have return correct requests")
  @Test
  void test_listRequestByUserId_shouldReturnCorrectRequests() {
    List<RequestForVisitDTO> list = Arrays.asList(mockRequestDTO1, mockRequestDTO2);
    int userId = 1;
    Mockito.when(requestForVisitDAO.findByUserId(userId)).thenReturn(list);

    assertEquals(list, requestUCC.listRequestByUserId(userId)
        ,"called listRequestByUserId should have return all requests from client");

    Mockito.verify(requestForVisitDAO).findByUserId(userId);
    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal, Mockito.never()).rollbackTransaction();
    Mockito.verify(mockDal).commitTransaction();
  }

  @DisplayName("TEST listRequestByUserId() should have return correct requests")
  @Test
  void test_listRequestByUserIdWithEmptyDb_shouldReturnCorrectRequests() {
    List<RequestForVisitDTO> list = Arrays.asList();
    int userId = 1;
    Mockito.when(requestForVisitDAO.findByUserId(userId)).thenReturn(list);

    assertEquals(list, requestUCC.listRequestByUserId(userId)
        ,"called listRequestByUserId with empty db should have return an empty list");

    Mockito.verify(requestForVisitDAO).findByUserId(userId);
    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal, Mockito.never()).rollbackTransaction();
    Mockito.verify(mockDal).commitTransaction();
  }

  @DisplayName("TEST listRequestByUserId() DAO throws an Error, should rollback and throw Error")
  @Test
  void test_listRequestByUser_ErrorThrown_shouldThrowInternalErrorAndRollback() {
    int userId = 1;
    Mockito.when(requestForVisitDAO.findByUserId(userId)).thenThrow(new InternalError());

    assertThrows(InternalError.class, () -> requestUCC.listRequestByUserId(userId),
        "DAO throws an Error, should rollback and throw InternalError");

    Mockito.verify(requestForVisitDAO).findByUserId(userId);
    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal).rollbackTransaction();
    Mockito.verify(mockDal, Mockito.never()).commitTransaction();
  }

  @DisplayName("TEST cancelRequest() with the good userId and a good request " +
      "should return the good request")
  @Test
  void test_cancelRequest_shouldReturnRequest() {
    int requestId = 1;
    int userId = 1;
    Mockito.when(requestForVisitDAO.findByRequestId(requestId)).thenReturn(mockRequestDTO1);
    Mockito.when(mockRequestDTO1.getRequestStatus()).thenReturn(RequestStatus.WAITING);
    Mockito.when(mockRequestDTO1.getUserId()).thenReturn(userId);

    assertEquals(mockRequestDTO1, requestUCC.cancelRequest(requestId, userId)
        , "called cancelRequest() with the good userId and a good request " +
            "should return the good request");

    Mockito.verify(requestForVisitDAO).cancelRequest(requestId);
    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal, Mockito.never()).rollbackTransaction();
    Mockito.verify(mockDal).commitTransaction();
  }

  @DisplayName("TEST cancelRequest() with a bad userId and a good request " +
      "should have thrown Unauthorized Exception")
  @Test
  void test_cancelRequestWithBadUserId_shouldThrowUnauthorizedException() {
    int requestId = 1;
    int goodUserId = 1;
    int badUserId = 2;
    Mockito.when(requestForVisitDAO.findByRequestId(requestId)).thenReturn(mockRequestDTO1);
    Mockito.when(mockRequestDTO1.getRequestStatus()).thenReturn(RequestStatus.WAITING);
    Mockito.when(mockRequestDTO1.getUserId()).thenReturn(goodUserId);

    assertThrows(UnauthorizedException.class, () -> requestUCC.cancelRequest(requestId, badUserId)
        , "called cancelRequest() with a bad userId" +
            "should have thrown Unauthorized Exception");

    Mockito.verify(requestForVisitDAO, Mockito.never()).cancelRequest(requestId);
    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal).rollbackTransaction();
    Mockito.verify(mockDal, Mockito.never()).commitTransaction();
  }

  @DisplayName("TEST cancelRequest() with a bad request " +
      "should have thrown Conflict Exception")
  @ParameterizedTest
  @EnumSource(value = RequestStatus.class, names = {"CANCELED", "CONFIRMED"})
  void test_cancelRequestWithBadRequest_shouldThrowConflictException(RequestStatus requestStatus) {
    int requestId = 1;
    int userId = 1;
    Mockito.when(requestForVisitDAO.findByRequestId(requestId)).thenReturn(mockRequestDTO1);
    Mockito.when(mockRequestDTO1.getRequestStatus()).thenReturn(requestStatus);
    Mockito.when(mockRequestDTO1.getUserId()).thenReturn(userId);

    assertThrows(ConflictException.class, () -> requestUCC.cancelRequest(requestId, userId)
        , "called cancelRequest() with a bad request" +
            "should have thrown Conflict Exception");

    Mockito.verify(requestForVisitDAO, Mockito.never()).cancelRequest(requestId);
    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal).rollbackTransaction();
    Mockito.verify(mockDal, Mockito.never()).commitTransaction();
  }

  @DisplayName("TEST cancelRequest() DAO throws an Error, should rollback and throw Error")
  @Test
  void test_cancelRequest_ErrorThrown_shouldThrowInternalErrorAndRollback() {
    int requestId = 1;
    int userId = 1;
    Mockito.doThrow(new InternalError()).when(requestForVisitDAO).cancelRequest(requestId);
    Mockito.when(requestForVisitDAO.findByRequestId(requestId)).thenReturn(mockRequestDTO1);
    Mockito.when(mockRequestDTO1.getRequestStatus()).thenReturn(RequestStatus.WAITING);
    Mockito.when(mockRequestDTO1.getUserId()).thenReturn(userId);

    assertThrows(InternalError.class, () -> requestUCC.cancelRequest(requestId, userId),
        "DAO throws an Error, should rollback and throw InternalError");

    Mockito.verify(requestForVisitDAO).cancelRequest(userId);
    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal).rollbackTransaction();
    Mockito.verify(mockDal, Mockito.never()).commitTransaction();
  }

  @DisplayName("TEST acceptRequest() with the good userId and a good request " +
      "should return the good request")
  @Test
  void test_acceptRequest_shouldReturnRequest() {
    int requestId = 1;
    int userId = 1;
    Mockito.when(requestForVisitDAO.findByRequestId(requestId)).thenReturn(mockRequestDTO1);
    Mockito.when(mockRequestDTO1.getRequestStatus()).thenReturn(RequestStatus.WAITING);
    Mockito.when(mockRequestDTO1.getUserId()).thenReturn(userId);

    assertEquals(mockRequestDTO1, requestUCC.acceptRequest(requestId, userId)
        , "called acceptRequest() with the good userId and a good request " +
            "should return the good request");

    Mockito.verify(requestForVisitDAO).acceptRequest(requestId);
    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal, Mockito.never()).rollbackTransaction();
    Mockito.verify(mockDal).commitTransaction();
  }

  @DisplayName("TEST acceptRequest() with a bad userId and a good request " +
      "should have thrown Unauthorized Exception")
  @Test
  void test_acceptRequestWithBadUserId_shouldThrowUnauthorizedException() {
    int requestId = 1;
    int goodUserId = 1;
    int badUserId = 2;
    Mockito.when(requestForVisitDAO.findByRequestId(requestId)).thenReturn(mockRequestDTO1);
    Mockito.when(mockRequestDTO1.getRequestStatus()).thenReturn(RequestStatus.WAITING);
    Mockito.when(mockRequestDTO1.getUserId()).thenReturn(goodUserId);

    assertThrows(UnauthorizedException.class, () -> requestUCC.acceptRequest(requestId, badUserId)
        , "called acceptRequest() with a bad userId" +
            "should have thrown Unauthorized Exception");

    Mockito.verify(requestForVisitDAO, Mockito.never()).acceptRequest(requestId);
    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal).rollbackTransaction();
    Mockito.verify(mockDal, Mockito.never()).commitTransaction();
  }

  @DisplayName("TEST acceptRequest() with a bad request " +
      "should have thrown Conflict Exception")
  @ParameterizedTest
  @EnumSource(value = RequestStatus.class, names = {"CANCELED", "CONFIRMED"})
  void test_acceptRequestWithBadRequest_shouldThrowConflictException(RequestStatus requestStatus) {
    int requestId = 1;
    int userId = 1;
    Mockito.when(requestForVisitDAO.findByRequestId(requestId)).thenReturn(mockRequestDTO1);
    Mockito.when(mockRequestDTO1.getRequestStatus()).thenReturn(requestStatus);
    Mockito.when(mockRequestDTO1.getUserId()).thenReturn(userId);

    assertThrows(ConflictException.class, () -> requestUCC.acceptRequest(requestId, userId)
        , "called acceptRequest() with a bad request" +
            "should have thrown Conflict Exception");

    Mockito.verify(requestForVisitDAO, Mockito.never()).acceptRequest(requestId);
    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal).rollbackTransaction();
    Mockito.verify(mockDal, Mockito.never()).commitTransaction();
  }

  @DisplayName("TEST acceptRequest() DAO throws an Error, should rollback and throw Error")
  @Test
  void test_acceptRequest_ErrorThrown_shouldThrowInternalErrorAndRollback() {
    int requestId = 1;
    int userId = 1;
    Mockito.doThrow(new InternalError()).when(requestForVisitDAO).acceptRequest(requestId);
    Mockito.when(requestForVisitDAO.findByRequestId(requestId)).thenReturn(mockRequestDTO1);
    Mockito.when(mockRequestDTO1.getRequestStatus()).thenReturn(RequestStatus.WAITING);
    Mockito.when(mockRequestDTO1.getUserId()).thenReturn(userId);

    assertThrows(InternalError.class, () -> requestUCC.acceptRequest(requestId, userId),
        "DAO throws an Error, should rollback and throw InternalError");

    Mockito.verify(requestForVisitDAO).acceptRequest(userId);
    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal).rollbackTransaction();
    Mockito.verify(mockDal, Mockito.never()).commitTransaction();
  }
}