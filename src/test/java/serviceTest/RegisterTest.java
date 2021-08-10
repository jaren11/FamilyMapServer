package serviceTest;

import DataAccess.DataAccessException;
import DataAccess.Database;
import Service.ClearService;
import Service.RegisterService;
import Service.Request.RegisterRequest;
import Service.Result.RegisterResult;
import org.junit.*;

public class RegisterTest {
    private Database db = new Database();

    @Before
    public void setUp()
    {
        ClearService clearService = new ClearService();
        clearService.clear();
    }

    @Test
    public void registerNewUser() throws DataAccessException //Using the services to register a new user
    {
        RegisterService registerService = new RegisterService();
        RegisterRequest registerRequest = new RegisterRequest("jaren11", "hahaha101", "jaren11@gmail.com", "Jaren", "Garff","m", "2q340t987y");

        RegisterResult regRes = registerService.register(registerRequest);

        Assert.assertNotNull(regRes.getPersonID());
        Assert.assertNotNull(regRes.getAuthtoken());
        Assert.assertNotNull(regRes.getUsername());
        Assert.assertEquals(null, regRes.getMessage());

    }

    @Test
    public void registerNewUserInvalidInput() throws DataAccessException //Registering a new user with bad input
    {
        RegisterService registerService = new RegisterService();
        RegisterRequest registerRequest = new RegisterRequest("jaren11", "hahaha101", "jaren11@gmail.com", "Jaren", "Garff","m", "2q340t987y");
        registerRequest.setGender(null);

        RegisterResult registerResult = registerService.register(registerRequest);

        Assert.assertNotNull(registerResult.getMessage());
        Assert.assertEquals(registerResult.getMessage(),"Invalid Input");
        Assert.assertNull(registerResult.getAuthtoken());
        Assert.assertNull(registerResult.getUsername());
        Assert.assertNull(registerResult.getPersonID());

    }

}
