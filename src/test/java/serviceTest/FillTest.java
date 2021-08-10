package serviceTest;


import DataAccess.AuthTokenDao;
import DataAccess.DataAccessException;
import DataAccess.Database;
import DataAccess.UserDao;
import Model.AuthToken;
import Model.User;
import Service.ClearService;
import Service.FillService;
import Service.PersonService;
import Service.Result.FillResult;
import Service.Result.PersonResult;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.Timestamp;

public class FillTest {
    private Database db = new Database();
    @BeforeEach
    public void setUp() throws DataAccessException {
        ClearService clearService = new ClearService();
        clearService.clear();

        AuthTokenDao authTokenDao = new AuthTokenDao(db.getConnection());
        AuthToken tokenOne = new AuthToken("jaren11", "1234", new Timestamp(System.currentTimeMillis()));
        tokenOne.setAuthToken("1234");
        authTokenDao.insert(tokenOne);
        authTokenDao.insert(new AuthToken("10", "nonexistant", new Timestamp(System.currentTimeMillis())));

        UserDao userDao = new UserDao(db.getConnection());
        User testUser = new User("jaren11", "hahaha101", "jaren11@gmail.com", "Jaren", "Garff","m","1234");
        userDao.insert(testUser);

        db.closeConnection(true);
    }

    @AfterEach
    public void tearDown() {
        ClearService clearService = new ClearService();
        clearService.clear();
    }

    @Test
    public void fill() throws DataAccessException
    {
        PersonService personService = new PersonService();
        PersonResult result = personService.person("1234");
        Assertions.assertNull(result.getData());
        Assertions.assertEquals(result.getMessage(), "Error: There are no people under user");

        FillService fillService = new FillService();
        FillResult fillResult = fillService.fill("jaren11", 4);

        personService = new PersonService();
        result = personService.person("1234");

        Assertions.assertNotNull(result.getData());
        Assertions.assertEquals(result.getData().size(), 31);
        Assertions.assertNotEquals(fillResult.getMessage(), "Error: User not found!");
        Assertions.assertNotEquals(fillResult.getMessage(), "Error: Number of Generations invalid");
        Assertions.assertNotEquals(fillResult.getMessage(), "Error");
    }
}
