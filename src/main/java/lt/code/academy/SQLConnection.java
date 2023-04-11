package lt.code.academy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static lt.code.academy.tools.Print.*;

public class SQLConnection {
    private static SQLConnection instance;
    private Connection connection;

    private SQLConnection() {
        try {
            connection = DriverManager.getConnection(
                    ApplicationProperties.getInstance().getValue("jdbc.postgresql.connection.url"),
                    ApplicationProperties.getInstance().getValue("jdbc.postgresql.connection.username"),
                    ApplicationProperties.getInstance().getValue("jdbc.postgresql.connection.password"));
            if (connection != null) {
                pSuccess("Successfully connected to database...");
            }
        } catch (SQLException e) {
            pError("Cannot establish connection with database..." + e.getMessage());
            System.exit(0);
        }
    }

    public static SQLConnection getInstance() {
        if (instance == null) {
            instance = new SQLConnection();
        }

        return instance;
    }

    public Connection getConnection() {

        return connection;
    }
}
