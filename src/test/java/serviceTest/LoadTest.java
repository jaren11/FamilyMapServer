package serviceTest;

import DataAccess.DataAccessException;
import DataAccess.Database;
import Model.Event;
import Model.Person;
import Model.User;
import Service.ClearService;
import Service.LoadService;
import Service.Request.LoadRequest;
import Service.Result.LoadResult;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.util.ArrayList;

public class LoadTest {
    private Database db = new Database();

    @BeforeEach
    public void setUp() throws DataAccessException {
        ClearService clearService = new ClearService();
        clearService.clear();
    }

    @AfterEach
    public void tearDown()
    {
        ClearService clearService = new ClearService();
        clearService.clear();
    }


    @Test
    public void loadInfo() throws DataAccessException, SQLException {
        Event eventOne = new Event("yes", "no", "false", 1000, 4000,"m","tokyo", "death", 1969);
        Event eventTwo = new Event("no", "no", "can", 999, 3333,"stuff","yessir", "more death", 1900);
        Event eventThree = new Event("nope", "no", "yup", 494, 1029304,"not America","not New York", "birth", 1870);
        Event eventFour = new Event("1010", "yes", "whocares", 4293, 4059309,"Iraq","1234", "death", 1400);

        Person personOne = new Person("102-", "no", "false", "john", "doe","m","1234", null);
        Person personTwo = new Person("1-39", "no","Jack","Frost","f",null,null,null);
        Person personThree = new Person("123", "no","jenny","F.","x",null,"yup",null);
        Person personFour = new Person("2345", "yes","jenny","F.","x",null,"yup",null);

        User userOne = new User("yes", "no", "false", "john", "doe","m","1234");
        User userTwo = new User("no", "whe", "whawaha", "jack", "nabbit","m","54321");

        ArrayList<Event> eventArray = new ArrayList<Event>();
        eventArray.add(eventOne);
        eventArray.add(eventTwo);
        eventArray.add(eventThree);
        eventArray.add(eventFour);

        ArrayList<Person> personArray = new ArrayList<Person>();
        personArray.add(personOne);
        personArray.add(personTwo);
        personArray.add(personThree);
        personArray.add(personFour);

        ArrayList<User> userArray = new ArrayList<User>();
        userArray.add(userOne);
        userArray.add(userTwo);

        LoadRequest loadRequest = new LoadRequest(userArray, personArray, eventArray);
        LoadService loadService = new LoadService();
        LoadResult loadResult = loadService.load(loadRequest);

        Assertions.assertEquals(loadResult.getMessage(), "Successfully added 2 users, 4 persons, and 4 events.");

    }
}
