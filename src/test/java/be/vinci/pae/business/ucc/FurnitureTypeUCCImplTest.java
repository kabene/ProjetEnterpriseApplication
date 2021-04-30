package be.vinci.pae.business.ucc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import be.vinci.pae.business.dto.FurnitureTypeDTO;
import be.vinci.pae.business.pojos.FurnitureTypeImpl;
import be.vinci.pae.main.TestBinder;
import be.vinci.pae.persistence.dal.ConnectionDalServices;
import be.vinci.pae.persistence.dao.FurnitureTypeDAO;
import java.util.Arrays;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

class FurnitureTypeUCCImplTest {

  private static FurnitureTypeUCC furnitureTypeUCC;

  private static FurnitureTypeDAO mockFurnitureTypeDAO;
  private static ConnectionDalServices mockDal;

  private static FurnitureTypeDTO mockFurnitureTypeDTO1;
  private static FurnitureTypeDTO mockFurnitureTypeDTO2;

  private static int defaultTypeId1 = 1;
  private static int defaultTypeId2 = 2;

  private static String defaultTypeName1 = "type1";
  private static String defaultTypeName2 = "type2";

  @BeforeAll
  public static void init() {
    ServiceLocator locator = ServiceLocatorUtilities.bind(new TestBinder());
    furnitureTypeUCC = locator.getService(FurnitureTypeUCC.class);

    mockFurnitureTypeDAO = locator.getService(FurnitureTypeDAO.class);
    mockDal = locator.getService(ConnectionDalServices.class);

    mockFurnitureTypeDTO1 = Mockito.mock(FurnitureTypeImpl.class);
    mockFurnitureTypeDTO2 = Mockito.mock(FurnitureTypeImpl.class);
  }

  @BeforeEach
  public void setUp() {
    Mockito.reset(mockFurnitureTypeDAO);
    Mockito.reset(mockDal);
    Mockito.reset(mockFurnitureTypeDTO1);
    Mockito.reset(mockFurnitureTypeDTO2);

    Mockito.when(mockFurnitureTypeDTO1.getTypeId()).thenReturn(defaultTypeId1);
    Mockito.when(mockFurnitureTypeDTO2.getTypeId()).thenReturn(defaultTypeId2);

    Mockito.when(mockFurnitureTypeDTO1.getTypeName()).thenReturn(defaultTypeName1);
    Mockito.when(mockFurnitureTypeDTO2.getTypeName()).thenReturn(defaultTypeName2);
  }

  @DisplayName("TEST FurnitureTypeUCC.findAll : nominal, should return List of DTOs")
  @Test
  void test_findAll_nominal_shouldReturnDtoLst() {
    Mockito.when(mockFurnitureTypeDAO.findAll())
        .thenReturn(Arrays.asList(mockFurnitureTypeDTO1, mockFurnitureTypeDTO2));

    assertEquals(
        Arrays.asList(mockFurnitureTypeDTO1, mockFurnitureTypeDTO2),
        furnitureTypeUCC.findAll(),
        "A valid call to findAll should return the corresponding list of DTO");

    InOrder inOrder = Mockito.inOrder(mockDal, mockFurnitureTypeDAO);

    inOrder.verify(mockDal).startTransaction();
    inOrder.verify(mockFurnitureTypeDAO).findAll();
    inOrder.verify(mockDal).commitTransaction();
    inOrder.verifyNoMoreInteractions();
    inOrder.verify(mockDal, Mockito.never()).rollbackTransaction();
  }

  @DisplayName("TEST FurnitureTypeUCC.findAll : catches InternalError, should throw it back")
  @Test
  void test_findAll_catchesInternal_shouldThrowInternal() {
    Mockito.when(mockFurnitureTypeDAO.findAll())
        .thenThrow(new InternalError());

    assertThrows(InternalError.class, () ->
        furnitureTypeUCC.findAll(),
        "A call to findAll catching InternalError should throw it back");

    InOrder inOrder = Mockito.inOrder(mockDal, mockFurnitureTypeDAO);

    inOrder.verify(mockDal).startTransaction();
    inOrder.verify(mockFurnitureTypeDAO).findAll();
    inOrder.verify(mockDal).rollbackTransaction();
    inOrder.verifyNoMoreInteractions();
    inOrder.verify(mockDal, Mockito.never()).commitTransaction();
  }
}