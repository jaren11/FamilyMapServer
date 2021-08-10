package Service;

import DataAccess.*;
import Generate.FamilyTreeData;
import Generate.GenerateData;
import Model.Person;
import Model.User;
import Service.Result.FillResult;


/**
 * This class contains methods for filling the database.
 */
public class FillService {
    private Database db;
    private EventDao eventDao;
    private PersonDao personDao;
    private UserDao userDao;


    public FillService() throws DataAccessException {
        db = new Database();
        eventDao = new EventDao(db.getConnection());
        personDao = new PersonDao(db.getConnection());
        userDao = new UserDao(db.getConnection());
    }

    /**
     * This fills the database with information.
     * @return FillResult object that contains either an error or success message
     */
    public FillResult fill(String username, int generations) throws DataAccessException {
        if(generations <= 0){
            db.closeConnection(false);
            return new FillResult("Error: 0 or less generations");
        }

        try{
            GenerateData dataGen = new GenerateData();
            User theUser = userDao.find(username);

            if(theUser == null){
                db.closeConnection(false);
                return new FillResult("Error: User not found");
            } else{
                Person userPerson = new Person();
                userPerson.setAssociatedUsername(theUser.getUsername());
                userPerson.setFirstName(theUser.getFirstName());
                userPerson.setLastName(theUser.getLastName());
                userPerson.setGender(theUser.getGender());

                FamilyTreeData genData = dataGen.beginGeneration(generations, userPerson);

                for (int i = 0; i < genData.getFamilyTree().size(); i++){
                    personDao.insert(genData.getFamilyTree().get(i));
                }

                for (int k = 0; k < genData.getTheEvents().size(); k++){
                    eventDao.insert(genData.getTheEvents().get(k));
                }

                db.closeConnection(true);
                return new FillResult("Successfully added " + genData.getFamilyTree().size() +  " persons and " + genData.getTheEvents().size() + " events to the database.", true);
            }
        }catch(DataAccessException e){
            try{
                db.closeConnection(false);
            } catch (DataAccessException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }

        return new FillResult("Error");
    }
}

