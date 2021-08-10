package Service;

import DataAccess.*;
import Model.AuthToken;
import Model.Person;
import Service.Result.PersonIDResult;

import java.sql.SQLException;

/**
 * This class contains methods to call the DAO for PersonIDs.
 */
public class PersonIDService {
    private Database db;
    private AuthTokenDao authTokenDao;
    private PersonDao personDao;

    public PersonIDService() throws DataAccessException {
        db = new Database();
        authTokenDao = new AuthTokenDao(db.getConnection());
        personDao = new PersonDao(db.getConnection());
    }
    /**
     * This method gets the info of a single person from the database.
     * @return PersonIDResult object that contains either an error or success message.
     */
    public PersonIDResult personID(String personID, String authToken){

        try {
            AuthToken theAuth = authTokenDao.find(authToken);

            if (theAuth == null){
                db.closeConnection(false);
                return new PersonIDResult("Error: Invalid Authorization Token");
            }  else {
                Person thePerson = personDao.find(personID);
                if (thePerson == null){
                    db.closeConnection(false);
                    return new PersonIDResult("Error: Person not found");
                } else if (!theAuth.getUsername().equals(thePerson.getAssociatedUsername())){
                    db.closeConnection(false);
                    return new PersonIDResult("Error: Person not registered under current user");
                } else {
                    db.closeConnection(true);
                    PersonIDResult answer = new PersonIDResult(thePerson, thePerson.getPersonID());
                    return answer;
                }
            }
        }
        catch (DataAccessException databaseError){
            try{
                db.closeConnection(false);
            } catch (DataAccessException ex) {
                ex.printStackTrace();
            }
            return new PersonIDResult("Error: cannot locate person");
        }
    };
}
