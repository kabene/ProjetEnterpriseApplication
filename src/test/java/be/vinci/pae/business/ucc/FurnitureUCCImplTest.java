package be.vinci.pae.business.ucc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import be.vinci.pae.business.dto.FurnitureDTO;
import be.vinci.pae.business.dto.FurnitureTypeDTO;
import be.vinci.pae.business.dto.OptionDTO;
import be.vinci.pae.business.dto.PhotoDTO;
import be.vinci.pae.business.dto.RequestForVisitDTO;
import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.business.pojos.FurnitureStatus;
import be.vinci.pae.business.pojos.FurnitureImpl;
import be.vinci.pae.business.pojos.FurnitureTypeImpl;
import be.vinci.pae.business.pojos.RequestStatus;
import be.vinci.pae.business.pojos.UserImpl;
import be.vinci.pae.business.pojos.PhotoImpl;
import be.vinci.pae.business.pojos.OptionImpl;
import be.vinci.pae.exceptions.ConflictException;
import be.vinci.pae.exceptions.NotFoundException;
import be.vinci.pae.main.TestBinder;
import be.vinci.pae.persistence.dal.ConnectionDalServices;
import be.vinci.pae.persistence.dao.FurnitureDAO;
import be.vinci.pae.persistence.dao.FurnitureTypeDAO;
import be.vinci.pae.persistence.dao.OptionDAO;
import be.vinci.pae.persistence.dao.PhotoDAO;
import be.vinci.pae.persistence.dao.RequestForVisitDAO;
import be.vinci.pae.persistence.dao.UserDAO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import java.util.stream.Stream;

import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InOrder;
import org.mockito.Mockito;

class FurnitureUCCImplTest {

  private static FurnitureUCC furnitureUCC;
  private static FurnitureDAO mockFurnitureDAO;
  private static UserDAO mockUserDAO;
  private static PhotoDAO mockPhotoDAO;
  private static OptionDAO mockOptionDAO;
  private static FurnitureTypeDAO mockFurnitureTypeDAO;
  private static RequestForVisitDAO mockRequestDAO;
  private static ConnectionDalServices mockDal;

  private static FurnitureDTO mockFurnitureDTO1;
  private static FurnitureDTO mockFurnitureDTO2;
  private static FurnitureDTO mockFurnitureDTO3;
  private static UserDTO mockUserDTO1;
  private static UserDTO mockUserDTO2;
  private static UserDTO mockUserDTO3;
  private static PhotoDTO mockPhotoDTO1;
  private static PhotoDTO mockPhotoDTO2;
  private static PhotoDTO mockPhotoDTO3;
  private static OptionDTO mockOptionDTO;
  private static FurnitureTypeDTO mockFurnitureType1;
  private static FurnitureTypeDTO mockFurnitureType2;
  private static RequestForVisitDTO mockRequest1;

  private static final int defaultFurnitureId1 = 0;
  private static final int defaultFurnitureId2 = 1;

  private static final int defaultUserId1 = 2;
  private static final int defaultUserId2 = 3;
  private static final int defaultUserId3 = 4;

  private static final String defaultDescription1 = "desc1";
  private static final String defaultDescription2 = "desc2";

  private static final int defaultPhotoId1 = 5;
  private static final int defaultPhotoId2 = 6;
  private static final int defaultPhotoId3 = 7;

  private static final int defaultOptionId1 = 8;

  private static final int defaultTypeId1 = 9;
  private static final int defaultTypeId2 = 10;

  private static final Integer defaultRequestId1 = 11;
  private static final Integer defaultRequestId2 = null;

  private static final String defaultUsername1 = "user1";
  private static final String defaultUsername2 = "user2";
  private static final String defaultUsername3 = "user3";

  private static final FurnitureStatus defaultStatus = FurnitureStatus.AVAILABLE_FOR_SALE;

  private static final double defaultSellingPrice1 = 1.50;
  private static final double defaultSellingPrice2 = 1.75;
  private static final double defaultSpecialSalePrice = 2.50;

  private static final RequestForVisitDTO defaultRequest1 = mockRequest1;

  //buyer1 = user1
  private static final int defaultBuyerId1 = defaultUserId1;
  private static final String defaultBuyerUsername1 = defaultUsername1;
  private static UserDTO defaultBuyer1;

  //seller1 = user2
  private static final int defaultSellerId1 = defaultUserId2;
  private static UserDTO defaultSeller1;

  //buyer2 = user3
  private static final int defaultBuyerId2 = defaultUserId3;

  //seller2 = user1
  private static final int defaultSellerId2 = defaultUserId1;
  private static UserDTO defaultSeller2;

  private static final int defaultFavouritePhotoId1 = defaultPhotoId1;
  private static PhotoDTO defaultFavouritePhoto1;

  private static final int defaultFavouritePhotoId2 = defaultPhotoId3;

  private static final String defaultTypeName1 = "type 1";
  private static final String defaultTypeName2 = "type 2";

  @BeforeAll
  public static void init() {
    ServiceLocator locator = ServiceLocatorUtilities.bind(new TestBinder());
    furnitureUCC = locator.getService(FurnitureUCC.class);

    mockFurnitureDAO = locator.getService(FurnitureDAO.class);
    mockUserDAO = locator.getService(UserDAO.class);
    mockPhotoDAO = locator.getService(PhotoDAO.class);
    mockFurnitureTypeDAO = locator.getService(FurnitureTypeDAO.class);
    mockOptionDAO = locator.getService(OptionDAO.class);
    mockRequestDAO = locator.getService(RequestForVisitDAO.class);
    mockDal = locator.getService(ConnectionDalServices.class);

    mockFurnitureDTO1 = Mockito.mock(FurnitureImpl.class);
    mockFurnitureDTO2 = Mockito.mock(FurnitureImpl.class);
    mockFurnitureDTO3 = Mockito.mock(FurnitureImpl.class);
    mockUserDTO1 = Mockito.mock(UserImpl.class);
    mockUserDTO2 = Mockito.mock(UserImpl.class);
    mockUserDTO3 = Mockito.mock(UserImpl.class);
    mockPhotoDTO1 = Mockito.mock(PhotoImpl.class);
    mockPhotoDTO2 = Mockito.mock(PhotoImpl.class);
    mockPhotoDTO3 = Mockito.mock(PhotoImpl.class);
    mockOptionDTO = Mockito.mock(OptionImpl.class);
    mockFurnitureType1 = Mockito.mock(FurnitureTypeImpl.class);
    mockFurnitureType2 = Mockito.mock(FurnitureTypeImpl.class);
    mockRequest1 = Mockito.mock(RequestForVisitDTO.class);

    defaultBuyer1 = mockUserDTO1;
    defaultSeller1 = mockUserDTO2;
    defaultSeller2 = mockUserDTO1;

    defaultFavouritePhoto1 = mockPhotoDTO1;
  }

  @BeforeEach
  public void setUp() {
    Mockito.reset(mockFurnitureDAO);
    Mockito.reset(mockUserDAO);
    Mockito.reset(mockPhotoDAO);
    Mockito.reset(mockFurnitureTypeDAO);
    Mockito.reset(mockOptionDAO);
    Mockito.reset(mockRequestDAO);
    Mockito.reset(mockDal);
    Mockito.reset(mockFurnitureDTO1);
    Mockito.reset(mockFurnitureDTO2);
    Mockito.reset(mockFurnitureDTO3);
    Mockito.reset(mockUserDTO1);
    Mockito.reset(mockUserDTO2);
    Mockito.reset(mockUserDTO3);
    Mockito.reset(mockPhotoDTO1);
    Mockito.reset(mockPhotoDTO2);
    Mockito.reset(mockPhotoDTO3);
    Mockito.reset(mockOptionDTO);
    Mockito.reset(mockRequest1);
    Mockito.reset(mockFurnitureType1);
    Mockito.reset(mockFurnitureType2);

    Mockito.when(mockFurnitureDAO.findById(defaultFurnitureId1)).thenReturn(mockFurnitureDTO1);

    Mockito.when(mockUserDAO.findById(defaultUserId1)).thenReturn(mockUserDTO1);
    Mockito.when(mockUserDAO.findById(defaultUserId2)).thenReturn(mockUserDTO2);
    Mockito.when(mockUserDAO.findById(defaultUserId3)).thenReturn(mockUserDTO3);

    Mockito.when(mockUserDAO.findByUsername(defaultUsername1)).thenReturn(mockUserDTO1);
    Mockito.when(mockUserDAO.findByUsername(defaultUsername2)).thenReturn(mockUserDTO2);
    Mockito.when(mockUserDAO.findByUsername(defaultUsername3)).thenReturn(mockUserDTO3);

    Mockito.when(mockUserDTO1.getId()).thenReturn(defaultUserId1);
    Mockito.when(mockUserDTO2.getId()).thenReturn(defaultUserId2);
    Mockito.when(mockUserDTO3.getId()).thenReturn(defaultUserId3);

    Mockito.when(mockUserDTO1.getUsername()).thenReturn(defaultUsername1);
    Mockito.when(mockUserDTO2.getUsername()).thenReturn(defaultUsername2);
    Mockito.when(mockUserDTO3.getUsername()).thenReturn(defaultUsername3);

    Mockito.when(mockPhotoDAO.findById(defaultPhotoId1)).thenReturn(mockPhotoDTO1);
    Mockito.when(mockPhotoDAO.findById(defaultPhotoId2)).thenReturn(mockPhotoDTO2);
    Mockito.when(mockPhotoDAO.findById(defaultPhotoId3)).thenReturn(mockPhotoDTO3);

    Mockito.when(mockFurnitureTypeDAO.findById(defaultTypeId1)).thenReturn(mockFurnitureType1);
    Mockito.when(mockFurnitureTypeDAO.findById(defaultTypeId2)).thenReturn(mockFurnitureType2);

    Mockito.when(mockFurnitureDTO1.getFurnitureId()).thenReturn(defaultFurnitureId1);
    Mockito.when(mockFurnitureDTO1.getBuyerId()).thenReturn(defaultBuyerId1);
    Mockito.when(mockFurnitureDTO1.getSellerId()).thenReturn(defaultSellerId1);
    Mockito.when(mockFurnitureDTO1.getFavouritePhotoId()).thenReturn(defaultFavouritePhotoId1);
    Mockito.when(mockFurnitureDTO1.getTypeId()).thenReturn(defaultTypeId1);
    Mockito.when(mockFurnitureDTO1.getStatus()).thenReturn(defaultStatus);
    Mockito.when(mockFurnitureDTO1.getRequestId()).thenReturn(defaultRequestId1);
    Mockito.when(mockFurnitureDTO1.getDescription()).thenReturn(defaultDescription1);

    Mockito.when(mockFurnitureDTO2.getFurnitureId()).thenReturn(defaultFurnitureId2);
    Mockito.when(mockFurnitureDTO2.getBuyerId()).thenReturn(defaultBuyerId2);
    Mockito.when(mockFurnitureDTO2.getSellerId()).thenReturn(defaultSellerId2);
    Mockito.when(mockFurnitureDTO2.getFavouritePhotoId()).thenReturn(defaultFavouritePhotoId2);
    Mockito.when(mockFurnitureDTO2.getTypeId()).thenReturn(defaultTypeId2);
    Mockito.when(mockFurnitureDTO2.getStatus()).thenReturn(defaultStatus);
    Mockito.when(mockFurnitureDTO2.getRequestId()).thenReturn(defaultRequestId2);
    Mockito.when(mockFurnitureDTO2.getDescription()).thenReturn(defaultDescription2);

    Mockito.when(mockOptionDTO.getOptionId()).thenReturn(defaultOptionId1);

    Mockito.when(mockRequestDAO.findById(defaultRequestId1)).thenReturn(mockRequest1);

    Mockito.when(mockFurnitureType1.getTypeId()).thenReturn(defaultTypeId1);
    Mockito.when(mockFurnitureType2.getTypeId()).thenReturn(defaultTypeId2);

    Mockito.when(mockFurnitureType1.getTypeName()).thenReturn(defaultTypeName1);
    Mockito.when(mockFurnitureType2.getTypeName()).thenReturn(defaultTypeName2);
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

    Mockito.verify(mockPhotoDAO).findById(defaultFavouritePhotoId1);
    Mockito.verify(mockPhotoDAO).findAllByFurnitureId(defaultFurnitureId1);

    Mockito.verify(mockFurnitureTypeDAO).findById(defaultTypeId1);

    Mockito.verify(mockFurnitureDTO1).setBuyer(defaultBuyer1);
    Mockito.verify(mockFurnitureDTO1).setSeller(defaultSeller1);
    Mockito.verify(mockFurnitureDTO1).setFavouritePhoto(defaultFavouritePhoto1);
    Mockito.verify(mockFurnitureDTO1).setPhotos(photos);
    Mockito.verify(mockFurnitureDTO1).setType(defaultTypeName1);
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
    Mockito.when(mockFurnitureDAO.findById(defaultFurnitureId1))
        .thenThrow(new InternalError("some error"));

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
    final List<PhotoDTO> photos2 = Collections.singletonList(mockPhotoDTO3);
    final FurnitureStatus status2 = FurnitureStatus.UNDER_OPTION;

    final List<FurnitureDTO> expected = Arrays.asList(mockFurnitureDTO1, mockFurnitureDTO2);

    Mockito.when(mockFurnitureDAO.findAll()).thenReturn(expected);

    Mockito.when(mockOptionDAO.findByFurnitureId(defaultFurnitureId2)).thenReturn(mockOptionDTO);

    Mockito.when(mockPhotoDAO.findAllByFurnitureId(defaultFurnitureId1)).thenReturn(photos1);
    Mockito.when(mockPhotoDAO.findAllByFurnitureId(defaultFurnitureId2)).thenReturn(photos2);

    Mockito.when(mockFurnitureDTO2.getBuyerId()).thenReturn(null);
    Mockito.when(mockFurnitureDTO2.getSellerId()).thenReturn(null);
    Mockito.when(mockFurnitureDTO2.getFavouritePhotoId()).thenReturn(null);
    Mockito.when(mockFurnitureDTO2.getStatus()).thenReturn(status2);

    Mockito.when(mockOptionDTO.getUserId()).thenReturn(defaultUserId3);

    List<FurnitureDTO> actual = furnitureUCC.getAll();
    assertEquals(expected, actual,
        "Calling getAll with a non-empty db should return a corresponding list of DTOs.");

    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal).commitTransaction();

    Mockito.verify(mockFurnitureDAO).findAll();

    Mockito.verify(mockUserDAO).findById(defaultBuyerId1);
    Mockito.verify(mockUserDAO).findById(defaultSellerId1);

    Mockito.verify(mockPhotoDAO).findById(defaultFavouritePhotoId1);
    Mockito.verify(mockPhotoDAO).findAllByFurnitureId(defaultFurnitureId1);
    Mockito.verify(mockPhotoDAO).findAllByFurnitureId(defaultFurnitureId2);

    Mockito.verify(mockFurnitureTypeDAO).findById(defaultTypeId1);
    Mockito.verify(mockFurnitureTypeDAO).findById(defaultTypeId2);

    Mockito.verify(mockFurnitureDTO1).setBuyer(defaultBuyer1);
    Mockito.verify(mockFurnitureDTO1).setSeller(defaultSeller1);
    Mockito.verify(mockFurnitureDTO1).setFavouritePhoto(defaultFavouritePhoto1);
    Mockito.verify(mockFurnitureDTO1).setPhotos(photos1);
    Mockito.verify(mockFurnitureDTO1).setType(defaultTypeName1);

    Mockito.verify(mockFurnitureDTO2).setPhotos(photos2);
    Mockito.verify(mockFurnitureDTO2).setType(defaultTypeName2);
    Mockito.verify(mockFurnitureDTO2).setOption(mockOptionDTO);

    Mockito.verify(mockOptionDTO).setUser(mockUserDTO3);
  }

  @DisplayName("TEST FurnitureUCC.getAll : with empty furniture table in db,"
      + " should return empty list of dto")
  @Test
  public void test_getAll_emptyDB_shouldReturnEmptyListOfFurnitureDTOs() {
    List<FurnitureDTO> emptyList = new ArrayList<>();

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
    final FurnitureStatus startingStatus = FurnitureStatus.ACCEPTED;
    final FurnitureStatus expectedEndingStatus = FurnitureStatus.IN_RESTORATION;
    List<PhotoDTO> emptyList = new ArrayList<>();

    Mockito.when(mockPhotoDAO.findAllByFurnitureId(defaultFurnitureId1)).thenReturn(emptyList);

    Mockito.when(mockFurnitureDAO.updateStatusOnly(mockFurnitureDTO1))
        .thenReturn(mockFurnitureDTO2);

    Mockito.when(mockFurnitureDTO1.getStatus()).thenReturn(startingStatus);
    Mockito.when(mockFurnitureDTO1.getBuyerId()).thenReturn(null);
    Mockito.when(mockFurnitureDTO1.getFavouritePhotoId()).thenReturn(null);

    Mockito.when(mockFurnitureDTO2.getFurnitureId()).thenReturn(defaultFurnitureId1);

    assertEquals(mockFurnitureDTO2, furnitureUCC.toRestoration(defaultFurnitureId1),
        "The toRestoration method should return the corresponding dto "
            + "if it is called with a valid id.");

    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal).commitTransaction();
    Mockito.verify(mockDal, Mockito.never()).rollbackTransaction();

    Mockito.verify(mockFurnitureDTO1).setStatus(expectedEndingStatus);

    Mockito.verify(mockFurnitureDAO).updateStatusOnly(mockFurnitureDTO1);

    Mockito.verify(mockFurnitureDTO2).setSeller(defaultSeller2);
    Mockito.verify(mockFurnitureDTO2).setPhotos(emptyList);
  }

  @DisplayName("TEST FurnitureUCC.toRestoration : given invalid id (invalid status),"
      + " should throw ConflictException")
  @ParameterizedTest
  @EnumSource(value = FurnitureStatus.class, names = {"REQUESTED_FOR_VISIT", "REFUSED",
      "IN_RESTORATION", "AVAILABLE_FOR_SALE", "UNDER_OPTION", "SOLD", "RESERVED", "DELIVERED",
      "COLLECTED", "WITHDRAWN"})
  public void test_toRest_givenInvalidId_shouldThrowConflict(FurnitureStatus startingStatus) {
    Mockito.when(mockFurnitureDAO.findById(defaultFurnitureId1)).thenReturn(mockFurnitureDTO1);
    Mockito.when(mockFurnitureDTO1.getStatus()).thenReturn(startingStatus);

    assertThrows(ConflictException.class, () -> furnitureUCC.toRestoration(defaultFurnitureId1),
        "The toRestoration method should throw a ConflictException if it is given "
            + "the id of a piece of furniture in the '" + startingStatus + "' status.");

    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal, Mockito.never()).commitTransaction();
    Mockito.verify(mockDal).rollbackTransaction();
  }

  @DisplayName("TEST FurnitureUCC.toRestoration : given invalid id (not in db),"
      + " should throw NotFoundException")
  @Test
  public void test_toRestoration_givenInvalidId2_shouldThrowNotFound() {
    Mockito.when(mockFurnitureDAO.findById(defaultFurnitureId1)).thenThrow(NotFoundException.class);

    assertThrows(NotFoundException.class, () ->
            furnitureUCC.toRestoration(defaultFurnitureId1),
        "The toRestoration method should throw a NotFoundException "
            + "if it is called with an id that isn't present in the database");

    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal, Mockito.never()).commitTransaction();
    Mockito.verify(mockDal).rollbackTransaction();
  }

  @DisplayName("TEST UserUCC.toAvailable : DAO throws InternalError, "
      + "Should rollback and throw InternalError")
  @Test
  public void test_toRestoration_InternalErrorThrown_shouldThrowInternalErrorAndRollback() {
    Mockito.when(mockFurnitureDAO.findById(defaultFurnitureId1))
        .thenThrow(new InternalError("some error"));

    assertThrows(InternalError.class, () -> furnitureUCC.toRestoration(defaultFurnitureId1),
        "If the DAO throws an exception, it should be thrown back");

    Mockito.verify(mockFurnitureDAO).findById(defaultFurnitureId1);

    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal).rollbackTransaction();
    Mockito.verify(mockDal, Mockito.never()).commitTransaction();
  }

  @DisplayName("TEST FurnitureUCC.toAvailable : given valid id, should return dto")
  @ParameterizedTest
  @EnumSource(value = FurnitureStatus.class, names = {"ACCEPTED", "IN_RESTORATION"})
  public void test_toAvailable_givenValidId_shouldReturnDTO(FurnitureStatus startingStatus) {
    final FurnitureStatus expectedEndingStatus = FurnitureStatus.AVAILABLE_FOR_SALE;
    final List<PhotoDTO> emptyList = new ArrayList<>();

    Mockito.when(mockFurnitureDAO.updateToAvailable(mockFurnitureDTO1))
        .thenReturn(mockFurnitureDTO2);

    Mockito.when(mockUserDAO.findById(defaultSellerId1)).thenReturn(mockUserDTO1);

    Mockito.when(mockPhotoDAO.findAllByFurnitureId(defaultFurnitureId1)).thenReturn(emptyList);

    Mockito.when(mockFurnitureDTO1.getStatus()).thenReturn(startingStatus);

    Mockito.when(mockFurnitureDTO2.getBuyerId()).thenReturn(null);
    Mockito.when(mockFurnitureDTO2.getSellerId()).thenReturn(defaultSellerId1);
    Mockito.when(mockFurnitureDTO2.getFavouritePhotoId()).thenReturn(null);

    assertEquals(mockFurnitureDTO2,
        furnitureUCC.toAvailable(defaultFurnitureId1, defaultSellingPrice1),
        "The toAvailable method should return the corresponding dto "
            + "if it is called with a valid id.");

    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal).commitTransaction();
    Mockito.verify(mockDal, Mockito.never()).rollbackTransaction();

    Mockito.verify(mockFurnitureDTO1).setStatus(expectedEndingStatus);

    Mockito.verify(mockFurnitureDAO).updateToAvailable(mockFurnitureDTO1);

    Mockito.verify(mockFurnitureDTO2).setSeller(defaultSeller2);
    Mockito.verify(mockFurnitureDTO2).setPhotos(emptyList);

  }

  @DisplayName("TEST FurnitureUCC.toAvailable : given invalid id (invalid status),"
      + " should throw ConflictException")
  @ParameterizedTest
  @EnumSource(value = FurnitureStatus.class, names = {"REQUESTED_FOR_VISIT", "REFUSED",
      "AVAILABLE_FOR_SALE", "UNDER_OPTION", "SOLD", "RESERVED", "DELIVERED", "COLLECTED",
      "WITHDRAWN"})
  public void test_toAv_givenInvalidStates_shouldThrowConflict(FurnitureStatus startingStatus) {
    Mockito.when(mockFurnitureDAO.findById(defaultFurnitureId1)).thenReturn(mockFurnitureDTO1);
    Mockito.when(mockFurnitureDTO1.getStatus()).thenReturn(startingStatus);

    assertThrows(ConflictException.class,
        () -> furnitureUCC.toAvailable(defaultFurnitureId1, defaultSellingPrice1),
        "The toAvailable method should throw a ConflictException if it is given "
            + "the id of a piece of furniture in the '" + startingStatus + "' status.");

    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal, Mockito.never()).commitTransaction();
    Mockito.verify(mockDal).rollbackTransaction();
  }

  @DisplayName("TEST FurnitureUCC.toAvailable : given invalid id (not in db),"
      + " should throw NotFoundException")
  @Test
  public void test_toAvailable_givenInvalidId_shouldThrowNotFound() {
    Mockito.when(mockFurnitureDAO.findById(defaultFurnitureId1)).thenThrow(NotFoundException.class);

    assertThrows(NotFoundException.class, () ->
            furnitureUCC.toAvailable(defaultFurnitureId1, defaultSellingPrice1),
        "The toAvailable method should throw a NotFoundException "
            + "if it is called with an id that isn't present in the database");

    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal, Mockito.never()).commitTransaction();
    Mockito.verify(mockDal).rollbackTransaction();
  }

  @DisplayName("TEST UserUCC.toAvailable : DAO throws InternalError, "
      + "Should rollback and throw InternalError")
  @Test
  public void test_toAvailable_InternalErrorThrown_shouldThrowInternalErrorAndRollback() {
    Mockito.when(mockFurnitureDAO.findById(defaultFurnitureId1))
        .thenThrow(new InternalError("some error"));

    assertThrows(InternalError.class,
        () -> furnitureUCC.toAvailable(defaultFurnitureId1, defaultSellingPrice1),
        "If the DAO throws an exception, it should be thrown back");

    Mockito.verify(mockFurnitureDAO).findById(defaultFurnitureId1);

    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal).rollbackTransaction();
    Mockito.verify(mockDal, Mockito.never()).commitTransaction();
  }

  @DisplayName("TEST FurnitureUCC.withdraw : given valid id, should return dto")
  @ParameterizedTest
  @EnumSource(value = FurnitureStatus.class, names = {"IN_RESTORATION", "AVAILABLE_FOR_SALE"})
  public void test_withdraw_givenValidId_shouldReturnDTO(FurnitureStatus startingStatus) {
    final FurnitureStatus expectedEndingCondition = FurnitureStatus.WITHDRAWN;
    final List<PhotoDTO> emptyList = new ArrayList<>();
    Mockito.when(mockFurnitureDAO.updateToWithdrawn(mockFurnitureDTO1))
        .thenReturn(mockFurnitureDTO2);

    Mockito.when(mockFurnitureDTO1.getStatus()).thenReturn(startingStatus);

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

    Mockito.verify(mockFurnitureDTO1).setStatus(expectedEndingCondition);
    Mockito.verify(mockFurnitureDTO2).setSeller(defaultSeller2);
    Mockito.verify(mockFurnitureDTO2).setPhotos(emptyList);
  }

  @DisplayName("TEST FurnitureUCC.withdraw : given invalid id (invalid status),"
      + " should throw ConflictException")
  @ParameterizedTest
  @EnumSource(value = FurnitureStatus.class, names = {"REQUESTED_FOR_VISIT", "REFUSED", "ACCEPTED",
      "UNDER_OPTION", "SOLD", "RESERVED", "DELIVERED", "COLLECTED", "WITHDRAWN"})
  public void test_withdraw_givenInvalidStates_shouldThrowConflict(FurnitureStatus startingStatus) {
    Mockito.when(mockFurnitureDTO1.getStatus()).thenReturn(startingStatus);

    assertThrows(ConflictException.class, () ->
            furnitureUCC.withdraw(defaultFurnitureId1),
        "The withdraw method should throw a ConflictException if it is given "
            + "the id of a piece of furniture in the '" + startingStatus + "' status.");

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

  @DisplayName("TEST FurnitureUCC.withdraw : DAO throws InternalError, "
      + "Should rollback and throw InternalError")
  @Test
  public void test_withdraw_InternalErrorThrown_shouldThrowInternalErrorAndRollback() {
    Mockito.when(mockFurnitureDAO.findById(defaultFurnitureId1))
        .thenThrow(new InternalError("some error"));

    assertThrows(InternalError.class, () -> furnitureUCC.withdraw(defaultFurnitureId1),
        "If the DAO throws an exception, it should be thrown back");

    Mockito.verify(mockFurnitureDAO).findById(defaultFurnitureId1);

    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal).rollbackTransaction();
    Mockito.verify(mockDal, Mockito.never()).commitTransaction();
  }

  @DisplayName("TEST FurnitureUCC.updateFavouritePhoto : given valid ids, should return dto")
  @Test
  public void test_updateFavouritePhoto_givenValidArgs_shouldReturnDTO() {
    Mockito.when(mockFurnitureDAO.updateFavouritePhoto(mockFurnitureDTO1))
        .thenReturn(mockFurnitureDTO2);

    assertEquals(mockFurnitureDTO2,
        furnitureUCC.updateFavouritePhoto(defaultFurnitureId1, defaultPhotoId1),
        "A valid call to updateFavouritePhoto should return the modified dto");

    Mockito.verify(mockDal, Mockito.never()).rollbackTransaction();

    InOrder inOrder = Mockito
        .inOrder(mockDal, mockFurnitureDAO, mockUserDAO, mockPhotoDAO, mockFurnitureTypeDAO);

    inOrder.verify(mockDal).startTransaction();
    inOrder.verify(mockFurnitureDAO).findById(defaultFurnitureId1);
    inOrder.verify(mockPhotoDAO).findById(defaultPhotoId1);
    inOrder.verify(mockFurnitureDAO).updateFavouritePhoto(mockFurnitureDTO1);
    inOrder.verify(mockDal).commitTransaction();
    inOrder.verifyNoMoreInteractions();
  }

  @DisplayName("TEST FurnitureUCC.updateFavouritePhoto : "
      + "given invalid furnitureId, should throw NotFoundException")
  @Test
  public void test_updateFavouritePhoto_givenInvalidFurnitureId_shouldThrowNotFound() {
    Mockito.when(mockFurnitureDAO.findById(defaultFurnitureId1)).thenThrow(new NotFoundException());

    assertThrows(NotFoundException.class,
        () -> furnitureUCC.updateFavouritePhoto(defaultFurnitureId1, defaultPhotoId1),
        "calling updateFavouritePhoto with a non-existing furniture id should throw "
            + "NotFoundException");

    Mockito.verify(mockDal, Mockito.never()).commitTransaction();

    InOrder inOrder = Mockito
        .inOrder(mockDal, mockFurnitureDAO, mockUserDAO, mockPhotoDAO, mockFurnitureTypeDAO);

    inOrder.verify(mockDal).startTransaction();
    inOrder.verify(mockFurnitureDAO).findById(defaultFurnitureId1);
    inOrder.verify(mockDal).rollbackTransaction();
  }

  @DisplayName("TEST FurnitureUCC.updateFavouritePhoto :"
      + " given invalid photoId, should throw NotFoundException")
  @Test
  public void test_updateFavouritePhoto_givenInvalidPhotoId_shouldThrowNotFound() {
    Mockito.when(mockPhotoDAO.findById(defaultPhotoId1)).thenThrow(new NotFoundException());

    assertThrows(NotFoundException.class, () ->
            furnitureUCC.updateFavouritePhoto(defaultFurnitureId1, defaultPhotoId1),
        "calling updateFavouritePhoto with a non-existing photo id should throw "
            + "NotFoundException");

    Mockito.verify(mockDal, Mockito.never()).commitTransaction();

    InOrder inOrder = Mockito
        .inOrder(mockDal, mockFurnitureDAO, mockUserDAO, mockPhotoDAO, mockFurnitureTypeDAO);

    inOrder.verify(mockDal).startTransaction();
    inOrder.verify(mockFurnitureDAO).findById(defaultFurnitureId1);
    inOrder.verify(mockPhotoDAO).findById(defaultPhotoId1);
    inOrder.verify(mockDal).rollbackTransaction();
  }

  @DisplayName("TEST FurnitureUCC.updateFavouritePhoto : "
      + "given not matching ids, should throw ConflictException")
  @Test
  public void test_updateFavouritePhoto_givenNotMatchingIds_shouldThrowConflict() {
    Mockito.when(mockPhotoDTO1.getFurnitureId()).thenReturn(defaultFurnitureId2);

    assertThrows(ConflictException.class,
        () -> furnitureUCC.updateFavouritePhoto(defaultFurnitureId1, defaultPhotoId1),
        "calling updateFavouritePhoto with a photo id that references a photo not belonging"
            + "to the given furniture id should throw ConflictException");

    Mockito.verify(mockDal, Mockito.never()).commitTransaction();

    InOrder inOrder = Mockito
        .inOrder(mockDal, mockFurnitureDAO, mockUserDAO, mockPhotoDAO, mockFurnitureTypeDAO);

    inOrder.verify(mockDal).startTransaction();
    inOrder.verify(mockFurnitureDAO).findById(defaultFurnitureId1);
    inOrder.verify(mockPhotoDAO).findById(defaultPhotoId1);
    inOrder.verify(mockDal).rollbackTransaction();
  }

  @DisplayName("TEST FurnitureUCC.updateFavouritePhoto : "
      + "catches InternalError, should throw it back after rollback")
  @Test
  public void test_updateFavouritePhoto_catchesInternalError_shouldThrowInternal() {
    Mockito.when(mockFurnitureDAO.updateFavouritePhoto(mockFurnitureDTO1))
        .thenThrow(new InternalError());

    assertThrows(InternalError.class,
        () -> furnitureUCC.updateFavouritePhoto(defaultFurnitureId1, defaultPhotoId1),
        "if updateFavouritePhoto catches an InternalError, it should throw it back");

    Mockito.verify(mockDal, Mockito.never()).commitTransaction();

    InOrder inOrder = Mockito
        .inOrder(mockDal, mockFurnitureDAO, mockUserDAO, mockPhotoDAO, mockFurnitureTypeDAO);

    inOrder.verify(mockDal).startTransaction();
    inOrder.verify(mockFurnitureDAO).findById(defaultFurnitureId1);
    inOrder.verify(mockPhotoDAO).findById(defaultPhotoId1);
    inOrder.verify(mockFurnitureDAO).updateFavouritePhoto(mockFurnitureDTO1);
    inOrder.verify(mockDal).rollbackTransaction();
  }

  @DisplayName("TEST FurnitureUCC.toSold : nominal scenario without special sale price, "
      + "should return FurnitureDTO")
  @ParameterizedTest
  @EnumSource(value = FurnitureStatus.class, names = {"AVAILABLE_FOR_SALE", "UNDER_OPTION"})
  void test_toSold_nominalWithoutSpecialSale_shouldReturnDTO(FurnitureStatus status) {
    Mockito.when(mockFurnitureDTO1.getStatus()).thenReturn(status);
    Mockito.when(mockOptionDAO.findByFurnitureId(defaultFurnitureId1)).thenReturn(mockOptionDTO);
    Mockito.when(mockOptionDTO.getUserId()).thenReturn(defaultBuyerId1);
    assertEquals(mockFurnitureDTO1,
        furnitureUCC.toSold(defaultFurnitureId1, defaultBuyerUsername1, null),
        "A valid call of toSold() without specialSalePrice should "
            + "return the corresponding dto");

    InOrder inOrder = Mockito.inOrder(mockDal, mockUserDAO, mockFurnitureDAO, mockFurnitureDTO1);
    inOrder.verify(mockDal).startTransaction();
    inOrder.verify(mockUserDAO).findByUsername(defaultBuyerUsername1);
    inOrder.verify(mockFurnitureDAO).findById(defaultFurnitureId1);
    inOrder.verify(mockFurnitureDTO1).setBuyerId(defaultBuyerId1);
    inOrder.verify(mockFurnitureDTO1).setStatus(FurnitureStatus.SOLD);
    inOrder.verify(mockFurnitureDAO).updateToSold(mockFurnitureDTO1);
    inOrder.verify(mockFurnitureDAO).findById(defaultFurnitureId1);
    inOrder.verify(mockDal).commitTransaction();
    inOrder.verifyNoMoreInteractions();
    inOrder.verify(mockDal, Mockito.never()).rollbackTransaction();
  }

  @DisplayName("TEST FurnitureUCC.toSold : nominal scenario with special sale price, "
      + "should return FurnitureDTO")
  @ParameterizedTest
  @EnumSource(value = FurnitureStatus.class, names = {"AVAILABLE_FOR_SALE"})
  void test_toSold_nominalWithSpecialSale_shouldReturnDTO(FurnitureStatus status) {
    Mockito.when(mockFurnitureDTO1.getStatus()).thenReturn(status);
    Mockito.when(mockOptionDAO.findByFurnitureId(defaultFurnitureId1)).thenReturn(mockOptionDTO);
    Mockito.when(mockOptionDTO.getUserId()).thenReturn(defaultBuyerId1);
    String role = "antique_dealer";
    Mockito.when(defaultBuyer1.getRole()).thenReturn(role);
    assertEquals(mockFurnitureDTO1,
        furnitureUCC.toSold(defaultFurnitureId1, defaultBuyerUsername1, defaultSpecialSalePrice),
        "A valid call of toSold() with specialSalePrice should "
            + "return the corresponding dto");

    InOrder inOrder = Mockito.inOrder(mockDal, mockUserDAO, mockFurnitureDAO, mockFurnitureDTO1);
    inOrder.verify(mockDal).startTransaction();
    inOrder.verify(mockUserDAO).findByUsername(defaultBuyerUsername1);
    inOrder.verify(mockFurnitureDAO).findById(defaultFurnitureId1);
    inOrder.verify(mockFurnitureDTO1).setBuyerId(defaultBuyerId1);
    inOrder.verify(mockFurnitureDTO1).setStatus(FurnitureStatus.SOLD);
    inOrder.verify(mockFurnitureDTO1).setSpecialSalePrice(defaultSpecialSalePrice);
    inOrder.verify(mockFurnitureDAO).updateToSoldWithSpecialSale(mockFurnitureDTO1);
    inOrder.verify(mockFurnitureDAO).findById(defaultFurnitureId1);
    inOrder.verify(mockDal).commitTransaction();
    inOrder.verifyNoMoreInteractions();
    inOrder.verify(mockDal, Mockito.never()).rollbackTransaction();
  }

  @DisplayName("TEST FurnitureUCC.toSold : furniture under option with special sale price, "
      + "should throw ConflictException")
  @ParameterizedTest
  @EnumSource(value = FurnitureStatus.class, names = {"UNDER_OPTION"})
  void test_toSold_underOptionWithSpecialSale_shouldThrowConflict(FurnitureStatus status) {
    Mockito.when(mockFurnitureDTO1.getStatus()).thenReturn(status);
    Mockito.when(mockOptionDAO.findByFurnitureId(defaultFurnitureId1)).thenReturn(mockOptionDTO);
    Mockito.when(mockOptionDTO.getUserId()).thenReturn(defaultBuyerId1);
    String role = "antique_dealer";
    Mockito.when(defaultBuyer1.getRole()).thenReturn(role);
    assertThrows(ConflictException.class, () ->
            furnitureUCC
                .toSold(defaultFurnitureId1, defaultBuyerUsername1, defaultSpecialSalePrice),
        "A valid call of toSold() with specialSalePrice should "
            + "return the corresponding dto");

    InOrder inOrder = Mockito.inOrder(mockDal, mockUserDAO, mockFurnitureDAO, mockFurnitureDTO1);
    inOrder.verify(mockDal).startTransaction();
    inOrder.verify(mockUserDAO).findByUsername(defaultBuyerUsername1);
    inOrder.verify(mockFurnitureDAO).findById(defaultFurnitureId1);
    inOrder.verify(mockDal).rollbackTransaction();
    inOrder.verifyNoMoreInteractions();
    inOrder.verify(mockDal, Mockito.never()).commitTransaction();
  }

  @DisplayName("TEST FurnitureUCC.toSold : invalid status (without special sale price), "
      + "should throw ConflictException")
  @ParameterizedTest
  @EnumSource(value = FurnitureStatus.class, names = {"REQUESTED_FOR_VISIT", "REFUSED", "ACCEPTED",
      "IN_RESTORATION", "SOLD", "RESERVED", "DELIVERED", "COLLECTED", "WITHDRAWN"})
  void test_toSold_invalidStatesWithoutSpecialSale_shouldReturnDTO(FurnitureStatus status) {
    Mockito.when(mockFurnitureDTO1.getStatus()).thenReturn(status);
    Mockito.when(mockOptionDAO.findByFurnitureId(defaultFurnitureId1)).thenReturn(mockOptionDTO);
    Mockito.when(mockOptionDTO.getUserId()).thenReturn(defaultBuyerId1);

    assertThrows(ConflictException.class, () ->
            furnitureUCC.toSold(defaultFurnitureId1, defaultBuyerUsername1, null),
        "A call to toSold() with invalid furniture status (without specialSalePrice) should"
            + "throw ConflictException");

    InOrder inOrder = Mockito.inOrder(mockDal, mockUserDAO, mockFurnitureDAO, mockFurnitureDTO1);
    inOrder.verify(mockDal).startTransaction();
    inOrder.verify(mockUserDAO).findByUsername(defaultBuyerUsername1);
    inOrder.verify(mockFurnitureDAO).findById(defaultFurnitureId1);
    inOrder.verify(mockDal).rollbackTransaction();
    inOrder.verifyNoMoreInteractions();
    inOrder.verify(mockDal, Mockito.never()).commitTransaction();
  }

  @DisplayName("TEST FurnitureUCC.toSold : invalid status (with special sale price), "
      + "should throw ConflictException")
  @ParameterizedTest
  @EnumSource(value = FurnitureStatus.class, names = {"REQUESTED_FOR_VISIT", "REFUSED", "ACCEPTED",
      "IN_RESTORATION", "SOLD", "RESERVED", "DELIVERED", "COLLECTED", "WITHDRAWN"})
  void test_toSold_invalidStatesWithSpecialSale_shouldReturnDTO(FurnitureStatus status) {
    Mockito.when(mockFurnitureDTO1.getStatus()).thenReturn(status);
    Mockito.when(mockOptionDAO.findByFurnitureId(defaultFurnitureId1)).thenReturn(mockOptionDTO);
    Mockito.when(mockOptionDTO.getUserId()).thenReturn(defaultBuyerId1);
    String role = "antique_dealer";
    Mockito.when(defaultBuyer1.getRole()).thenReturn(role);

    assertThrows(ConflictException.class, () ->
            furnitureUCC
                .toSold(defaultFurnitureId1, defaultBuyerUsername1, defaultSpecialSalePrice),
        "A call to toSold() with invalid furniture status (with specialSalePrice) should "
            + "throw ConflictException");

    InOrder inOrder = Mockito.inOrder(mockDal, mockUserDAO, mockFurnitureDAO, mockFurnitureDTO1);
    inOrder.verify(mockDal).startTransaction();
    inOrder.verify(mockUserDAO).findByUsername(defaultBuyerUsername1);
    inOrder.verify(mockFurnitureDAO).findById(defaultFurnitureId1);
    inOrder.verify(mockDal).rollbackTransaction();
    inOrder.verifyNoMoreInteractions();
    inOrder.verify(mockDal, Mockito.never()).commitTransaction();
  }

  @DisplayName(
      "TEST FurnitureUCC.toSold : call with special sale price and non-antique dealer buyer, "
          + "should throw ConflictException")
  @Test
  void test_toSold_callWithSpecialSaleAndNonAntiqueDealerBuyer_shouldThrowConflict() {
    String role = "customer";
    Mockito.when(defaultBuyer1.getRole()).thenReturn(role);
    assertThrows(ConflictException.class, () ->
            furnitureUCC
                .toSold(defaultFurnitureId1, defaultBuyerUsername1, defaultSpecialSalePrice),
        "A call of toSold() with specialSalePrice (non antique dealer buyer) should "
            + "throw ConflictException");

    InOrder inOrder = Mockito.inOrder(mockDal, mockUserDAO, mockFurnitureDAO, mockFurnitureDTO1);
    inOrder.verify(mockDal).startTransaction();
    inOrder.verify(mockUserDAO).findByUsername(defaultBuyerUsername1);
    inOrder.verify(mockDal).rollbackTransaction();
    inOrder.verifyNoMoreInteractions();
    inOrder.verify(mockDal, Mockito.never()).commitTransaction();
  }

  @DisplayName(
      "TEST FurnitureUCC.toSold : call with invalid buyer username, "
          + "should throw NotFoundException")
  @ParameterizedTest
  @NullSource
  @ValueSource(doubles = {defaultSpecialSalePrice})
  void test_toSold_callInvalidUsername_shouldThrowNotFound(Double specialSalePrice) {
    Mockito.when(mockUserDAO.findByUsername(defaultBuyerUsername1))
        .thenThrow(new NotFoundException());

    assertThrows(NotFoundException.class, () ->
            furnitureUCC
                .toSold(defaultFurnitureId1, defaultBuyerUsername1, defaultSpecialSalePrice),
        "A call of toSold() with invalid buyer username should "
            + "throw NotFoundException");

    InOrder inOrder = Mockito.inOrder(mockDal, mockUserDAO, mockFurnitureDAO, mockFurnitureDTO1);
    inOrder.verify(mockDal).startTransaction();
    inOrder.verify(mockUserDAO).findByUsername(defaultBuyerUsername1);
    inOrder.verify(mockDal).rollbackTransaction();
    inOrder.verifyNoMoreInteractions();
    inOrder.verify(mockDal, Mockito.never()).commitTransaction();
  }

  @DisplayName(
      "TEST FurnitureUCC.toSold : call with invalid furnitureId, "
          + "should throw NotFoundException")
  @ParameterizedTest
  @NullSource
  @ValueSource(doubles = {defaultSpecialSalePrice})
  void test_toSold_callInvalidFurnitureId_shouldThrowNotFound(Double specialSalePrice) {
    Mockito.when(mockFurnitureDAO.findById(defaultFurnitureId1)).thenThrow(new NotFoundException());

    assertThrows(NotFoundException.class, () ->
            furnitureUCC.toSold(defaultFurnitureId1, defaultBuyerUsername1, specialSalePrice),
        "A call of toSold() with invalid furnitureId should "
            + "throw NotFoundException");

    InOrder inOrder = Mockito.inOrder(mockDal, mockUserDAO, mockFurnitureDAO, mockFurnitureDTO1);
    inOrder.verify(mockDal).startTransaction();
    inOrder.verify(mockUserDAO).findByUsername(defaultBuyerUsername1);
    inOrder.verify(mockFurnitureDAO).findById(defaultFurnitureId1);
    inOrder.verify(mockDal).rollbackTransaction();
    inOrder.verifyNoMoreInteractions();
    inOrder.verify(mockDal, Mockito.never()).commitTransaction();
  }

  @DisplayName("TEST FurnitureUCC.toSold : catches InternalError (without special sale price), "
      + "should throw it back")
  @Test
  void test_toSold_catchesInternal1_shouldThrowInternal() {
    Mockito.when(mockFurnitureDAO.updateToSold(mockFurnitureDTO1)).thenThrow(new InternalError());
    assertThrows(InternalError.class, () ->
            furnitureUCC.toSold(defaultFurnitureId1, defaultBuyerUsername1, null),
        "A call to toSold() without specialSalePrice catching an InternalError should "
            + "throw it back");

    InOrder inOrder = Mockito.inOrder(mockDal, mockUserDAO, mockFurnitureDAO, mockFurnitureDTO1);
    inOrder.verify(mockDal).startTransaction();
    inOrder.verify(mockUserDAO).findByUsername(defaultBuyerUsername1);
    inOrder.verify(mockFurnitureDAO).findById(defaultFurnitureId1);
    inOrder.verify(mockFurnitureDTO1).setBuyerId(defaultBuyerId1);
    inOrder.verify(mockFurnitureDTO1).setStatus(FurnitureStatus.SOLD);
    inOrder.verify(mockFurnitureDAO).updateToSold(mockFurnitureDTO1);
    inOrder.verify(mockDal).rollbackTransaction();
    inOrder.verifyNoMoreInteractions();
    inOrder.verify(mockDal, Mockito.never()).commitTransaction();
  }

  @DisplayName("TEST FurnitureUCC.toSold : catches InternalError (with special sale price), "
      + "should throw it back")
  @Test
  void test_toSold_catchesInternal2_shouldThrowInternal() {
    String role = "antique_dealer";
    Mockito.when(defaultBuyer1.getRole()).thenReturn(role);
    Mockito.when(mockFurnitureDAO.updateToSoldWithSpecialSale(mockFurnitureDTO1))
        .thenThrow(new InternalError());
    assertThrows(InternalError.class, () ->
            furnitureUCC
                .toSold(defaultFurnitureId1, defaultBuyerUsername1, defaultSpecialSalePrice),
        "A call to toSold() catching an InternalError should "
            + "throw it back");

    InOrder inOrder = Mockito.inOrder(mockDal, mockUserDAO, mockFurnitureDAO, mockFurnitureDTO1);
    inOrder.verify(mockDal).startTransaction();
    inOrder.verify(mockUserDAO).findByUsername(defaultBuyerUsername1);
    inOrder.verify(mockFurnitureDAO).findById(defaultFurnitureId1);
    inOrder.verify(mockFurnitureDTO1).setBuyerId(defaultBuyerId1);
    inOrder.verify(mockFurnitureDTO1).setStatus(FurnitureStatus.SOLD);
    inOrder.verify(mockFurnitureDTO1).setSpecialSalePrice(defaultSpecialSalePrice);
    inOrder.verify(mockFurnitureDAO).updateToSoldWithSpecialSale(mockFurnitureDTO1);
    inOrder.verify(mockDal).rollbackTransaction();
    inOrder.verifyNoMoreInteractions();
    inOrder.verify(mockDal, Mockito.never()).commitTransaction();
  }

  @DisplayName("TEST FurnitureUCC.toSold : UNDER_OPTION Invalid buyer (not option owner), "
      + "should throw ConflictException")
  @Test
  void test_toSold_underOptionInvalidBuyer_shouldThrowConflict() {
    Mockito.when(mockFurnitureDTO1.getStatus()).thenReturn(FurnitureStatus.UNDER_OPTION);
    Mockito.when(mockOptionDAO.findByFurnitureId(defaultFurnitureId1)).thenReturn(mockOptionDTO);
    Mockito.when(mockOptionDTO.getUserId()).thenReturn(defaultBuyerId2);
    String role = "antique_dealer";
    Mockito.when(defaultBuyer1.getRole()).thenReturn(role);
    assertThrows(ConflictException.class, () ->
            furnitureUCC.toSold(defaultFurnitureId1, defaultBuyerUsername1, null),
        "A call to toSold() with under option furniture and non-owner buyer should "
            + "throw ConflictException");

    InOrder inOrder = Mockito.inOrder(mockDal, mockUserDAO, mockFurnitureDAO, mockFurnitureDTO1);
    inOrder.verify(mockDal).startTransaction();
    inOrder.verify(mockUserDAO).findByUsername(defaultBuyerUsername1);
    inOrder.verify(mockFurnitureDAO).findById(defaultFurnitureId1);
    inOrder.verify(mockDal).rollbackTransaction();
    inOrder.verifyNoMoreInteractions();
    inOrder.verify(mockDal, Mockito.never()).commitTransaction();
  }

  @DisplayName("TEST FurnitureUCC.toAccepted : nominal scenario, should return dto")
  @Test
  void test_toAccepted_nominal_shouldReturnDTO() {
    Mockito.when(mockFurnitureDTO1.getStatus()).thenReturn(FurnitureStatus.REQUESTED_FOR_VISIT);
    Mockito.when(mockFurnitureDAO.updateStatusOnly(mockFurnitureDTO1))
        .thenReturn(mockFurnitureDTO2);
    Mockito.when(mockFurnitureDTO1.getRequestId()).thenReturn(defaultRequestId1);
    Mockito.when(mockRequest1.getRequestStatus()).thenReturn(RequestStatus.CONFIRMED);
    assertEquals(mockFurnitureDTO2, furnitureUCC.toAccepted(defaultFurnitureId1),
        "A valid call of toAccepted() with specialSalePrice should "
            + "return the corresponding dto");

    InOrder inOrder = Mockito.inOrder(mockDal, mockFurnitureDAO, mockFurnitureDTO1, mockRequestDAO);
    inOrder.verify(mockDal).startTransaction();
    inOrder.verify(mockFurnitureDAO).findById(defaultFurnitureId1);
    inOrder.verify(mockRequestDAO).findById(defaultRequestId1);
    inOrder.verify(mockFurnitureDTO1).setStatus(FurnitureStatus.ACCEPTED);
    inOrder.verify(mockFurnitureDAO).updateStatusOnly(mockFurnitureDTO1);
    inOrder.verify(mockDal).commitTransaction();
    inOrder.verifyNoMoreInteractions();
    inOrder.verify(mockDal, Mockito.never()).rollbackTransaction();
  }

  @DisplayName("TEST FurnitureUCC.toAccepted : given invalid furnitureId, "
      + "should throw NotFoundException")
  @Test
  void test_toAccepted_givenInvalidFurnitureId_shouldThrowNotFound() {
    Mockito.when(mockFurnitureDAO.findById(defaultFurnitureId1)).thenThrow(new NotFoundException());
    assertThrows(NotFoundException.class, () ->
            furnitureUCC.toAccepted(defaultFurnitureId1),
        "A call to toAccepted with invalid furnitureId should throw NotFoundException");

    InOrder inOrder = Mockito.inOrder(mockDal);
    inOrder.verify(mockDal).startTransaction();
    inOrder.verify(mockDal).rollbackTransaction();
    inOrder.verifyNoMoreInteractions();
    inOrder.verify(mockDal, Mockito.never()).commitTransaction();
  }

  @DisplayName("TEST FurnitureUCC.toAccepted : given invalid FurnitureStatus, "
      + "should throw ConflictException")
  @ParameterizedTest
  @EnumSource(value = FurnitureStatus.class, names = {"REFUSED", "ACCEPTED", "IN_RESTORATION",
      "AVAILABLE_FOR_SALE", "UNDER_OPTION", "SOLD", "RESERVED", "DELIVERED", "COLLECTED",
      "WITHDRAWN"})
  void test_toAccepted_givenInvalidFurnitureStatus_shouldThrowConflict(FurnitureStatus s) {
    Mockito.when(mockFurnitureDTO1.getStatus()).thenReturn(s);
    Mockito.when(mockFurnitureDTO1.getRequestId()).thenReturn(defaultRequestId1);
    Mockito.when(mockRequest1.getRequestStatus()).thenReturn(RequestStatus.CONFIRMED);
    assertThrows(ConflictException.class, () ->
            furnitureUCC.toAccepted(defaultFurnitureId1),
        "A call to toAccepted with invalid starting furniture status"
            + " should throw ConflictException");

    InOrder inOrder = Mockito.inOrder(mockDal, mockFurnitureDAO);
    inOrder.verify(mockDal).startTransaction();
    inOrder.verify(mockFurnitureDAO).findById(defaultFurnitureId1);
    inOrder.verify(mockDal).rollbackTransaction();
    inOrder.verifyNoMoreInteractions();
    inOrder.verify(mockDal, Mockito.never()).commitTransaction();
  }

  @DisplayName("TEST FurnitureUCC.toAccepted : catching InternalError, "
      + "should throw it back")
  @Test
  void test_toAccepted_catchesInternal_shouldThrowInternal() {
    Mockito.when(mockFurnitureDTO1.getStatus()).thenReturn(FurnitureStatus.REQUESTED_FOR_VISIT);
    Mockito.when(mockFurnitureDAO.updateStatusOnly(mockFurnitureDTO1))
        .thenThrow(new InternalError());
    Mockito.when(mockFurnitureDTO1.getRequestId()).thenReturn(defaultRequestId1);
    Mockito.when(mockRequest1.getRequestStatus()).thenReturn(RequestStatus.CONFIRMED);
    assertThrows(InternalError.class, () ->
            furnitureUCC.toAccepted(defaultFurnitureId1),
        "A call to toAccepted catching an InternalError should throw it back");

    InOrder inOrder = Mockito.inOrder(mockDal, mockFurnitureDAO, mockFurnitureDTO1);
    inOrder.verify(mockDal).startTransaction();
    inOrder.verify(mockFurnitureDAO).findById(defaultFurnitureId1);
    inOrder.verify(mockFurnitureDTO1).setStatus(FurnitureStatus.ACCEPTED);
    inOrder.verify(mockFurnitureDAO).updateStatusOnly(mockFurnitureDTO1);
    inOrder.verify(mockDal).rollbackTransaction();
    inOrder.verifyNoMoreInteractions();
    inOrder.verify(mockDal, Mockito.never()).commitTransaction();
  }

  @DisplayName("TEST FurnitureUCC.toAccepted : given invalid request status, "
      + "should throw ConflictException")
  @ParameterizedTest
  @EnumSource(value = RequestStatus.class, names = {"WAITING", "CANCELED"})
  void test_toAccepted_invalidRequestStatus_shouldThrowConflict(RequestStatus requestStatus) {
    Mockito.when(mockFurnitureDTO1.getStatus()).thenReturn(FurnitureStatus.REQUESTED_FOR_VISIT);
    Mockito.when(mockFurnitureDAO.updateStatusOnly(mockFurnitureDTO1))
        .thenReturn(mockFurnitureDTO2);
    Mockito.when(mockFurnitureDTO1.getRequestId()).thenReturn(defaultRequestId1);
    Mockito.when(mockRequest1.getRequestStatus()).thenReturn(requestStatus);
    assertThrows(ConflictException.class, () ->
            furnitureUCC.toAccepted(defaultFurnitureId1),
        "Call to toAccepted with an invalid request status should throw ConflictException");

    InOrder inOrder = Mockito.inOrder(mockDal, mockFurnitureDAO, mockFurnitureDTO1, mockRequestDAO);
    inOrder.verify(mockDal).startTransaction();
    inOrder.verify(mockFurnitureDAO).findById(defaultFurnitureId1);
    inOrder.verify(mockRequestDAO).findById(defaultRequestId1);
    inOrder.verify(mockDal).rollbackTransaction();
    inOrder.verifyNoMoreInteractions();
    inOrder.verify(mockDal, Mockito.never()).commitTransaction();
  }

  // -----

  @DisplayName("TEST FurnitureUCC.toRefused : nominal scenario, should return dto")
  @Test
  void test_toRefused_nominal_shouldReturnDTO() {
    Mockito.when(mockFurnitureDTO1.getStatus()).thenReturn(FurnitureStatus.REQUESTED_FOR_VISIT);
    Mockito.when(mockFurnitureDAO.updateStatusOnly(mockFurnitureDTO1))
        .thenReturn(mockFurnitureDTO2);
    Mockito.when(mockFurnitureDTO1.getRequestId()).thenReturn(defaultRequestId1);
    Mockito.when(mockRequest1.getRequestStatus()).thenReturn(RequestStatus.CONFIRMED);
    assertEquals(mockFurnitureDTO2, furnitureUCC.toRefused(defaultFurnitureId1),
        "A valid call of toRefused() with specialSalePrice should "
            + "return the corresponding dto");

    InOrder inOrder = Mockito.inOrder(mockDal, mockFurnitureDAO, mockFurnitureDTO1, mockRequestDAO);
    inOrder.verify(mockDal).startTransaction();
    inOrder.verify(mockFurnitureDAO).findById(defaultFurnitureId1);
    inOrder.verify(mockRequestDAO).findById(defaultRequestId1);
    inOrder.verify(mockFurnitureDTO1).setStatus(FurnitureStatus.REFUSED);
    inOrder.verify(mockFurnitureDAO).updateStatusOnly(mockFurnitureDTO1);
    inOrder.verify(mockDal).commitTransaction();
    inOrder.verifyNoMoreInteractions();
    inOrder.verify(mockDal, Mockito.never()).rollbackTransaction();
  }

  @DisplayName("TEST FurnitureUCC.toRefused : given invalid furnitureId, "
      + "should throw NotFoundException")
  @Test
  void test_toRefused_givenInvalidFurnitureId_shouldThrowNotFound() {
    Mockito.when(mockFurnitureDAO.findById(defaultFurnitureId1)).thenThrow(new NotFoundException());
    assertThrows(NotFoundException.class, () ->
            furnitureUCC.toRefused(defaultFurnitureId1),
        "A call to toRefused with invalid furnitureId should throw NotFoundException");

    InOrder inOrder = Mockito.inOrder(mockDal);
    inOrder.verify(mockDal).startTransaction();
    inOrder.verify(mockDal).rollbackTransaction();
    inOrder.verifyNoMoreInteractions();
    inOrder.verify(mockDal, Mockito.never()).commitTransaction();
  }

  @DisplayName("TEST FurnitureUCC.toRefused : given invalid FurnitureStatus, "
      + "should throw ConflictException")
  @ParameterizedTest
  @EnumSource(value = FurnitureStatus.class, names = {"REFUSED", "ACCEPTED", "IN_RESTORATION",
      "AVAILABLE_FOR_SALE", "UNDER_OPTION", "SOLD", "RESERVED", "DELIVERED", "COLLECTED",
      "WITHDRAWN"})
  void test_toRefused_givenInvalidStatus_shouldThrowConflict(FurnitureStatus startingStatus) {
    Mockito.when(mockFurnitureDTO1.getStatus()).thenReturn(startingStatus);
    assertThrows(ConflictException.class, () ->
            furnitureUCC.toRefused(defaultFurnitureId1),
        "A call to toRefused with invalid starting furniture status"
            + " should throw ConflictException");

    InOrder inOrder = Mockito.inOrder(mockDal, mockFurnitureDAO);
    inOrder.verify(mockDal).startTransaction();
    inOrder.verify(mockFurnitureDAO).findById(defaultFurnitureId1);
    inOrder.verify(mockDal).rollbackTransaction();
    inOrder.verifyNoMoreInteractions();
    inOrder.verify(mockDal, Mockito.never()).commitTransaction();
  }

  @DisplayName("TEST FurnitureUCC.toRefused : catching InternalError, "
      + "should throw it back")
  @Test
  void test_toRefused_catchesInternal_shouldThrowInternal() {
    Mockito.when(mockFurnitureDTO1.getStatus()).thenReturn(FurnitureStatus.REQUESTED_FOR_VISIT);
    Mockito.when(mockFurnitureDAO.updateStatusOnly(mockFurnitureDTO1))
        .thenThrow(new InternalError());
    Mockito.when(mockFurnitureDTO1.getRequestId()).thenReturn(defaultRequestId1);
    Mockito.when(mockRequest1.getRequestStatus()).thenReturn(RequestStatus.CONFIRMED);
    assertThrows(InternalError.class, () ->
            furnitureUCC.toRefused(defaultFurnitureId1),
        "A call to toRefused catching an InternalError should throw it back");

    InOrder inOrder = Mockito.inOrder(mockDal, mockFurnitureDAO, mockFurnitureDTO1);
    inOrder.verify(mockDal).startTransaction();
    inOrder.verify(mockFurnitureDAO).findById(defaultFurnitureId1);
    inOrder.verify(mockFurnitureDTO1).setStatus(FurnitureStatus.REFUSED);
    inOrder.verify(mockFurnitureDAO).updateStatusOnly(mockFurnitureDTO1);
    inOrder.verify(mockDal).rollbackTransaction();
    inOrder.verifyNoMoreInteractions();
    inOrder.verify(mockDal, Mockito.never()).commitTransaction();
  }

  @DisplayName("TEST FurnitureUCC.toAccepted : given invalid request status, "
      + "should throw ConflictException")
  @Test
  void test_toRefused_invalidRequestStatus_shouldThrowConflict() {
    Mockito.when(mockFurnitureDTO1.getStatus()).thenReturn(FurnitureStatus.REQUESTED_FOR_VISIT);
    Mockito.when(mockFurnitureDAO.updateStatusOnly(mockFurnitureDTO1))
        .thenReturn(mockFurnitureDTO2);
    Mockito.when(mockFurnitureDTO1.getRequestId()).thenReturn(defaultRequestId1);
    Mockito.when(mockRequest1.getRequestStatus()).thenReturn(RequestStatus.WAITING);
    assertThrows(ConflictException.class, () ->
            furnitureUCC.toRefused(defaultFurnitureId1),
        "Call to toAccepted with an invalid request status should throw ConflictException");

    InOrder inOrder = Mockito.inOrder(mockDal, mockFurnitureDAO, mockFurnitureDTO1, mockRequestDAO);
    inOrder.verify(mockDal).startTransaction();
    inOrder.verify(mockFurnitureDAO).findById(defaultFurnitureId1);
    inOrder.verify(mockRequestDAO).findById(defaultRequestId1);
    inOrder.verify(mockDal).rollbackTransaction();
    inOrder.verifyNoMoreInteractions();
    inOrder.verify(mockDal, Mockito.never()).commitTransaction();
  }

  @DisplayName("TEST FurnitureUCC.updateInfos : nominal, should return dto")
  @ParameterizedTest
  @ArgumentsSource(ValidUpdateInfosProvider.class)
  void test_updateInfos_nominal_shouldReturnDTO(String desc, Integer typeId, Double sellingPrice,
                                                Double startingSellingPrice,
                                                FurnitureStatus status) {
    /*
    Parallel with implementation:

    mockFurnitureDTO3 = bodyDTO (method param)
    mockFurnitureDTO1 = foundFurnitureDTO
    mockFurnitureDTO2 = res (return value)
    */

    Mockito.when(mockFurnitureDTO3.getFurnitureId()).thenReturn(defaultFurnitureId1);
    Mockito.when(mockFurnitureDTO3.getDescription()).thenReturn(desc);
    Mockito.when(mockFurnitureDTO3.getTypeId()).thenReturn(typeId);
    Mockito.when(mockFurnitureDTO3.getSellingPrice()).thenReturn(sellingPrice);
    Mockito.when(mockFurnitureDTO1.getSellingPrice()).thenReturn(startingSellingPrice);

    Mockito.when(mockFurnitureDTO1.getStatus()).thenReturn(status);

    Mockito.when(mockFurnitureDAO.updateDescription(mockFurnitureDTO1))
        .thenReturn(mockFurnitureDTO2);
    Mockito.when(mockFurnitureDAO.updateTypeId(mockFurnitureDTO1)).thenReturn(mockFurnitureDTO2);
    Mockito.when(mockFurnitureDAO.updateSellingPrice(mockFurnitureDTO1))
        .thenReturn(mockFurnitureDTO2);

    Mockito.when(mockFurnitureDAO.updateTypeId(mockFurnitureDTO2)).thenReturn(mockFurnitureDTO2);
    Mockito.when(mockFurnitureDAO.updateSellingPrice(mockFurnitureDTO2))
        .thenReturn(mockFurnitureDTO2);

    assertEquals(mockFurnitureDTO2, furnitureUCC.updateInfos(mockFurnitureDTO3),
        "A valid call of updateInfos() should "
            + "return the corresponding dto");

    InOrder inOrder = Mockito.inOrder(mockDal, mockFurnitureDAO, mockFurnitureDTO1);
    inOrder.verify(mockDal).startTransaction();
    inOrder.verify(mockFurnitureDAO).findById(defaultFurnitureId1);
    if (desc != null) {
      inOrder.verify(mockFurnitureDTO1).setDescription(desc);
    } else {
      inOrder.verify(mockFurnitureDTO1, Mockito.never()).setDescription(desc);
    }
    if (typeId != null) {
      inOrder.verify(mockFurnitureDTO1).setTypeId(typeId);
    } else {
      inOrder.verify(mockFurnitureDTO1, Mockito.never()).setTypeId(typeId);
    }
    if (sellingPrice != null) {
      inOrder.verify(mockFurnitureDTO1).setSellingPrice(sellingPrice);
    } else {
      inOrder.verify(mockFurnitureDTO1, Mockito.never()).setSellingPrice(sellingPrice);
    }
    inOrder.verify(mockDal).commitTransaction();
    inOrder.verifyNoMoreInteractions();
    inOrder.verify(mockDal, Mockito.never()).rollbackTransaction();
  }

  @DisplayName("TEST FurnitureUCC.updateInfos : update selling price while not AVAILABLE_FOR_SALE,"
      + "should throw ConflictException")
  @ParameterizedTest
  @EnumSource(value = FurnitureStatus.class, names = {"REQUESTED_FOR_VISIT", "REFUSED",
      "ACCEPTED", "IN_RESTORATION", "UNDER_OPTION", "SOLD", "RESERVED", "DELIVERED",
      "COLLECTED", "WITHDRAWN"})
  void test_updateInfos_sellingPriceAndNotAvailable_shouldThrowConflict(FurnitureStatus status) {
    Mockito.when(mockFurnitureDTO3.getFurnitureId()).thenReturn(defaultFurnitureId1);
    Mockito.when(mockFurnitureDTO3.getDescription()).thenReturn(null);
    Mockito.when(mockFurnitureDTO3.getTypeId()).thenReturn(null);
    Mockito.when(mockFurnitureDTO3.getSellingPrice()).thenReturn(defaultSellingPrice2);
    Mockito.when(mockFurnitureDTO1.getStatus()).thenReturn(status);

    assertThrows(ConflictException.class, () ->
            furnitureUCC.updateInfos(mockFurnitureDTO3),
        "A call to updateInfos with sellingPrice on a furniture not available for sale"
            + "should throw ConflictException");

    InOrder inOrder = Mockito.inOrder(mockDal, mockFurnitureDAO);
    inOrder.verify(mockDal).startTransaction();
    inOrder.verify(mockFurnitureDAO).findById(defaultFurnitureId1);
    inOrder.verify(mockDal).rollbackTransaction();
    inOrder.verifyNoMoreInteractions();
    inOrder.verify(mockDal, Mockito.never()).commitTransaction();
  }

  @DisplayName("TEST FurnitureUCC.updateInfos : invalid furniture id, "
      + "should throw NotFoundException")
  @Test
  void test_updateInfos_invalidFurnitureId_shouldThrowNotFound() {
    Mockito.when(mockFurnitureDTO3.getFurnitureId()).thenReturn(defaultFurnitureId1);
    Mockito.when(mockFurnitureDTO3.getDescription()).thenReturn(null);
    Mockito.when(mockFurnitureDTO3.getTypeId()).thenReturn(null);
    Mockito.when(mockFurnitureDTO3.getSellingPrice()).thenReturn(defaultSellingPrice2);

    Mockito.when(mockFurnitureDAO.findById(defaultFurnitureId1)).thenThrow(new NotFoundException());

    assertThrows(NotFoundException.class, () ->
            furnitureUCC.updateInfos(mockFurnitureDTO3),
        "A call to updateInfos with invalid furniture id "
            + "should throw NotFoundException");

    InOrder inOrder = Mockito.inOrder(mockDal, mockFurnitureDAO);
    inOrder.verify(mockDal).startTransaction();
    inOrder.verify(mockFurnitureDAO).findById(defaultFurnitureId1);
    inOrder.verify(mockDal).rollbackTransaction();
    inOrder.verifyNoMoreInteractions();
    inOrder.verify(mockDal, Mockito.never()).commitTransaction();
  }

  @DisplayName("TEST FurnitureUCC.updateInfos : catches InternalError, "
      + "should throw it back")
  @Test
  void test_updateInfos_catchesInternal_shouldThrowInternal() {
    Mockito.when(mockFurnitureDTO3.getFurnitureId()).thenReturn(defaultFurnitureId1);
    Mockito.when(mockFurnitureDTO3.getDescription()).thenReturn(null);
    Mockito.when(mockFurnitureDTO3.getTypeId()).thenReturn(null);
    Mockito.when(mockFurnitureDTO3.getSellingPrice()).thenReturn(defaultSellingPrice2);

    Mockito.when(mockFurnitureDAO.updateDescription(mockFurnitureDTO1))
        .thenThrow(new InternalError());

    assertThrows(InternalError.class, () ->
            furnitureUCC.updateInfos(mockFurnitureDTO3),
        "A call to updateInfos catching an InternalError "
            + "should throw it back");

    InOrder inOrder = Mockito.inOrder(mockDal, mockFurnitureDAO);
    inOrder.verify(mockDal).startTransaction();
    inOrder.verify(mockFurnitureDAO).findById(defaultFurnitureId1);
    inOrder.verify(mockDal).rollbackTransaction();
    inOrder.verifyNoMoreInteractions();
    inOrder.verify(mockDal, Mockito.never()).commitTransaction();
  }


  /**
   * Provider of valid arguments for @ParametrizedTest on FurnitureUCC.updateInfos.
   */
  private static class ValidUpdateInfosProvider implements ArgumentsProvider {

    @Override
    public Stream<Arguments> provideArguments(ExtensionContext extensionContext) {
      Stream<Arguments> res = Stream.of();
      for (String desc : Arrays.asList(null, defaultDescription2)) {
        for (Integer typeId : Arrays.asList(null, defaultTypeId2)) {
          for (Double sellingPrice : Arrays.asList(null, defaultSellingPrice2)) {
            for (Double startingSellingPrice : Arrays.asList(null, defaultSellingPrice1)) {
              res = Stream.concat(res, Stream.of(FurnitureStatus.values())
                  .map((s) -> Arguments
                      .arguments(desc, typeId, sellingPrice, startingSellingPrice, s)));
            }
          }
        }
      }
      return res.filter((args) -> areValidArguments(args));
    }

    /**
     * verifies if a set of arguments are valid.
     *
     * @param args : arguments
     * @return a boolean
     */
    private static boolean areValidArguments(Arguments args) {
      String desc = (String) args.get()[0];
      Integer typeId = (Integer) args.get()[1];
      Double sellingPrice = (Double) args.get()[2];
      Double startingSellingPrice = (Double) args.get()[3];
      FurnitureStatus status = (FurnitureStatus) args.get()[4];
      if (desc == null && typeId == null && sellingPrice == null) {
        return false;
      }
      if (sellingPrice != null && !status.equals(FurnitureStatus.AVAILABLE_FOR_SALE)) {
        return false;
      }
      if (!status.equals(FurnitureStatus.AVAILABLE_FOR_SALE)
          && !status.equals(FurnitureStatus.UNDER_OPTION)
          && !status.equals(FurnitureStatus.SOLD)) {
        if (startingSellingPrice != null) {
          return false;
        }
      } else {
        if (startingSellingPrice == null) {
          return false;
        }
      }
      return true;
    }
  }
}