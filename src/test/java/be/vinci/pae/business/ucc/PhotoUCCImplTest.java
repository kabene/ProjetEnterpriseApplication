package be.vinci.pae.business.ucc;

import be.vinci.pae.business.dto.FurnitureDTO;
import be.vinci.pae.business.dto.PhotoDTO;
import be.vinci.pae.business.pojos.PhotoImpl;
import be.vinci.pae.exceptions.NotFoundException;
import be.vinci.pae.main.TestBinder;
import be.vinci.pae.persistence.dal.ConnectionDalServices;
import be.vinci.pae.persistence.dao.FurnitureDAO;
import be.vinci.pae.persistence.dao.PhotoDAO;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
  private static ConnectionDalServices mockDal;

  private static final String defaultSource1 = "a";
  private static final String defaultSource2 = "b";
  private static final String defaultSource3 = "c";

  private static int defaultPhotoId1 = 3;
  private static int defaultPhotoId2 = 4;
  private static int defaultPhotoId3 = 5;

  private static PhotoDTO mockPhotoDTO1;
  private static PhotoDTO mockPhotoDTO2;
  private static PhotoDTO mockPhotoDTO3;

  private static FurnitureDTO mockFurnitureDTO1;
  private static FurnitureDTO mockFurnitureDTO2;

  private static final int defaultFurnitureId1 = 1;
  private static final int defaultFurnitureId2 = 2;

  @BeforeAll
  public static void init() {
    ServiceLocator locator = ServiceLocatorUtilities.bind(new TestBinder());

    photoUCC = locator.getService(PhotoUCC.class);
    mockPhotoDAO = locator.getService(PhotoDAO.class);
    mockFurnitureDAO = locator.getService(FurnitureDAO.class);
    mockDal = locator.getService(ConnectionDalServices.class);

    mockPhotoDTO1 = Mockito.mock(PhotoImpl.class);
    mockPhotoDTO2 = Mockito.mock(PhotoImpl.class);
    mockPhotoDTO3 = Mockito.mock(PhotoImpl.class);

    mockFurnitureDTO1 = Mockito.mock(FurnitureDTO.class);
    mockFurnitureDTO2 = Mockito.mock(FurnitureDTO.class);
  }

  @BeforeEach
  void setUp() {
    Mockito.reset(mockPhotoDAO);
    Mockito.reset(mockFurnitureDAO);
    Mockito.reset(mockDal);
    Mockito.reset(mockPhotoDTO1);
    Mockito.reset(mockPhotoDTO2);
    Mockito.reset(mockPhotoDTO3);

    Mockito.when(mockPhotoDTO1.getPhotoId()).thenReturn(defaultPhotoId1);
    Mockito.when(mockPhotoDTO2.getPhotoId()).thenReturn(defaultPhotoId2);
    Mockito.when(mockPhotoDTO3.getPhotoId()).thenReturn(defaultPhotoId3);

    Mockito.when(mockPhotoDTO1.getSource()).thenReturn(defaultSource1);
    Mockito.when(mockPhotoDTO2.getSource()).thenReturn(defaultSource2);
    Mockito.when(mockPhotoDTO3.getSource()).thenReturn(defaultSource3);

    Mockito.when(mockPhotoDAO.getPhotoById(defaultPhotoId1)).thenReturn(mockPhotoDTO1);
    Mockito.when(mockPhotoDAO.getPhotoById(defaultPhotoId2)).thenReturn(mockPhotoDTO2);
    Mockito.when(mockPhotoDAO.getPhotoById(defaultPhotoId3)).thenReturn(mockPhotoDTO3);

    Mockito.when(mockFurnitureDAO.findById(defaultFurnitureId1)).thenReturn(mockFurnitureDTO1);
    Mockito.when(mockFurnitureDAO.findById(defaultFurnitureId2)).thenReturn(mockFurnitureDTO2);
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
    inOrder.verify(mockPhotoDAO).getPhotoById(defaultPhotoId1);
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
    });
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

}