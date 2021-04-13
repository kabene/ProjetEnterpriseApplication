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
import be.vinci.pae.exceptions.UnauthorizedException;
import be.vinci.pae.main.TestBinder;
import be.vinci.pae.persistence.dal.ConnectionDalServices;
import be.vinci.pae.persistence.dao.AddressDAO;
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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.Mockito;

public class UserUCCImplTest {

  private static User mockUser1;
  private static User mockUser2;
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
    ServiceLocator locator = ServiceLocatorUtilities.bind(new TestBinder());
    userUCC = locator.getService(UserUCC.class);

    mockUser1 = Mockito.mock(UserImpl.class);
    mockUser2 = Mockito.mock(UserImpl.class);
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
    Mockito.reset(mockUser1);
    Mockito.reset(mockUser2);
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
    Mockito.when(mockUserDAO.findByUsername(username)).thenReturn(mockUser1);
    Mockito.when(mockUser1.checkPassword(pwd)).thenReturn(false);

    assertThrows(ForbiddenException.class,
        () -> userUCC.login(username, pwd),
        "UserUCC.login should throw ForbiddenException after being given a wrong password");

    Mockito.verify(mockUserDAO).findByUsername(username);
    Mockito.verify(mockUser1).checkPassword(pwd);
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
    Mockito.when(mockUserDAO.findByUsername(username)).thenReturn(mockUser1);
    Mockito.when(mockUser1.getId()).thenReturn(0);
    Mockito.when(mockUser1.checkPassword(pwd)).thenReturn(true);

    UserDTO actual = userUCC.login(username, pwd);

    Mockito.verify(mockUserDAO).findByUsername(username);
    Mockito.verify(mockUser1).checkPassword(pwd);
    assertEquals(mockUser1, actual,
        "UserUCC.login should return the good UserDTO if the credentials are valid");

    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal).commitTransaction();
    Mockito.verify(mockDal, Mockito.never()).rollbackTransaction();
  }

  @DisplayName("TEST UserUCC.login : login by a user waiting"
      + " for confirmation, should throw UnauthorizedException")
  @Test
  public void test_login_byWaitingUser_shouldThrowUnauthorizedException() {
    final String username = "userInWaiting";
    final String pwd = "goodPwd";
    Mockito.when(mockUserDAO.findByUsername(username)).thenReturn(mockUser1);
    Mockito.when(mockUser1.getId()).thenReturn(0);
    Mockito.when(mockUser1.isWaiting()).thenReturn(true);

    assertThrows(UnauthorizedException.class,
        () -> userUCC.login(username, pwd),
        "UserUCC.login should throw UnauthorizedException"
            + "as long as the user is waiting for confirmation");

    Mockito.verify(mockUserDAO).findByUsername(username);
    Mockito.verify(mockUser1, Mockito.never()).checkPassword(pwd);
    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal, Mockito.never()).commitTransaction();
    Mockito.verify(mockDal).rollbackTransaction();
  }

  @DisplayName("TEST UserUCC.login : DAO throws InternalError"
      + ", Should rollback and throw InternalError")
  @Test
  public void test_login_InternalErrorThrown_shouldThrowInternalErrorAndRollback() {
    String username = "username";
    String password = "password";

    Mockito.when(mockUserDAO.findByUsername(username)).thenThrow(new InternalError("some error"));

    assertThrows(InternalError.class, () -> userUCC.login(username, password),
        "If the DAO throws an exception, it should be thrown back");

    Mockito.verify(mockUserDAO).findByUsername(username);
    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal).rollbackTransaction();
    Mockito.verify(mockDal, Mockito.never()).commitTransaction();
  }

  /**
   * Test.
   * @param role : role.
   */
  @DisplayName("TEST UserUCC.register : given valid fields, should return matching UserDTO")
  @ParameterizedTest
  @ValueSource(strings = {"customer", "antique_dealer", "admin"})
  public void test_register_success(String role) {
    String email = "email@gmail.com";
    String username = "username";
    String blancPwd = "pwd";
    String hashPwd = "hash";
    int addressId = 0;

    Mockito.when(mockUser1.getRole()).thenReturn(role);
    Mockito.when(mockUser1.getEmail()).thenReturn(email);
    Mockito.when(mockUser1.getUsername()).thenReturn(username);
    Mockito.when(mockUser1.getPassword()).thenReturn(blancPwd);
    Mockito.when(mockUser1.hashPassword(blancPwd)).thenReturn(hashPwd);

    Mockito.when(mockUserDAO.usernameAlreadyTaken(username)).thenReturn(false);
    Mockito.when(mockUserDAO.emailAlreadyTaken(email)).thenReturn(false);
    Mockito.when(mockUserDAO.findByUsername(username)).thenReturn(mockUser1);

    Mockito.when(mockAddressDAO.getId(mockAddressDTO)).thenReturn(addressId);

    UserDTO actual = userUCC.register(mockUser1, mockAddressDTO);
    assertEquals(mockUser1, actual, "The successful register should return the UserDTO");

    Mockito.verify(mockUser1).setWaiting(!role.equals("customer"));
    Mockito.verify(mockUser1, Mockito.atLeastOnce()).getUsername();
    Mockito.verify(mockUser1, Mockito.atLeastOnce()).getEmail();
    Mockito.verify(mockUser1).getPassword();
    Mockito.verify(mockUser1).hashPassword(blancPwd);
    Mockito.verify(mockUser1).setPassword(hashPwd);

    Mockito.verify(mockUserDAO).usernameAlreadyTaken(username);
    Mockito.verify(mockUserDAO).emailAlreadyTaken(email);
    Mockito.verify(mockUserDAO).register(mockUser1, addressId);
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

    Mockito.when(mockUser1.getUsername()).thenReturn(username);
    Mockito.when(mockUserDAO.usernameAlreadyTaken(username)).thenReturn(true);

    assertThrows(ConflictException.class, () ->
            userUCC.register(mockUser1, mockAddressDTO),
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
    Mockito.when(mockUser1.getUsername()).thenReturn(username);
    Mockito.when(mockUser1.getEmail()).thenReturn(email);
    Mockito.when(mockUserDAO.usernameAlreadyTaken(username)).thenReturn(false);
    Mockito.when(mockUserDAO.emailAlreadyTaken(email)).thenReturn(true);

    assertThrows(ConflictException.class, () ->
            userUCC.register(mockUser1, mockAddressDTO),
        "The call to register should throw a ConflictException when given a taken email");
    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockUserDAO).usernameAlreadyTaken(username);
    Mockito.verify(mockUserDAO).emailAlreadyTaken(email);
    Mockito.verify(mockDal).rollbackTransaction();
    Mockito.verify(mockDal, Mockito.never()).commitTransaction();

  }

  @DisplayName("TEST UserUCC.getAll : DAO throws InternalError,"
      + " should rollback and throw InternalError")
  @Test
  public void test_register_InternalErrorThrown1_shouldThrowInternalErrorAndRollback() {
    String username = "username";
    String email = "email";

    Mockito.when(mockUser1.getUsername()).thenReturn(username);
    Mockito.when(mockUser1.getEmail()).thenReturn(email);

    Mockito.when(mockUserDAO.usernameAlreadyTaken(username)).thenThrow(new InternalError());

    assertThrows(InternalError.class, () -> userUCC.register(mockUser1, mockAddressDTO),
        "If the DAO throws an exception, it should be thrown back");

    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal).rollbackTransaction();
    Mockito.verify(mockDal, Mockito.never()).commitTransaction();
  }

  @DisplayName("TEST UserUCC.getAll : DAO throws InternalError,"
      + " should rollback and throw InternalError")
  @Test
  public void test_register_InternalErrorThrown2_shouldThrowInternalErrorAndRollback() {
    String username = "username";
    String email = "email";

    Mockito.when(mockUser1.getUsername()).thenReturn(username);
    Mockito.when(mockUser1.getEmail()).thenReturn(email);

    Mockito.when(mockUserDAO.usernameAlreadyTaken(username)).thenReturn(false);
    Mockito.when(mockUserDAO.emailAlreadyTaken(email)).thenThrow(new InternalError());

    assertThrows(InternalError.class, () -> userUCC.register(mockUser1, mockAddressDTO),
        "If the DAO throws an exception, it should be thrown back");

    Mockito.verify(mockUserDAO).usernameAlreadyTaken(username);

    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal).rollbackTransaction();
    Mockito.verify(mockDal, Mockito.never()).commitTransaction();
  }

  @DisplayName("TEST UserUCC.getAll : given nothing,"
      + " should return all Users")
  @Test
  public void test_getAll_shouldReturnListOfAllUsers() {
    List<UserDTO> allUsers = Arrays.asList(mockUser1, mockUser2);
    Mockito.when(mockUserDAO.findAll()).thenReturn(allUsers);
    assertEquals(allUsers, userUCC.getAll(),
        "UserUCC.getAll should return a List<UserDTO> of all users");
    Mockito.verify(mockUserDAO).findAll();
    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal).commitTransaction();
    Mockito.verify(mockDal, Mockito.never()).rollbackTransaction();
  }

  @DisplayName("TEST UserUCC.getAll : given nothing,"
      + " should return empty list of Users")
  @Test
  public void test_getAll_emptyDB_shouldReturnEmptyListOfUsers() {
    List<UserDTO> emptyList = new ArrayList<>();
    Mockito.when(mockUserDAO.findAll()).thenReturn(emptyList);
    assertEquals(emptyList, userUCC.getAll(),
        "UserUCC.getAll should return a empty List<UserDTO> of all users");
    Mockito.verify(mockUserDAO).findAll();
    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal).commitTransaction();
    Mockito.verify(mockDal, Mockito.never()).rollbackTransaction();
  }

  @DisplayName("TEST UserUCC.getAll : DAO throws InternalError,"
      + " should rollback and throw InternalError")
  @Test
  public void test_getAll_InternalErrorThrown_shouldThrowInternalErrorAndRollback() {
    Mockito.when(mockUserDAO.findAll()).thenThrow(new InternalError());

    assertThrows(InternalError.class, () -> userUCC.getAll(),
        "If the DAO throws an exception, it should be thrown back");

    Mockito.verify(mockUserDAO).findAll();
    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal).rollbackTransaction();
    Mockito.verify(mockDal, Mockito.never()).commitTransaction();
  }

  @DisplayName("TEST UserUCC.getAllWaiting : given nothing,"
      + " should return all waiting Users")
  @Test
  public void test_getAllWaiting_shouldReturnListOfAllWaitingUsers() {
    List<UserDTO> allWaitingUsers = Arrays.asList(mockUser1, mockUser2);
    Mockito.when(mockUserDAO.getAllWaitingUsers()).thenReturn(allWaitingUsers);
    assertEquals(allWaitingUsers, userUCC.getAllWaiting(),
        "UserUCC.getAllWaiting should return a List<UserDTO> of all waiting users");
    Mockito.verify(mockUserDAO).getAllWaitingUsers();
    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal).commitTransaction();
    Mockito.verify(mockDal, Mockito.never()).rollbackTransaction();
  }

  @DisplayName("TEST UserUCC.getAllWaiting : given nothing,"
      + " should return empty list of Users")
  @Test
  public void test_getAllWaiting_noUserWaiting_shouldReturnEmptyListOfUsers() {
    List<UserDTO> emptyList = new ArrayList<>();
    Mockito.when(mockUserDAO.getAllWaitingUsers()).thenReturn(emptyList);
    assertEquals(emptyList, userUCC.getAllWaiting(),
        "UserUCC.getAllWaiting should return a empty List<UserDTO> of user");
    Mockito.verify(mockUserDAO).getAllWaitingUsers();
    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal).commitTransaction();
    Mockito.verify(mockDal, Mockito.never()).rollbackTransaction();
  }

  @DisplayName("TEST UserUCC.getAllWaiting : DAO throws InternalError,"
      + " should rollback and throw InternalError")
  @Test
  public void test_getAllWaiting_InternalErrorThrown_shouldThrowInternalErrorAndRollback() {
    Mockito.when(mockUserDAO.getAllWaitingUsers()).thenThrow(new InternalError());

    assertThrows(InternalError.class, () -> userUCC.getAllWaiting(),
        "If the DAO throws an exception, it should be thrown back");

    Mockito.verify(mockUserDAO).getAllWaitingUsers();
    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal).rollbackTransaction();
    Mockito.verify(mockDal, Mockito.never()).commitTransaction();
  }

  @DisplayName("TEST UserUCC.getConfirmed : given nothing,"
      + " should return all confirmed Users")
  @Test
  public void test_getAllConfirmed_shouldReturnListOfAllConfirmedUsers() {
    List<UserDTO> allConfirmedUsers = Arrays.asList(mockUser1, mockUser2);
    Mockito.when(mockUserDAO.getAllConfirmedUsers()).thenReturn(allConfirmedUsers);
    assertEquals(allConfirmedUsers, userUCC.getAllConfirmed(),
        "UserUCC.getAllConfirmed should return a List<UserDTO> of all confirmed users");
    Mockito.verify(mockUserDAO).getAllConfirmedUsers();
    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal).commitTransaction();
    Mockito.verify(mockDal, Mockito.never()).rollbackTransaction();
  }

  @DisplayName("TEST UserUCC.getAllConfirmed : given nothing,"
      + " should return empty list of Users")
  @Test
  public void test_getAllConfirmed_noUserConfirmed_shouldReturnEmptyListOfUsers() {
    List<UserDTO> emptyList = new ArrayList<>();
    Mockito.when(mockUserDAO.getAllConfirmedUsers()).thenReturn(emptyList);
    assertEquals(emptyList, userUCC.getAllConfirmed(),
        "UserUCC.getAllConfirmed should return a empty List<UserDTO> of user");
    Mockito.verify(mockUserDAO).getAllConfirmedUsers();
    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal).commitTransaction();
    Mockito.verify(mockDal, Mockito.never()).rollbackTransaction();
  }

  @DisplayName("TEST UserUCC.getAllConfirmed : DAO throws InternalError,"
      + " should rollback and throw InternalError")
  @Test
  public void test_getAllConfirmed_InternalErrorThrown_shouldThrowInternalErrorAndRollback() {
    Mockito.when(mockUserDAO.getAllConfirmedUsers()).thenThrow(new InternalError());

    assertThrows(InternalError.class, () -> userUCC.getAllConfirmed(),
        "If the DAO throws an exception, it should be thrown back");

    Mockito.verify(mockUserDAO).getAllConfirmedUsers();
    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal).rollbackTransaction();
    Mockito.verify(mockDal, Mockito.never()).commitTransaction();
  }

  @DisplayName("TEST UserUCC.getSearchResult : given an existing username,"
      + " should return respective user")
  @Test
  public void test_getSearchResult_givenExistingUsername_shouldReturnUser() {
    String username = "existingUsername";
    List<UserDTO> allSearchResults = Arrays.asList(mockUser1);

    Mockito.when(mockUserDAO.findBySearch(username)).thenReturn(allSearchResults);
    assertEquals(allSearchResults, userUCC.getSearchResult(username),
        "UserUCC.getSearchResult should return a List<UserDTO> with respective user");
    Mockito.verify(mockUserDAO).findBySearch(username);
    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal).commitTransaction();
    Mockito.verify(mockDal, Mockito.never()).rollbackTransaction();
  }

  @DisplayName("TEST UserUCC.getSearchResult : given a not existing username,"
      + " should return empty list of Users")
  @Test
  public void test_getSearchResult_givenNotExistingUsername_shouldReturnEmptyListOfUsers() {
    String username = "notExistingUsername";
    List<UserDTO> emptyList = new ArrayList<>();

    Mockito.when(mockUserDAO.findBySearch(username)).thenReturn(emptyList);
    assertEquals(emptyList, userUCC.getSearchResult(username),
        "UserUCC.getSearchResult should return a empty List<UserDTO> of all users");
    Mockito.verify(mockUserDAO).findBySearch(username);
    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal).commitTransaction();
    Mockito.verify(mockDal, Mockito.never()).rollbackTransaction();
  }

  @DisplayName("TEST UserUCC.getSearchResult : DAO throws InternalError,"
      + " Should rollback and throw InternalError")
  @Test
  public void test_getSearchResult_InternalErrorThrown_shouldThrowInternalErrorAndRollback() {
    String pattern = "pattern";

    Mockito.when(mockUserDAO.findBySearch(pattern)).thenThrow(new InternalError("some error"));

    assertThrows(InternalError.class, () -> userUCC.getSearchResult(pattern),
        "If the DAO throws an exception, it should be thrown back");

    Mockito.verify(mockUserDAO).findBySearch(pattern);
    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal).rollbackTransaction();
    Mockito.verify(mockDal, Mockito.never()).commitTransaction();
  }

  /**
   * Unit Test.
   */
  @DisplayName("TEST UserUCC.getOne : nominal, should return DTO")
  @Test
  public void test_getOne_givenValidId_shouldReturnDTO() {
    int userId = 1;
    int addressId = 1;

    Mockito.when(mockUserDAO.findById(userId)).thenReturn(mockUser1);

    Mockito.when(mockUser1.getAddressId()).thenReturn(addressId);

    Mockito.when(mockAddressDAO.findById(addressId)).thenReturn(mockAddressDTO);

    assertEquals(userUCC.getOne(userId), mockUser1, "getOne should"
        + " return the corresponding DTO");

    Mockito.verify(mockUser1).setAddress(mockAddressDTO);

    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal, Mockito.never()).rollbackTransaction();
    Mockito.verify(mockDal).commitTransaction();
  }

  @DisplayName("TEST UserUCC.getOne : nominal, should return DTO")
  @Test
  public void test_getOne_givenInvalidId_shouldThrowNotFoundException() {
    int userId = 1;

    Mockito.when(mockUserDAO.findById(userId)).thenThrow(new NotFoundException());

    assertThrows(NotFoundException.class, () -> userUCC.getOne(userId),
        "getOne should return the corresponding DTO");

    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal, Mockito.never()).commitTransaction();
    Mockito.verify(mockDal).rollbackTransaction();
  }

  @DisplayName("TEST UserUCC.getOne : DAO throws InternalError, "
      + "Should rollback and throw InternalError")
  @Test
  public void test_getOne_InternalErrorThrown_shouldThrowInternalErrorAndRollback() {
    int userId = 1;

    Mockito.when(mockUserDAO.findById(userId)).thenThrow(new InternalError());

    assertThrows(InternalError.class, () -> userUCC.getOne(userId),
        "getOne should return the corresponding DTO");

    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal, Mockito.never()).commitTransaction();
    Mockito.verify(mockDal).rollbackTransaction();
  }

  /**
   * Unit Test.
   */
  @DisplayName("TEST UserUCC.validateUser : nominal, return DTO")
  @ParameterizedTest
  @ValueSource(booleans = {true, false})
  public void test_validateUser_givenValidId_shouldReturnDTO(boolean value) {
    int userId = 1;

    Mockito.when(mockUserDAO.findById(userId)).thenReturn(mockUser1);

    assertEquals(mockUser1, userUCC.validateUser(userId, value),
        "A valid call should return the corresponding DTO");

    Mockito.verify(mockUserDAO).setRole(userId, value);
    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal, Mockito.never()).rollbackTransaction();
    Mockito.verify(mockDal).commitTransaction();
  }

  /**
   * Unit Test..
   */
  @DisplayName("TEST UserUCC.validateUser : invalid id, throw NotFoundException")
  @ParameterizedTest
  @ValueSource(booleans = {true, false})
  public void test_validateUser_givenInvalidId_shouldThrowNotFound(boolean value) {
    int userId = 1;

    Mockito.when(mockUserDAO.findById(userId)).thenThrow(new NotFoundException());

    assertThrows(NotFoundException.class, () -> userUCC.validateUser(userId, value),
        "an invalid id should throw NotFoundException");

    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal).rollbackTransaction();
    Mockito.verify(mockDal, Mockito.never()).commitTransaction();
  }

  /**
   * Unit Test..
   */
  @DisplayName("TEST UserUCC.validateUser : catch InternalError 1")
  @ParameterizedTest
  @ValueSource(booleans = {true, false})
  public void test_validateUser_catchesInternalError1_shouldThrowNotFound(boolean value) {
    int userId = 1;

    Mockito.when(mockUserDAO.findById(userId)).thenThrow(new InternalError());

    assertThrows(InternalError.class, () -> userUCC.validateUser(userId, value),
        "an invalid id should throw NotFoundException");

    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal).rollbackTransaction();
    Mockito.verify(mockDal, Mockito.never()).commitTransaction();
  }

  /**
   * Unit Test..
   */
  @DisplayName("TEST UserUCC.validateUser : catch InternalError 2")
  @ParameterizedTest
  @ValueSource(booleans = {true, false})
  public void test_validateUser_catchesInternalError2_shouldThrowNotFound(boolean value) {
    int userId = 1;

    Mockito.doThrow(new InternalError()).when(mockUserDAO).setRole(userId, value);

    assertThrows(InternalError.class, () -> userUCC.validateUser(userId, value),
        "an invalid id should throw NotFoundException");

    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal).rollbackTransaction();
    Mockito.verify(mockDal, Mockito.never()).commitTransaction();
  }
}