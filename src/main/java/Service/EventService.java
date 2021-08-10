package Service;

import DataAccess.AuthTokenDao;
import DataAccess.DataAccessException;
import DataAccess.Database;
import DataAccess.EventDao;
import Model.AuthToken;
import Model.Event;
import Service.Result.EventResult;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * This class contains methods to call the DAO for Events.
 */
public class EventService {
    private Database db;
    private EventDao eventDao;
    private AuthTokenDao authTokenDao;
    private ArrayList<Event> eventArray;

    public EventService() throws DataAccessException {
        db = new Database();
        eventDao = new EventDao(db.getConnection());
        authTokenDao = new AuthTokenDao(db.getConnection());
    }

    /**
     * This method gets the info of all the events from a single user from the database.
     * @param authToken
     * @return EventResult object that contains either an error or success message.
     */
    public EventResult event(String authToken){
        try {
            AuthToken theAuth = authTokenDao.find(authToken);
            if (theAuth == null) {
                db.closeConnection(false);
                return new EventResult("Error: Invalid Authorization Token");
            } else {
                //insert all events into eventArray
               eventArray = eventDao.findAllEvents(theAuth.getUsername());
                if (eventArray == null) {
                    db.closeConnection(false);
                    return new EventResult("Error: There are no events under user");
                } else {
                    EventResult result = new EventResult(eventArray, null, true);
                    db.closeConnection(true);
                    return result;
                }
            }
        } catch (DataAccessException | SQLException e) {
            try{
                db.closeConnection(false);
            } catch (DataAccessException ex) {
                ex.printStackTrace();
            }
            return new EventResult("Error: Problem with the database");
        }
    }
}
