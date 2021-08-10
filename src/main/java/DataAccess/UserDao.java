package DataAccess;

import Model.User;

import org.junit.jupiter.api.*;
import java.sql.*;

/**
 * Class includes insert, find, update, and return functions on users to change the database.
 */
public class UserDao {
    /**
     * Connection to the database.
     */
    private final Connection conn;

    /**
     * Creates a new UserDao.
     * @param conn
     */
    public UserDao(Connection conn)
    {
        this.conn = conn;
    }

    /**
     * Inserts a user into the database.
     * @param user
     */
    public void insert(User user) throws DataAccessException{
        String sql = "INSERT INTO users (username, password, email, firstName, lastName, gender, personID) VALUES(?,?,?,?,?,?,?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getFirstName());
            stmt.setString(5, user.getLastName());
            stmt.setString(6, user.getGender());
            stmt.setString(7, user.getPersonID());

            stmt.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
            throw new DataAccessException("Error inserting user into the database");
        }
    }

    /**
     * Finds whether or not a person with the given username exists in the database.
     * @param username
     * @return true or false
     */
    public User find(String username) throws DataAccessException{
        User user;
        ResultSet rs = null;
        String sql = "SELECT * FROM users WHERE username = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            if (rs.next()) {
                user = new User(rs.getString("username"), rs.getString("password"),
                        rs.getString("email"), rs.getString("firstName"), rs.getString("lastName"),
                        rs.getString("gender"), rs.getString("personID"));
                return user;
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
     * Clears the database of users.
     */
    public void clear() throws DataAccessException {
        try (Statement stmt = conn.createStatement()){
            String sql = "DELETE FROM users";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DataAccessException("SQL Error encountered while clearing tables");
        }
    }
}
