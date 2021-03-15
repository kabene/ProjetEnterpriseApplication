package be.vinci.pae.business.ucc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.business.pojos.User;
import be.vinci.pae.business.pojos.UserImpl;
import be.vinci.pae.main.TestBinder;
import be.vinci.pae.persistence.dao.UserDAO;
import be.vinci.pae.utils.Configurate;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class UserUCCImplTest {

  private static UserUCC userUCC;
  private static User mockUser;
  private static UserDAO mockUserDAO;

  /**
   * Initialise test variable and dependency injection.
   */
  @BeforeAll
  public static void init() {
    Configurate.load("properties/test.properties");
    ServiceLocator locator = ServiceLocatorUtilities.bind(new TestBinder());
    userUCC = locator.getService(UserUCC.class);
    mockUserDAO = locator.getService(UserDAO.class);
    mockUser = Mockito.mock(UserImpl.class);
  }


  @BeforeEach
  public void setUp() {
    Mockito.reset(mockUser);
    Mockito.reset(mockUserDAO);
  }

  @DisplayName("TEST UserUCC.login : given bad password, should return null")
  @Test
  public void test_login_givenBadPassword_shouldReturnNull() {
    String username = "ex";
    String pwd = "badPwd";
    Mockito.when(mockUserDAO.findByUsername(username)).thenReturn(mockUser);
    Mockito.when(mockUser.checkPassword(pwd)).thenReturn(false);

    UserDTO actual = userUCC.login(username, pwd);

    Mockito.verify(mockUserDAO).findByUsername(username);
    Mockito.verify(mockUser).checkPassword(pwd);
    assertNull(actual, "UserUCC.login should return null after being given a wrong password");
  }

  @DisplayName("TEST UserUCC.login : given bad username, should return null")
  @Test
  public void test_login_givenBadUsername_shouldReturnNull() {
    String username = "other";
    String pwd = "azerty";
    Mockito.when(mockUserDAO.findByUsername(username)).thenReturn(null);

    UserDTO actual = userUCC.login(username, pwd);

    Mockito.verify(mockUserDAO).findByUsername(username);
    assertNull(actual,
        "UserUCC.login should return null after being given"
            + " a username not present in the database");
  }

  @DisplayName("TEST UserUCC.login : given valid credentials, should return matching UserDTO")
  @Test
  public void test_login_givenGoodUsernameAndGoodPassword_shouldReturnUser() {
    String username = "goodUsername";
    String pwd = "goodPwd";
    Mockito.when(mockUserDAO.findByUsername(username)).thenReturn(mockUser);
    Mockito.when(mockUser.getID()).thenReturn(0);
    Mockito.when(mockUser.checkPassword(pwd)).thenReturn(true);

    UserDTO actual = userUCC.login(username, pwd);

    Mockito.verify(mockUserDAO).findByUsername(username);
    Mockito.verify(mockUser).checkPassword(pwd);
    assertEquals(mockUser, actual,
        "UserUCC.login should return the good UserDTO if the credentials are valid");
  }


}