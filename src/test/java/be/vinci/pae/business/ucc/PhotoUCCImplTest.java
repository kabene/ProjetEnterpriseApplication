package be.vinci.pae.business.ucc;

import be.vinci.pae.business.dto.PhotoDTO;
import be.vinci.pae.business.pojos.PhotoImpl;
import be.vinci.pae.exceptions.ConflictException;
import be.vinci.pae.exceptions.NotFoundException;
import be.vinci.pae.main.TestBinder;
import be.vinci.pae.persistence.dal.ConnectionDalServices;
import be.vinci.pae.persistence.dao.PhotoDAO;
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
  private static ConnectionDalServices mockDal;

  private static PhotoDTO mockPhotoDTO1;
  private static PhotoDTO mockPhotoDTO2;
  private static PhotoDTO mockPhotoDTO3;

  private static int defaultPhotoId1 = 1;
  private static int defaultPhotoId2 = 2;
  private static int defaultPhotoId3 = 3;
  private static boolean defaultIsVisible = true;
  private static boolean defaultIsOnHomePage = true;

  @BeforeAll
  public static void init() {
    ServiceLocator locator = ServiceLocatorUtilities.bind(new TestBinder());

    photoUCC = locator.getService(PhotoUCC.class);
    mockPhotoDAO = locator.getService(PhotoDAO.class);
    mockDal = locator.getService(ConnectionDalServices.class);

    mockPhotoDTO1 = Mockito.mock(PhotoImpl.class);
    mockPhotoDTO2 = Mockito.mock(PhotoImpl.class);
    mockPhotoDTO3 = Mockito.mock(PhotoImpl.class);
  }

  @BeforeEach
  void setUp() {
    Mockito.reset(mockPhotoDAO);
    Mockito.reset(mockDal);
    Mockito.reset(mockPhotoDTO1);
    Mockito.reset(mockPhotoDTO2);
    Mockito.reset(mockPhotoDTO3);

    Mockito.when(mockPhotoDAO.getPhotoById(defaultPhotoId1)).thenReturn(mockPhotoDTO1);
    Mockito.when(mockPhotoDAO.getPhotoById(defaultPhotoId2)).thenReturn(mockPhotoDTO2);
    Mockito.when(mockPhotoDAO.getPhotoById(defaultPhotoId3)).thenReturn(mockPhotoDTO3);

    Mockito.when(mockPhotoDTO1.getPhotoId()).thenReturn(defaultPhotoId1);
    Mockito.when(mockPhotoDTO2.getPhotoId()).thenReturn(defaultPhotoId2);
    Mockito.when(mockPhotoDTO3.getPhotoId()).thenReturn(defaultPhotoId3);

    Mockito.when(mockPhotoDTO1.isVisible()).thenReturn(defaultIsVisible);
    Mockito.when(mockPhotoDTO2.isVisible()).thenReturn(defaultIsVisible);
    Mockito.when(mockPhotoDTO3.isVisible()).thenReturn(defaultIsVisible);

    Mockito.when(mockPhotoDTO1.isOnHomePage()).thenReturn(defaultIsOnHomePage);
    Mockito.when(mockPhotoDTO2.isOnHomePage()).thenReturn(defaultIsOnHomePage);
    Mockito.when(mockPhotoDTO3.isOnHomePage()).thenReturn(defaultIsOnHomePage);
  }


  @DisplayName("TEST PhotoUCC.getAllHomePageVisiblePhotos: "
      + "should return all visible photo on home page.")
  @Test
  void test_getAllVisibleHomePage_shouldReturnAllVisibleHomePage() {
    List<PhotoDTO> photoDTOS = Arrays.asList(mockPhotoDTO1, mockPhotoDTO2, mockPhotoDTO3);

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

  @DisplayName("TEST PhotoUCC.patchDisplayFlags() : nominal scenario")
  @ParameterizedTest
  @MethodSource
  void test_patchDisplayFlags_nominalScenario_shouldReturnDTO(boolean isVisible,
      boolean isOnHomePage) {
    Mockito.when(mockPhotoDAO.updateDisplayFlags(mockPhotoDTO1)).thenReturn(mockPhotoDTO2);

    PhotoDTO actual = photoUCC.patchDisplayFlags(defaultPhotoId1, isVisible, isOnHomePage);
    assertEquals(mockPhotoDTO2, actual);

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
   * MethodSource for test_patchDisplayFlags_nominalScenario_shouldReturnDTO
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
        () -> photoUCC.patchDisplayFlags(defaultPhotoId1, false, true));

    InOrder inOrder = Mockito.inOrder(mockDal);
    inOrder.verify(mockDal).startTransaction();
    inOrder.verify(mockDal).rollbackTransaction();
    inOrder.verify(mockDal, Mockito.never()).commitTransaction();
  }

  @DisplayName("TEST PhotoUCC.patchDisplayFlags() : "
      + "given invalid id, should throw NotFoundException")
  @Test
  void test_patchDisplayFlags_givenInvalidId_shouldThrowNotFound() {
    Mockito.when(mockPhotoDAO.getPhotoById(defaultPhotoId1)).thenThrow(new NotFoundException());
    assertThrows(NotFoundException.class,
        () -> photoUCC.patchDisplayFlags(defaultPhotoId1, true, true));

    InOrder inOrder = Mockito.inOrder(mockDal, mockPhotoDAO);
    inOrder.verify(mockDal).startTransaction();
    inOrder.verify(mockPhotoDAO).getPhotoById(defaultPhotoId1);
    inOrder.verify(mockDal).rollbackTransaction();
    inOrder.verify(mockDal, Mockito.never()).commitTransaction();
  }

  @DisplayName("TEST PhotoUCC.patchDisplayFlags() : "
      + "catches InternalError, should throw it back after rollback")
  @Test
  void test_patchDisplayFlags_catchesInternal_shouldThrowInternal() {
    Mockito.when(mockPhotoDAO.updateDisplayFlags(mockPhotoDTO1)).thenThrow(new InternalError());

    assertThrows(InternalError.class,
        () -> photoUCC.patchDisplayFlags(defaultPhotoId1, true, true));

    InOrder inOrder = Mockito.inOrder(mockDal, mockPhotoDAO);
    inOrder.verify(mockDal).startTransaction();
    inOrder.verify(mockPhotoDAO).getPhotoById(defaultPhotoId1);
    inOrder.verify(mockPhotoDAO).updateDisplayFlags(mockPhotoDTO1);
    inOrder.verify(mockDal).rollbackTransaction();
    inOrder.verify(mockDal, Mockito.never()).commitTransaction();
  }
}