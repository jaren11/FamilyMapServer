package Service;

import DataAccess.AuthTokenDao;
import DataAccess.DataAccessException;
import DataAccess.Database;
import DataAccess.UserDao;
import Model.AuthToken;
import Model.User;
import Service.Result.LoginResult;
import Service.Request.LoginRequest;

import java.sql.SQLException;

/**
 * This class contains methods for logging into the server.
 */
public class LoginService {
    private Database db;
    private AuthToken authToken;
    private AuthTokenDao authTokenDao;
    private UserDao userDao;
    private User theUser;

    public LoginService() throws DataAccessException {
        db = new Database();
        authToken = new AuthToken();
        authTokenDao = new AuthTokenDao(db.getConnection());
        userDao = new UserDao(db.getConnection());
        theUser = new User();
    }

    /**
     * Logs a user into the server.
     * @param r
     * @return LoginResult object that contains either an error or success message.
     */
    public LoginResult login(LoginRequest r){
        try {
            if (r.getPassword() == null || r.getUsername() == null) {
                db.closeConnection(false);
                return new LoginResult("Error: Invalid input");
            }
            theUser = userDao.find(r.getUsername());

            if (theUser == null) {
                db.closeConnection(false);
                return new LoginResult("Error: User not found ");
            } else if (!theUser.getPassword().equals(r.getPassword())) {
                db.closeConnection(false);
                return new LoginResult("Error: Incorrect Password");
            } else {
                authToken.setUsername(r.getUsername());
                authTokenDao.insert(authToken);
                db.closeConnection(true);
                return new LoginResult(authToken.getAuthToken(), theUser.getPersonID(), theUser.getUsername(), null);
            }
        }
        catch (DataAccessException databaseError) {
            try{
                db.closeConnection(false);
            } catch (DataAccessException ex) {
                ex.printStackTrace();
            }
            return new LoginResult("Error: Internal Server");
        }
    }
}
