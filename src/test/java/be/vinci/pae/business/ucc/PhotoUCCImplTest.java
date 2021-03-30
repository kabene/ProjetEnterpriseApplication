package be.vinci.pae.business.ucc;

import be.vinci.pae.business.dto.PhotoDTO;
import be.vinci.pae.business.pojos.PhotoImpl;
import be.vinci.pae.main.TestBinder;
import be.vinci.pae.persistence.dal.ConnectionDalServices;
import be.vinci.pae.persistence.dao.PhotoDAO;
import be.vinci.pae.utils.Configurate;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PhotoUCCImplTest {


  private static PhotoUCC photoUCC;
  private static PhotoDAO mockPhotoDAO;
  private static ConnectionDalServices mockDal;

  private static PhotoDTO mockPhotoDTO1;
  private static PhotoDTO mockPhotoDTO2;
  private static PhotoDTO mockPhotoDTO3;

  @BeforeAll
  public static void init() {
    Configurate.load("properties/test.properties");
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
  }


  @DisplayName("TEST PhotoUCC.getAllHomePageVisiblePhotos: " +
      "should return all visible photo on home page.")
  @Test
  void test_getAllVisibleHomePage_shouldReturnAllVisibleHomePage() {

    Mockito.when(mockPhotoDTO1.getPhotoId()).thenReturn(1);
    Mockito.when(mockPhotoDTO1.getFurnitureId()).thenReturn(1);
    Mockito.when(mockPhotoDTO1.isOnHomePage()).thenReturn(true);
    Mockito.when(mockPhotoDTO1.isVisible()).thenReturn(true);
    Mockito.when(mockPhotoDTO1.getSource()).thenReturn("content1");

    Mockito.when(mockPhotoDTO1.getPhotoId()).thenReturn(2);
    Mockito.when(mockPhotoDTO1.getFurnitureId()).thenReturn(1);
    Mockito.when(mockPhotoDTO1.isOnHomePage()).thenReturn(true);
    Mockito.when(mockPhotoDTO1.isVisible()).thenReturn(true);
    Mockito.when(mockPhotoDTO1.getSource()).thenReturn("content2");

    Mockito.when(mockPhotoDTO1.getPhotoId()).thenReturn(3);
    Mockito.when(mockPhotoDTO1.getFurnitureId()).thenReturn(2);
    Mockito.when(mockPhotoDTO1.isOnHomePage()).thenReturn(true);
    Mockito.when(mockPhotoDTO1.isVisible()).thenReturn(true);
    Mockito.when(mockPhotoDTO1.getSource()).thenReturn("content3");


    List<PhotoDTO> photoDTOS = Arrays.asList(mockPhotoDTO1, mockPhotoDTO2, mockPhotoDTO3);

    Mockito.when(mockPhotoDAO.getAllHomePageVisiblePhotos()).thenReturn(photoDTOS);

    List<PhotoDTO> expected = photoDTOS;
    List<PhotoDTO> actual = photoUCC.getAllHomePageVisiblePhotos();
    assertEquals(expected, actual,
        "called photoUCC.getAllHomePageVisiblePhotos(),"
        + " should have return all visible photo on home page.");

    Mockito.verify(mockPhotoDAO).getAllHomePageVisiblePhotos();

    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal).commitTransaction();
  }

  @DisplayName("TEST PhotoUCC.getAllHomePageVisiblePhotos without any data present:"
      + " should return an empty list.")
  @Test
  void test_getAllVisibleHomePageWithEmptyDataBase_shouldReturnEmptyList() {
    List<PhotoDTO> photoDTOS = new ArrayList<PhotoDTO>();

    Mockito.when(mockPhotoDAO.getAllHomePageVisiblePhotos()).thenReturn(photoDTOS);

    List<PhotoDTO> expected = photoDTOS;
    List<PhotoDTO> actual = photoUCC.getAllHomePageVisiblePhotos();
    assertEquals(expected, actual,
        "called photoUCC.getAllHomePageVisiblePhotos() without any data present,"
        + " should have return an empty list.");

    Mockito.verify(mockPhotoDAO).getAllHomePageVisiblePhotos();

    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal).commitTransaction();
  }

}