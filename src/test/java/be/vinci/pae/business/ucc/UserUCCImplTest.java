package be.vinci.pae.business.ucc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import be.vinci.pae.business.dto.AddressDTO;
import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.business.pojos.AddressImpl;
import be.vinci.pae.business.pojos.User;
import be.vinci.pae.business.pojos.UserImpl;
import be.vinci.pae.exceptions.ConflictException;
import be.vinci.pae.exceptions.ForbiddenException;
import be.vinci.pae.exceptions.NotFoundException;
import be.vinci.pae.main.TestBinder;
import be.vinci.pae.persistence.dal.ConnectionDalServices;
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
  private static ConnectionDalServices mockDal;

  /**
   * Initialise test variable and dependency injection.
   */
  @BeforeAll
  public static void init() {
    Configurate.load("properties/test.properties");
    ServiceLocator locator = ServiceLocatorUtilities.bind(new TestBinder());
    userUCC = locator.getService(UserUCC.class);

    mockUser = Mockito.mock(UserImpl.class);
    mockAddressDTO = Mockito.mock(AddressImpl.class);
    mockUserDAO = locator.getService(UserDAO.class);
    mockAddressDAO = locator.getService(AddressDAO.class);
    mockDal = locator.getService(ConnectionDalServices.class);

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
    Mockito.reset(mockDal);
  }

  @DisplayName("TEST UserUCC.login : given bad password, should throw ForbiddenException")
  @Test
  public void test_login_givenBadPassword_shouldThrowForbidden() {
    String username = "ex";
    String pwd = "badPwd";
    Mockito.when(mockUserDAO.findByUsername(username)).thenReturn(mockUser);
    Mockito.when(mockUser.checkPassword(pwd)).thenReturn(false);

    assertThrows(ForbiddenException.class,
        () -> userUCC.login(username, pwd),
        "UserUCC.login should throw ForbiddenException after being given a wrong password");

    Mockito.verify(mockUserDAO).findByUsername(username);
    Mockito.verify(mockUser).checkPassword(pwd);
    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal, Mockito.never()).commitTransaction();
    Mockito.verify(mockDal).rollbackTransaction();
  }

  @DisplayName("TEST UserUCC.login : given bad username, should throw ForbiddenException")
  @Test
  public void test_login_givenBadUsername_shouldReturnNull() {
    String username = "other";
    String pwd = "azerty";
    Mockito.when(mockUserDAO.findByUsername(username)).thenThrow(NotFoundException.class);

    assertThrows(ForbiddenException.class,
        () -> userUCC.login(username, pwd),
        "UserUCC.login should throw ForbiddenException after being given"
            + " a username not present in the database");

    Mockito.verify(mockUserDAO).findByUsername(username);

    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal, Mockito.never()).commitTransaction();
    Mockito.verify(mockDal).rollbackTransaction();
  }

  @DisplayName("TEST UserUCC.login : given valid credentials, should return matching UserDTO")
  @Test
  public void test_login_givenGoodUsernameAndGoodPassword_shouldReturnUser() {
    String username = "goodUsername";
    String pwd = "goodPwd";
    Mockito.when(mockUserDAO.findByUsername(username)).thenReturn(mockUser);
    Mockito.when(mockUser.getId()).thenReturn(0);
    Mockito.when(mockUser.checkPassword(pwd)).thenReturn(true);

    UserDTO actual = userUCC.login(username, pwd);

    Mockito.verify(mockUserDAO).findByUsername(username);
    Mockito.verify(mockUser).checkPassword(pwd);
    assertEquals(mockUser, actual,
        "UserUCC.login should return the good UserDTO if the credentials are valid");

    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal).commitTransaction();
    Mockito.verify(mockDal, Mockito.never()).rollbackTransaction();
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
    assertEquals(mockUser, actual, "The successful register should return the UserDTO");

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

    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal).commitTransaction();
    Mockito.verify(mockDal, Mockito.never()).rollbackTransaction();
  }

  @DisplayName("TEST UserUCC.register : given already "
      + "existing username, should throw ConflictException")
  @Test
  public void test_register_givenTakenUsername_shouldThrowTakenException() {
    String username = "takenUser";

    Mockito.when(mockUser.getUsername()).thenReturn(username);
    Mockito.when(mockUserDAO.usernameAlreadyTaken(username)).thenReturn(true);

    assertThrows(ConflictException.class, () ->
            userUCC.register(mockUser, mockAddressDTO),
        "The call to register should throw a ConflictException when given a taken username");

    Mockito.verify(mockUserDAO).usernameAlreadyTaken(username);

    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal).rollbackTransaction();
    Mockito.verify(mockDal, Mockito.never()).commitTransaction();
  }

  @DisplayName("TEST UserUCC.register : given already existing email,"
      + " should throw ConflictException")
  @Test
  public void test_register_givenTakenEmail_shouldThrowTakenException() {
    String username = "username";
    String email = "taken@gmail.com";
    Mockito.when(mockUser.getUsername()).thenReturn(username);
    Mockito.when(mockUser.getEmail()).thenReturn(email);
    Mockito.when(mockUserDAO.usernameAlreadyTaken(username)).thenReturn(false);
    Mockito.when(mockUserDAO.emailAlreadyTaken(email)).thenReturn(true);

    assertThrows(ConflictException.class, () ->
            userUCC.register(mockUser, mockAddressDTO),
        "The call to register should throw a ConflictException when given a taken email");
    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockUserDAO).usernameAlreadyTaken(username);
    Mockito.verify(mockUserDAO).emailAlreadyTaken(email);
    Mockito.verify(mockDal).rollbackTransaction();
    Mockito.verify(mockDal, Mockito.never()).commitTransaction();

  }
}