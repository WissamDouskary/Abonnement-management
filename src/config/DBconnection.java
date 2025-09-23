package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBconnection {

    private String url = "jdbc:postgresql://localhost:5432/abonnement";
    private String username = "postgres";
    private String pass = "Wissam0908";

    public static DBconnection instance = null;
    private Connection connection = null;

    public static DBconnection getInstance() {
        if (instance == null)
            instance = new DBconnection();
        return instance;
    }

    private DBconnection() {
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, username, pass);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection getConnection() {
        return connection;
    }
}