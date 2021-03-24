package be.vinci.pae.business.ucc;

import static org.junit.jupiter.api.Assertions.assertEquals;

import be.vinci.pae.business.dto.FurnitureDTO;
import be.vinci.pae.business.dto.PhotoDTO;
import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.business.pojos.FurnitureImpl;
import be.vinci.pae.business.pojos.PhotoImpl;
import be.vinci.pae.business.pojos.UserImpl;
import be.vinci.pae.main.TestBinder;
import be.vinci.pae.persistence.dal.ConnectionDalServices;
import be.vinci.pae.persistence.dao.FurnitureDAO;
import be.vinci.pae.persistence.dao.FurnitureTypeDAO;
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
import org.mockito.Mockito;

class FurnitureUCCImplTest {

  private static FurnitureUCC furnitureUCC;
  private static FurnitureDAO mockFurnitureDAO;
  private static UserDAO mockUserDAO;
  private static PhotoDAO mockPhotoDAO;
  private static FurnitureTypeDAO mockFurnitureTypeDAO;
  private static ConnectionDalServices mockDal;

  private static FurnitureDTO mockFurnitureDTO1;
  private static FurnitureDTO mockFurnitureDTO2;
  private static UserDTO mockUserDTO1;
  private static UserDTO mockUserDTO2;
  private static PhotoDTO mockPhotoDTO1;
  private static PhotoDTO mockPhotoDTO2;
  private static PhotoDTO mockPhotoDTO3;


  @BeforeAll
  public static void init() {
    Configurate.load("properties/test.properties");
    ServiceLocator locator = ServiceLocatorUtilities.bind(new TestBinder());
    furnitureUCC = locator.getService(FurnitureUCC.class);

    mockFurnitureDAO = locator.getService(FurnitureDAO.class);
    mockUserDAO = locator.getService(UserDAO.class);
    mockPhotoDAO = locator.getService(PhotoDAO.class);
    mockFurnitureTypeDAO = locator.getService(FurnitureTypeDAO.class);
    mockDal = locator.getService(ConnectionDalServices.class);

    mockFurnitureDTO1 = Mockito.mock(FurnitureImpl.class);
    mockFurnitureDTO2 = Mockito.mock(FurnitureImpl.class);
    mockUserDTO1 = Mockito.mock(UserImpl.class);
    mockUserDTO2 = Mockito.mock(UserImpl.class);
    mockPhotoDTO1 = Mockito.mock(PhotoImpl.class);
    mockPhotoDTO2 = Mockito.mock(PhotoImpl.class);
    mockPhotoDTO3 = Mockito.mock(PhotoImpl.class);
  }

  @BeforeEach
  public void setUp() {
    Mockito.reset(mockFurnitureDAO);
    Mockito.reset(mockUserDAO);
    Mockito.reset(mockPhotoDAO);
    Mockito.reset(mockFurnitureTypeDAO);
    Mockito.reset(mockDal);
    Mockito.reset(mockFurnitureDTO1);
    Mockito.reset(mockFurnitureDTO2);
    Mockito.reset(mockUserDTO1);
    Mockito.reset(mockUserDTO2);
    Mockito.reset(mockPhotoDTO1);
    Mockito.reset(mockPhotoDTO2);
    Mockito.reset(mockPhotoDTO3);
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

  @DisplayName("TEST FurnitureUCC.getOne : given invalid id, should return null")
  @Test
  public void test_getOne_givenInvalidId_shouldReturnNull() {
    int furnitureId = 1;

    Mockito.when(mockFurnitureDAO.findById(furnitureId)).thenReturn(null);

    FurnitureDTO actual = furnitureUCC.getOne(furnitureId);
    assertEquals(null, actual, "Calling getOne with an invalid id should return null.");

    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal).rollbackTransaction();

    Mockito.verify(mockFurnitureDAO).findById(furnitureId);
  }

  @DisplayName("TEST FurnitureUCC.getAll : should return list of dto")
  @Test
  public void test_getAll_shouldReturnListOfFurnitureDTOs() {
    int furnitureId1 = 1;
    int furnitureId2 = 2;
    int buyerId1 = 1;
    int sellerId1 = 2;
    int typeId1 = 1;
    int typeId2 = 2;
    int favouritePhotoId1 = 1;
    List<PhotoDTO> photos1 = Arrays.asList(mockPhotoDTO1, mockPhotoDTO2);
    List<PhotoDTO> photos2 = Arrays.asList(mockPhotoDTO3);
    String type1 = "type1";
    String type2 = "type2";
    List<FurnitureDTO> furnitureDTOS = Arrays.asList(mockFurnitureDTO1, mockFurnitureDTO2);

    Mockito.when(mockFurnitureDAO.findAll()).thenReturn(furnitureDTOS);

    Mockito.when(mockUserDAO.findById(buyerId1)).thenReturn(mockUserDTO1);
    Mockito.when(mockUserDAO.findById(sellerId1)).thenReturn(mockUserDTO2);

    Mockito.when(mockPhotoDAO.getPhotoById(favouritePhotoId1)).thenReturn(mockPhotoDTO1);
    Mockito.when(mockPhotoDAO.getPhotosByFurnitureId(furnitureId1)).thenReturn(photos1);
    Mockito.when(mockPhotoDAO.getPhotosByFurnitureId(furnitureId2)).thenReturn(photos2);

    Mockito.when(mockFurnitureTypeDAO.findById(typeId1)).thenReturn(type1);
    Mockito.when(mockFurnitureTypeDAO.findById(typeId2)).thenReturn(type2);

    Mockito.when(mockFurnitureDTO1.getFurnitureId()).thenReturn(furnitureId1);
    Mockito.when(mockFurnitureDTO1.getBuyerId()).thenReturn(buyerId1);
    Mockito.when(mockFurnitureDTO1.getSellerId()).thenReturn(sellerId1);
    Mockito.when(mockFurnitureDTO1.getFavouritePhotoId()).thenReturn(favouritePhotoId1);
    Mockito.when(mockFurnitureDTO1.getTypeId()).thenReturn(typeId1);

    Mockito.when(mockFurnitureDTO2.getFurnitureId()).thenReturn(furnitureId2);
    Mockito.when(mockFurnitureDTO2.getBuyerId()).thenReturn(null);
    Mockito.when(mockFurnitureDTO2.getSellerId()).thenReturn(null);
    Mockito.when(mockFurnitureDTO2.getFavouritePhotoId()).thenReturn(null);
    Mockito.when(mockFurnitureDTO2.getTypeId()).thenReturn(typeId2);

    List<FurnitureDTO> expected = furnitureDTOS;
    List<FurnitureDTO> actual = furnitureUCC.getAll();
    assertEquals(expected, actual, "Calling getAll with a non-empty db should return a corresponding list of DTOs.");

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
  }

  @DisplayName("TEST FurnitureUCC.getAll : with empty furniture table in db, should return empty list of dto")
  @Test
  public void test_getAll_emptyDB_shouldReturnEmptyListOfFurnitureDTOs() {
    List<FurnitureDTO> emptyList = new ArrayList<FurnitureDTO>();

    Mockito.when(mockFurnitureDAO.findAll()).thenReturn(emptyList);

    List<FurnitureDTO> actual = furnitureUCC.getAll();
    assertEquals(emptyList, actual, "Calling the getAll method without pieces of furniture in db should return an empty list.");

    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal).commitTransaction();

    Mockito.verify(mockFurnitureDAO).findAll();
  }
}