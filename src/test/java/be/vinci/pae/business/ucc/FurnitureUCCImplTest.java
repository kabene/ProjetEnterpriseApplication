package be.vinci.pae.business.ucc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import be.vinci.pae.business.dto.FurnitureDTO;
import be.vinci.pae.business.dto.OptionDTO;
import be.vinci.pae.business.dto.PhotoDTO;
import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.business.pojos.FurnitureImpl;
import be.vinci.pae.business.pojos.OptionImpl;
import be.vinci.pae.business.pojos.PhotoImpl;
import be.vinci.pae.business.pojos.UserImpl;
import be.vinci.pae.exceptions.ConflictException;
import be.vinci.pae.exceptions.NotFoundException;
import be.vinci.pae.main.TestBinder;
import be.vinci.pae.persistence.dal.ConnectionDalServices;
import be.vinci.pae.persistence.dao.FurnitureDAO;
import be.vinci.pae.persistence.dao.FurnitureTypeDAO;
import be.vinci.pae.persistence.dao.OptionDAO;
import be.vinci.pae.persistence.dao.PhotoDAO;
import be.vinci.pae.persistence.dao.UserDAO;
import be.vinci.pae.utils.Configurate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

class FurnitureUCCImplTest {

  private static FurnitureUCC furnitureUCC;
  private static FurnitureDAO mockFurnitureDAO;
  private static UserDAO mockUserDAO;
  private static PhotoDAO mockPhotoDAO;
  private static OptionDAO mockOptionDAO;
  private static FurnitureTypeDAO mockFurnitureTypeDAO;
  private static ConnectionDalServices mockDal;

  private static FurnitureDTO mockFurnitureDTO1;
  private static FurnitureDTO mockFurnitureDTO2;
  private static UserDTO mockUserDTO1;
  private static UserDTO mockUserDTO2;
  private static UserDTO mockUserDTO3;
  private static PhotoDTO mockPhotoDTO1;
  private static PhotoDTO mockPhotoDTO2;
  private static PhotoDTO mockPhotoDTO3;
  private static OptionDTO mockOptionDTO;

  private static final int defaultFurnitureId1 = 0;
  private static final int defaultFurnitureId2 = 1;

  private static final int defaultUserId1 = 2;
  private static final int defaultUserId2 = 3;
  private static final int defaultUserId3 = 4;

  private static final int defaultPhotoId1 = 5;
  private static final int defaultPhotoId2 = 6;
  private static final int defaultPhotoId3 = 7;

  private static final int defaultOptionId1 = 8;

  private static final int defaultTypeId1 = 9;
  private static final int defaultTypeId2 = 10;

  private static final int defaultBuyerId1 = defaultUserId1;
  private static UserDTO defaultBuyer1;

  private static final int defaultSellerId1 = defaultUserId2;
  private static UserDTO defaultSeller1;

  private static final int defaultBuyerId2 = defaultUserId3;
  private static UserDTO defaultBuyer2;

  private static final int defaultSellerId2 = defaultUserId1;
  private static UserDTO defaultSeller2;

  private static final int defaultFavouritePhotoId1 = defaultPhotoId1;
  private static PhotoDTO defaultFavouritePhoto1;

  private static final int defaultFavouritePhotoId2 = defaultPhotoId3;
  private static PhotoDTO defaultFavouritePhoto2;

  private static final String defaultType1 = "type 1";
  private static final String defaultType2 = "type 2";

  private static final String defaultCondition = "available_for_sale";
  private static final double defaultSellingPrice1 = 1.50;
  private static final double defaultSellingPrice2 = 2.50;



  @BeforeAll
  public static void init() {
    ServiceLocator locator = ServiceLocatorUtilities.bind(new TestBinder());
    furnitureUCC = locator.getService(FurnitureUCC.class);

    mockFurnitureDAO = locator.getService(FurnitureDAO.class);
    mockUserDAO = locator.getService(UserDAO.class);
    mockPhotoDAO = locator.getService(PhotoDAO.class);
    mockFurnitureTypeDAO = locator.getService(FurnitureTypeDAO.class);
    mockOptionDAO = locator.getService(OptionDAO.class);
    mockDal = locator.getService(ConnectionDalServices.class);

    mockFurnitureDTO1 = Mockito.mock(FurnitureImpl.class);
    mockFurnitureDTO2 = Mockito.mock(FurnitureImpl.class);
    mockUserDTO1 = Mockito.mock(UserImpl.class);
    mockUserDTO2 = Mockito.mock(UserImpl.class);
    mockUserDTO3 = Mockito.mock(UserImpl.class);
    mockPhotoDTO1 = Mockito.mock(PhotoImpl.class);
    mockPhotoDTO2 = Mockito.mock(PhotoImpl.class);
    mockPhotoDTO3 = Mockito.mock(PhotoImpl.class);
    mockOptionDTO = Mockito.mock(OptionImpl.class);

    defaultBuyer1 = mockUserDTO1;
    defaultSeller1 = mockUserDTO2;
    defaultBuyer2 = mockUserDTO3;
    defaultSeller2 = mockUserDTO1;

    defaultFavouritePhoto1 = mockPhotoDTO1;
    defaultFavouritePhoto2 = mockPhotoDTO3;
  }

  @BeforeEach
  public void setUp() {
    Mockito.reset(mockFurnitureDAO);
    Mockito.reset(mockUserDAO);
    Mockito.reset(mockPhotoDAO);
    Mockito.reset(mockFurnitureTypeDAO);
    Mockito.reset(mockOptionDAO);
    Mockito.reset(mockDal);
    Mockito.reset(mockFurnitureDTO1);
    Mockito.reset(mockFurnitureDTO2);
    Mockito.reset(mockUserDTO1);
    Mockito.reset(mockUserDTO2);
    Mockito.reset(mockUserDTO3);
    Mockito.reset(mockPhotoDTO1);
    Mockito.reset(mockPhotoDTO2);
    Mockito.reset(mockPhotoDTO3);
    Mockito.reset(mockOptionDTO);

    Mockito.when(mockFurnitureDAO.findById(defaultFurnitureId1)).thenReturn(mockFurnitureDTO1);

    Mockito.when(mockUserDAO.findById(defaultUserId1)).thenReturn(mockUserDTO1);
    Mockito.when(mockUserDAO.findById(defaultUserId2)).thenReturn(mockUserDTO2);
    Mockito.when(mockUserDAO.findById(defaultUserId3)).thenReturn(mockUserDTO3);

    Mockito.when(mockPhotoDAO.getPhotoById(defaultPhotoId1)).thenReturn(mockPhotoDTO1);
    Mockito.when(mockPhotoDAO.getPhotoById(defaultPhotoId2)).thenReturn(mockPhotoDTO2);
    Mockito.when(mockPhotoDAO.getPhotoById(defaultPhotoId3)).thenReturn(mockPhotoDTO3);

    Mockito.when(mockFurnitureTypeDAO.findById(defaultTypeId1)).thenReturn(defaultType1);
    Mockito.when(mockFurnitureTypeDAO.findById(defaultTypeId2)).thenReturn(defaultType2);

    Mockito.when(mockFurnitureDTO1.getFurnitureId()).thenReturn(defaultFurnitureId1);
    Mockito.when(mockFurnitureDTO1.getBuyerId()).thenReturn(defaultBuyerId1);
    Mockito.when(mockFurnitureDTO1.getSellerId()).thenReturn(defaultSellerId1);
    Mockito.when(mockFurnitureDTO1.getFavouritePhotoId()).thenReturn(defaultFavouritePhotoId1);
    Mockito.when(mockFurnitureDTO1.getTypeId()).thenReturn(defaultTypeId1);
    Mockito.when(mockFurnitureDTO1.getCondition()).thenReturn(defaultCondition);

    Mockito.when(mockFurnitureDTO2.getFurnitureId()).thenReturn(defaultFurnitureId2);
    Mockito.when(mockFurnitureDTO2.getBuyerId()).thenReturn(defaultBuyerId2);
    Mockito.when(mockFurnitureDTO2.getSellerId()).thenReturn(defaultSellerId2);
    Mockito.when(mockFurnitureDTO2.getFavouritePhotoId()).thenReturn(defaultFavouritePhotoId2);
    Mockito.when(mockFurnitureDTO2.getTypeId()).thenReturn(defaultTypeId2);
    Mockito.when(mockFurnitureDTO2.getCondition()).thenReturn(defaultCondition);

    Mockito.when(mockOptionDTO.getOptionId()).thenReturn(defaultOptionId1);
  }

  @DisplayName("TEST FurnitureUCC.getOne : given valid id, should return dto "
      + "(containing seller + buyer + favourite photo + 2 photos + type)")
  @Test
  public void test_getOne_givenValidId_shouldReturnFurnitureDTO() {
    List<PhotoDTO> photos = Arrays.asList(mockPhotoDTO1, mockPhotoDTO2);

    Mockito.when(mockPhotoDAO.findAllByFurnitureId(defaultFurnitureId1)).thenReturn(photos);

    FurnitureDTO actual = furnitureUCC.getOne(defaultFurnitureId1);
    FurnitureDTO expected = mockFurnitureDTO1;
    assertEquals(expected, actual,
        "The getOne method should return the corresponding dto if the given id is valid.");

    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal).commitTransaction();
    Mockito.verify(mockDal, Mockito.never()).rollbackTransaction();

    Mockito.verify(mockFurnitureDAO).findById(defaultFurnitureId1);

    Mockito.verify(mockUserDAO).findById(defaultBuyerId1);
    Mockito.verify(mockUserDAO).findById(defaultSellerId1);

    Mockito.verify(mockPhotoDAO).getPhotoById(defaultFavouritePhotoId1);
    Mockito.verify(mockPhotoDAO).findAllByFurnitureId(defaultFurnitureId1);

    Mockito.verify(mockFurnitureTypeDAO).findById(defaultTypeId1);

    Mockito.verify(mockFurnitureDTO1).setBuyer(defaultBuyer1);
    Mockito.verify(mockFurnitureDTO1).setSeller(defaultSeller1);
    Mockito.verify(mockFurnitureDTO1).setFavouritePhoto(defaultFavouritePhoto1);
    Mockito.verify(mockFurnitureDTO1).setPhotos(photos);
    Mockito.verify(mockFurnitureDTO1).setType(defaultType1);
  }

  @DisplayName("TEST FurnitureUCC.getOne : given invalid id, should throw NotFoundException")
  @Test
  public void test_getOne_givenInvalidId_shouldReturnNull() {
    Mockito.when(mockFurnitureDAO.findById(defaultFurnitureId1)).thenThrow(NotFoundException.class);

    assertThrows(NotFoundException.class,
        () -> furnitureUCC.getOne(defaultFurnitureId1),
        "Calling getOne with an invalid id should throw NotFoundException.");

    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal).rollbackTransaction();
    Mockito.verify(mockDal, Mockito.never()).commitTransaction();

    Mockito.verify(mockFurnitureDAO).findById(defaultFurnitureId1);
  }

  @DisplayName("TEST UserUCC.getOne : DAO throws InternalError, "
      + "Should rollback and throw InternalError")
  @Test
  public void test_getOne_InternalErrorThrown_shouldThrowInternalErrorAndRollback() {
    Mockito.when(mockFurnitureDAO.findById(defaultFurnitureId1)).thenThrow(new InternalError("some error"));

    assertThrows(InternalError.class, () -> furnitureUCC.getOne(defaultFurnitureId1),
        "If the DAO throws an exception, it should be thrown back");

    Mockito.verify(mockFurnitureDAO).findById(defaultFurnitureId1);

    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal).rollbackTransaction();
    Mockito.verify(mockDal, Mockito.never()).commitTransaction();
  }

  @DisplayName("TEST FurnitureUCC.getAll : should return list of dto")
  @Test
  public void test_getAll_shouldReturnListOfFurnitureDTOs() {
    final List<PhotoDTO> photos1 = Arrays.asList(mockPhotoDTO1, mockPhotoDTO2);
    final List<PhotoDTO> photos2 = Arrays.asList(mockPhotoDTO3);
    final String condition2 = "under_option";

    List<FurnitureDTO> furnitureDTOS = Arrays.asList(mockFurnitureDTO1, mockFurnitureDTO2);

    Mockito.when(mockFurnitureDAO.findAll()).thenReturn(furnitureDTOS);

    Mockito.when(mockOptionDAO.findByFurnitureId(defaultFurnitureId2)).thenReturn(mockOptionDTO);

    Mockito.when(mockPhotoDAO.findAllByFurnitureId(defaultFurnitureId1)).thenReturn(photos1);
    Mockito.when(mockPhotoDAO.findAllByFurnitureId(defaultFurnitureId2)).thenReturn(photos2);

    Mockito.when(mockFurnitureDTO2.getBuyerId()).thenReturn(null);
    Mockito.when(mockFurnitureDTO2.getSellerId()).thenReturn(null);
    Mockito.when(mockFurnitureDTO2.getFavouritePhotoId()).thenReturn(null);
    Mockito.when(mockFurnitureDTO2.getCondition()).thenReturn(condition2);

    Mockito.when(mockOptionDTO.getUserId()).thenReturn(defaultUserId3);

    List<FurnitureDTO> expected = furnitureDTOS;
    List<FurnitureDTO> actual = furnitureUCC.getAll();
    assertEquals(expected, actual,
        "Calling getAll with a non-empty db should return a corresponding list of DTOs.");

    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal).commitTransaction();

    Mockito.verify(mockFurnitureDAO).findAll();

    Mockito.verify(mockUserDAO).findById(defaultBuyerId1);
    Mockito.verify(mockUserDAO).findById(defaultSellerId1);

    Mockito.verify(mockPhotoDAO).getPhotoById(defaultFavouritePhotoId1);
    Mockito.verify(mockPhotoDAO).findAllByFurnitureId(defaultFurnitureId1);
    Mockito.verify(mockPhotoDAO).findAllByFurnitureId(defaultFurnitureId2);

    Mockito.verify(mockFurnitureTypeDAO).findById(defaultTypeId1);
    Mockito.verify(mockFurnitureTypeDAO).findById(defaultTypeId2);

    Mockito.verify(mockFurnitureDTO1).setBuyer(defaultBuyer1);
    Mockito.verify(mockFurnitureDTO1).setSeller(defaultSeller1);
    Mockito.verify(mockFurnitureDTO1).setFavouritePhoto(defaultFavouritePhoto1);
    Mockito.verify(mockFurnitureDTO1).setPhotos(photos1);
    Mockito.verify(mockFurnitureDTO1).setType(defaultType1);

    Mockito.verify(mockFurnitureDTO2).setPhotos(photos2);
    Mockito.verify(mockFurnitureDTO2).setType(defaultType2);
    Mockito.verify(mockFurnitureDTO2).setOption(mockOptionDTO);

    Mockito.verify(mockOptionDTO).setUser(mockUserDTO3);
  }

  @DisplayName("TEST FurnitureUCC.getAll : with empty furniture table in db,"
      + " should return empty list of dto")
  @Test
  public void test_getAll_emptyDB_shouldReturnEmptyListOfFurnitureDTOs() {
    List<FurnitureDTO> emptyList = new ArrayList<FurnitureDTO>();

    Mockito.when(mockFurnitureDAO.findAll()).thenReturn(emptyList);

    assertEquals(emptyList, furnitureUCC.getAll(), "The getAll method should return "
        + "an empty list if it is called while the db is empty");

    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal).commitTransaction();
    Mockito.verify(mockDal, Mockito.never()).rollbackTransaction();

    Mockito.verify(mockFurnitureDAO).findAll();
  }

  @DisplayName("TEST UserUCC.getAll : "
      + "DAO throws InternalError, Should rollback and throw InternalError")
  @Test
  public void test_getAll_InternalErrorThrown_shouldThrowInternalErrorAndRollback() {
    Mockito.when(mockFurnitureDAO.findAll()).thenThrow(new InternalError("some error"));

    assertThrows(InternalError.class, () -> furnitureUCC.getAll(),
        "If the DAO throws an exception, it should be thrown back");

    Mockito.verify(mockFurnitureDAO).findAll();

    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal).rollbackTransaction();
    Mockito.verify(mockDal, Mockito.never()).commitTransaction();
  }

  @DisplayName("TEST FurnitureUCC.toRestoration : given valid id, should return dto")
  @Test
  public void test_toRestoration_givenValidId_shouldReturnDTO() {
    final String startingCondition = "accepted";
    final String expectedEndingCondition = "in_restoration";
    List<PhotoDTO> emptyList = new ArrayList<PhotoDTO>();

    Mockito.when(mockPhotoDAO.findAllByFurnitureId(defaultFurnitureId1)).thenReturn(emptyList);

    Mockito.when(mockFurnitureDAO.updateConditionOnly(mockFurnitureDTO1))
        .thenReturn(mockFurnitureDTO2);

    Mockito.when(mockFurnitureDTO1.getCondition()).thenReturn(startingCondition);
    Mockito.when(mockFurnitureDTO1.getBuyerId()).thenReturn(null);
    Mockito.when(mockFurnitureDTO1.getFavouritePhotoId()).thenReturn(null);

    Mockito.when(mockFurnitureDTO2.getFurnitureId()).thenReturn(defaultFurnitureId1);

    assertEquals(mockFurnitureDTO2, furnitureUCC.toRestoration(defaultFurnitureId1),
        "The toRestoration method should return the corresponding dto "
            + "if it is called with a valid id.");

    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal).commitTransaction();
    Mockito.verify(mockDal, Mockito.never()).rollbackTransaction();

    Mockito.verify(mockFurnitureDTO1).setCondition(expectedEndingCondition);

    Mockito.verify(mockFurnitureDAO).updateConditionOnly(mockFurnitureDTO1);

    Mockito.verify(mockFurnitureDTO2).setSeller(defaultSeller2);
    Mockito.verify(mockFurnitureDTO2).setPhotos(emptyList);
  }

  @DisplayName("TEST FurnitureUCC.toRestoration : given invalid id (invalid condition),"
      + " should throw ConflictException")
  @ParameterizedTest
  @ValueSource(strings = {"requested_for_visit", "refused", "in_restoration", "available_for_sale",
      "under_option", "sold", "reserved", "delivered", "collected", "withdrawn"})
  public void test_toRestoration_givenInvalidId1_shouldThrowConflict(String startingCondition) {
    Mockito.when(mockFurnitureDAO.findById(defaultFurnitureId1)).thenReturn(mockFurnitureDTO1);
    Mockito.when(mockFurnitureDTO1.getCondition()).thenReturn(startingCondition);

    assertThrows(ConflictException.class, () -> {
      furnitureUCC.toRestoration(defaultFurnitureId1);
    }, "The toRestoration method should throw a ConflictException if it is given "
        + "the id of a piece of furniture in the '" + startingCondition + "' condition.");

    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal, Mockito.never()).commitTransaction();
    Mockito.verify(mockDal).rollbackTransaction();
  }

  @DisplayName("TEST FurnitureUCC.toRestoration : given invalid id (not in db),"
      + " should throw NotFoundException")
  @Test
  public void test_toRestoration_givenInvalidId2_shouldThrowNotFound() {
    Mockito.when(mockFurnitureDAO.findById(defaultFurnitureId1)).thenThrow(NotFoundException.class);

    assertThrows(NotFoundException.class, () -> {
      furnitureUCC.toRestoration(defaultFurnitureId1);
    }, "The toRestoration method should throw a NotFoundException "
        + "if it is called with an id that isn't present in the database");

    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal, Mockito.never()).commitTransaction();
    Mockito.verify(mockDal).rollbackTransaction();
  }

  @DisplayName("TEST UserUCC.toAvailable : DAO throws InternalError, "
      + "Should rollback and throw InternalError")
  @Test
  public void test_toRestoration_InternalErrorThrown_shouldThrowInternalErrorAndRollback() {
    Mockito.when(mockFurnitureDAO.findById(defaultFurnitureId1)).thenThrow(new InternalError("some error"));

    assertThrows(InternalError.class, () -> furnitureUCC.toRestoration(defaultFurnitureId1),
        "If the DAO throws an exception, it should be thrown back");

    Mockito.verify(mockFurnitureDAO).findById(defaultFurnitureId1);

    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal).rollbackTransaction();
    Mockito.verify(mockDal, Mockito.never()).commitTransaction();
  }

  @DisplayName("TEST FurnitureUCC.toAvailable : given valid id, should return dto")
  @ParameterizedTest
  @ValueSource(strings = {"accepted", "in_restoration"})
  public void test_toAvailable_givenValidId_shouldReturnDTO(String startingCondition) {
    final String expectedEndingCondition = "available_for_sale";
    final List<PhotoDTO> emptyList = new ArrayList<PhotoDTO>();

    Mockito.when(mockFurnitureDAO.updateToAvailable(mockFurnitureDTO1))
        .thenReturn(mockFurnitureDTO2);

    Mockito.when(mockUserDAO.findById(defaultSellerId1)).thenReturn(mockUserDTO1);

    Mockito.when(mockPhotoDAO.findAllByFurnitureId(defaultFurnitureId1)).thenReturn(emptyList);

    Mockito.when(mockFurnitureDTO1.getCondition()).thenReturn(startingCondition);

    Mockito.when(mockFurnitureDTO2.getBuyerId()).thenReturn(null);
    Mockito.when(mockFurnitureDTO2.getSellerId()).thenReturn(defaultSellerId1);
    Mockito.when(mockFurnitureDTO2.getFavouritePhotoId()).thenReturn(null);

    assertEquals(mockFurnitureDTO2, furnitureUCC.toAvailable(defaultFurnitureId1, defaultSellingPrice1),
        "The toAvailable method should return the corresponding dto "
            + "if it is called with a valid id.");

    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal).commitTransaction();
    Mockito.verify(mockDal, Mockito.never()).rollbackTransaction();

    Mockito.verify(mockFurnitureDTO1).setCondition(expectedEndingCondition);

    Mockito.verify(mockFurnitureDAO).updateToAvailable(mockFurnitureDTO1);

    Mockito.verify(mockFurnitureDTO2).setSeller(defaultSeller2);
    Mockito.verify(mockFurnitureDTO2).setPhotos(emptyList);

  }

  @DisplayName("TEST FurnitureUCC.toAvailable : given invalid id (invalid condition),"
      + " should throw ConflictException")
  @ParameterizedTest
  @ValueSource(strings = {"requested_for_visit", "refused", "available_for_sale",
      "under_option", "sold", "reserved", "delivered", "collected", "withdrawn"})
  public void test_toAvailable_givenInvalidStates_shouldThrowConflict(String startingCondition) {
    Mockito.when(mockFurnitureDAO.findById(defaultFurnitureId1)).thenReturn(mockFurnitureDTO1);
    Mockito.when(mockFurnitureDTO1.getCondition()).thenReturn(startingCondition);

    assertThrows(ConflictException.class, () -> {
      furnitureUCC.toAvailable(defaultFurnitureId1, defaultSellingPrice1);
    }, "The toAvailable method should throw a ConflictException if it is given "
        + "the id of a piece of furniture in the '" + startingCondition + "' condition.");

    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal, Mockito.never()).commitTransaction();
    Mockito.verify(mockDal).rollbackTransaction();
  }

  @DisplayName("TEST FurnitureUCC.toAvailable : given invalid id (not in db),"
      + " should throw NotFoundException")
  @Test
  public void test_toAvailable_givenInvalidId_shouldThrowNotFound() {
    Mockito.when(mockFurnitureDAO.findById(defaultFurnitureId1)).thenThrow(NotFoundException.class);

    assertThrows(NotFoundException.class, () -> {
      furnitureUCC.toAvailable(defaultFurnitureId1, defaultSellingPrice1);
    }, "The toAvailable method should throw a NotFoundException "
        + "if it is called with an id that isn't present in the database");

    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal, Mockito.never()).commitTransaction();
    Mockito.verify(mockDal).rollbackTransaction();
  }

  @DisplayName("TEST UserUCC.toAvailable : DAO throws InternalError, "
      + "Should rollback and throw InternalError")
  @Test
  public void test_toAvailable_InternalErrorThrown_shouldThrowInternalErrorAndRollback() {
    Mockito.when(mockFurnitureDAO.findById(defaultFurnitureId1)).thenThrow(new InternalError("some error"));

    assertThrows(InternalError.class, () -> furnitureUCC.toAvailable(defaultFurnitureId1, defaultSellingPrice1),
        "If the DAO throws an exception, it should be thrown back");

    Mockito.verify(mockFurnitureDAO).findById(defaultFurnitureId1);

    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal).rollbackTransaction();
    Mockito.verify(mockDal, Mockito.never()).commitTransaction();
  }

  @DisplayName("TEST FurnitureUCC.withdraw : given valid id, should return dto")
  @ParameterizedTest
  @ValueSource(strings = {"in_restoration", "available_for_sale"})
  public void test_withdraw_givenValidId_shouldReturnDTO(String startingCondition) {
    final String expectedEndingCondition = "withdrawn";
    final List<PhotoDTO> emptyList = new ArrayList<PhotoDTO>();
    Mockito.when(mockFurnitureDAO.updateToWithdrawn(mockFurnitureDTO1))
        .thenReturn(mockFurnitureDTO2);

    Mockito.when(mockFurnitureDTO1.getCondition()).thenReturn(startingCondition);

    Mockito.when(mockFurnitureDTO2.getBuyerId()).thenReturn(null);
    Mockito.when(mockFurnitureDTO2.getFavouritePhotoId()).thenReturn(null);
    Mockito.when(mockPhotoDAO.findAllByFurnitureId(defaultFurnitureId2)).thenReturn(emptyList);

    assertEquals(mockFurnitureDTO2, furnitureUCC.withdraw(defaultFurnitureId1),
        "The withdraw method should return the corresponding dto "
            + "if it is called with a valid id.");

    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal).commitTransaction();
    Mockito.verify(mockDal, Mockito.never()).rollbackTransaction();

    Mockito.verify(mockFurnitureDAO).updateToWithdrawn(mockFurnitureDTO1);

    Mockito.verify(mockFurnitureDTO1).setCondition(expectedEndingCondition);
    Mockito.verify(mockFurnitureDTO2).setSeller(defaultSeller2);
    Mockito.verify(mockFurnitureDTO2).setPhotos(emptyList);
  }

  @DisplayName("TEST FurnitureUCC.withdraw : given invalid id (invalid condition),"
      + " should throw ConflictException")
  @ParameterizedTest
  @ValueSource(strings = {"requested_for_visit", "refused", "accepted",
      "under_option", "sold", "reserved", "delivered", "collected", "withdrawn"})
  public void test_withdraw_givenInvalidStates_shouldThrowConflict(String startingCondition) {
    Mockito.when(mockFurnitureDTO1.getCondition()).thenReturn(startingCondition);

    assertThrows(ConflictException.class, () -> {
      furnitureUCC.withdraw(defaultFurnitureId1);
    }, "The withdraw method should throw a ConflictException if it is given "
        + "the id of a piece of furniture in the '" + startingCondition + "' condition.");

    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal, Mockito.never()).commitTransaction();
    Mockito.verify(mockDal).rollbackTransaction();
  }

  @DisplayName("TEST FurnitureUCC.withdraw : given invalid id (not in db),"
      + " should throw NotFoundException")
  @Test
  public void test_withdraw_givenInvalidId_shouldThrowNotFound() {
    Mockito.when(mockFurnitureDAO.findById(defaultFurnitureId1)).thenThrow(NotFoundException.class);

    assertThrows(NotFoundException.class, () -> furnitureUCC.withdraw(defaultFurnitureId1),
        "The withdraw method should throw a NotFoundException "
            + "if it is called with an id that isn't present in the database");

    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal, Mockito.never()).commitTransaction();
    Mockito.verify(mockDal).rollbackTransaction();
  }

  @DisplayName("TEST UserUCC.withdraw : DAO throws InternalError, "
      + "Should rollback and throw InternalError")
  @Test
  public void test_withdraw_InternalErrorThrown_shouldThrowInternalErrorAndRollback() {
    Mockito.when(mockFurnitureDAO.findById(defaultFurnitureId1)).thenThrow(new InternalError("some error"));

    assertThrows(InternalError.class, () -> furnitureUCC.withdraw(defaultFurnitureId1),
        "If the DAO throws an exception, it should be thrown back");

    Mockito.verify(mockFurnitureDAO).findById(defaultFurnitureId1);

    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal).rollbackTransaction();
    Mockito.verify(mockDal, Mockito.never()).commitTransaction();
  }
}