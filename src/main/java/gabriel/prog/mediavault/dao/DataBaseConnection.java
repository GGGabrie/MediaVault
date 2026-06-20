package gabriel.prog.mediavault.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Singleton class for managing database connection
 */
public class DataBaseConnection {
    static DataBaseConnection instance;
    private Connection connection;

    private static final String URL = "jdbc:h2:./mediavault_db;DB_CLOSE_DELAY=-1;AUTO_SERVER=TRUE";
    private static final String USER = "mv";
    private static final String PASSWORD = "";


    private DataBaseConnection(){
        try{
            Class.forName("org.h2.Driver");
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Failed to connect to database", e);
        }
    }


    /**
     * Limits the number of instances of the Class to 1
     * @return The only database connection
     */
    public static DataBaseConnection getInstance(){
        if (instance == null) {
            instance = new DataBaseConnection();
        }
        return instance;
    }

    /**
     * Getter for the connection
     * @return The current connection
     */
    public Connection getConnection(){
        try{
            if (connection == null || connection.isClosed()){
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return connection;
    }
}
