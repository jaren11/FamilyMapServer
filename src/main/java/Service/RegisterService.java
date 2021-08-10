package Service;

import DataAccess.*;
import Generate.FamilyTreeData;
import Generate.GenerateData;
import Model.AuthToken;
import Model.Event;
import Model.Person;
import Model.User;
import Service.Result.RegisterResult;
import Service.Request.RegisterRequest;

import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * This class contains all the methods required to register a new user.
 */
public class RegisterService {
    private Database db;
    private AuthTokenDao authTokenDao;
    private EventDao eventDao;
    private PersonDao personDao;
    private UserDao userDao;

    private AuthToken authtoken;
    private Event theEvent;
    private Person thePerson;
    private User theUserRegister;

    private GenerateData generateData;
    private int numGen;

    public RegisterService() throws DataAccessException {
        db = new Database();
        authTokenDao = new AuthTokenDao(db.getConnection());
        eventDao = new EventDao(db.getConnection());
        personDao = new PersonDao(db.getConnection());
        userDao = new UserDao(db.getConnection());

        authtoken = new AuthToken();
        theEvent = new Event();
        thePerson = new Person();
        theUserRegister = new User();

        generateData = new GenerateData();
        numGen = 4;
    }

    /**
     * Registers a new user.
     * @param r
     * @return RegisterResult object that contains either an error or success message
     */
    public RegisterResult register(RegisterRequest r) throws DataAccessException {
        if ((r.getUsername() == null) ||
                (r.getEmail() == null) ||
                (r.getFirstName() == null) ||
                (r.getLastName() == null) ||
                (r.getPassword() == null) ||
                (r.getGender() == null)){
            return new RegisterResult("Invalid Input");
        }

        theUserRegister.setUsername(r.getUsername());
        theUserRegister.setPassword(r.getPassword());
        theUserRegister.setEmail(r.getEmail());
        theUserRegister.setFirstName(r.getFirstName());
        theUserRegister.setLastName(r.getLastName());
        theUserRegister.setGender(r.getGender());
        theUserRegister.setPersonID(thePerson.getPersonID());

        thePerson.setAssociatedUsername(theUserRegister.getUsername());
        thePerson.setFirstName(theUserRegister.getFirstName());
        thePerson.setLastName(theUserRegister.getLastName());
        thePerson.setGender(theUserRegister.getGender());


        try {
            User testing = userDao.find(theUserRegister.getUsername());

            if (testing == null) {
                userDao.insert(theUserRegister);
                authtoken = new AuthToken(theUserRegister.getUsername(), "", new Timestamp(System.currentTimeMillis()));
                authTokenDao.insert(authtoken);

                FamilyTreeData familyTreeData = generateData.beginGeneration(numGen, thePerson);

                ArrayList<Person> personArrayList = familyTreeData.getFamilyTree();
                //insert all the persons
                for (int i = 0; i < personArrayList.size(); i++){
                    personDao.insert(personArrayList.get(i));
                }

                ArrayList<Event> eventArrayList = familyTreeData.getTheEvents();
                //insert all the events
                for (int k = 0; k < eventArrayList.size(); k++){
                    eventDao.insert(eventArrayList.get(k));
                }

                RegisterResult registerResult = new RegisterResult(authtoken.getAuthToken(), theUserRegister.getUsername(), theUserRegister.getPersonID(), null);

                db.closeConnection(true);
                return registerResult;
            } else {
                db.closeConnection(false);
                return new RegisterResult("Error: username already exists");
            }

        } catch (DataAccessException databaseError){
            try{
                db.closeConnection(false);
            } catch (DataAccessException ex) {
                ex.printStackTrace();
            }
            return new RegisterResult(databaseError.toString());
        }
    }
}
