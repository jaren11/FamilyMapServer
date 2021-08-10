package Service;

import DataAccess.AuthTokenDao;
import DataAccess.DataAccessException;
import DataAccess.Database;
import DataAccess.EventDao;
import Model.AuthToken;
import Model.Event;
import Service.Result.EventIDResult;

/**
 * This class contains methods to call the DAO for EventIDs.
 */
public class EventIDService {
    private Database db;
    private EventDao eventDao;
    private AuthTokenDao authTokenDao;

    public EventIDService() throws DataAccessException {
        db = new Database();
        eventDao = new EventDao(db.getConnection());
        authTokenDao = new AuthTokenDao(db.getConnection());
    }

    /**
     * This method gets the info of a single event from the database.
     * @param eventID
     * @param authToken
     * @return EventIDResult object that contains either an error or success message.
     */
    public EventIDResult eventIDResult(String eventID, String authToken)
    {
        try {
            //check to see if authtoken is found in DB
            AuthToken theAuth = authTokenDao.find(authToken);
            //if not, or if null
            if (theAuth == null) {
                db.closeConnection(false);
                return new EventIDResult("Error: Invalid Authorization Token");
            } else {
                //see if event is found in the DB
                Event theEvent = eventDao.find(eventID);
                //make sure it's there and that the authtoken's username is the same as the event's username
                if (theEvent == null) {
                    db.closeConnection(false);
                    return new EventIDResult("Error: Event not found");
                } else if (!theAuth.getUsername().equals(theEvent.getAssociatedUsername())) {
                    db.closeConnection(false);
                    return new EventIDResult("Error: AuthToken and Event usernames do not match");
                } else {
                    EventIDResult result = new EventIDResult(theEvent, theAuth.getUsername());
                    db.closeConnection(true);
                    return result;
                }
            }

        }
        catch (DataAccessException e) {
            try{
                db.closeConnection(false);
            } catch (DataAccessException ex) {
                ex.printStackTrace();
            }
            return new EventIDResult("Error: Problem with the Database");
        }

    }
}
