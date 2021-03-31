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
import be.vinci.pae.utils.Configurate;
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
  private static FurnitureDTO mockFurnitureDTO1;
  private static UserDTO mockUserDTO1;

  @BeforeEach
  public void init() {
    Configurate.load("properties/test.properties");
    ServiceLocator locator = ServiceLocatorUtilities.bind(new TestBinder());

    optionUCC = locator.getService(OptionUCC.class);
    mockOptionDAO = locator.getService(OptionDAO.class);
    mockFurnitureDAO = locator.getService(FurnitureDAO.class);
    mockDal = locator.getService(ConnectionDalServices.class);

    mockOptionDTO1 = Mockito.mock(OptionDTO.class);
    mockFurnitureDTO1 = Mockito.mock(FurnitureDTO.class);
    mockUserDTO1 = Mockito.mock(UserDTO.class);


  }

  @BeforeEach
  void setUp() {
    Mockito.reset(mockOptionDAO);
    Mockito.reset(mockFurnitureDAO);
    Mockito.reset(mockDal);
    Mockito.reset(mockOptionDTO1);
    Mockito.reset(mockFurnitureDTO1);
    Mockito.reset(mockUserDTO1);
  }

  @DisplayName("TEST OptionUCC.introduceOption : given valid arguments, should return DTO")
  @Test
  public void test_introduceOption_givenValidIds_shouldReturnDTO() {
    int furnitureId = 2;
    String condition = "available_for_sale";

    Mockito.when(mockFurnitureDAO.findById(furnitureId)).thenReturn(mockFurnitureDTO1);
    Mockito.when(mockFurnitureDTO1.getCondition()).thenReturn(condition);
    Mockito.when(mockOptionDAO.introduceOption(mockUserDTO1, furnitureId))
        .thenReturn(mockOptionDTO1);

    assertEquals(mockOptionDTO1, optionUCC.introduceOption(mockUserDTO1, furnitureId),
        "calling the function with valid arguments should return corresponding DTO");

    Mockito.verify(mockFurnitureDTO1).setCondition("under_option");
    Mockito.verify(mockOptionDAO).introduceOption(mockUserDTO1, furnitureId);
    Mockito.verify(mockFurnitureDAO).updateConditionOnly(mockFurnitureDTO1);

    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal, Mockito.never()).rollbackTransaction();
    Mockito.verify(mockDal).commitTransaction();
  }

  @DisplayName("TEST OptionUCC.introduceOption : invalid furniture condition, should throw ConflictException")
  @ParameterizedTest
  @ValueSource(strings = {"requested_for_visit", "refused", "accepted", "in_restoration",
      "under_option", "sold", "reserved", "delivered", "collected", "withdrawn"})
  public void test_introduceOption_givenInvalidCondition_shouldThrowConflict(String condition) {
    int furnitureId = 2;

    Mockito.when(mockFurnitureDAO.findById(furnitureId)).thenReturn(mockFurnitureDTO1);
    Mockito.when(mockFurnitureDTO1.getCondition()).thenReturn(condition);
    Mockito.when(mockOptionDAO.introduceOption(mockUserDTO1, furnitureId))
        .thenReturn(mockOptionDTO1);

    assertThrows(ConflictException.class,
        () -> optionUCC.introduceOption(mockUserDTO1, furnitureId),
        "calling the function with furniture id corresponding to resource in"
            + " invalid condition should throw ConflictException");

    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal).rollbackTransaction();
    Mockito.verify(mockDal, Mockito.never()).commitTransaction();
  }

  @DisplayName("TEST OptionUCC.introduceOption : given invalid furniture id, should throw NotFoundException")
  @Test
  public void test_introduceOption_givenInvalidFurnitureId_shouldThrowNotFound() {
    int furnitureId = 2;
    String condition = "available_for_sale";

    Mockito.when(mockFurnitureDAO.findById(furnitureId)).thenThrow(new NotFoundException());
    Mockito.when(mockFurnitureDTO1.getCondition()).thenReturn(condition);
    Mockito.when(mockOptionDAO.introduceOption(mockUserDTO1, furnitureId))
        .thenReturn(mockOptionDTO1);

    assertThrows(NotFoundException.class,
        () -> optionUCC.introduceOption(mockUserDTO1, furnitureId),
        "calling the function with invalid userId should throw NotFoundException");

    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal).rollbackTransaction();
    Mockito.verify(mockDal, Mockito.never()).commitTransaction();
  }

  @DisplayName("TEST OptionUCC.introduceOption : catches InternalError, should throw it back after rollback")
  @Test
  public void test_introduceOption_catchesInternalError_shouldThrowInternalError() {
    int furnitureId = 2;
    String condition = "available_for_sale";

    Mockito.when(mockFurnitureDAO.findById(furnitureId)).thenReturn(mockFurnitureDTO1);
    Mockito.when(mockFurnitureDTO1.getCondition()).thenReturn(condition);
    Mockito.when(mockOptionDAO.introduceOption(mockUserDTO1, furnitureId))
        .thenThrow(new InternalError());

    assertThrows(InternalError.class, () -> optionUCC.introduceOption(mockUserDTO1, furnitureId),
        "catching InternalError should throw it back after rollback");

    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal).rollbackTransaction();
    Mockito.verify(mockDal, Mockito.never()).commitTransaction();
  }

  @DisplayName("TEST OptionUCC.cancelOption : nominal, should return OptionDTO")
  @Test
  public void test_cancelOption_givenValidArgs_shouldReturnDTO() {
    int optionId = 1;
    int furnitureId = 2;
    int userID = 3;
    String condition = "under_option";

    Mockito.when(mockOptionDAO.getOption(optionId)).thenReturn(mockOptionDTO1);
    Mockito.when(mockOptionDTO1.isCanceled()).thenReturn(false);
    Mockito.when(mockOptionDTO1.getFurnitureId()).thenReturn(furnitureId);
    Mockito.when(mockOptionDTO1.getClientId()).thenReturn(userID);
    Mockito.when(mockUserDTO1.getId()).thenReturn(userID);
    Mockito.when(mockFurnitureDAO.findById(furnitureId)).thenReturn(mockFurnitureDTO1);
    Mockito.when(mockFurnitureDTO1.getCondition()).thenReturn(condition);

    assertEquals(mockOptionDTO1, optionUCC.cancelOption(mockUserDTO1, optionId),
        "nominal, should return OptionDTO");

    Mockito.verify(mockFurnitureDTO1).setCondition("available_for_sale");
    Mockito.verify(mockFurnitureDAO).updateConditionOnly(mockFurnitureDTO1);
    Mockito.verify(mockOptionDAO).cancelOption(optionId);

    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal, Mockito.never()).rollbackTransaction();
    Mockito.verify(mockDal).commitTransaction();
  }

  @DisplayName("TEST OptionUCC.cancelOption : given unknown option id, should throw NotFoundException")
  @Test
  public void test_cancelOption_invalidOptionId_shouldThrowNotFoundException() {
    int optionId = 1;

    Mockito.when(mockOptionDAO.getOption(optionId)).thenThrow(new NotFoundException());

    assertThrows(NotFoundException.class, () -> optionUCC.cancelOption(mockUserDTO1, optionId),
        "unknown option id, should throw NotFoundException");

    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal).rollbackTransaction();
    Mockito.verify(mockDal, Mockito.never()).commitTransaction();
  }

  @DisplayName("TEST OptionUCC.cancelOption : given already canceled option id, should throw ConflictException")
  @Test
  public void test_cancelOption_alreadyCancelledOption_shouldThrowConflict() {
    int optionId = 1;

    Mockito.when(mockOptionDAO.getOption(optionId)).thenReturn(mockOptionDTO1);
    Mockito.when(mockOptionDTO1.isCanceled()).thenReturn(true);

    assertThrows(ConflictException.class, () -> optionUCC.cancelOption(mockUserDTO1, optionId),
        "already canceled option id, should throw ConflictException");

    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal).rollbackTransaction();
    Mockito.verify(mockDal, Mockito.never()).commitTransaction();
  }

  @DisplayName("TEST OptionUCC.cancelOption : given not owned option id, should throw UnauthorizedException")
  @Test
  public void test_cancelOption_notOptionOwner_shouldThrowUnauthorized() {
    int optionId = 1;
    int furnitureId = 2;
    int userId1 = 3;
    int userId2 = 4;

    Mockito.when(mockOptionDAO.getOption(optionId)).thenReturn(mockOptionDTO1);
    Mockito.when(mockOptionDTO1.isCanceled()).thenReturn(false);
    Mockito.when(mockOptionDTO1.getFurnitureId()).thenReturn(furnitureId);
    Mockito.when(mockOptionDTO1.getClientId()).thenReturn(userId1);
    Mockito.when(mockUserDTO1.getId()).thenReturn(userId2);

    assertThrows(UnauthorizedException.class, () -> optionUCC.cancelOption(mockUserDTO1, optionId),
        "not owner of option, should throw UnauthorizedException");

    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal).rollbackTransaction();
    Mockito.verify(mockDal, Mockito.never()).commitTransaction();
  }

  @DisplayName("TEST OptionUCC.cancelOption : on furniture not under option, should throw ConflictException")
  @ParameterizedTest
  @ValueSource(strings = {"requested_for_visit", "refused", "accepted", "in_restoration",
      "available_for_sale", "sold", "reserved", "delivered", "collected", "withdrawn"})
  public void test_cancelOption_givenValidArgs_shouldReturnDTO(String condition) {
    int optionId = 1;
    int furnitureId = 2;
    int userID = 3;

    Mockito.when(mockOptionDAO.getOption(optionId)).thenReturn(mockOptionDTO1);
    Mockito.when(mockOptionDTO1.isCanceled()).thenReturn(false);
    Mockito.when(mockOptionDTO1.getFurnitureId()).thenReturn(furnitureId);
    Mockito.when(mockOptionDTO1.getClientId()).thenReturn(userID);
    Mockito.when(mockUserDTO1.getId()).thenReturn(userID);
    Mockito.when(mockFurnitureDAO.findById(furnitureId)).thenReturn(mockFurnitureDTO1);
    Mockito.when(mockFurnitureDTO1.getCondition()).thenReturn(condition);

    assertThrows(ConflictException.class, () -> optionUCC.cancelOption(mockUserDTO1, optionId),
        "furniture not under option, should throw ConflictException");

    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal).rollbackTransaction();
    Mockito.verify(mockDal, Mockito.never()).commitTransaction();
  }

  @DisplayName("TEST OptionUCC.cancelOption : catches InternalError, should throw it back after rollback")
  @Test
  public void test_cancelOption_catchesInternalError_shouldThrowInternalError() {
    int optionId = 1;
    int furnitureId = 2;
    int userID = 3;

    Mockito.when(mockOptionDAO.getOption(optionId)).thenReturn(mockOptionDTO1);
    Mockito.when(mockOptionDTO1.isCanceled()).thenReturn(false);
    Mockito.when(mockOptionDTO1.getFurnitureId()).thenReturn(furnitureId);
    Mockito.when(mockOptionDTO1.getClientId()).thenReturn(userID);
    Mockito.when(mockUserDTO1.getId()).thenReturn(userID);
    Mockito.when(mockFurnitureDAO.findById(furnitureId)).thenThrow(new InternalError());

    assertThrows(InternalError.class, () -> optionUCC.cancelOption(mockUserDTO1, optionId),
        "catches InternalError, should throw it back");

    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal).rollbackTransaction();
    Mockito.verify(mockDal, Mockito.never()).commitTransaction();
  }
}