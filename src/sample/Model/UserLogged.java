package sample.Model;

import com.sun.glass.ui.Clipboard;
import javafx.collections.ObservableList;
import sample.helper.JDBC;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserLogged {
   public static User signedUser;
   public static String signedName;




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

    public static User getSignedUser(){
        return signedUser;
    }

    public static String getSignedName (){
        return signedName;
    }

}
