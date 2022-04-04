package sample.Model;

import sample.helper.JDBC;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/** This class is for logged in user  of Appointments Scheduling application.
 * @author Veronika Ramey
 * */
public class UserLogged {

    /** Signed in user*/
   public static User signedUser;

   /**Name of signed in user*/
   public static String signedName;

    /**Method to select signed in user from the DB. This method selects user from the DB for specific username and password
     *
     * @param userName username
     * @param password password
     * @return boolean true if user is found
     * @throws SQLException
     */

    public static boolean select(String userName, String password) throws SQLException {
        String sql = " SELECT* FROM USERS WHERE User_Name = ? and Password = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, userName);
        ps.setString(2, password);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {

           signedUser = new User (rs.getInt ("User_ID"), rs.getString ("User_Name"), rs.getString ("Password"));
signedName = rs.getString("User_Name");

            return true;

        } else
            return false;
    }

    /**Method to get signed in user.
     *
     * @return Logged in user's name.
     */
    public static String getSignedName (){
        return signedName;
    }

}
