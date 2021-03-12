package be.vinci.pae.business.ucc;

import static org.junit.jupiter.api.Assertions.*;

import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.business.pojos.UserImpl;
import be.vinci.pae.business.ucc.UserUCC;
import be.vinci.pae.business.ucc.UserUCCImpl;
import be.vinci.pae.main.ApplicationBinder;
import be.vinci.pae.utils.Configurate;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserUCCImplTest {

  private static UserUCC userUCC;
  private UserDTO user;

  @BeforeAll
  static void init() {
    Configurate.load("src/main/resources/test.properties");
    ServiceLocator locator = ServiceLocatorUtilities.bind(new ApplicationBinder());
    userUCC = locator.getService(UserUCC.class);
  }


  @BeforeEach
  void setUp() throws Exception {
    user = new UserImpl();
  }

  @Test
  void test_login_givenBadPassword_shouldReturnNull() {
    assertNull(userUCC.login("ex", "azertyu"));
  }

  @Test
  void test_login_givenBadUsername_shouldReturnNull() {
    assertNull(userUCC.login("Ba.", "noob"));
  }

  @Test
  void test_login_givenBadUsernameAndBadPassword_shouldReturnNull() {
    assertNull(userUCC.login("Notime", "inTrouble"));
  }

  @Test
  void test_login_givenGoodUsernameAndGoodPassword_shouldReturnUser() {
    user.setUsername("ex");
    user.setPassword("$2a$04$62XdSoqyDOBZWQCk/cuh1.OY/x3mnPi2wjcmDC0HCCzc7MVcj/VmW"); //hash for azerty
    user.setID(1);
    assertEquals(user.getID(), userUCC.login("ex", "azerty").getID());
  }


}