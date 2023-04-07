package lt.code.academy;
import com.diogonunes.jcolor.AnsiFormat;
import static com.diogonunes.jcolor.Attribute.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class SQLConnection {
    AnsiFormat fError = new AnsiFormat(YELLOW_TEXT(), RED_BACK());
    AnsiFormat fSuccess = new AnsiFormat(GREEN_TEXT());
    AnsiFormat fAlert = new AnsiFormat(RED_TEXT());
    private static SQLConnection instance;
    private Connection connection;

    private SQLConnection() {
        try {
            connection = DriverManager.getConnection(
                    ApplicationProperties.getInstance().getValue("jdbc.postgresql.connection.url"),
                    ApplicationProperties.getInstance().getValue("jdbc.postgresql.connection.username"),
                    ApplicationProperties.getInstance().getValue("jdbc.postgresql.connection.password"));
            if (connection != null) {
                System.out.println(fSuccess.format("Successfully connected to database..."));
            }
        } catch (SQLException e) {
            System.out.println(fError.format("Cannot establish connection with database..." + e.getMessage()));
            System.exit(0);
        }
    }
    public static SQLConnection getInstance() {
        if(instance == null){
            instance = new SQLConnection();
        }

        return instance;
    }
    public Connection getConnection(){

        return connection;
    }
}
