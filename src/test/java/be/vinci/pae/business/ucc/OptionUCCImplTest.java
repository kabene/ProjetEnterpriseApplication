package be.vinci.pae.business.ucc;

import be.vinci.pae.business.dto.OptionDTO;
import be.vinci.pae.business.dto.PhotoDTO;
import be.vinci.pae.business.pojos.OptionImpl;
import be.vinci.pae.business.pojos.PhotoImpl;
import be.vinci.pae.main.TestBinder;
import be.vinci.pae.persistence.dal.ConnectionDalServices;
import be.vinci.pae.persistence.dao.OptionDAO;
import be.vinci.pae.persistence.dao.PhotoDAO;
import be.vinci.pae.utils.Configurate;
import javax.swing.text.html.Option;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class OptionUCCImplTest {

  private static OptionUCC optionUCC;
  private static OptionDAO mockOptionDAO;
  private static ConnectionDalServices mockDal;

  private static OptionDTO mockOptionDTO1;
  private static OptionDTO mockOptionDTO2;
  private static OptionDTO mockOptionDTO3;

  @BeforeEach
  public static void init(){
    Configurate.load("properties/test.properties");
    ServiceLocator locator = ServiceLocatorUtilities.bind(new TestBinder());

    optionUCC = locator.getService(OptionUCC.class);
    mockOptionDAO = locator.getService(OptionDAO.class);
    mockDal = locator.getService(ConnectionDalServices.class);

    mockOptionDTO1 = Mockito.mock(OptionImpl.class);
    mockOptionDTO2 = Mockito.mock(OptionImpl.class);
    mockOptionDTO3 = Mockito.mock(OptionImpl.class);

  }

  @BeforeEach
  void setUp() {
    Mockito.reset(mockOptionDAO);
    Mockito.reset(mockDal);
    Mockito.reset(mockOptionDTO1);
    Mockito.reset(mockOptionDTO2);
    Mockito.reset(mockOptionDTO3);
  }

  @Test
  void introduceOption() {
  }

  @Test
  void cancelOption() {
  }
}