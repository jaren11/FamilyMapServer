package serviceTest;

import DataAccess.AuthTokenDao;
import DataAccess.DataAccessException;
import DataAccess.Database;
import DataAccess.PersonDao;
import Model.AuthToken;
import Model.Person;
import Service.ClearService;
import Service.PersonIDService;
import Service.Result.PersonIDResult;
import org.junit.jupiter.api.*;

public class PersonIDTest {
    private Database db = new Database();
    @BeforeEach
    public void setUp() throws DataAccessException {
        ClearService clearService = new ClearService();
        clearService.clear();
        AuthTokenDao authTokenDao = new AuthTokenDao(db.getConnection());
        authTokenDao.insert(new AuthToken("1234", "no"));
        authTokenDao.insert(new AuthToken("10", "nonexistant"));
    }

    @AfterEach
    public void takeDown(){
        ClearService clearService = new ClearService();
        clearService.clear();
    }

    @Test
    public void personIDPass() throws DataAccessException //Find a single person from the database
    {
        PersonDao personDao = new PersonDao(db.getConnection());
        Person thePerson = new Person("yes", "no", "false", "john", "doe","m","1234", null);
        personDao.insert(thePerson);
        db.closeConnection(true);

        PersonIDService personIDService = new PersonIDService();
        PersonIDResult personIDResult = personIDService.personID( "yes","1234");

        Assertions.assertNotNull(personIDResult.getAssociatedUsername());
        Assertions.assertNotNull(personIDResult.getFatherID());
        Assertions.assertNotNull(personIDResult.getFirstName());
        Assertions.assertNotNull(personIDResult.getLastName());
        Assertions.assertNotNull(personIDResult.getGender());
        Assertions.assertNull(personIDResult.getMessage());
        Assertions.assertEquals(personIDResult.getAssociatedUsername(), "no");

    }
}
