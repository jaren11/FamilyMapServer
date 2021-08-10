package serviceTest;

import DataAccess.AuthTokenDao;
import DataAccess.DataAccessException;
import DataAccess.Database;
import DataAccess.PersonDao;
import Model.AuthToken;
import Model.Person;
import Service.ClearService;
import Service.PersonService;
import Service.Result.PersonResult;
import org.junit.jupiter.api.*;

import java.util.ArrayList;

public class PersonTest {
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
    public void allPersons() throws DataAccessException {
        PersonDao personDao = new PersonDao(db.getConnection());
        Person personOne = new Person("yes", "no", "false", "john", "doe", "m", "1234", null);
        Person personTwo = new Person("no", "no", "Jack", "Frost", "f", null, null, null);
        Person personThree = new Person("123", "no", "jenny", "F.", "x", null, "yup", null);
        Person personFour = new Person("1010", "false", "jenny", "F.", "x", null, "yup", null);

        personDao.insert(personOne);
        personDao.insert(personTwo);
        personDao.insert(personThree);
        personDao.insert(personFour);
        db.closeConnection(true);

        PersonService personService = new PersonService();
        PersonResult personResults = personService.person("1234");

        Assertions.assertNotNull(personResults.getData());
        Assertions.assertEquals(personResults.getData().size(), 3);
        Assertions.assertEquals(null, personResults.getMessage());

        ArrayList<Person> personArray = new ArrayList<Person>();
        personArray.add(personOne);
        personArray.add(personTwo);
        personArray.add(personThree);

        for (int i = 0; i < personArray.size(); i++) {
            Assertions.assertEquals(personArray.get(i).getAssociatedUsername(), personResults.getData().get(i).getAssociatedUsername());
        }
    }

}
