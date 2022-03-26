package sample.Model;

import sample.helper.JDBC;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class User {


   private String userName;
    private String password;
    //ID?
    private int userId;
 ;

    public String getUserName() {
        return userName;
    }

    public User (int userId, String userName, String password){

        this.userId = userId;
        this.userName = userName;
        this.password = password;
    }




    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
