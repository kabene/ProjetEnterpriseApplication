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


  @BeforeAll
  public static void init() {
    Configurate.load("properties/test.properties");
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
  }

  @DisplayName("TEST FurnitureUCC.getOne : given valid id, should return dto "
      + "(containing seller + buyer + favourite photo + 2 photos + type)")
  @Test
  public void test_getOne_givenValidId_shouldReturnFurnitureDTO() {
    int furnitureId = 1;
    int buyerId = 1;
    int sellerId = 2;
    int typeId = 1;
    int favouritePhotoId = 1;
    List<PhotoDTO> photos = Arrays.asList(mockPhotoDTO1, mockPhotoDTO2);
    String type = "type";
    String condition = "available_for_sale";

    Mockito.when(mockFurnitureDAO.findById(furnitureId)).thenReturn(mockFurnitureDTO1);

    Mockito.when(mockUserDAO.findById(buyerId)).thenReturn(mockUserDTO1);
    Mockito.when(mockUserDAO.findById(sellerId)).thenReturn(mockUserDTO2);

    Mockito.when(mockPhotoDAO.getPhotoById(favouritePhotoId)).thenReturn(mockPhotoDTO1);
    Mockito.when(mockPhotoDAO.getPhotosByFurnitureId(furnitureId)).thenReturn(photos);

    Mockito.when(mockFurnitureTypeDAO.findById(typeId)).thenReturn(type);

    Mockito.when(mockFurnitureDTO1.getFurnitureId()).thenReturn(furnitureId);
    Mockito.when(mockFurnitureDTO1.getBuyerId()).thenReturn(buyerId);
    Mockito.when(mockFurnitureDTO1.getSellerId()).thenReturn(sellerId);
    Mockito.when(mockFurnitureDTO1.getFavouritePhotoId()).thenReturn(favouritePhotoId);
    Mockito.when(mockFurnitureDTO1.getTypeId()).thenReturn(typeId);
    Mockito.when(mockFurnitureDTO1.getCondition()).thenReturn(condition);

    FurnitureDTO actual = furnitureUCC.getOne(furnitureId);
    FurnitureDTO expected = mockFurnitureDTO1;
    assertEquals(expected, actual,
        "The getOne method should return the corresponding dto if the given id is valid.");

    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal).commitTransaction();

    Mockito.verify(mockFurnitureDAO).findById(furnitureId);

    Mockito.verify(mockUserDAO).findById(buyerId);
    Mockito.verify(mockUserDAO).findById(sellerId);

    Mockito.verify(mockPhotoDAO).getPhotoById(favouritePhotoId);
    Mockito.verify(mockPhotoDAO).getPhotosByFurnitureId(furnitureId);

    Mockito.verify(mockFurnitureTypeDAO).findById(typeId);

    Mockito.verify(mockFurnitureDTO1).setBuyer(mockUserDTO1);
    Mockito.verify(mockFurnitureDTO1).setSeller(mockUserDTO2);
    Mockito.verify(mockFurnitureDTO1).setFavouritePhoto(mockPhotoDTO1);
    Mockito.verify(mockFurnitureDTO1).setPhotos(photos);
    Mockito.verify(mockFurnitureDTO1).setType(type);
  }

  @DisplayName("TEST FurnitureUCC.getOne : given invalid id, should throw NotFoundException")
  @Test
  public void test_getOne_givenInvalidId_shouldReturnNull() {
    int furnitureId = 1;

    Mockito.when(mockFurnitureDAO.findById(furnitureId)).thenThrow(NotFoundException.class);

    assertThrows(NotFoundException.class,
        () -> furnitureUCC.getOne(furnitureId),
        "Calling getOne with an invalid id should throw NotFoundException.");

    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal).rollbackTransaction();

    Mockito.verify(mockFurnitureDAO).findById(furnitureId);
  }

  @DisplayName("TEST UserUCC.getOne : DAO throws InternalError, "
      + "Should rollback and throw InternalError")
  @Test
  public void test_getOne_InternalErrorThrown_shouldThrowInternalErrorAndRollback() {
    int furnitureId = 1;

    Mockito.when(mockFurnitureDAO.findById(furnitureId)).thenThrow(new InternalError("some error"));

    assertThrows(InternalError.class, () -> furnitureUCC.getOne(furnitureId),
        "If the DAO throws an exception, it should be thrown back");

    Mockito.verify(mockFurnitureDAO).findById(furnitureId);

    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal).rollbackTransaction();
    Mockito.verify(mockDal, Mockito.never()).commitTransaction();
  }

  @DisplayName("TEST FurnitureUCC.getAll : should return list of dto")
  @Test
  public void test_getAll_shouldReturnListOfFurnitureDTOs() {
    final int furnitureId1 = 1;
    final int furnitureId2 = 2;
    final int buyerId1 = 3;
    final int sellerId1 = 4;
    final int typeId1 = 5;
    final int typeId2 = 6;
    final int favouritePhotoId1 = 7;
    final List<PhotoDTO> photos1 = Arrays.asList(mockPhotoDTO1, mockPhotoDTO2);
    final List<PhotoDTO> photos2 = Arrays.asList(mockPhotoDTO3);

    final String type1 = "type1";
    final String condition1 = "available_for_sale";

    final String type2 = "type2";
    final String condition2 = "under_option";
    final int optionUserId = 8;

    List<FurnitureDTO> furnitureDTOS = Arrays.asList(mockFurnitureDTO1, mockFurnitureDTO2);

    Mockito.when(mockFurnitureDAO.findAll()).thenReturn(furnitureDTOS);

    Mockito.when(mockUserDAO.findById(buyerId1)).thenReturn(mockUserDTO1);
    Mockito.when(mockUserDAO.findById(sellerId1)).thenReturn(mockUserDTO2);
    Mockito.when(mockUserDAO.findById(optionUserId)).thenReturn(mockUserDTO3);

    Mockito.when(mockPhotoDAO.getPhotoById(favouritePhotoId1)).thenReturn(mockPhotoDTO1);
    Mockito.when(mockPhotoDAO.getPhotosByFurnitureId(furnitureId1)).thenReturn(photos1);
    Mockito.when(mockPhotoDAO.getPhotosByFurnitureId(furnitureId2)).thenReturn(photos2);

    Mockito.when(mockFurnitureTypeDAO.findById(typeId1)).thenReturn(type1);
    Mockito.when(mockFurnitureTypeDAO.findById(typeId2)).thenReturn(type2);

    Mockito.when(mockOptionDAO.findByFurnitureId(furnitureId2)).thenReturn(mockOptionDTO);

    Mockito.when(mockFurnitureDTO1.getFurnitureId()).thenReturn(furnitureId1);
    Mockito.when(mockFurnitureDTO1.getBuyerId()).thenReturn(buyerId1);
    Mockito.when(mockFurnitureDTO1.getSellerId()).thenReturn(sellerId1);
    Mockito.when(mockFurnitureDTO1.getFavouritePhotoId()).thenReturn(favouritePhotoId1);
    Mockito.when(mockFurnitureDTO1.getTypeId()).thenReturn(typeId1);
    Mockito.when(mockFurnitureDTO1.getCondition()).thenReturn(condition1);

    Mockito.when(mockFurnitureDTO2.getFurnitureId()).thenReturn(furnitureId2);
    Mockito.when(mockFurnitureDTO2.getBuyerId()).thenReturn(null);
    Mockito.when(mockFurnitureDTO2.getSellerId()).thenReturn(null);
    Mockito.when(mockFurnitureDTO2.getFavouritePhotoId()).thenReturn(null);
    Mockito.when(mockFurnitureDTO2.getTypeId()).thenReturn(typeId2);
    Mockito.when(mockFurnitureDTO2.getCondition()).thenReturn(condition2);

    Mockito.when(mockOptionDTO.getUserId()).thenReturn(optionUserId);

    List<FurnitureDTO> expected = furnitureDTOS;
    List<FurnitureDTO> actual = furnitureUCC.getAll();
    assertEquals(expected, actual,
        "Calling getAll with a non-empty db should return a corresponding list of DTOs.");

    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal).commitTransaction();

    Mockito.verify(mockFurnitureDAO).findAll();

    Mockito.verify(mockUserDAO).findById(buyerId1);
    Mockito.verify(mockUserDAO).findById(sellerId1);

    Mockito.verify(mockPhotoDAO).getPhotoById(favouritePhotoId1);
    Mockito.verify(mockPhotoDAO).getPhotosByFurnitureId(furnitureId1);
    Mockito.verify(mockPhotoDAO).getPhotosByFurnitureId(furnitureId2);

    Mockito.verify(mockFurnitureTypeDAO).findById(typeId1);
    Mockito.verify(mockFurnitureTypeDAO).findById(typeId2);

    Mockito.verify(mockFurnitureDTO1).setBuyer(mockUserDTO1);
    Mockito.verify(mockFurnitureDTO1).setSeller(mockUserDTO2);
    Mockito.verify(mockFurnitureDTO1).setFavouritePhoto(mockPhotoDTO1);
    Mockito.verify(mockFurnitureDTO1).setPhotos(photos1);
    Mockito.verify(mockFurnitureDTO1).setType(type1);

    Mockito.verify(mockFurnitureDTO2).setPhotos(photos2);
    Mockito.verify(mockFurnitureDTO2).setType(type2);
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
    int id = 1;
    final String startingCondition = "accepted";
    final String expectedEndingCondition = "in_restoration";
    int sellerId = 1;
    List<PhotoDTO> emptyList = new ArrayList<PhotoDTO>();

    Mockito.when(mockFurnitureDAO.findById(id)).thenReturn(mockFurnitureDTO1);
    Mockito.when(mockFurnitureDAO.updateConditionOnly(mockFurnitureDTO1))
        .thenReturn(mockFurnitureDTO1);

    Mockito.when(mockPhotoDAO.getPhotosByFurnitureId(id)).thenReturn(emptyList);

    Mockito.when(mockUserDAO.findById(sellerId)).thenReturn(mockUserDTO1);

    Mockito.when(mockFurnitureDTO1.getCondition()).thenReturn(startingCondition);
    Mockito.when(mockFurnitureDTO1.getFurnitureId()).thenReturn(id);
    Mockito.when(mockFurnitureDTO1.getBuyerId()).thenReturn(null);
    Mockito.when(mockFurnitureDTO1.getSellerId()).thenReturn(sellerId);
    Mockito.when(mockFurnitureDTO1.getFavouritePhotoId()).thenReturn(null);

    assertEquals(mockFurnitureDTO1, furnitureUCC.toRestauration(id),
        "The toRestoration method should return the corresponding dto "
            + "if it is called with a valid id.");

    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal).commitTransaction();
    Mockito.verify(mockDal, Mockito.never()).rollbackTransaction();

    Mockito.verify(mockFurnitureDAO).updateConditionOnly(mockFurnitureDTO1);

    Mockito.verify(mockFurnitureDTO1).setCondition(expectedEndingCondition);
    Mockito.verify(mockFurnitureDTO1).setSeller(mockUserDTO1);
    Mockito.verify(mockFurnitureDTO1).setPhotos(emptyList);
  }

  @DisplayName("TEST FurnitureUCC.toRestoration : given invalid id (invalid condition),"
      + " should throw ConflictException")
  @ParameterizedTest
  @ValueSource(strings = {"requested_for_visit", "refused", "in_restoration", "available_for_sale",
      "under_option", "sold", "reserved", "delivered", "collected", "withdrawn"})
  public void test_toRestoration_givenInvalidId1_shouldThrowConflict(String startingCondition) {
    int id = 1;

    Mockito.when(mockFurnitureDAO.findById(id)).thenReturn(mockFurnitureDTO1);
    Mockito.when(mockFurnitureDTO1.getCondition()).thenReturn(startingCondition);

    assertThrows(ConflictException.class, () -> {
      furnitureUCC.toRestauration(id);
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
    int id = 1;

    Mockito.when(mockFurnitureDAO.findById(id)).thenThrow(NotFoundException.class);

    assertThrows(NotFoundException.class, () -> {
      furnitureUCC.toRestauration(id);
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
    int furnitureId = 1;

    Mockito.when(mockFurnitureDAO.findById(furnitureId)).thenThrow(new InternalError("some error"));

    assertThrows(InternalError.class, () -> furnitureUCC.toRestauration(furnitureId),
        "If the DAO throws an exception, it should be thrown back");

    Mockito.verify(mockFurnitureDAO).findById(furnitureId);

    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal).rollbackTransaction();
    Mockito.verify(mockDal, Mockito.never()).commitTransaction();
  }

  @DisplayName("TEST FurnitureUCC.toAvailable : given valid id, should return dto")
  @ParameterizedTest
  @ValueSource(strings = {"accepted", "in_restoration"})
  public void test_toAvailable_givenValidId_shouldReturnDTO(String startingCondition) {
    final int id = 1;
    final String expectedEndingCondition = "available_for_sale";
    final double sellingPrice = 149.99;
    final int sellerId = 1;
    final List<PhotoDTO> emptyList = new ArrayList<PhotoDTO>();

    Mockito.when(mockFurnitureDAO.findById(id)).thenReturn(mockFurnitureDTO1);
    Mockito.when(mockFurnitureDAO.updateToAvailable(mockFurnitureDTO1))
        .thenReturn(mockFurnitureDTO1);

    Mockito.when(mockUserDAO.findById(sellerId)).thenReturn(mockUserDTO1);

    Mockito.when(mockPhotoDAO.getPhotosByFurnitureId(id)).thenReturn(emptyList);

    Mockito.when(mockFurnitureDTO1.getCondition()).thenReturn(startingCondition);
    Mockito.when(mockFurnitureDTO1.getFurnitureId()).thenReturn(id);
    Mockito.when(mockFurnitureDTO1.getBuyerId()).thenReturn(null);
    Mockito.when(mockFurnitureDTO1.getSellerId()).thenReturn(sellerId);
    Mockito.when(mockFurnitureDTO1.getFavouritePhotoId()).thenReturn(null);

    assertEquals(mockFurnitureDTO1, furnitureUCC.toAvailable(id, sellingPrice),
        "The toAvailable method should return the corresponding dto "
            + "if it is called with a valid id.");

    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal).commitTransaction();
    Mockito.verify(mockDal, Mockito.never()).rollbackTransaction();

    Mockito.verify(mockFurnitureDAO).updateToAvailable(mockFurnitureDTO1);

    Mockito.verify(mockFurnitureDTO1).setCondition(expectedEndingCondition);
    Mockito.verify(mockFurnitureDTO1).setSeller(mockUserDTO1);
    Mockito.verify(mockFurnitureDTO1).setPhotos(emptyList);

  }

  @DisplayName("TEST FurnitureUCC.toAvailable : given invalid id (invalid condition),"
      + " should throw ConflictException")
  @ParameterizedTest
  @ValueSource(strings = {"requested_for_visit", "refused", "available_for_sale",
      "under_option", "sold", "reserved", "delivered", "collected", "withdrawn"})
  public void test_toAvailable_givenInvalidStates_shouldThrowConflict(String startingCondition) {
    final int id = 1;
    final double sellingPrice = 149.99;

    Mockito.when(mockFurnitureDAO.findById(id)).thenReturn(mockFurnitureDTO1);
    Mockito.when(mockFurnitureDTO1.getCondition()).thenReturn(startingCondition);

    assertThrows(ConflictException.class, () -> {
      furnitureUCC.toAvailable(id, sellingPrice);
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
    final int id = 1;
    final double sellingPrice = 149.99;

    Mockito.when(mockFurnitureDAO.findById(id)).thenThrow(NotFoundException.class);

    assertThrows(NotFoundException.class, () -> {
      furnitureUCC.toAvailable(id, sellingPrice);
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
    int furnitureId = 1;
    double price = 150;

    Mockito.when(mockFurnitureDAO.findById(furnitureId)).thenThrow(new InternalError("some error"));

    assertThrows(InternalError.class, () -> furnitureUCC.toAvailable(furnitureId, price),
        "If the DAO throws an exception, it should be thrown back");

    Mockito.verify(mockFurnitureDAO).findById(furnitureId);

    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal).rollbackTransaction();
    Mockito.verify(mockDal, Mockito.never()).commitTransaction();
  }

  @DisplayName("TEST FurnitureUCC.withdraw : given valid id, should return dto")
  @ParameterizedTest
  @ValueSource(strings = {"in_restoration", "available_for_sale"})
  public void test_withdraw_givenValidId_shouldReturnDTO(String startingCondition) {
    final int id = 1;
    final String expectedEndingCondition = "withdrawn";
    final int sellerId = 1;
    final List<PhotoDTO> emptyList = new ArrayList<PhotoDTO>();

    Mockito.when(mockFurnitureDAO.findById(id)).thenReturn(mockFurnitureDTO1);
    Mockito.when(mockFurnitureDAO.updateToWithdrawn(mockFurnitureDTO1))
        .thenReturn(mockFurnitureDTO1);

    Mockito.when(mockPhotoDAO.getPhotosByFurnitureId(id)).thenReturn(emptyList);

    Mockito.when(mockUserDAO.findById(sellerId)).thenReturn(mockUserDTO1);

    Mockito.when(mockFurnitureDTO1.getCondition()).thenReturn(startingCondition);
    Mockito.when(mockFurnitureDTO1.getFurnitureId()).thenReturn(id);
    Mockito.when(mockFurnitureDTO1.getBuyerId()).thenReturn(null);
    Mockito.when(mockFurnitureDTO1.getSellerId()).thenReturn(sellerId);
    Mockito.when(mockFurnitureDTO1.getFavouritePhotoId()).thenReturn(null);

    assertEquals(mockFurnitureDTO1, furnitureUCC.withdraw(id),
        "The withdraw method should return the corresponding dto "
            + "if it is called with a valid id.");

    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal).commitTransaction();
    Mockito.verify(mockDal, Mockito.never()).rollbackTransaction();

    Mockito.verify(mockFurnitureDAO).updateToWithdrawn(mockFurnitureDTO1);

    Mockito.verify(mockFurnitureDTO1).setCondition(expectedEndingCondition);
    Mockito.verify(mockFurnitureDTO1).setSeller(mockUserDTO1);
    Mockito.verify(mockFurnitureDTO1).setPhotos(emptyList);
  }

  @DisplayName("TEST FurnitureUCC.withdraw : given invalid id (invalid condition),"
      + " should throw ConflictException")
  @ParameterizedTest
  @ValueSource(strings = {"requested_for_visit", "refused", "accepted",
      "under_option", "sold", "reserved", "delivered", "collected", "withdrawn"})
  public void test_withdraw_givenInvalidStates_shouldThrowConflict(String startingCondition) {
    final int id = 1;

    Mockito.when(mockFurnitureDAO.findById(id)).thenReturn(mockFurnitureDTO1);
    Mockito.when(mockFurnitureDTO1.getCondition()).thenReturn(startingCondition);

    assertThrows(ConflictException.class, () -> {
      furnitureUCC.withdraw(id);
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
    final int id = 1;

    Mockito.when(mockFurnitureDAO.findById(id)).thenThrow(NotFoundException.class);

    assertThrows(NotFoundException.class, () -> furnitureUCC.withdraw(id),
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
    int furnitureId = 1;

    Mockito.when(mockFurnitureDAO.findById(furnitureId)).thenThrow(new InternalError("some error"));

    assertThrows(InternalError.class, () -> furnitureUCC.withdraw(furnitureId),
        "If the DAO throws an exception, it should be thrown back");

    Mockito.verify(mockFurnitureDAO).findById(furnitureId);

    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal).rollbackTransaction();
    Mockito.verify(mockDal, Mockito.never()).commitTransaction();
  }
}