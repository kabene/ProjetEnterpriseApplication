package be.vinci.pae.business.ucc;

import be.vinci.pae.business.dto.FurnitureDTO;
import be.vinci.pae.business.dto.FurnitureTypeDTO;
import be.vinci.pae.business.dto.PhotoDTO;
import be.vinci.pae.business.dto.RequestForVisitDTO;
import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.business.pojos.PhotoImpl;
import be.vinci.pae.exceptions.ConflictException;
import be.vinci.pae.exceptions.NotFoundException;
import be.vinci.pae.exceptions.UnauthorizedException;
import be.vinci.pae.main.TestBinder;
import be.vinci.pae.persistence.dal.ConnectionDalServices;
import be.vinci.pae.persistence.dao.FurnitureDAO;
import be.vinci.pae.persistence.dao.FurnitureTypeDAO;
import be.vinci.pae.persistence.dao.PhotoDAO;
import be.vinci.pae.persistence.dao.RequestForVisitDAO;
import java.util.stream.Stream;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InOrder;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PhotoUCCImplTest {

  private static PhotoUCC photoUCC;
  private static PhotoDAO mockPhotoDAO;
  private static FurnitureDAO mockFurnitureDAO;
  private static FurnitureTypeDAO mockFurnitureTypeDAO;
  private static RequestForVisitDAO mockRequestDAO;
  private static ConnectionDalServices mockDal;

  private static final String defaultSource1 = "a";
  private static final String defaultSource2 = "b";
  private static final String defaultSource3 = "c";

  private static final int defaultFurnitureId1 = 1;
  private static final int defaultFurnitureId2 = 2;

  private static int defaultPhotoId1 = 3;
  private static int defaultPhotoId2 = 4;
  private static int defaultPhotoId3 = 5;

  private static int defaultRequestId = 6;

  private static int defaultUserId = 7;

  private static boolean defaultIsVisible = true;
  private static boolean defaultIsOnHomePage = true;
  private static boolean defaultIsFromRequest = false;

  private static PhotoDTO mockPhotoDTO1;
  private static PhotoDTO mockPhotoDTO2;
  private static PhotoDTO mockPhotoDTO3;

  private static FurnitureDTO mockFurnitureDTO1;
  private static FurnitureDTO mockFurnitureDTO2;

  private static RequestForVisitDTO mockRequestDTO;

  private static FurnitureTypeDTO mockFurnitureTypeDTO;

  private static UserDTO mockUserDTO;

  private static final List<PhotoDTO> defaultPhotoList = Arrays
      .asList(mockPhotoDTO1, mockPhotoDTO2, mockPhotoDTO3);

  @BeforeAll
  public static void init() {
    ServiceLocator locator = ServiceLocatorUtilities.bind(new TestBinder());

    photoUCC = locator.getService(PhotoUCC.class);
    mockPhotoDAO = locator.getService(PhotoDAO.class);
    mockFurnitureDAO = locator.getService(FurnitureDAO.class);
    mockFurnitureTypeDAO = locator.getService(FurnitureTypeDAO.class);
    mockRequestDAO = locator.getService(RequestForVisitDAO.class);
    mockDal = locator.getService(ConnectionDalServices.class);

    mockPhotoDTO1 = Mockito.mock(PhotoImpl.class);
    mockPhotoDTO2 = Mockito.mock(PhotoImpl.class);
    mockPhotoDTO3 = Mockito.mock(PhotoImpl.class);

    mockFurnitureDTO1 = Mockito.mock(FurnitureDTO.class);
    mockFurnitureDTO2 = Mockito.mock(FurnitureDTO.class);

    mockRequestDTO = Mockito.mock(RequestForVisitDTO.class);

    mockUserDTO = Mockito.mock(UserDTO.class);

    mockFurnitureTypeDTO = Mockito.mock(FurnitureTypeDTO.class);
  }

  @BeforeEach
  void setUp() {
    Mockito.reset(mockPhotoDAO);
    Mockito.reset(mockFurnitureDAO);
    Mockito.reset(mockRequestDAO);
    Mockito.reset(mockDal);
    Mockito.reset(mockPhotoDTO1);
    Mockito.reset(mockPhotoDTO2);
    Mockito.reset(mockPhotoDTO3);
    Mockito.reset(mockFurnitureDTO1);
    Mockito.reset(mockFurnitureDTO2);
    Mockito.reset(mockRequestDTO);
    Mockito.reset(mockUserDTO);

    Mockito.when(mockPhotoDTO1.getPhotoId()).thenReturn(defaultPhotoId1);
    Mockito.when(mockPhotoDTO2.getPhotoId()).thenReturn(defaultPhotoId2);
    Mockito.when(mockPhotoDTO3.getPhotoId()).thenReturn(defaultPhotoId3);

    Mockito.when(mockPhotoDTO1.getSource()).thenReturn(defaultSource1);
    Mockito.when(mockPhotoDTO2.getSource()).thenReturn(defaultSource2);
    Mockito.when(mockPhotoDTO3.getSource()).thenReturn(defaultSource3);

    Mockito.when(mockPhotoDAO.findById(defaultPhotoId1)).thenReturn(mockPhotoDTO1);
    Mockito.when(mockPhotoDAO.findById(defaultPhotoId2)).thenReturn(mockPhotoDTO2);
    Mockito.when(mockPhotoDAO.findById(defaultPhotoId3)).thenReturn(mockPhotoDTO3);

    Mockito.when(mockPhotoDAO.findAllByFurnitureId(defaultFurnitureId1))
        .thenReturn(new ArrayList<>(defaultPhotoList));

    Mockito.when(mockPhotoDTO1.isVisible()).thenReturn(defaultIsVisible);
    Mockito.when(mockPhotoDTO2.isVisible()).thenReturn(defaultIsVisible);
    Mockito.when(mockPhotoDTO3.isVisible()).thenReturn(defaultIsVisible);

    Mockito.when(mockPhotoDTO1.isOnHomePage()).thenReturn(defaultIsOnHomePage);
    Mockito.when(mockPhotoDTO2.isOnHomePage()).thenReturn(defaultIsOnHomePage);
    Mockito.when(mockPhotoDTO3.isOnHomePage()).thenReturn(defaultIsOnHomePage);

    Mockito.when(mockPhotoDTO1.isFromRequest()).thenReturn(defaultIsFromRequest);
    Mockito.when(mockPhotoDTO2.isFromRequest()).thenReturn(defaultIsFromRequest);
    Mockito.when(mockPhotoDTO3.isFromRequest()).thenReturn(defaultIsFromRequest);

    Mockito.when(mockFurnitureDAO.findById(defaultFurnitureId1)).thenReturn(mockFurnitureDTO1);
    Mockito.when(mockFurnitureDAO.findById(defaultFurnitureId2)).thenReturn(mockFurnitureDTO2);

    Mockito.when(mockRequestDAO.findById(defaultRequestId)).thenReturn(mockRequestDTO);

    Mockito.when(mockFurnitureDTO1.getFavouritePhotoId()).thenReturn(defaultPhotoId1);
    Mockito.when(mockFurnitureDTO1.getRequestId()).thenReturn(defaultRequestId);

    Mockito.when(mockRequestDTO.getRequestId()).thenReturn(defaultRequestId);
    Mockito.when(mockRequestDTO.getUserId()).thenReturn(defaultUserId);

    Mockito.when(mockUserDTO.getId()).thenReturn(defaultUserId);
  }


  @DisplayName("TEST PhotoUCC.getAllHomePageVisiblePhotos: "
      + "should return all visible photo on home page.")
  @Test
  void test_getAllVisibleHomePage_shouldReturnAllVisibleHomePage() {
    List<PhotoDTO> photoDTOS = Arrays.asList(mockPhotoDTO1, mockPhotoDTO2, mockPhotoDTO3);
    for (PhotoDTO photoDTO : photoDTOS) {
      Mockito.when(photoDTO.getFurniture()).thenReturn(mockFurnitureDTO1);
    }
    Mockito.when(mockFurnitureDAO.findById(mockFurnitureDTO1.getFurnitureId()))
        .thenReturn(mockFurnitureDTO1);
    Mockito.when(mockFurnitureTypeDAO.findById(mockFurnitureTypeDTO.getTypeId()))
        .thenReturn(mockFurnitureTypeDTO);

    Mockito.when(mockPhotoDAO.getAllHomePageVisiblePhotos()).thenReturn(photoDTOS);

    List<PhotoDTO> actual = photoUCC.getAllHomePageVisiblePhotos();
    assertEquals(photoDTOS, actual,
        "called photoUCC.getAllHomePageVisiblePhotos(),"
            + " should have return all visible photo on home page.");

    Mockito.verify(mockPhotoDAO).getAllHomePageVisiblePhotos();

    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal).commitTransaction();
    Mockito.verify(mockDal, Mockito.never()).rollbackTransaction();
  }


  @DisplayName("TEST PhotoUCC.getAllHomePageVisiblePhotos without any data present:"
      + " should return an empty list.")
  @Test
  void test_getAllVisibleHomePageWithEmptyDataBase_shouldReturnEmptyList() {
    List<PhotoDTO> photoDTOS = new ArrayList<>();

    Mockito.when(mockPhotoDAO.getAllHomePageVisiblePhotos()).thenReturn(photoDTOS);

    List<PhotoDTO> actual = photoUCC.getAllHomePageVisiblePhotos();
    assertEquals(photoDTOS, actual,
        "called photoUCC.getAllHomePageVisiblePhotos() without any data present,"
            + " should have return an empty list.");

    Mockito.verify(mockPhotoDAO).getAllHomePageVisiblePhotos();

    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal).commitTransaction();
  }

  @DisplayName("TEST PhotoUCC.getAllHomePageVisiblePhotos : DAO throws InternalError,"
      + " Should rollback and throw InternalError")
  @Test
  void test_getAllVisibleHomePage_InternalErrorThrown_shouldThrowInternalErrorAndRollback() {
    Mockito.when(mockPhotoDAO.getAllHomePageVisiblePhotos()).thenThrow(new InternalError());

    assertThrows(InternalError.class, () -> photoUCC.getAllHomePageVisiblePhotos(),
        "If the DAO throws an exception, it should be thrown back");

    Mockito.verify(mockPhotoDAO).getAllHomePageVisiblePhotos();

    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal).rollbackTransaction();
    Mockito.verify(mockDal, Mockito.never()).commitTransaction();
  }

  @DisplayName("TEST PhotoUCC.insert: nominal : should return DTO")
  @Test
  void test_add_nominal_shouldReturnDTO() {
    Mockito.when(mockPhotoDAO.insert(defaultFurnitureId1, defaultSource1))
        .thenReturn(defaultPhotoId1);

    assertEquals(mockPhotoDTO1, photoUCC.add(defaultFurnitureId1, defaultSource1),
        "The nominal case should return a dto");

    InOrder inOrder = Mockito.inOrder(mockDal, mockPhotoDAO, mockFurnitureDAO);
    inOrder.verify(mockDal).startTransaction();
    inOrder.verify(mockFurnitureDAO).findById(defaultFurnitureId1);
    inOrder.verify(mockPhotoDAO).insert(defaultFurnitureId1, defaultSource1);
    inOrder.verify(mockPhotoDAO).findById(defaultPhotoId1);
    inOrder.verify(mockDal).commitTransaction();
    inOrder.verify(mockDal, Mockito.never()).rollbackTransaction();
    inOrder.verifyNoMoreInteractions();
  }

  @DisplayName("TEST PhotoUCC.insert: invalid furniture id, should throw NotFoundError")
  @Test
  void test_add_invalidFurnitureId_shouldThrowNotFound() {
    Mockito.when(mockFurnitureDAO.findById(defaultFurnitureId1)).thenThrow(new NotFoundException());

    assertThrows(NotFoundException.class, () -> {
      photoUCC.add(defaultFurnitureId1, defaultSource1);
    }, "a call to insert with a non-existing furniture id should throw NotFoundException");
    InOrder inOrder = Mockito.inOrder(mockDal, mockPhotoDAO, mockFurnitureDAO);
    inOrder.verify(mockDal).startTransaction();
    inOrder.verify(mockFurnitureDAO).findById(defaultFurnitureId1);
    inOrder.verify(mockDal).rollbackTransaction();
    inOrder.verify(mockDal, Mockito.never()).commitTransaction();
    inOrder.verifyNoMoreInteractions();
  }

  @DisplayName(
      "TEST PhotoUCC.insert: DAO throws InternalError,"
          + "Should rollback and throw InternalError")
  @Test
  void test_add_catchesInternalShouldThrowInternal() {
    Mockito.when(mockPhotoDAO.insert(defaultFurnitureId1, defaultSource1))
        .thenThrow(new InternalError());

    assertThrows(InternalError.class, () -> photoUCC.add(defaultFurnitureId1, defaultSource1),
        "If the DAO throws an exception, it should be thrown back");

    Mockito.verify(mockPhotoDAO).insert(defaultFurnitureId1, defaultSource1);

    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal).rollbackTransaction();
    Mockito.verify(mockDal, Mockito.never()).commitTransaction();
  }

  @DisplayName("TEST PhotoUCC.patchDisplayFlags() : nominal scenario")
  @ParameterizedTest
  @MethodSource
  void test_patchDisplayFlags_nominalScenario_shouldReturnDTO(boolean isVisible,
      boolean isOnHomePage) {
    Mockito.when(mockPhotoDAO.updateDisplayFlags(mockPhotoDTO1)).thenReturn(mockPhotoDTO2);

    PhotoDTO actual = photoUCC.patchDisplayFlags(defaultPhotoId1, isVisible, isOnHomePage);
    assertEquals(mockPhotoDTO2, actual,
        "a valid call to patchDisplayFlags should return the modified PhotoDTO");

    InOrder inOrder = Mockito
        .inOrder(mockPhotoDTO1, mockPhotoDAO, mockDal); // enforce invocation order

    inOrder.verify(mockDal).startTransaction();
    inOrder.verify(mockPhotoDTO1).setVisible(isVisible);
    inOrder.verify(mockPhotoDTO1).setOnHomePage(isOnHomePage);
    inOrder.verify(mockPhotoDAO).updateDisplayFlags(mockPhotoDTO1);
    inOrder.verify(mockDal).commitTransaction();

    Mockito.verify(mockDal, Mockito.never()).rollbackTransaction();
  }

  /**
   * MethodSource for test_patchDisplayFlags_nominalScenario_shouldReturnDTO.
   *
   * @return Stream of Arguments
   */
  private static Stream<Arguments> test_patchDisplayFlags_nominalScenario_shouldReturnDTO() {
    return Stream.of(
        Arguments.arguments(true, true),
        Arguments.arguments(true, false),
        Arguments.arguments(false, false)
    );
  }

  @DisplayName(
      "TEST PhotoUCC.patchDisplayFlags() : given invalid flags (!isVisible & isOnHomePage),"
          + " should throw ConflicException")
  @Test
  void test_patchDisplayFlags_givenInvalidFlags_shouldThrowConflict() {
    assertThrows(ConflictException.class,
        () -> photoUCC.patchDisplayFlags(defaultPhotoId1, false, true),
        "a call to patchDisplayFlags with invalid flags (!isVisible & isOnHomePage) "
            + "should throw ConflictException");

    InOrder inOrder = Mockito.inOrder(mockDal);
    inOrder.verify(mockDal).startTransaction();
    inOrder.verify(mockDal).rollbackTransaction();
    inOrder.verify(mockDal, Mockito.never()).commitTransaction();
  }

  @DisplayName("TEST PhotoUCC.patchDisplayFlags() : given valid flags on request photo,"
      + " should throw ConflictException")
  @ParameterizedTest
  @MethodSource
  void test_patchDisplayFlags_requestPhoto_shouldThrowConflict(boolean isVisible,
      boolean isOnHomePage) {
    Mockito.when(mockPhotoDTO1.isFromRequest()).thenReturn(true);
    assertThrows(ConflictException.class, () ->
        photoUCC.patchDisplayFlags(defaultPhotoId1, isVisible, isOnHomePage));

    InOrder inOrder = Mockito
        .inOrder(mockPhotoDTO1, mockPhotoDAO, mockDal); // enforce invocation order
    inOrder.verify(mockDal).startTransaction();
    inOrder.verify(mockPhotoDAO).findById(defaultPhotoId1);
    inOrder.verify(mockDal).rollbackTransaction();
    inOrder.verifyNoMoreInteractions();
    Mockito.verify(mockDal, Mockito.never()).commitTransaction();
  }

  /**
   * MethodSource for test_patchDisplayFlags_nominalScenario_shouldReturnDTO.
   *
   * @return Stream of Arguments
   */
  private static Stream<Arguments> test_patchDisplayFlags_requestPhoto_shouldThrowConflict() {
    return Stream.of(
        Arguments.arguments(true, true),
        Arguments.arguments(true, false),
        Arguments.arguments(false, false)
    );
  }

  @DisplayName("TEST PhotoUCC.patchDisplayFlags() : "
      + "given invalid id, should throw NotFoundException")
  @Test
  void test_patchDisplayFlags_givenInvalidId_shouldThrowNotFound() {
    Mockito.when(mockPhotoDAO.findById(defaultPhotoId1)).thenThrow(new NotFoundException());
    assertThrows(NotFoundException.class,
        () -> photoUCC.patchDisplayFlags(defaultPhotoId1, true, true),
        "a call to patchDisplayFlags with invalid photo id should throw "
            + "NotFoundException");

    InOrder inOrder = Mockito.inOrder(mockDal, mockPhotoDAO);
    inOrder.verify(mockDal).startTransaction();
    inOrder.verify(mockPhotoDAO).findById(defaultPhotoId1);
    inOrder.verify(mockDal).rollbackTransaction();
    inOrder.verify(mockDal, Mockito.never()).commitTransaction();
  }

  @DisplayName("TEST PhotoUCC.patchDisplayFlags() : "
      + "catches InternalError, should throw it back after rollback")
  @Test
  void test_patchDisplayFlags_catchesInternal_shouldThrowInternal() {
    Mockito.when(mockPhotoDAO.updateDisplayFlags(mockPhotoDTO1)).thenThrow(new InternalError());

    assertThrows(InternalError.class,
        () -> photoUCC.patchDisplayFlags(defaultPhotoId1, true, true),
        "if patchDisplayFlags catches an InternalError, it should throw it back");

    InOrder inOrder = Mockito.inOrder(mockDal, mockPhotoDAO);
    inOrder.verify(mockDal).startTransaction();
    inOrder.verify(mockPhotoDAO).findById(defaultPhotoId1);
    inOrder.verify(mockPhotoDAO).updateDisplayFlags(mockPhotoDTO1);
    inOrder.verify(mockDal).rollbackTransaction();
    inOrder.verify(mockDal, Mockito.never()).commitTransaction();
  }

  @DisplayName("TEST PhotoUCC.getFavourite() : nominal, should return PhotoDTO")
  @Test
  void test_getFavourite_nominal_shouldReturnDTO() {
    assertEquals(mockPhotoDTO1, photoUCC.getFavourite(defaultFurnitureId1));

    InOrder inOrder = Mockito.inOrder(mockDal, mockFurnitureDAO, mockPhotoDAO);
    inOrder.verify(mockDal).startTransaction();
    inOrder.verify(mockFurnitureDAO).findById(defaultFurnitureId1);
    inOrder.verify(mockPhotoDAO).findById(defaultPhotoId1);
    inOrder.verify(mockDal).commitTransaction();
    inOrder.verifyNoMoreInteractions();
    inOrder.verify(mockDal, Mockito.never()).rollbackTransaction();
  }

  @DisplayName("TEST PhotoUCC.getFavourite() : given invalid furniture id, "
      + "should throw NotFoundException")
  @Test
  void test_getFavourite_invalidFurnitureId_shouldThrowNotFound() {
    Mockito.when(mockFurnitureDAO.findById(defaultFurnitureId1)).thenThrow(new NotFoundException());
    assertThrows(NotFoundException.class, () -> photoUCC.getFavourite(defaultFurnitureId1));

    InOrder inOrder = Mockito.inOrder(mockDal, mockFurnitureDAO);
    inOrder.verify(mockDal).startTransaction();
    inOrder.verify(mockFurnitureDAO).findById(defaultFurnitureId1);
    inOrder.verify(mockDal).rollbackTransaction();
    inOrder.verifyNoMoreInteractions();
    inOrder.verify(mockDal, Mockito.never()).commitTransaction();
  }

  @DisplayName("TEST PhotoUCC.getFavourite() : given invalid furniture id, "
      + "should throw NotFoundException")
  @Test
  void test_getFavourite_noFav_shouldThrowNotFound() {
    Mockito.when(mockFurnitureDTO1.getFavouritePhotoId()).thenReturn(null);
    assertThrows(NotFoundException.class, () -> photoUCC.getFavourite(defaultFurnitureId1));

    InOrder inOrder = Mockito.inOrder(mockDal, mockFurnitureDAO, mockPhotoDAO);
    inOrder.verify(mockDal).startTransaction();
    inOrder.verify(mockFurnitureDAO).findById(defaultFurnitureId1);
    inOrder.verify(mockDal).rollbackTransaction();
    inOrder.verifyNoMoreInteractions();
    inOrder.verify(mockDal, Mockito.never()).commitTransaction();
  }

  @DisplayName("TEST PhotoUCC.getFavourite() : catches InternalError, should throw InternalError")
  @Test
  void test_getFavourite_catchesInternal_shouldThrowInternal() {
    Mockito.when(mockPhotoDAO.findById(defaultPhotoId1)).thenThrow(new InternalError());
    assertThrows(InternalError.class, () -> photoUCC.getFavourite(defaultFurnitureId1));

    InOrder inOrder = Mockito.inOrder(mockDal, mockFurnitureDAO, mockPhotoDAO);
    inOrder.verify(mockDal).startTransaction();
    inOrder.verify(mockFurnitureDAO).findById(defaultFurnitureId1);
    inOrder.verify(mockPhotoDAO).findById(defaultPhotoId1);
    inOrder.verify(mockDal).rollbackTransaction();
    inOrder.verifyNoMoreInteractions();
    inOrder.verify(mockDal, Mockito.never()).commitTransaction();
  }

  @DisplayName("TEST PhotoUCC.getAllForFurniture() : nominal, should return List of dtos")
  @Test
  void test_getAllForFurniture_nominal_shouldReturnListDto() {
    List<PhotoDTO> expected = new ArrayList<>(defaultPhotoList);
    assertEquals(expected, photoUCC.getAllForFurniture(defaultFurnitureId1));

    InOrder inOrder = Mockito.inOrder(mockDal, mockPhotoDAO);
    inOrder.verify(mockDal).startTransaction();
    inOrder.verify(mockPhotoDAO).findAllByFurnitureId(defaultFurnitureId1);
    inOrder.verify(mockDal).commitTransaction();
    inOrder.verifyNoMoreInteractions();
    inOrder.verify(mockDal, Mockito.never()).rollbackTransaction();
  }

  @DisplayName("TEST PhotoUCC.getAllForFurniture() : catches InternalError, should throw it back")
  @Test
  void test_getAllForFurniture_catchesInternal_shouldThrowInternal() {
    Mockito.when(mockPhotoDAO.findAllByFurnitureId(defaultFurnitureId1))
        .thenThrow(new InternalError());
    assertThrows(InternalError.class, () -> photoUCC.getAllForFurniture(defaultFurnitureId1));

    InOrder inOrder = Mockito.inOrder(mockDal, mockPhotoDAO);
    inOrder.verify(mockDal).startTransaction();
    inOrder.verify(mockPhotoDAO).findAllByFurnitureId(defaultFurnitureId1);
    inOrder.verify(mockDal).rollbackTransaction();
    inOrder.verifyNoMoreInteractions();
    inOrder.verify(mockDal, Mockito.never()).commitTransaction();
  }

  @DisplayName("TEST PhotoUCC.getRequestPhotos() : nominal, should return dto list")
  @Test
  void test_getRequestPhotos_nominal_shouldReturnDTOList() {
    Mockito.when(mockPhotoDAO.findAllRequestPhotosByFurnitureId(defaultFurnitureId1))
        .thenReturn(Arrays.asList(mockPhotoDTO1, mockPhotoDTO2, mockPhotoDTO3));
    List<PhotoDTO> expected = Arrays.asList(mockPhotoDTO1, mockPhotoDTO2, mockPhotoDTO3);
    assertEquals(expected, photoUCC.getRequestPhotos(mockUserDTO, defaultFurnitureId1));

    InOrder inOrder = Mockito
        .inOrder(mockDal, mockPhotoDAO, mockFurnitureDAO, mockRequestDAO);
    inOrder.verify(mockDal).startTransaction();
    inOrder.verify(mockFurnitureDAO).findById(defaultFurnitureId1);
    inOrder.verify(mockRequestDAO).findById(defaultRequestId);
    inOrder.verify(mockPhotoDAO).findAllRequestPhotosByFurnitureId(defaultFurnitureId1);
    inOrder.verify(mockDal).commitTransaction();
    inOrder.verifyNoMoreInteractions();
    inOrder.verify(mockDal, Mockito.never()).rollbackTransaction();
  }

  @DisplayName("TEST PhotoUCC.getRequestPhotos() : not owner, should throw UnauthorizedException")
  @Test
  void test_getRequestPhotos_notOwner_shouldReturnThrowUnauthorized() {
    Mockito.when(mockRequestDTO.getUserId()).thenReturn(defaultUserId + 1);
    assertThrows(UnauthorizedException.class,
        () -> photoUCC.getRequestPhotos(mockUserDTO, defaultFurnitureId1));

    InOrder inOrder = Mockito
        .inOrder(mockDal, mockPhotoDAO, mockFurnitureDAO, mockRequestDAO);
    inOrder.verify(mockDal).startTransaction();
    inOrder.verify(mockFurnitureDAO).findById(defaultFurnitureId1);
    inOrder.verify(mockRequestDAO).findById(defaultRequestId);
    inOrder.verify(mockDal).rollbackTransaction();
    inOrder.verifyNoMoreInteractions();
    inOrder.verify(mockDal, Mockito.never()).commitTransaction();
  }
}