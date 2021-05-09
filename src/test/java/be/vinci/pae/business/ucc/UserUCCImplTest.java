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
import org.mockito.Mockito;

public class UserUCCImplTest {

  private static User mockUser1;
  private static User mockUser2;
  private static AddressDTO mockAddressDTO;
  private static UserUCC userUCC;
  private static UserDAO mockUserDAO;
  private static AddressDAO mockAddressDAO;
  private static ConnectionDalServices mockDal;

  private static final int defaultUserId1 = 1;
  private static final int defaultUserId2 = 2;
  private static final String defaultUsername1 = "user1";
  private static final String defaultUsername2 = "user2";
  private static final String defaultPwd1 = "pwd1";
  private static final String defaultPwd2 = "pwd2";
  private static final String defaultEmail1 = "email1";
  private static final String defaultEmail2 = "email2";
  private static final String defaultRole = "customer";


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

    Mockito.when(mockUser1.getId()).thenReturn(defaultUserId1);
    Mockito.when(mockUser1.getUsername()).thenReturn(defaultUsername1);
    Mockito.when(mockUser1.getPassword()).thenReturn(defaultPwd1);
    Mockito.when(mockUser1.getEmail()).thenReturn(defaultEmail1);
    Mockito.when(mockUser1.getRole()).thenReturn(defaultRole);
    Mockito.when(mockUser1.isWaiting()).thenReturn(false);

    Mockito.when(mockUser2.getId()).thenReturn(defaultUserId2);
    Mockito.when(mockUser2.getUsername()).thenReturn(defaultUsername2);
    Mockito.when(mockUser2.getPassword()).thenReturn(defaultPwd2);
    Mockito.when(mockUser2.getEmail()).thenReturn(defaultEmail2);
    Mockito.when(mockUser2.getRole()).thenReturn(defaultRole);
    Mockito.when(mockUser2.isWaiting()).thenReturn(false);

    Mockito.when(mockUserDAO.findById(defaultUserId1)).thenReturn(mockUser1);
    Mockito.when(mockUserDAO.findById(defaultUserId2)).thenReturn(mockUser2);
    Mockito.when(mockUserDAO.findByUsername(defaultUsername1)).thenReturn(mockUser1);
    Mockito.when(mockUserDAO.findByUsername(defaultUsername2)).thenReturn(mockUser2);
  }

  @DisplayName("TEST UserUCC.login : given bad password, should throw ForbiddenException")
  @Test
  public void test_login_givenBadPassword_shouldThrowForbidden() {
    Mockito.when(mockUser1.checkPassword(defaultPwd1)).thenReturn(false);

    assertThrows(ForbiddenException.class,
        () -> userUCC.login(defaultUsername1, defaultPwd1),
        "UserUCC.login should throw ForbiddenException after being given a wrong password");

    Mockito.verify(mockUserDAO).findByUsername(defaultUsername1);
    Mockito.verify(mockUser1).checkPassword(defaultPwd1);

    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal, Mockito.never()).commitTransaction();
    Mockito.verify(mockDal).rollbackTransaction();
  }

  @DisplayName("TEST UserUCC.login : given bad username, should throw ForbiddenException")
  @Test
  public void test_login_givenBadUsername_shouldReturnNull() {
    Mockito.when(mockUserDAO.findByUsername(defaultUsername1)).thenThrow(NotFoundException.class);

    assertThrows(ForbiddenException.class,
        () -> userUCC.login(defaultUsername1, defaultPwd1),
        "UserUCC.login should throw ForbiddenException after being given"
            + " a username not present in the database");

    Mockito.verify(mockUserDAO).findByUsername(defaultUsername1);

    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal, Mockito.never()).commitTransaction();
    Mockito.verify(mockDal).rollbackTransaction();
  }

  @DisplayName("TEST UserUCC.login : given valid credentials, should return matching UserDTO")
  @Test
  public void test_login_givenGoodUsernameAndGoodPassword_shouldReturnUser() {
    Mockito.when(mockUser1.checkPassword(defaultPwd1)).thenReturn(true);

    UserDTO actual = userUCC.login(defaultUsername1, defaultPwd1);

    Mockito.verify(mockUserDAO).findByUsername(defaultUsername1);
    Mockito.verify(mockUser1).checkPassword(defaultPwd1);
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
    Mockito.when(mockUser1.isWaiting()).thenReturn(true);

    assertThrows(UnauthorizedException.class,
        () -> userUCC.login(defaultUsername1, defaultPwd1),
        "UserUCC.login should throw UnauthorizedException"
            + "as long as the user is waiting for confirmation");

    Mockito.verify(mockUserDAO).findByUsername(defaultUsername1);
    Mockito.verify(mockUser1, Mockito.never()).checkPassword(defaultPwd1);
    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal, Mockito.never()).commitTransaction();
    Mockito.verify(mockDal).rollbackTransaction();
  }

  @DisplayName("TEST UserUCC.login : DAO throws InternalError"
      + ", Should rollback and throw InternalError")
  @Test
  public void test_login_InternalErrorThrown_shouldThrowInternalErrorAndRollback() {
    Mockito.when(mockUserDAO.findByUsername(defaultUsername1))
        .thenThrow(new InternalError("some error"));

    assertThrows(InternalError.class, () -> userUCC.login(defaultUsername1, defaultPwd1),
        "If the DAO throws an exception, it should be thrown back");

    Mockito.verify(mockUserDAO).findByUsername(defaultUsername1);
    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal).rollbackTransaction();
    Mockito.verify(mockDal, Mockito.never()).commitTransaction();
  }

  /**
   * Test.
   *
   * @param role : role.
   */
  @DisplayName("TEST UserUCC.register : given valid fields, should return matching UserDTO")
  @ParameterizedTest
  @ValueSource(strings = {"customer", "antique_dealer", "admin"})
  public void test_register_success(String role) {
    int addressId = 0;
    String hashPwd = "hash";

    Mockito.when(mockUser1.getRole()).thenReturn(role);
    Mockito.when(mockUser1.hashPassword(defaultPwd1)).thenReturn(hashPwd);

    Mockito.when(mockUserDAO.usernameAlreadyTaken(defaultUsername1)).thenReturn(false);
    Mockito.when(mockUserDAO.emailAlreadyTaken(defaultEmail1)).thenReturn(false);

    Mockito.when(mockAddressDAO.getId(mockAddressDTO)).thenReturn(addressId);

    UserDTO actual = userUCC.register(mockUser1, mockAddressDTO);
    assertEquals(mockUser1, actual, "The successful register should return the UserDTO");

    Mockito.verify(mockUser1).setWaiting(!role.equals("customer"));
    Mockito.verify(mockUser1, Mockito.atLeastOnce()).getUsername();
    Mockito.verify(mockUser1, Mockito.atLeastOnce()).getEmail();
    Mockito.verify(mockUser1).getPassword();
    Mockito.verify(mockUser1).hashPassword(defaultPwd1);
    Mockito.verify(mockUser1).setPassword(hashPwd);

    Mockito.verify(mockUserDAO).usernameAlreadyTaken(defaultUsername1);
    Mockito.verify(mockUserDAO).emailAlreadyTaken(defaultEmail1);
    Mockito.verify(mockUserDAO).register(mockUser1, addressId);
    Mockito.verify(mockUserDAO).findByUsername(defaultUsername1);

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
    Mockito.when(mockUserDAO.usernameAlreadyTaken(defaultUsername1)).thenReturn(true);

    assertThrows(ConflictException.class, () ->
            userUCC.register(mockUser1, mockAddressDTO),
        "The call to register should throw a ConflictException when given a taken username");

    Mockito.verify(mockUserDAO).usernameAlreadyTaken(defaultUsername1);

    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal).rollbackTransaction();
    Mockito.verify(mockDal, Mockito.never()).commitTransaction();
  }

  @DisplayName("TEST UserUCC.register : given already existing email,"
      + " should throw ConflictException")
  @Test
  public void test_register_givenTakenEmail_shouldThrowTakenException() {
    Mockito.when(mockUserDAO.usernameAlreadyTaken(defaultUsername1)).thenReturn(false);
    Mockito.when(mockUserDAO.emailAlreadyTaken(defaultEmail1)).thenReturn(true);

    assertThrows(ConflictException.class, () ->
            userUCC.register(mockUser1, mockAddressDTO),
        "The call to register should throw a ConflictException when given a taken email");
    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockUserDAO).usernameAlreadyTaken(defaultUsername1);
    Mockito.verify(mockUserDAO).emailAlreadyTaken(defaultEmail1);
    Mockito.verify(mockDal).rollbackTransaction();
    Mockito.verify(mockDal, Mockito.never()).commitTransaction();

  }

  @DisplayName("TEST UserUCC.getAll : DAO throws InternalError,"
      + " should rollback and throw InternalError")
  @Test
  public void test_register_InternalErrorThrown1_shouldThrowInternalErrorAndRollback() {
    Mockito.when(mockUserDAO.usernameAlreadyTaken(defaultUsername1)).thenThrow(new InternalError());

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
    Mockito.when(mockUserDAO.usernameAlreadyTaken(defaultUsername1)).thenReturn(false);
    Mockito.when(mockUserDAO.emailAlreadyTaken(defaultEmail1)).thenThrow(new InternalError());

    assertThrows(InternalError.class, () -> userUCC.register(mockUser1, mockAddressDTO),
        "If the DAO throws an exception, it should be thrown back");

    Mockito.verify(mockUserDAO).usernameAlreadyTaken(defaultUsername1);

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
    String pattern = "user";
    List<UserDTO> allSearchResults = Arrays.asList(mockUser1, mockUser2);

    Mockito.when(mockUserDAO.findBySearch(pattern)).thenReturn(allSearchResults);
    assertEquals(allSearchResults, userUCC.getSearchResult(pattern),
        "UserUCC.getSearchResult should return a List<UserDTO> with respective user");
    Mockito.verify(mockUserDAO).findBySearch(pattern);
    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal).commitTransaction();
    Mockito.verify(mockDal, Mockito.never()).rollbackTransaction();
  }

  @DisplayName("TEST UserUCC.getSearchResult : given a not existing username,"
      + " should return empty list of Users")
  @Test
  public void test_getSearchResult_givenNotExistingUsername_shouldReturnEmptyListOfUsers() {
    String pattern = "pattern";
    List<UserDTO> emptyList = new ArrayList<>();

    Mockito.when(mockUserDAO.findBySearch(pattern)).thenReturn(emptyList);
    assertEquals(emptyList, userUCC.getSearchResult(pattern),
        "UserUCC.getSearchResult should return a empty List<UserDTO> of all users");
    Mockito.verify(mockUserDAO).findBySearch(pattern);
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
    int addressId = 1;

    Mockito.when(mockUser1.getAddressId()).thenReturn(addressId);
    Mockito.when(mockAddressDAO.findById(addressId)).thenReturn(mockAddressDTO);

    assertEquals(userUCC.getOne(defaultUserId1), mockUser1, "getOne should"
        + " return the corresponding DTO");

    Mockito.verify(mockUser1).setAddress(mockAddressDTO);

    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal, Mockito.never()).rollbackTransaction();
    Mockito.verify(mockDal).commitTransaction();
  }

  @DisplayName("TEST UserUCC.getOne : nominal, should return DTO")
  @Test
  public void test_getOne_givenInvalidId_shouldThrowNotFoundException() {
    Mockito.when(mockUserDAO.findById(defaultUserId1)).thenThrow(new NotFoundException());

    assertThrows(NotFoundException.class, () -> userUCC.getOne(defaultUserId1),
        "getOne should return the corresponding DTO");

    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal, Mockito.never()).commitTransaction();
    Mockito.verify(mockDal).rollbackTransaction();
  }

  @DisplayName("TEST UserUCC.getOne : DAO throws InternalError, "
      + "Should rollback and throw InternalError")
  @Test
  public void test_getOne_InternalErrorThrown_shouldThrowInternalErrorAndRollback() {
    Mockito.when(mockUserDAO.findById(defaultUserId1)).thenThrow(new InternalError());

    assertThrows(InternalError.class, () -> userUCC.getOne(defaultUserId1),
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
    Mockito.when(mockUserDAO.findById(defaultUserId1)).thenReturn(mockUser1);

    assertEquals(mockUser1, userUCC.validateUser(defaultUserId1, value),
        "A valid call should return the corresponding DTO");

    Mockito.verify(mockUserDAO).updateRole(defaultUserId1, value);
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
    Mockito.when(mockUserDAO.findById(defaultUserId1)).thenThrow(new NotFoundException());

    assertThrows(NotFoundException.class, () -> userUCC.validateUser(defaultUserId1, value),
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
    Mockito.when(mockUserDAO.findById(defaultUserId1)).thenThrow(new InternalError());

    assertThrows(InternalError.class, () -> userUCC.validateUser(defaultUserId1, value),
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
    Mockito.doThrow(new InternalError()).when(mockUserDAO).updateRole(defaultUserId1, value);

    assertThrows(InternalError.class, () -> userUCC.validateUser(defaultUserId1, value),
        "an invalid id should throw NotFoundException");

    Mockito.verify(mockDal).startTransaction();
    Mockito.verify(mockDal).rollbackTransaction();
    Mockito.verify(mockDal, Mockito.never()).commitTransaction();
  }
}