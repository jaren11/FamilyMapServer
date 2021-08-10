package daoTest;

import DataAccess.DataAccessException;
import DataAccess.Database;
import DataAccess.PersonDao;
import Model.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PersonTest {
    private PersonDao personDao;
    private Person emptyPerson;
    private Person fullPerson;
    private String theID;
    private Database db = new Database();

    @BeforeEach
    public void setUp() throws DataAccessException {
        personDao = new PersonDao(db.openConnection());
        emptyPerson = new Person(null,"","","","","","","");
        fullPerson = new Person("goal", "jaren11", "Jaren", "Garff", "m", "1432r2v3", "sdfwyy2345", "q89w376i8");
        theID = "goal";
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        db.closeConnection(false);
    }

    @org.junit.jupiter.api.Test
    public void positiveInsert() throws DataAccessException {
        //Makes sure no exceptions are thrown with insertion of a normal Person object
        Assertions.assertDoesNotThrow(()->{personDao.insert(fullPerson);});
        Person thePerson = personDao.find(theID);
        Assertions.assertEquals(fullPerson, thePerson);
    }

    @org.junit.jupiter.api.Test
    public void negativeInsert() throws DataAccessException {
        personDao.insert(fullPerson);
        //Inserts the same User object twice
        Assertions.assertThrows(DataAccessException.class, ()->{personDao.insert(fullPerson);});
        //Inserts with null primary key
        Assertions.assertThrows(DataAccessException.class, ()->{personDao.insert(fullPerson);});
    }

    @org.junit.jupiter.api.Test
    public void findAfterInsert() throws DataAccessException {
        //Inserts 2 people
        personDao.insert(fullPerson);
        personDao.insert(new Person("aijwshfgk", "ManTheman", "Man", "Theman", "f", "qawer4t25", "34yrt", "fs53w45"));
        //Finds one specific person from a list of 2
        Person thePerson = personDao.find(theID);
        Assertions.assertEquals(fullPerson, thePerson);
    }

    @org.junit.jupiter.api.Test
    public void negativeFindAfterInsert() throws DataAccessException {
        personDao.insert(fullPerson);
        personDao.insert(new Person("aijwshfgk", "ManTheman", "Man", "Theman", "f", "qawer4t25", "34yrt", "fs53w45"));
        //Tries to find a person not in the DB
        Assertions.assertNull(personDao.find("butts"));
        //Tries to find a person with a null primary key
        Assertions.assertNull(personDao.find(null));
    }

    @Test
    public void clearTable() throws DataAccessException {
        personDao.insert(fullPerson);
        personDao.insert(new Person("aijwshfgk", "ManTheman", "Man", "Theman", "f", "qawer4t25", "34yrt", "fs53w45"));
        personDao.clear();
        Assertions.assertNull(personDao.find("awtreqv324523"));
    }
}
