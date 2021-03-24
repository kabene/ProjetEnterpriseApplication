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

  private static FurnitureDTO mockFurnitureDTO;
  private static UserDTO mockUserDTO1;
  private static UserDTO mockUserDTO2;
  private static PhotoDTO mockPhotoDTO1;
  private static PhotoDTO mockPhotoDTO2;


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

    mockFurnitureDTO = Mockito.mock(FurnitureImpl.class);
    mockUserDTO1 = Mockito.mock(UserImpl.class);
    mockUserDTO2 = Mockito.mock(UserImpl.class);
    mockPhotoDTO1 = Mockito.mock(PhotoImpl.class);
    mockPhotoDTO2 = Mockito.mock(PhotoImpl.class);
  }

  @BeforeEach
  public void setUp() {
    Mockito.reset(mockFurnitureDAO);
    Mockito.reset(mockUserDAO);
    Mockito.reset(mockPhotoDAO);
    Mockito.reset(mockFurnitureTypeDAO);
    Mockito.reset(mockDal);
    Mockito.reset(mockFurnitureDTO);
    Mockito.reset(mockUserDTO1);
    Mockito.reset(mockUserDTO2);
    Mockito.reset(mockPhotoDTO1);
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

    Mockito.when(mockFurnitureDAO.findById(furnitureId)).thenReturn(mockFurnitureDTO);

    Mockito.when(mockUserDAO.findById(buyerId)).thenReturn(mockUserDTO1);
    Mockito.when(mockUserDAO.findById(sellerId)).thenReturn(mockUserDTO2);

    Mockito.when(mockPhotoDAO.getPhotoById(favouritePhotoId)).thenReturn(mockPhotoDTO1);
    Mockito.when(mockPhotoDAO.getPhotosByFurnitureId(furnitureId)).thenReturn(photos);

    Mockito.when(mockFurnitureTypeDAO.findById(typeId)).thenReturn(type);

    Mockito.when(mockFurnitureDTO.getFurnitureId()).thenReturn(furnitureId);
    Mockito.when(mockFurnitureDTO.getBuyerId()).thenReturn(buyerId);
    Mockito.when(mockFurnitureDTO.getSellerId()).thenReturn(sellerId);
    Mockito.when(mockFurnitureDTO.getFavouritePhotoId()).thenReturn(favouritePhotoId);
    Mockito.when(mockFurnitureDTO.getTypeId()).thenReturn(typeId);

    FurnitureDTO actual = furnitureUCC.getOne(furnitureId);
    FurnitureDTO expected = mockFurnitureDTO;
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

    Mockito.verify(mockFurnitureDTO).setBuyer(mockUserDTO1);
    Mockito.verify(mockFurnitureDTO).setSeller(mockUserDTO2);
    Mockito.verify(mockFurnitureDTO).setFavouritePhoto(mockPhotoDTO1);
    Mockito.verify(mockFurnitureDTO).setPhotos(photos);
    Mockito.verify(mockFurnitureDTO).setType(type);
  }

  @DisplayName("TEST FurnitureUCC.getOne : given valid id, should return dto")
  @Test
  public void test_getOne_givenInvalidId_shouldReturnNull() {
    //TODO
  }

  @DisplayName("TEST FurnitureUCC.getAll : should return list of dto")
  @Test
  public void test_getAll_shouldReturnListOfFurnitureDTOs() {
    //TODO
  }

  @DisplayName("TEST FurnitureUCC.getAll : with empty db, should return empty list of dto")
  @Test
  public void test_getAll_emptyDB_shouldReturnEmptyListOfFurnitureDTOs() {
    //TODO
  }
}