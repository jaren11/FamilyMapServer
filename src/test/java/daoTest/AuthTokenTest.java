package daoTest;

import DataAccess.AuthTokenDao;
import DataAccess.DataAccessException;
import DataAccess.Database;
import Model.AuthToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;

public class AuthTokenTest {
    private AuthTokenDao authToken;
    private AuthToken emptyAuth;
    private AuthToken fullAuth;
    private String theAuthToken;
    private Database db = new Database();

    //Setup stuff
    @BeforeEach
    public void setUp() throws DataAccessException {
        authToken = new AuthTokenDao(db.openConnection());
        emptyAuth = new AuthToken(null,null,null);;
        fullAuth = new AuthToken("jaren11", "hahaha101", new Timestamp(9));
        theAuthToken = "hahaha101";
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        db.closeConnection(false);
    }

    //Positive insert test
    @Test
    public void positiveInsert() throws DataAccessException {
        //Makes sure no exceptions are thrown with insertion of a normal AuthToken object
        Assertions.assertDoesNotThrow(()->{authToken.insert(fullAuth);});
        AuthToken theAuth = authToken.find(theAuthToken);
        Assertions.assertEquals(fullAuth, theAuth);
    }

    //Negative insert test
    @Test
    public void negativeInsert() throws DataAccessException {
        authToken.insert(fullAuth);
        //Inserts the same AuthToken object twice
        Assertions.assertThrows(DataAccessException.class, ()->{authToken.insert(fullAuth);});
        //Inserts with null primary key
        Assertions.assertThrows(DataAccessException.class, ()->{authToken.insert(emptyAuth);});
    }

    //Positive find test
    @Test
    public void findAfterInsert() throws DataAccessException {
        authToken.insert(fullAuth);
        authToken.insert(new AuthToken("jaaihws", "imtheman101", null));
        //Finds one specific AuthToken from a list of 2
        AuthToken theAuth = authToken.find(theAuthToken);
        Assertions.assertEquals(fullAuth, theAuth);
    }

    //Negative find test
    @Test
    public void negativeFindAfterInsert() throws DataAccessException {
        authToken.insert(fullAuth);
        authToken.insert(new AuthToken("jaaihws", "imtheman101", null));
        //Tries to find an AuthToken not in the DB
        Assertions.assertNull(authToken.find("butts"));
        //Tries to find an AuthToken with a null primary key
        Assertions.assertNull(authToken.find(null));
    }

    //Clear test
    @Test
    public void clearTable() throws DataAccessException {
        authToken.insert(fullAuth);
        authToken.insert(new AuthToken("jaaihws", "imtheman101", null));
        //clears table after two insert calls
        authToken.clear();
        Assertions.assertNull(authToken.find(theAuthToken));
    }
}
