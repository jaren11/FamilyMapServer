package DataAccess;

import Model.AuthToken;
import Model.Event;

import java.sql.*;

/**
 * Class includes insert, find, update, and return functions on AuthTokens to change the database.
 */
public class AuthTokenDao{
    /**
     * Connection to the database.
     */
    private final Connection conn;

    /**
     * Creates a new EventDao.
     * @param conn
     */

    public AuthTokenDao(Connection conn)
    {
        this.conn = conn;
    }

    /**
     * Inserts an AuthToken into the database.
     * @param token
     */
    public void insert(AuthToken token) throws DataAccessException {
        String sql = "INSERT INTO authTokens (username, authToken, timeStamp) VALUES(?,?,?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, token.getUsername());
            stmt.setString(2, token.getAuthToken());
            stmt.setTimestamp(3, token.getTimeStamp());

            stmt.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
            throw new DataAccessException("Error encountered while inserting event into the database");
        }
    }

    /**
     * Finds whether or not the AuthToken exists in the database.
     * @param token
     * @return true or false
     */
    public AuthToken find(String token) throws DataAccessException {
        AuthToken authTokenVar;
        ResultSet rs = null;
        String sql = "SELECT * FROM authTokens WHERE authToken = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, token);
            rs = stmt.executeQuery();
            if (rs.next()) {
                authTokenVar = new AuthToken(rs.getString("username"), rs.getString("authToken"),
                        rs.getTimestamp("timeStamp"));
                return authTokenVar;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding user");
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
     * Clears the database of AuthTokens.
     */
    public void clear() throws DataAccessException {
        try (Statement stmt = conn.createStatement()){
            String sql = "DELETE FROM authTokens";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DataAccessException("SQL Error encountered while clearing tables");
        }
    }

}
