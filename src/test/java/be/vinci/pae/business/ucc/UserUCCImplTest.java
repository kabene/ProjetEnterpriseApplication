package be.vinci.pae.business.ucc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import be.vinci.pae.business.dto.AddressDTO;
import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.business.pojos.AddressesImpl;
import be.vinci.pae.business.pojos.User;
import be.vinci.pae.business.pojos.UserImpl;
import be.vinci.pae.exceptions.TakenException;
import be.vinci.pae.main.TestBinder;
import be.vinci.pae.persistence.dao.AddressDAO;
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

  private static User mockUser;
  private static AddressDTO mockAddressDTO;
  private static UserUCC userUCC;
  private static UserDAO mockUserDAO;
  private static AddressDAO mockAddressDAO;

  /**
   * Initialise test variable and dependency injection.
   */
  @BeforeAll
  public static void init() {
    Configurate.load("properties/test.properties");
    ServiceLocator locator = ServiceLocatorUtilities.bind(new TestBinder());
    userUCC = locator.getService(UserUCC.class);

    mockUser = Mockito.mock(UserImpl.class);
    mockAddressDTO = Mockito.mock(AddressesImpl.class);
    mockUserDAO = locator.getService(UserDAO.class);
    mockAddressDAO = locator.getService(AddressDAO.class);
  }


  /**
   * Reset all mocks between tests.
   */
  @BeforeEach
  public void setUp() {
    Mockito.reset(mockUser);
    Mockito.reset(mockAddressDTO);
    Mockito.reset(mockUserDAO);
    Mockito.reset(mockAddressDAO);
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

  @DisplayName("TEST UserUCC.register : given valid fields, should return matching UserDTO")
  @Test
  public void test_register_success() {
    String email = "email@gmail.com";
    String username = "username";
    String blancPwd = "pwd";
    String hashPwd = "hash";
    int addressId = 0;

    Mockito.when(mockUser.getEmail()).thenReturn(email);
    Mockito.when(mockUser.getUsername()).thenReturn(username);
    Mockito.when(mockUser.getPassword()).thenReturn(blancPwd);
    Mockito.when(mockUser.hashPassword(blancPwd)).thenReturn(hashPwd);

    Mockito.when(mockUserDAO.usernameAlreadyTaken(username)).thenReturn(false);
    Mockito.when(mockUserDAO.emailAlreadyTaken(email)).thenReturn(false);
    Mockito.when(mockUserDAO.findByUsername(username)).thenReturn(mockUser);

    Mockito.when(mockAddressDAO.getId(mockAddressDTO)).thenReturn(addressId);

    UserDTO actual = userUCC.register(mockUser, mockAddressDTO);

    Mockito.verify(mockUser, Mockito.atLeastOnce()).getUsername();
    Mockito.verify(mockUser, Mockito.atLeastOnce()).getEmail();
    Mockito.verify(mockUser).getPassword();
    Mockito.verify(mockUser).hashPassword(blancPwd);
    Mockito.verify(mockUser).setPassword(hashPwd);

    Mockito.verify(mockUserDAO).usernameAlreadyTaken(username);
    Mockito.verify(mockUserDAO).emailAlreadyTaken(email);
    Mockito.verify(mockUserDAO).register(mockUser, addressId);
    Mockito.verify(mockUserDAO).findByUsername(username);

    Mockito.verify(mockAddressDAO).addAddress(mockAddressDTO);
    Mockito.verify(mockAddressDAO).getId(mockAddressDTO);

    assertEquals(mockUser, actual, "The successful register should return the UserDTO");
  }

  @DisplayName("TEST UserUCC.register : given already existing username, should throw TakenException")
  @Test
  public void test_register_givenTakenUsername_shouldThrowTakenException() {
    String username = "takenUser";

    Mockito.when(mockUser.getUsername()).thenReturn(username);
    Mockito.when(mockUserDAO.usernameAlreadyTaken(username)).thenReturn(true);

    assertThrows(TakenException.class, () ->
            userUCC.register(mockUser, mockAddressDTO),
        "The call to register should throw a TakenException when given a taken username");

    Mockito.verify(mockUserDAO).usernameAlreadyTaken(username);
  }

  @DisplayName("TEST UserUCC.register : given already existing email, should throw TakenException")
  @Test
  public void test_register_givenTakenEmail_shouldThrowTakenException() {
    String username = "username";
    String email = "taken@gmail.com";

    Mockito.when(mockUser.getUsername()).thenReturn(username);
    Mockito.when(mockUser.getEmail()).thenReturn(email);
    Mockito.when(mockUserDAO.usernameAlreadyTaken(username)).thenReturn(false);
    Mockito.when(mockUserDAO.emailAlreadyTaken(email)).thenReturn(true);

    assertThrows(TakenException.class, () ->
            userUCC.register(mockUser, mockAddressDTO),
        "The call to register should throw a TakenException when given a taken email");

    Mockito.verify(mockUserDAO).usernameAlreadyTaken(username);
  }
}