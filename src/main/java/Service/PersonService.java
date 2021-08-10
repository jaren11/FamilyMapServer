package Service;

import DataAccess.AuthTokenDao;
import DataAccess.DataAccessException;
import DataAccess.Database;
import DataAccess.PersonDao;
import Model.AuthToken;
import Service.Result.PersonResult;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * This class contains methods to call the DAO for Persons.
 */
public class PersonService {
    private Database db;
    private AuthTokenDao authTokenDao;
    private PersonDao personDao;

    public PersonService() throws DataAccessException {
        db = new Database();
        authTokenDao = new AuthTokenDao(db.getConnection());
        personDao = new PersonDao(db.getConnection());
    }
    /**
     * This method gets the info of all the persons from a single user from the database.
     * @return PersonResult object that contains either an error or success message.
     */
    public PersonResult person(String authToken){
        try {
            AuthToken theAuth = authTokenDao.find(authToken);

            if (theAuth == null){
                db.closeConnection(false);
                return new PersonResult("Error: Invalid Authorization Token");
            } else {
                ArrayList allFoundPersons = personDao.findAll(theAuth.getUsername());

                if (allFoundPersons == null){
                    db.closeConnection(false);
                    return new PersonResult("Error: There are no people under user");
                } else {
                    db.closeConnection(true);
                    PersonResult answer = new PersonResult(allFoundPersons, null, true);
                    return answer;
                }
            }
        }
        catch (DataAccessException | SQLException databaseError){
            try{
                db.closeConnection(false);
            } catch (DataAccessException ex) {
                ex.printStackTrace();
            }
            return new PersonResult("Error: cannot locate all people");
        }
    }
}
