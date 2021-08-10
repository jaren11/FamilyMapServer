package generateTest;

import DataAccess.DataAccessException;
import DataAccess.Database;
import Generate.GenerateEvent;
import Generate.GenerateName;
import Model.Event;
import Model.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EventTest {
    private GenerateEvent generateEvent;
    private Database db = new Database();
    private Person husband;
    private Person wife;
    private int currentYear = 1995;

    //Setup stuff
    @BeforeEach
    public void setUp() throws DataAccessException {
        husband = new Person("iugh", "jaren11", "Jaren", "Garff", "m", "bq2o873", "1982o7yfhis", "72839diu");
        db.openConnection();
        generateEvent = new GenerateEvent("jaren11");
    }

    //closes connection
    @AfterEach
    public void tearDown() throws DataAccessException {
        db.closeConnection(false);
    }

    @Test
    public void marriagePass(){
        generateEvent.marriage(husband, wife, currentYear);
        Event answer = generateEvent.arrayEvents.get(0);
        Assertions.assertEquals("iugh", answer.getPersonID());
        Assertions.assertEquals("Marriage", answer.getEventType());
        Assertions.assertNotNull(answer.getCity());
        Assertions.assertNotNull(answer.getCountry());
    }

    @Test
    public void deathPass(){
        generateEvent.death(husband, currentYear);
        Event answer = generateEvent.arrayEvents.get(0);
        Assertions.assertEquals("iugh", answer.getPersonID());
        Assertions.assertEquals("Death", answer.getEventType());
        Assertions.assertNotNull(answer.getCity());
        Assertions.assertNotNull(answer.getCountry());

    }

    @Test
    public void birthPass(){
        generateEvent.birth(husband, currentYear);
        Event answer = generateEvent.arrayEvents.get(0);
        Assertions.assertEquals("iugh", answer.getPersonID());
        Assertions.assertEquals("Birth", answer.getEventType());
        Assertions.assertNotNull(answer.getCity());
        Assertions.assertNotNull(answer.getCountry());
    }

    @Test
    public void randomPass(){
        generateEvent.otherEvent(husband, currentYear);
        Event answer = generateEvent.arrayEvents.get(0);
        Assertions.assertEquals("iugh", answer.getPersonID());
        Assertions.assertNotNull(answer.getEventType());
        Assertions.assertNotNull(answer.getCity());
        Assertions.assertNotNull(answer.getCountry());
    }

}
