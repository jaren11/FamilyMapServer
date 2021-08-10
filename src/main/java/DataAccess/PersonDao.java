package DataAccess;

import Model.Person;

import javax.xml.crypto.Data;
import java.sql.*;
import java.util.ArrayList;

/**
 * Class includes insert, find, update, and return functions on persons to change the database.
 */
public class PersonDao {
    /**
     * Connection to the database.
     */
    private final Connection conn;

    /**
     * Creates a new PersonDao.
     * @param conn
     */
    public PersonDao(Connection conn)
    {
        this.conn = conn;
    }

    /**
     * Inserts a person into the database.
     * @param person
     */
    public void insert(Person person) throws DataAccessException {
        String sql = "INSERT INTO persons (personID, username, firstName, lastName, gender, fatherID, motherID, spouseID) VALUES(?,?,?,?,?,?,?,?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, person.getPersonID());
            stmt.setString(2, person.getAssociatedUsername());
            stmt.setString(3, person.getFirstName());
            stmt.setString(4, person.getLastName());
            stmt.setString(5, person.getGender());
            stmt.setString(6, person.getFatherID());
            stmt.setString(7, person.getMotherID());
            stmt.setString(8, person.getSpouseID());

            stmt.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
            throw new DataAccessException("Error encountered while inserting person into the database");
        }
    }

    /**
     * Finds whether or not the person exists in the database.
     * @param personID
     * @return true or false
     */
    public Person find(String personID) throws DataAccessException {
        Person person;
        ResultSet rs = null;
        String sql = "SELECT * FROM persons WHERE personID = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, personID);
            rs = stmt.executeQuery();
            if (rs.next()) {
                person = new Person(rs.getString("personID"), rs.getString("username"),
                        rs.getString("firstName"), rs.getString("lastName"), rs.getString("gender"),
                        rs.getString("fatherID"), rs.getString("motherID"), rs.getString("spouseID"));
                return person;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error finding user");
        } finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
        return null;
    }

    /**
     * Clears the database of persons.
     */
    public void clear() throws DataAccessException {
        try (Statement stmt = conn.createStatement()){
            String sql = "DELETE FROM persons";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DataAccessException("SQL Error clearing tables");
        }
    }


    public ArrayList<Person> findAll(String username) throws SQLException {
        Database db = new Database();
        ArrayList<Person> answer = null;
        //openconn
        answer = new ArrayList<Person>();
        ResultSet rs = null;
        String sql = "SELECT * FROM persons WHERE username = ?;";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            while (rs.next() == true) {
                Person person = new Person(rs.getString("personID"),
                        rs.getString("username"),
                        rs.getString("firstname"),
                        rs.getString("lastname"),
                        rs.getString("gender"),
                        rs.getString("fatherID"),
                        rs.getString("motherID"),
                        rs.getString("spouseID"));

                answer.add(person);
            }
        }
        catch (SQLException e){
            System.out.println(e.toString());
            throw e;
        }
        if (answer.size() == 0){
            answer = null;
        }

        return answer;
    }
}
