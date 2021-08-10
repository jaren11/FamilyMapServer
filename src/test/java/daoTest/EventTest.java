package daoTest;

import DataAccess.DataAccessException;
import DataAccess.Database;
import DataAccess.EventDao;
import Model.Event;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EventTest {
    private EventDao eventDao;
    private Event emptyEvent;
    private Event fullEvent;
    private String theID;
    private Database db = new Database();

    @BeforeEach
    public void setUp() throws DataAccessException {
        eventDao = new EventDao(db.openConnection());
        emptyEvent = new Event(null,"","",0,0,"","","", 0);
        fullEvent = new Event("sdoa8f76b", "jaren11", "23v4terq", 23, 43, "gsedf", "wegr", "weg", 1956);
        theID = "sdoa8f76b";
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        db.closeConnection(false);
    }

    @Test
    public void positiveInsert() throws DataAccessException {
        //Makes sure no exceptions are thrown with insertion of a normal Person object
        Assertions.assertDoesNotThrow(()->{eventDao.insert(fullEvent);});
        Event theEvent = eventDao.find(theID);
        Assertions.assertEquals(fullEvent, theEvent);
    }

    @Test
    public void negativeInsert() throws DataAccessException {
        eventDao.insert(fullEvent);
        //Inserts the same User object twice
        Assertions.assertThrows(DataAccessException.class, ()->{eventDao.insert(fullEvent);});
        //Inserts with null primary key
        Assertions.assertThrows(DataAccessException.class, ()->{eventDao.insert(fullEvent);});
    }

    @Test
    public void findAfterInsert() throws DataAccessException {
        //Inserts 2 events
        eventDao.insert(fullEvent);
        eventDao.insert(new Event("sqdfaqefv", "ManTheman", "Man", 30, 34, "qawer4t25", "34yrt", "fs53w45", 1839));
        //Finds one specific event from a list of 2
        Event theEvent = eventDao.find(theID);
        Assertions.assertEquals(fullEvent, theEvent);
    }

    @Test
    public void negativeFindAfterInsert() throws DataAccessException {
        eventDao.insert(fullEvent);
        eventDao.insert(new Event("sqdfaqefv", "ManTheman", "Man", 30, 34, "qawer4t25", "34yrt", "fs53w45", 1839));
        //Tries to find a person not in the DB
        Assertions.assertNull(eventDao.find("butts"));
        //Tries to find a person with a null primary key
        Assertions.assertNull(eventDao.find(null));
    }

    @Test
    public void clearTable() throws DataAccessException {
        eventDao.insert(fullEvent);
        eventDao.insert(new Event("sqdfaqefv", "ManTheman", "Man", 30, 34, "qawer4t25", "34yrt", "fs53w45", 1839));
        eventDao.clear();
        Assertions.assertNull(eventDao.find("awtreqv324523"));
    }
}
