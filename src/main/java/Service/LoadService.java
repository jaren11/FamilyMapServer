package Service;

import DataAccess.*;
import Model.AuthToken;
import Model.Event;
import Model.Person;
import Model.User;
import Service.Request.LoadRequest;
import Service.Result.LoadResult;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * This class contains methods for clearing then loading data into the database.
 */
public class LoadService {
    private Database db;
    private AuthTokenDao authTokenDao;
    private EventDao eventDao;
    private PersonDao personDao;
    private UserDao userDao;
    private String eventMessage;
    private String peopleMessage;
    private String userMessage;

    private AuthToken authToken;

    public LoadService() throws DataAccessException, SQLException {
        db = new Database();
        db.clearAllTables();
        authTokenDao = new AuthTokenDao(db.getConnection());
        userDao = new UserDao(db.getConnection());
        personDao = new PersonDao(db.getConnection());
        eventDao = new EventDao(db.getConnection());
        authToken = new AuthToken();
        eventMessage = new String();
        peopleMessage = new String();
        userMessage = new String();
    }

    /**
     * Loads information into the database.
     * @param r
     * @return LoadResult object that contains either an error or success message.
     */
    public LoadResult load(LoadRequest r) {

        try {
            if (!inputCheck(r)){
                db.closeConnection(false);
                return new LoadResult("Error: Invalid input parameters");
            }
            userMessage = "Done";
            ArrayList<User> theUsers = r.getUsers();

            for (int i = 0; i < theUsers.size(); i++){
                userDao.insert(theUsers.get(i));
            }

            eventMessage = "Done";
            ArrayList<Event> theEvents = r.getEvents();
            for (int i = 0; i < theEvents.size(); i++){
                eventDao.insert(theEvents.get(i));
            }

            peopleMessage = "Done";
            ArrayList<Person> thePersons = r.getPersons();

            for (int i = 0; i < thePersons.size(); i++){
                personDao.insert(thePersons.get(i));
            }

            if(!eventMessage.equals("Done")){
                db.closeConnection(false);
                return new LoadResult(eventMessage);
            } else if (!peopleMessage.equals("Done")){
                db.closeConnection(false);
                return new LoadResult(peopleMessage);
            } else if (!userMessage.equals("Done")){
                db.closeConnection(false);
                return new LoadResult(userMessage);
            } else {
                db.closeConnection(true);
                return new LoadResult("Successfully added " + r.getUsers().size() + " users, " + r.getPersons().size() + " persons, and " + r.getEvents().size() + " events.", true);
            }

        }
        catch (DataAccessException databaseError){
            try{
                db.closeConnection(false);
            } catch (DataAccessException ex) {
                ex.printStackTrace();
            }
            return new LoadResult("Error: Database Error", false);
        }
    }

    private boolean eventValidity(ArrayList<Event> eventArray)
    {
        for (int i = 0; i < eventArray.size(); i++){
            Event currEvent = eventArray.get(i);
            if (currEvent.getEventID() == null || currEvent.getAssociatedUsername() == null || currEvent.getPersonID() == null ||
                    currEvent.getCity() == null || currEvent.getCountry() == null || currEvent.getEventType() == null) {

                return false;
            }
        }
        return true;
    }

    private boolean personValidity(ArrayList<Person> personArray)
    {
        for (int i = 0; i < personArray.size(); i++){
            Person thePerson = personArray.get(i);
            if (thePerson.getPersonID() == null || thePerson.getAssociatedUsername() == null || thePerson.getFirstName() == null ||
                    thePerson.getLastName() == null || thePerson.getGender() == null) {

                return false;
            }
        }
        return true;
    }

    private boolean userValidity(ArrayList<User> users)
    {
        for (int i = 0; i < users.size(); i++){
            User user = users.get(i);
            if (user.getUsername() == null || user.getPassword() == null || user.getFirstName() == null ||
                    user.getLastName() == null || user.getEmail() == null || user.getGender() == null ||
                    user.getPersonID() == null) {

                return false;
            }
        }
        return true;
    }

    private boolean inputCheck(LoadRequest r)
    {
        return (userValidity(r.getUsers()) &&
                personValidity(r.getPersons()) &&
                eventValidity(r.getEvents()));
    }
}
