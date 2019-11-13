import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionManager {
    private static String host = "localhost";
    private static String port = "3306";
    private static String db = "csci3901";
    private static String dbParams = "serverTimezone=UTC";
    private static String user = "root";
    private static String password = "root";

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = null;
            String connectionStr = String.format("jdbc:mysql://%s:%s/%s?%s", host, port, db, dbParams);
            connection = DriverManager.getConnection(connectionStr, user, password);
            return connection;
        } catch (Exception e) {
            return null;
        }
    }

}
