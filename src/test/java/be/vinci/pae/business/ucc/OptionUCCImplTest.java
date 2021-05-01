package be.vinci.pae.business.ucc;

import be.vinci.pae.business.dto.FurnitureDTO;
import be.vinci.pae.business.dto.OptionDTO;
import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.business.pojos.FurnitureStatus;
import be.vinci.pae.exceptions.ConflictException;
import be.vinci.pae.exceptions.NotFoundException;
import be.vinci.pae.exceptions.UnauthorizedException;
import be.vinci.pae.main.TestBinder;
import be.vinci.pae.persistence.dal.ConnectionDalServices;
import be.vinci.pae.persistence.dao.FurnitureDAO;
import be.vinci.pae.persistence.dao.OptionDAO;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InOrder;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OptionUCCImplTest {

  private static OptionUCC optionUCC;
  private static OptionDAO mockOptionDAO;
  private static FurnitureDAO mockFurnitureDAO;
  private static ConnectionDalServices mockDal;

  private static OptionDTO mockOptionDTO1;
  private static OptionDTO mockOptionDTO2;
  private static OptionDTO mockOptionDTO3;
  private static FurnitureDTO mockFurnitureDTO1;
  private static FurnitureDTO mockFurnitureDTO2;
  private static UserDTO mockUserDTO1;
  private static UserDTO mockUserDTO2;


  private static final int defaultOptionId1 = 0;
  private static final int defaultOptionId2 = 1;
  private static final int defaultOptionId3 = 2;
  private static final int defaultFurnitureId1 = 3;
  private static final int defaultFurnitureId2 = 4;
  private static final int defaultDuration = 5;
  private static final int defaultUserId1 = 6;
  private static final int defaultUserId2 = 7;
  private static final FurnitureStatus defaultStatus = FurnitureStatus.AVAILABLE_FOR_SALE;

  private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
  private static final String twoDaysAgo = LocalDate.now().minusDays(2).format(formatter);
  private static final String today = LocalDate.now().format(formatter);


  @BeforeAll
  public static void init() {
    ServiceLocator locator = ServiceLocatorUtilities.bind(new TestBinder());

    optionUCC = locator.getService(OptionUCC.class);
    mockOptionDAO = locator.getService(OptionDAO.class);
    mockFurnitureDAO = locator.getService(FurnitureDAO.class);
    mockDal = locator.getService(ConnectionDalServices.class);

    mockOptionDTO1 = Mockito.mock(OptionDTO.class);
    mockOptionDTO2 = Mockito.mock(OptionDTO.class);
    mockOptionDTO3 = Mockito.mock(OptionDTO.class);
    mockFurnitureDTO1 = Mockito.mock(FurnitureDTO.class);
    mockFurnitureDTO2 = Mockito.mock(FurnitureDTO.class);
    mockUserDTO1 = Mockito.mock(UserDTO.class);
    mockUserDTO2 = Mockito.mock(UserDTO.class);

  }

  @BeforeEach
  void setUp() {
    Mockito.reset(mockOptionDAO);
    Mockito.reset(mockFurnitureDAO);
    Mockito.reset(mockDal);
    Mockito.reset(mockOptionDTO1);
    Mockito.reset(mockOptionDTO2);
    Mockito.reset(mockOptionDTO3);
    Mockito.reset(mockFurnitureDTO1);
    Mockito.reset(mockFurnitureDTO2);
    Mockito.reset(mockUserDTO1);
    Mockito.reset(mockUserDTO2);

    Mockito.when(mockOptionDAO.findById(defaultOptionId1)).thenReturn(mockOptionDTO1);
    Mockito.when(mockOptionDAO.findById(defaultOptionId2)).thenReturn(mockOptionDTO2);
    Mockito.when(mockOptionDAO.findById(defaultOptionId3)).thenReturn(mockOptionDTO3);
    Mockito.when(mockOptionDAO.findAll())
        .thenReturn(Arrays.asList(mockOptionDTO1, mockOptionDTO2, mockOptionDTO3));

    Mockito.when(mockOptionDTO1.getOptionId()).thenReturn(defaultOptionId1);
    Mockito.when(mockOptionDTO1.getFurnitureId()).thenReturn(defaultFurnitureId1);
    Mockito.when(mockOptionDTO1.getDuration()).thenReturn(defaultDuration);
    Mockito.when(mockOptionDTO1.getUserId()).thenReturn(defaultUserId1);
    Mockito.when(mockOptionDTO1.getUser()).thenReturn(mockUserDTO1);
    Mockito.when(mockOptionDTO1.isCanceled()).thenReturn(false);

    Mockito.when(mockOptionDTO2.getOptionId()).thenReturn(defaultOptionId2);
    Mockito.when(mockOptionDTO2.getFurnitureId()).thenReturn(defaultFurnitureId1);
    Mockito.when(mockOptionDTO2.getDuration()).thenReturn(defaultDuration);
    Mockito.when(mockOptionDTO2.getUserId()).thenReturn(defaultUserId2);
    Mockito.when(mockOptionDTO2.getUser()).thenReturn(mockUserDTO2);
    Mockito.when(mockOptionDTO2.isCanceled()).thenReturn(false);

    Mockito.when(mockOptionDTO3.getOptionId()).thenReturn(defaultOptionId3);
    Mockito.when(mockOptionDTO3.getFurnitureId()).thenReturn(defaultFurnitureId1);
    Mockito.when(mockOptionDTO3.getDuration()).thenReturn(defaultDuration);
    Mockito.when(mockOptionDTO3.getUserId()).thenReturn(null);
    Mockito.when(mockOptionDTO3.getUser()).thenReturn(null);
    Mockito.when(mockOptionDTO3.isCanceled()).thenReturn(true);

    Mockito.when(mockFurnitureDAO.findById(defaultFurnitureId1)).thenReturn(mockFurnitureDTO1);
    Mockito.when(mockFurnitureDAO.findById(defaultFurnitureId2)).thenReturn(mockFurnitureDTO2);

    Mockito.when(mockFurnitureDTO1.getFurnitureId()).thenReturn(defaultFurnitureId1);
    Mockito.when(mockFurnitureDTO1.getStatus()).thenReturn(defaultStatus);

    Mockito.when(mockFurnitureDTO2.getFurnitureId()).thenReturn(defaultFurnitureId2);
    Mockito.when(mockFurnitureDTO2.getStatus()).thenReturn(defaultStatus);

    Mockito.when(mockUserDTO1.getId()).thenReturn(defaultUserId1);
    Mockito.when(mockUserDTO2.getId()).thenReturn(defaultUserId2);
  }

  @DisplayName("TEST OptionUCC.introduceOption : given valid arguments, should return DTO")
  @Test
  public void test_introduceOption_givenValidId_shouldReturnDTO() {
    Mockito.when(mockOptionDAO.introduceOption(mockUserDTO1, defaultFurnitureId1, defaultDuration))
        .thenReturn(mockOptionDTO1);

    assertEquals(mockOptionDTO1,
        optionUCC.introduceOption(mockUserDTO1, defaultFurnitureId1, defaultDuration),
        "calling the function with valid arguments should return corresponding DTO");

    Mockito.verify(mockFurnitureDTO1).setStatus(FurnitureStatus.toEnum("under_option"));
    Mockito.verify(mockOptionDAO)
        .introduceOption(mockUserDTO1, defaultFurnitureId1, defaultDuration);
    Mockito.verify(mockFurnitureDAO).updateStatusOnly(mockFurnitureDTO1);

    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal, Mockito.never()).rollbackTransaction();
    Mockito.verify(mockDal).commitTransaction();
  }

  @DisplayName("TEST OptionUCC.introduceOption : invalid "
      + "furniture status, should throw ConflictException")
  @ParameterizedTest
  @EnumSource(value = FurnitureStatus.class, names = {"REQUESTED_FOR_VISIT", "REFUSED", "ACCEPTED",
      "IN_RESTORATION", "UNDER_OPTION", "SOLD", "RESERVED", "DELIVERED", "COLLECTED", "WITHDRAWN"})
  public void test_introduceOption_givenInvalidStatus_shouldThrowConflict(FurnitureStatus status) {
    Mockito.when(mockFurnitureDTO1.getStatus()).thenReturn(status);

    assertThrows(ConflictException.class,
        () -> optionUCC.introduceOption(mockUserDTO1, defaultFurnitureId1, defaultDuration),
        "calling the function with furniture id corresponding to resource in"
            + " invalid status should throw ConflictException");

    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal).rollbackTransaction();
    Mockito.verify(mockDal, Mockito.never()).commitTransaction();
  }

  @DisplayName("TEST OptionUCC.introduceOption : given invalid "
      + "furniture id, should throw NotFoundException")
  @Test
  public void test_introduceOption_givenInvalidFurnitureId_shouldThrowNotFound() {
    Mockito.when(mockFurnitureDAO.findById(defaultFurnitureId1)).thenThrow(new NotFoundException());
    Mockito.when(mockOptionDAO.introduceOption(mockUserDTO1, defaultFurnitureId1, defaultDuration))
        .thenReturn(mockOptionDTO1);

    assertThrows(NotFoundException.class,
        () -> optionUCC.introduceOption(mockUserDTO1, defaultFurnitureId1, defaultDuration),
        "calling the function with invalid userId should throw NotFoundException");

    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal).rollbackTransaction();
    Mockito.verify(mockDal, Mockito.never()).commitTransaction();
  }

  @DisplayName("TEST OptionUCC.introduceOption : catches "
      + "InternalError, should throw it back after rollback")
  @Test
  public void test_introduceOption_catchesInternalError_shouldThrowInternalError() {
    Mockito.when(mockOptionDAO.introduceOption(mockUserDTO1, defaultFurnitureId1, defaultDuration))
        .thenThrow(new InternalError());

    assertThrows(InternalError.class,
        () -> optionUCC.introduceOption(mockUserDTO1, defaultFurnitureId1, defaultDuration),
        "catching InternalError should throw it back after rollback");

    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal).rollbackTransaction();
    Mockito.verify(mockDal, Mockito.never()).commitTransaction();
  }

  @DisplayName("TEST OptionUCC.cancelOption : nominal, should return OptionDTO")
  @Test
  public void test_cancelOption_givenValidArgs_shouldReturnDTO() {
    String status = "under_option";
    Mockito.when(mockFurnitureDTO1.getStatus()).thenReturn(FurnitureStatus.toEnum(status));

    assertEquals(mockOptionDTO1, optionUCC.cancelOption(mockUserDTO1, defaultOptionId1),
        "a valid call to cancelOption should return OptionDTO");

    Mockito.verify(mockFurnitureDTO1).setStatus(FurnitureStatus.toEnum("available_for_sale"));
    Mockito.verify(mockFurnitureDAO).updateStatusOnly(mockFurnitureDTO1);
    Mockito.verify(mockOptionDAO).cancelOption(defaultOptionId1);

    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal, Mockito.never()).rollbackTransaction();
    Mockito.verify(mockDal).commitTransaction();
  }

  @DisplayName("TEST OptionUCC.cancelOption : given unknown "
      + "option id, should throw NotFoundException")
  @Test
  public void test_cancelOption_invalidOptionId_shouldThrowNotFoundException() {
    Mockito.when(mockOptionDAO.findById(defaultOptionId1)).thenThrow(new NotFoundException());

    assertThrows(NotFoundException.class,
        () -> optionUCC.cancelOption(mockUserDTO1, defaultOptionId1),
        "unknown option id, should throw NotFoundException");

    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal).rollbackTransaction();
    Mockito.verify(mockDal, Mockito.never()).commitTransaction();
  }

  @DisplayName("TEST OptionUCC.cancelOption : given already "
      + "canceled option id, should throw ConflictException")
  @Test
  public void test_cancelOption_alreadyCancelledOption_shouldThrowConflict() {
    Mockito.when(mockOptionDTO1.isCanceled()).thenReturn(true);

    assertThrows(ConflictException.class,
        () -> optionUCC.cancelOption(mockUserDTO1, defaultOptionId1),
        "already canceled option id, should throw ConflictException");

    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal).rollbackTransaction();
    Mockito.verify(mockDal, Mockito.never()).commitTransaction();
  }

  @DisplayName("TEST OptionUCC.cancelOption : "
      + "given not owned option id, should throw UnauthorizedException")
  @Test
  public void test_cancelOption_notOptionOwner_shouldThrowUnauthorized() {
    assertThrows(UnauthorizedException.class,
        () -> optionUCC.cancelOption(mockUserDTO2, defaultOptionId1),
        "not owner of option, should throw UnauthorizedException");

    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal).rollbackTransaction();
    Mockito.verify(mockDal, Mockito.never()).commitTransaction();
  }

  @DisplayName("TEST OptionUCC.cancelOption : on furniture "
      + "not under option, should throw ConflictException")
  @ParameterizedTest
  @EnumSource(value = FurnitureStatus.class, names = {"REQUESTED_FOR_VISIT", "REFUSED", "ACCEPTED",
      "IN_RESTORATION", "AVAILABLE_FOR_SALE", "SOLD", "RESERVED", "DELIVERED", "COLLECTED",
      "WITHDRAWN"})
  public void test_cancelOption_givenInvalidStatus_shouldThrowConflict(FurnitureStatus status) {
    Mockito.when(mockFurnitureDTO1.getStatus()).thenReturn(status);

    assertThrows(ConflictException.class,
        () -> optionUCC.cancelOption(mockUserDTO1, defaultOptionId1),
        "furniture not under option, should throw ConflictException");

    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal).rollbackTransaction();
    Mockito.verify(mockDal, Mockito.never()).commitTransaction();
  }

  @DisplayName("TEST OptionUCC.cancelOption : catches "
      + "InternalError, should throw it back after rollback")
  @Test
  public void test_cancelOption_catchesInternalError_shouldThrowInternalError() {
    Mockito.when(mockFurnitureDAO.findById(defaultFurnitureId1)).thenThrow(new InternalError());

    assertThrows(InternalError.class, () -> optionUCC.cancelOption(mockUserDTO1, defaultOptionId1),
        "catches InternalError, should throw it back");

    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal).rollbackTransaction();
    Mockito.verify(mockDal, Mockito.never()).commitTransaction();
  }

  @DisplayName("TEST OptionUCC.listOption : nominal, should return list of DTOs")
  @Test
  public void test_listOption_shouldReturnDTOList() {
    List<OptionDTO> lst = Arrays.asList(mockOptionDTO1, mockOptionDTO2, mockOptionDTO3);

    assertEquals(lst, optionUCC.listOption(),
        "a valid call to listOption should return a List of OptionDTO");

    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal, Mockito.never()).rollbackTransaction();
    Mockito.verify(mockDal).commitTransaction();
  }

  @DisplayName("TEST OptionUCC.listOption : empty db, should return empty list of DTOs")
  @Test
  public void test_listOption_emptyDB_shouldReturnEmptyDTOList() {
    List<OptionDTO> emptyLst = new ArrayList<>();

    Mockito.when(mockOptionDAO.findAll()).thenReturn(emptyLst);

    assertEquals(emptyLst, optionUCC.listOption(),
        "a valid call to listOption should return an empty List if the db is empty");

    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal, Mockito.never()).rollbackTransaction();
    Mockito.verify(mockDal).commitTransaction();
  }

  @DisplayName("TEST OptionUCC.cancelOption : catches "
      + "InternalError, should throw it back after rollback")
  @Test
  public void test_listOption_catchesInternalError_shouldThrowInternalError() {
    Mockito.when(mockOptionDAO.findAll()).thenThrow(new InternalError());

    assertThrows(InternalError.class, () -> optionUCC.listOption(),
        "catches InternalError, should throw it back");

    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal).rollbackTransaction();
    Mockito.verify(mockDal, Mockito.never()).commitTransaction();
  }


  @DisplayName("TEST OptionUCC.myOptions : nominal, should return dto list")
  @Test
  void test_myOptions_nominal1_shouldReturnDtoList() {
    Mockito.when(mockOptionDAO.findByUserId(defaultUserId1))
        .thenReturn(Arrays.asList(mockOptionDTO1, mockOptionDTO2));

    List<OptionDTO> expected = Arrays.asList(mockOptionDTO1, mockOptionDTO2);
    assertEquals(expected, optionUCC.myOptions(mockUserDTO1));

    InOrder inOrder = Mockito.inOrder(mockDal, mockOptionDAO);

    inOrder.verify(mockDal).startTransaction();
    inOrder.verify(mockOptionDAO).findByUserId(defaultUserId1);
    inOrder.verify(mockDal).commitTransaction();
    inOrder.verifyNoMoreInteractions();
    inOrder.verify(mockDal, Mockito.never()).rollbackTransaction();
  }

  @DisplayName("TEST OptionUCC.myOptions : nominal, should return dto list")
  @Test
  void test_myOptions_nominal2_shouldReturnDtoList() {
    Mockito.when(mockOptionDAO.findByUserId(defaultUserId1))
        .thenReturn(Arrays.asList(mockOptionDTO1, mockOptionDTO2));

    Mockito.when(mockOptionDTO2.isCanceled()).thenReturn(true);

    List<OptionDTO> expected = Collections.singletonList(mockOptionDTO1);
    assertEquals(expected, optionUCC.myOptions(mockUserDTO1));

    InOrder inOrder = Mockito.inOrder(mockDal, mockOptionDAO);

    inOrder.verify(mockDal).startTransaction();
    inOrder.verify(mockOptionDAO).findByUserId(defaultUserId1);
    inOrder.verify(mockDal).commitTransaction();
    inOrder.verifyNoMoreInteractions();
    inOrder.verify(mockDal, Mockito.never()).rollbackTransaction();
  }

  @DisplayName("TEST OptionUCC.myOptions : catches InternalError, should throw it back")
  @Test
  void test_myOptions_catchesInternal_shouldThrowInternal() {
    Mockito.when(mockOptionDAO.findByUserId(defaultUserId1)).thenThrow(new InternalError());

    assertThrows(InternalError.class, () -> optionUCC.myOptions(mockUserDTO1));

    InOrder inOrder = Mockito.inOrder(mockDal, mockOptionDAO);

    inOrder.verify(mockDal).startTransaction();
    inOrder.verify(mockOptionDAO).findByUserId(defaultUserId1);
    inOrder.verify(mockDal).rollbackTransaction();
    inOrder.verifyNoMoreInteractions();
    inOrder.verify(mockDal, Mockito.never()).commitTransaction();
  }

  @DisplayName("TEST OptionUCC.updateExpiredOptions : nominal, should complete normally")
  @Test
  void test_updateExpiredOptions_nominal() {
    Mockito.when(mockOptionDTO1.getFurnitureId()).thenReturn(defaultFurnitureId2);
    Mockito.when(mockOptionDTO3.isCanceled()).thenReturn(true);

    Mockito.when(mockOptionDTO2.getDuration()).thenReturn(1);

    Mockito.when(mockOptionDTO1.getDateOption()).thenReturn(today);
    Mockito.when(mockOptionDTO2.getDateOption()).thenReturn(twoDaysAgo);
    Mockito.when(mockOptionDTO3.getDateOption()).thenReturn(today);

    optionUCC.updateExpiredOptions();

    InOrder inOrder = Mockito.inOrder(mockDal, mockOptionDAO, mockFurnitureDAO);
    inOrder.verify(mockDal).startTransaction();
    inOrder.verify(mockOptionDAO).findAll();
    inOrder.verify(mockFurnitureDAO).findById(defaultFurnitureId1);
    inOrder.verify(mockFurnitureDAO).updateStatusOnly(mockFurnitureDTO1);
    inOrder.verify(mockOptionDAO).cancelOption(defaultOptionId2);
    inOrder.verify(mockDal).commitTransaction();
    inOrder.verifyNoMoreInteractions();
    inOrder.verify(mockDal, Mockito.never()).rollbackTransaction();
    inOrder.verify(mockOptionDAO, Mockito.never()).cancelOption(defaultOptionId1);
    inOrder.verify(mockOptionDAO, Mockito.never()).cancelOption(defaultOptionId3);
    inOrder.verify(mockFurnitureDAO, Mockito.never()).updateStatusOnly(mockFurnitureDTO2);
  }

  @DisplayName("TEST OptionUCC.updateExpiredOptions : catches InternalError, should throw it back")
  @Test
  void test_updateExpiredOptions_catchesInternal_shouldThrowItBack() {
    Mockito.when(mockOptionDTO1.getFurnitureId()).thenReturn(defaultFurnitureId2);
    Mockito.when(mockOptionDTO3.isCanceled()).thenReturn(true);

    Mockito.when(mockOptionDTO2.getDuration()).thenReturn(1);

    Mockito.when(mockOptionDTO1.getDateOption()).thenReturn(today);
    Mockito.when(mockOptionDTO2.getDateOption()).thenReturn(twoDaysAgo);
    Mockito.when(mockOptionDTO3.getDateOption()).thenReturn(today);

    Mockito.when(mockFurnitureDAO.updateStatusOnly(mockFurnitureDTO1))
        .thenThrow(new InternalError());

    assertThrows(InternalError.class, ()->
        optionUCC.updateExpiredOptions());

    InOrder inOrder = Mockito.inOrder(mockDal, mockOptionDAO, mockFurnitureDAO);
    inOrder.verify(mockDal).startTransaction();
    inOrder.verify(mockOptionDAO).findAll();
    inOrder.verify(mockFurnitureDAO).findById(defaultFurnitureId1);
    inOrder.verify(mockFurnitureDAO).updateStatusOnly(mockFurnitureDTO1);
    inOrder.verify(mockDal).rollbackTransaction();
    inOrder.verifyNoMoreInteractions();
    inOrder.verify(mockDal, Mockito.never()).commitTransaction();
  }
}