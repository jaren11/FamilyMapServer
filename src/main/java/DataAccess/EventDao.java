package DataAccess;

import Model.Event;
import Model.User;

import java.sql.*;
import java.util.ArrayList;

/**
 * Class includes insert, find, update, and return functions on events to change the database.
 */
public class EventDao {
    /**
     * Connection to the database.
     */
    private final Connection conn;

    /**
     * Creates a new EventDao.
     * @param conn
     */

    public EventDao(Connection conn)
    {
        this.conn = conn;
    }

    /**
     * Inserts an event into the database.
     * @param event
     */
    public void insert(Event event) throws DataAccessException {
        String sql = "INSERT INTO events (eventID, username, personID, latitude, longitude, country, city, eventType, year) VALUES(?,?,?,?,?,?,?,?,?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, event.getEventID());
            stmt.setString(2, event.getAssociatedUsername());
            stmt.setString(3, event.getPersonID());
            stmt.setDouble(4, event.getLatitude());
            stmt.setDouble(5, event.getLongitude());
            stmt.setString(6, event.getCountry());
            stmt.setString(7, event.getCity());
            stmt.setString(8, event.getEventType());
            stmt.setInt(9, event.getYear());

            stmt.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
            throw new DataAccessException("Error inserting event into the database");
        }
    }

    /**
     * Finds whether or not the event exists in the database.
     * @param eventID
     * @return true or false
     */
    public Event find(String eventID) throws DataAccessException {
        Event event;
        ResultSet rs = null;
        String sql = "SELECT * FROM events WHERE eventID = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, eventID);
            rs = stmt.executeQuery();
            if (rs.next()) {
                event = new Event(rs.getString("eventID"), rs.getString("username"),
                        rs.getString("personID"), rs.getFloat("latitude"), rs.getFloat("longitude"),
                        rs.getString("country"), rs.getString("city"), rs.getString("eventType"),
                        rs.getInt("year"));
                return event;
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
     * Clears the database of events.
     */
    public void clear() throws DataAccessException {
        try (Statement stmt = conn.createStatement()){
            String sql = "DELETE FROM events";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DataAccessException("SQL Error clearing tables");
        }
    }

    public ArrayList<Event> findAllEvents(String username) throws SQLException {
        ArrayList<Event> arrayEvents = new ArrayList<>();
        ResultSet rs = null;
        String sql = "SELECT * FROM events WHERE username = ?;";

        try (PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            while(rs.next() == true){
                Event event = new Event(rs.getString("eventID"),
                        rs.getString("username"),
                        rs.getString("personID"),
                        rs.getDouble("latitude"),
                        rs.getDouble("longitude"),
                        rs.getString("country"),
                        rs.getString("city"),
                        rs.getString("eventType"),
                        rs.getInt("year"));

                arrayEvents.add(event);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }

        if(arrayEvents.size() == 0){
            return null;
        }else{
            return arrayEvents;
        }
    }
}
