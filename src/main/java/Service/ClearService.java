package Service;

import DataAccess.*;
import Service.Result.ClearResult;

import java.sql.SQLException;

/**
 * This class contains methods for clearing the database.
 */
public class ClearService {

    /**
     * Clear uses the DAOs to clear the database.
     * @return ClearResult object that contains either an error or success message
     */
    public ClearResult clear() {

        Database db = new Database();

        try{
            if(db.clearAllTables()){
                return new ClearResult("Clear succeeded", true);
            } else{
                return new ClearResult("Error: cannot clear database", false);
            }
        } catch (SQLException | DataAccessException e) {
            e.printStackTrace();
            return new ClearResult("Error: " + e.toString(), false);
        }
    }
}
