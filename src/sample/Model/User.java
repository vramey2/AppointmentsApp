package sample.Model;

import sample.helper.JDBC;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** This class is for User object of Appointments Scheduling application.
 * @author Veronika Ramey
 * */
public class User {

    /**User's name*/
   private String userName;

   /**User's password*/
    private String password;

    /**User's ID*/
    private int userId;


    /**Method creates instance of User. This is a constructor method for  user with corresponding fields.
     *
     * @param userId ID of the user
     * @param userName Username of the user
     * @param password Password of the user
     */
    public User (int userId, String userName, String password){

        this.userId = userId;
        this.userName = userName;
        this.password = password;
    }

    /**Getter method for username.
     * @return Username
     */
    public String getUserName() {
        return userName;
    }

    /**Setter method for username.
     * @param userName Username
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**Getter method for password.
     *
     * @return Password
     */
    public String getPassword() {
        return password;
    }

    /**Setter method for password.
     *
     * @param password Password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**Getter method for user's id
     *
     * @return User's ID
     */
    public int getUserId() {
        return userId;
    }

    /**Setter method for user's ID
     *
     * @param userId User's ID
     */

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
