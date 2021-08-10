package serviceTest;

import DataAccess.AuthTokenDao;
import DataAccess.DataAccessException;
import DataAccess.Database;
import DataAccess.EventDao;
import Model.AuthToken;
import Model.Event;
import Service.ClearService;
import Service.EventIDService;
import Service.Result.EventIDResult;
import org.junit.jupiter.api.*;
import java.sql.Timestamp;

public class EventIDTest {
    private EventIDService eventIDService;
    private Database db = new Database();
    private String theID;
    private AuthTokenDao authTokenDao;

    //Setup stuff
    @BeforeEach
    public void setUp() throws DataAccessException {
        ClearService clearService = new ClearService();
        clearService.clear();
        theID = "2v553b";
        authTokenDao = new AuthTokenDao(db.getConnection());
        AuthToken theToken = new AuthToken("1234", "jaren11");
        authTokenDao.insert(theToken);
        authTokenDao.insert(new AuthToken("10", "nonexistant",  new Timestamp(System.currentTimeMillis())));
    }

    //Closes connection
    @AfterEach
    public void tearDown() {
        ClearService clearService = new ClearService();
        clearService.clear();
    }

    @Test
    public void findEventPass() throws DataAccessException {
        EventDao eventDao = new EventDao(db.getConnection());
        Event testEvent = new Event("2v553b", "jaren11", "45bh45b", 2000, 1000,"USA","Danville", "Birth", 1996);
        eventDao.insert(testEvent);
        db.closeConnection(true);

        eventIDService = new EventIDService();

        //search for an eventIDResult with the correct authToken
        EventIDResult theResult = eventIDService.eventIDResult(theID, "1234");

        Assertions.assertNotNull(theResult.getEventID());
        Assertions.assertNotNull(theResult.getAssociatedUsername());
        Assertions.assertNotNull(theResult.getPersonID());
        Assertions.assertNotNull(theResult.getCity());
        Assertions.assertNotNull(theResult.getCountry());
        Assertions.assertNotNull(theResult.getEventType());
        Assertions.assertEquals(1996, theResult.getYear());
        Assertions.assertEquals(2000, theResult.getLatitude(), .5);
        Assertions.assertEquals(1000, theResult.getLongitude(), .5);
        Assertions.assertEquals(theResult.getAssociatedUsername(), "jaren11");
        Assertions.assertTrue(theResult.isSuccess());
    }

    @Test
    public void findEventFail() throws DataAccessException {
        EventDao eventDao = new EventDao(db.getConnection());
        Event testEvent = new Event("2v553b", "jaren11", "45bh45b", 2000, 1000,"USA","Danville", "Birth", 1996);
        eventDao.insert(testEvent);
        db.closeConnection(true);

        eventIDService = new EventIDService();

        //search for an eventIDResult with the wrong authToken
        EventIDResult theResult = eventIDService.eventIDResult(theID, "12356");

        Assertions.assertNull(theResult.getEventID());
        Assertions.assertNull(theResult.getAssociatedUsername());
        Assertions.assertNull(theResult.getPersonID());
        Assertions.assertNull(theResult.getCity());
        Assertions.assertNull(theResult.getCountry());
        Assertions.assertNull(theResult.getEventType());
        Assertions.assertNotEquals(1996, theResult.getYear());
        Assertions.assertNotEquals(2000, theResult.getLatitude(), .5);
        Assertions.assertNotEquals(1000, theResult.getLongitude(), .5);
        Assertions.assertNotEquals(theResult.getAssociatedUsername(), "jaren11");
        Assertions.assertFalse(theResult.isSuccess());
    }
}
