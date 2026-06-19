package gabriel.prog.mediavault.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseConnection {
    static DataBaseConnection instance;
    private Connection connection;

    private static final String URL = "jdbc:postgresql://localhost:5432/mediavault";
    private static final String USER = "postgres";
    private static final String PASSWORD = "Tigerpython(?)1";

    private DataBaseConnection(){
        try{
            Class.forName("org.postgresql.Driver");
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Failed to connect to database", e);
        }
    }

    public static DataBaseConnection getInstance(){
        if (instance == null) {
            instance = new DataBaseConnection();
        }
        return instance;
    }

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

    public void closeConnection() {
        try{
            if (connection != null && !connection.isClosed()){
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
