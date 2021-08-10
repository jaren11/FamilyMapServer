package generateTest;

import DataAccess.DataAccessException;
import DataAccess.Database;
import Generate.GenerateName;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class NameTest {
    private GenerateName generateName;
    private Database db = new Database();

    //Setup stuff
    @BeforeEach
    public void setUp() throws DataAccessException {
        db.openConnection();
        generateName = new GenerateName();
    }

    //closes connection
    @AfterEach
    public void tearDown() throws DataAccessException {
        db.closeConnection(false);
    }

    @Test
    public void mNamePass(){
        String mName = generateName.maleFirstName();
        System.out.println(mName);
    }

    @Test
    public void fNamePass(){
        String mName = generateName.femaleFirstName();
        System.out.println(mName);
    }

    @Test
    public void sNamePass(){
        String mName = generateName.lastName();
        System.out.println(mName);
    }
}
