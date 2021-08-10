package serviceTest;

import DataAccess.AuthTokenDao;
import DataAccess.DataAccessException;
import DataAccess.Database;
import DataAccess.EventDao;
import Model.AuthToken;
import Model.Event;
import Service.ClearService;
import Service.EventService;
import Service.Result.EventResult;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.Timestamp;
import java.util.ArrayList;

public class EventTest {
    private EventService eventService;
    private AuthTokenDao authTokenDao;
    private Database db = new Database();
    private Event eventOne;
    private Event eventTwo;
    private Event eventThree;

    //Setup stuff
    @BeforeEach
    public void setUp() throws DataAccessException {
        ClearService clearService = new ClearService();
        clearService.clear();
        authTokenDao = new AuthTokenDao(db.getConnection());
        AuthToken theToken = new AuthToken("1234", "jaren11");
        authTokenDao.insert(theToken);
        authTokenDao.insert(new AuthToken("10", "nonexistant", new Timestamp(System.currentTimeMillis())));
    }

    //closes connection
    @AfterEach
    public void tearDown() {
        ClearService clearService = new ClearService();
        clearService.clear();
    }

    @Test
    public void allEventsPass() throws DataAccessException {
        EventDao eventDao = new EventDao(db.getConnection());
        eventOne = new Event("8oe7wy4t", "jaren11", "qc7o987a", 90, 54, "USA", "Danville", "Birth", 1996);
        eventTwo = new Event("23645", "jaren11", "3b46573", 90, 54, "USA", "Danville", "Birth", 1324);
        eventThree = new Event("efdgh345", "nn", "345ygh", 90, 54, "USA", "Danville", "Birth", 1675);
        eventDao.insert(eventOne);
        eventDao.insert(eventTwo);
        eventDao.insert(eventThree);
        db.closeConnection(true);

        eventService = new EventService();

        //search for an eventResult with the correct authToken
        EventResult eventResult = eventService.event("1234");

        Assertions.assertNotNull(eventResult.getData());
        Assertions.assertEquals(eventResult.getData().size(), 2);

        ArrayList<Event> arrayEvents = new ArrayList<>();
        arrayEvents.add(eventOne);
        arrayEvents.add(eventTwo);
        arrayEvents.add(eventThree);

        for(int i = 0; i < arrayEvents.size()-1; i++){
            Assertions.assertEquals(arrayEvents.get(i).getAssociatedUsername(), eventResult.getData().get(i).getAssociatedUsername());
        }
    }

    @Test
    public void allEventsFail() throws DataAccessException {
        EventDao eventDao = new EventDao(db.getConnection());
        eventOne = new Event("8oe7wy4t", "jaren11", "qc7o987a", 90, 54, "USA", "Danville", "Birth", 1996);
        eventTwo = new Event("23645", "jaren11", "3b46573", 90, 54, "USA", "Danville", "Birth", 1324);
        eventThree = new Event("efdgh345", "nn", "345ygh", 90, 54, "USA", "Danville", "Birth", 1675);
        eventDao.insert(eventOne);
        eventDao.insert(eventTwo);
        eventDao.insert(eventThree);
        db.closeConnection(true);

        eventService = new EventService();

        //search for an eventResult with the incorrect authToken
        EventResult eventResult = eventService.event("q4t12");

        Assertions.assertNull(eventResult.getData());
    }

}
