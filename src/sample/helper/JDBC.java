package sample.helper;

import java.sql.Connection;
import java.sql.DriverManager;

/**Class to open and close database connection.
 * @author Veronika Ramey
 */
public class JDBC {
    private static final String protocol = "jdbc";
    private static final String vendor = ":mysql:";
    private static final String location = "//localhost/";
    private static final String databaseName = "client_schedule";
    private static final String jdbURl = protocol + vendor + location + databaseName + "?connectionTimeZone = SERVER";
    private static final String driver = "com.mysql.cj.jdbc.Driver";
    private static final String userName = "sqlUser";
    private static final String password = "Passw0rd!";
    public static Connection connection;

    /**Method opens database connection. Method is used to open database connection.
     *
     */
    public static void openConnection() {
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(jdbURl, userName, password);
            System.out.println("Connection Successful!");
        }

        catch (Exception e) {

            System.out.println("Error: " + e.getMessage());
        }

    }

    /**Method closes database connection. Method is used to close database connection.
     *
     */
    public static void closeConnection(){
        try{
            connection.close();
            System.out.println("Connection Closed!");
        }
        catch(Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }
}
