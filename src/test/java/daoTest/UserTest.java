package daoTest;

import DataAccess.DataAccessException;
import DataAccess.Database;
import DataAccess.UserDao;
import Model.User;
import org.junit.jupiter.api.*;

public class UserTest {
    private UserDao userDao;
    private User emptyUser;
    private User fullUser;
    private String theUsername;
    private Database db = new Database();

    //Setup stuff
    @BeforeEach
    public void setUp() throws DataAccessException{
        userDao = new UserDao(db.openConnection());
        emptyUser = new User(null,"","","","","","");;
        fullUser = new User("jaren11", "hahaha101", "jaren11@gmail.com", "Jaren", "Garff", "m", "qlwikdufgkqj2u");
        theUsername = "jaren11";
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        db.closeConnection(false);
    }

    //Positive insert test
    @Test
    public void positiveInsert() throws DataAccessException {
        //Makes sure no exceptions are thrown with insertion of a normal User object
        Assertions.assertDoesNotThrow(()->{userDao.insert(fullUser);});
        User theUser = userDao.find(theUsername);
        Assertions.assertEquals(fullUser, theUser);
    }

    //Negative insert test
    @Test
    public void negativeInsert() throws DataAccessException {
        userDao.insert(fullUser);
        //Inserts the same User object twice
        Assertions.assertThrows(DataAccessException.class, ()->{userDao.insert(fullUser);});
        //Inserts with null primary key
        Assertions.assertThrows(DataAccessException.class, ()->{userDao.insert(emptyUser);});
    }

    //Positive find test
    @Test
    public void findAfterInsert() throws DataAccessException {
        userDao.insert(fullUser);
        userDao.insert(new User("ManTheman", "imtheman101", "man@gmail.com", "Man", "Theman", "f", "128437tytidfsuvyg"));
        //Finds one specific user from a list of 2
        User theUser = userDao.find(theUsername);
        Assertions.assertEquals(fullUser, theUser);
    }

    //Negative find test
    @Test
    public void negativeFindAfterInsert() throws DataAccessException {
        userDao.insert(fullUser);
        userDao.insert(new User("ManTheman", "imtheman101", "man@gmail.com", "Man", "Theman", "f", "128437tytidfsuvyg"));
        //Tries to find a user not in the DB
        Assertions.assertNull(userDao.find("butts"));
        //Tries to find a user with a null primary key
        Assertions.assertNull(userDao.find(null));
    }

    //Clear test
    @Test
    public void clearTable() throws DataAccessException {
        userDao.insert(fullUser);
        userDao.insert(new User("wqfwe", "qiwle", "asf@gmm.com", "SIWAdjf", "asdf", "qw4", "ue"));
        //clears table after two insert calls
        userDao.clear();
        Assertions.assertNull(userDao.find(theUsername));
    }
}
