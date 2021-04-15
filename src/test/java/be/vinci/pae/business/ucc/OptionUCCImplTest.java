package be.vinci.pae.business.ucc;

import be.vinci.pae.business.dto.FurnitureDTO;
import be.vinci.pae.business.dto.OptionDTO;
import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.exceptions.ConflictException;
import be.vinci.pae.exceptions.NotFoundException;
import be.vinci.pae.exceptions.UnauthorizedException;
import be.vinci.pae.main.TestBinder;
import be.vinci.pae.persistence.dal.ConnectionDalServices;
import be.vinci.pae.persistence.dao.FurnitureDAO;
import be.vinci.pae.persistence.dao.OptionDAO;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
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
  private static FurnitureDTO mockFurnitureDTO1;
  private static UserDTO mockUserDTO1;
  private static UserDTO mockUserDTO2;


  private static final int defaultOptionId1 = 0;
  private static final int defaultOptionId2 = 1;
  private static final int defaultFurnitureId1 = 2;
  private static final int defaultDuration1 = 3;
  private static final int defaultDuration2 = 4;
  private static final int defaultUserId1 = 5;
  private static final int defaultUserId2 = 6;
  private static final String defaultStatus = "available_for_sale";

  @BeforeEach
  public void init() {
    ServiceLocator locator = ServiceLocatorUtilities.bind(new TestBinder());

    optionUCC = locator.getService(OptionUCC.class);
    mockOptionDAO = locator.getService(OptionDAO.class);
    mockFurnitureDAO = locator.getService(FurnitureDAO.class);
    mockDal = locator.getService(ConnectionDalServices.class);

    mockOptionDTO1 = Mockito.mock(OptionDTO.class);
    mockOptionDTO2 = Mockito.mock(OptionDTO.class);
    mockFurnitureDTO1 = Mockito.mock(FurnitureDTO.class);
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
    Mockito.reset(mockFurnitureDTO1);
    Mockito.reset(mockUserDTO1);
    Mockito.reset(mockUserDTO2);

    Mockito.when(mockOptionDAO.getOption(defaultOptionId1)).thenReturn(mockOptionDTO1);
    Mockito.when(mockOptionDAO.getOption(defaultOptionId2)).thenReturn(mockOptionDTO2);

    Mockito.when(mockOptionDTO1.getOptionId()).thenReturn(defaultOptionId1);
    Mockito.when(mockOptionDTO1.getFurnitureId()).thenReturn(defaultFurnitureId1);
    Mockito.when(mockOptionDTO1.getDuration()).thenReturn(defaultDuration1);
    Mockito.when(mockOptionDTO1.getUserId()).thenReturn(defaultUserId1);
    Mockito.when(mockOptionDTO1.getUser()).thenReturn(mockUserDTO1);
    Mockito.when(mockOptionDTO1.isCanceled()).thenReturn(false);

    Mockito.when(mockOptionDTO2.getOptionId()).thenReturn(defaultOptionId2);
    Mockito.when(mockOptionDTO2.getFurnitureId()).thenReturn(defaultFurnitureId1);
    Mockito.when(mockOptionDTO2.getDuration()).thenReturn(defaultDuration2);
    Mockito.when(mockOptionDTO2.getUserId()).thenReturn(defaultUserId2);
    Mockito.when(mockOptionDTO2.getUser()).thenReturn(mockUserDTO2);
    Mockito.when(mockOptionDTO2.isCanceled()).thenReturn(false);

    Mockito.when(mockFurnitureDAO.findById(defaultFurnitureId1)).thenReturn(mockFurnitureDTO1);

    Mockito.when(mockFurnitureDTO1.getFurnitureId()).thenReturn(defaultFurnitureId1);
    Mockito.when(mockFurnitureDTO1.getStatus()).thenReturn(defaultStatus);

    Mockito.when(mockUserDTO1.getId()).thenReturn(defaultUserId1);
    Mockito.when(mockUserDTO2.getId()).thenReturn(defaultUserId2);
  }

  @DisplayName("TEST OptionUCC.introduceOption : given valid arguments, should return DTO")
  @Test
  public void test_introduceOption_givenValidId_shouldReturnDTO() {
    Mockito.when(mockOptionDAO.introduceOption(mockUserDTO1, defaultFurnitureId1, defaultDuration1))
        .thenReturn(mockOptionDTO1);

    assertEquals(mockOptionDTO1,
        optionUCC.introduceOption(mockUserDTO1, defaultFurnitureId1, defaultDuration1),
        "calling the function with valid arguments should return corresponding DTO");

    Mockito.verify(mockFurnitureDTO1).setStatus("under_option");
    Mockito.verify(mockOptionDAO)
        .introduceOption(mockUserDTO1, defaultFurnitureId1, defaultDuration1);
    Mockito.verify(mockFurnitureDAO).updateStatusOnly(mockFurnitureDTO1);

    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal, Mockito.never()).rollbackTransaction();
    Mockito.verify(mockDal).commitTransaction();
  }

  @DisplayName("TEST OptionUCC.introduceOption : invalid "
      + "furniture status, should throw ConflictException")
  @ParameterizedTest
  @ValueSource(strings = {"requested_for_visit", "refused", "accepted", "in_restoration",
      "under_option", "sold", "reserved", "delivered", "collected", "withdrawn"})
  public void test_introduceOption_givenInvalidStatus_shouldThrowConflict(String status) {
    Mockito.when(mockFurnitureDTO1.getStatus()).thenReturn(status);

    assertThrows(ConflictException.class,
        () -> optionUCC.introduceOption(mockUserDTO1, defaultFurnitureId1, defaultDuration1),
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
    Mockito.when(mockOptionDAO.introduceOption(mockUserDTO1, defaultFurnitureId1, defaultDuration1))
        .thenReturn(mockOptionDTO1);

    assertThrows(NotFoundException.class,
        () -> optionUCC.introduceOption(mockUserDTO1, defaultFurnitureId1, defaultDuration1),
        "calling the function with invalid userId should throw NotFoundException");

    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal).rollbackTransaction();
    Mockito.verify(mockDal, Mockito.never()).commitTransaction();
  }

  @DisplayName("TEST OptionUCC.introduceOption : catches "
      + "InternalError, should throw it back after rollback")
  @Test
  public void test_introduceOption_catchesInternalError_shouldThrowInternalError() {
    Mockito.when(mockOptionDAO.introduceOption(mockUserDTO1, defaultFurnitureId1, defaultDuration1))
        .thenThrow(new InternalError());

    assertThrows(InternalError.class,
        () -> optionUCC.introduceOption(mockUserDTO1, defaultFurnitureId1, defaultDuration1),
        "catching InternalError should throw it back after rollback");

    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal).rollbackTransaction();
    Mockito.verify(mockDal, Mockito.never()).commitTransaction();
  }

  @DisplayName("TEST OptionUCC.cancelOption : nominal, should return OptionDTO")
  @Test
  public void test_cancelOption_givenValidArgs_shouldReturnDTO() {
    String status = "under_option";
    Mockito.when(mockFurnitureDTO1.getStatus()).thenReturn(status);

    assertEquals(mockOptionDTO1, optionUCC.cancelOption(mockUserDTO1, defaultOptionId1),
        "nominal, should return OptionDTO");

    Mockito.verify(mockFurnitureDTO1).setStatus("available_for_sale");
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
    Mockito.when(mockOptionDAO.getOption(defaultOptionId1)).thenThrow(new NotFoundException());

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
  @ValueSource(strings = {"requested_for_visit", "refused", "accepted", "in_restoration",
      "available_for_sale", "sold", "reserved", "delivered", "collected", "withdrawn"})
  public void test_cancelOption_givenInvalidStatus_shouldThrowConflict(String status) {
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
    List<OptionDTO> lst = Arrays.asList(mockOptionDTO1, mockOptionDTO2);

    Mockito.when(mockOptionDAO.findAll()).thenReturn(lst);

    assertEquals(lst, optionUCC.listOption());

    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal, Mockito.never()).rollbackTransaction();
    Mockito.verify(mockDal).commitTransaction();
  }

  @DisplayName("TEST OptionUCC.listOption : empty db, should return empty list of DTOs")
  @Test
  public void test_listOption_emptyDB_shouldReturnEmptyDTOList() {
    List<OptionDTO> emptyLst = new ArrayList<>();

    Mockito.when(mockOptionDAO.findAll()).thenReturn(emptyLst);

    assertEquals(emptyLst, optionUCC.listOption());

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
}