package serviceTest;

import DataAccess.DataAccessException;
import DataAccess.Database;
import Service.ClearService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ClearTest {
    private ClearService clearService;
    private Database db = new Database();

    //Setup stuff
    @BeforeEach
    public void setUp() throws DataAccessException {
        db.openConnection();
        clearService = new ClearService();
    }

    //closes connection
    @AfterEach
    public void tearDown() throws DataAccessException {
        db.closeConnection(false);
    }

    @Test
    public void clearTesting(){
        clearService.clear();
        clearService.clear();
    }
}
