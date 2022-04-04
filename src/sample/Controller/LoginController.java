package sample.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.Model.UserLogged;
import sample.helper.JDBC;
 import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;

/**This is controller class to initialize functionality of Login.fxml.
 * @author Veronika Ramey
 */
public class LoginController implements Initializable  {

    /**Text field for user's log in name*/
    public TextField Userloginname;

    /**Text field for user's password*/
    public TextField Password;

    /**Label for user's location text*/
    public Label locationLabel;

    /**Label for user's name text field*/
    public Label UsernameLabel;

    /**Label for user's password text field*/
    public Label PasswordLabel;

    /**Button to submit user's password and username*/
    @FXML
    private Button submitButton;


    /**Initializes log in  scene. Method is used to initialize controller for logging in - Login.fxml.
     * @param url Describes resolving relative paths for the root object
     * @param resourceBundle The root object's localization resources, if root object is not localized - null.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        locationLabel.setText("Your location: " + ZoneId.systemDefault());

    //  Locale.setDefault(new Locale("fr"));
        ResourceBundle rb = ResourceBundle.getBundle("sample/translt", Locale.getDefault());

    if(Locale.getDefault().getLanguage().equals("fr")){
        UsernameLabel.setText (rb.getString("UsernameLabel.text"));
        PasswordLabel.setText(rb.getString("PasswordLabel.text"));
        submitButton.setText(rb.getString("submitButton.text"));
        locationLabel.setText (rb.getString("locationLabel.text") + ZoneId.systemDefault());
    }
    }


    /**Method validates users credentials and changes the scene to Customer.fxml. This method changes scene to customers view once user's credentials are validated.
     * If user input is missing or invalid a corresponding alert is displayed. Successful and failed log in attempts are recorded in login_activity.txt file.
     * Alert is displayed if an appointment is scheduled within 15 minutes from login time or a message is shown confirming no upcoming appointments.
     * @param event Action on submit button*/
    public void submitButtonClicked(ActionEvent event) throws SQLException, IOException {

        String userName = Userloginname.getText();
        String password = Password.getText();

        if (userName.isEmpty() || password.isEmpty()){

            if (!userName.isEmpty()){


                String myFile = "login_activity.txt";
                File login_activity = new File (myFile);
                if (!login_activity.exists()) {
                    PrintWriter pw = new PrintWriter("login_activity.txt");
                    pw.append(userName + " " + ZonedDateTime.now() + " failed to log in" + "\n");
                    pw.close();
                }
                else {
                    FileWriter fw = new FileWriter("login_activity.txt", true);
                    fw.append(userName + " " + ZonedDateTime.now() + "  failed to log in" + "\n");
                    fw.close();

                }


            }

             Alert alert = new Alert (Alert.AlertType.ERROR);

            if(Locale.getDefault().getLanguage().equals("fr"))

            {    ResourceBundle rb = ResourceBundle.getBundle("sample/translt", Locale.getDefault());
                alert.setHeaderText(rb.getString("alertempty.text"));

            }
            else
                alert.setHeaderText("Please enter your user name and password!");
            alert.showAndWait();

        }

        else
        {
            JDBC.openConnection();
          boolean validate =  UserLogged.select(userName, password);
            // insert here method to logger

            String myFile = "login_activity.txt";

            File login_activity = new File (myFile);

            if (!login_activity.exists()) {

                PrintWriter pw = new PrintWriter("login_activity.txt");
                if (validate) {
                    pw.append(userName + " " + ZonedDateTime.now() + " successfully logged in" + "\n");
                    pw.append("\n");}

                else
                    pw.append(userName + " " + ZonedDateTime.now() + " failed to log in" + "\n");

                pw.close();

            }
            else {
                FileWriter fw = new FileWriter("login_activity.txt", true);

                if (validate) {
                    fw.append(userName + " " + ZonedDateTime.now() + " successfully logged in" + "\n");
                    fw.append("\n");

                } else {
                    fw.append(userName + " " + ZonedDateTime.now() + "  failed to log in" + "\n");

                }
                fw.close();
            }

            if (validate) {
              System.out.println("Success!");


              Instant now = Instant.now();
              ZonedDateTime myZDT = ZonedDateTime.ofInstant(now,
                      ZoneId.systemDefault());
              //convert to utc
              ZoneId utcZoneID = ZoneId.of("UTC");
              ZonedDateTime utcZDT = ZonedDateTime.ofInstant(myZDT.toInstant(), utcZoneID);
              System.out.println ("UTC tme: " + utcZDT);
              DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
              String utcStart = utcZDT.format(formatter);
              String utcEnd =  utcZDT.plusMinutes(15).format(formatter);

                  try {
                  JDBC.openConnection();
                  System.out.println("inside try");
                  String sql = "SELECT Appointment_ID, Start, End from appointments WHERE Start BETWEEN ? AND ? ";
                  PreparedStatement ps = JDBC.connection.prepareStatement(sql);
                  ps.setObject(1, utcStart);
                  ps.setObject(2, utcEnd);
                  ResultSet resultSet = ps.executeQuery();


                 if (!resultSet.next()) {
                     Alert alert = new Alert(Alert.AlertType.WARNING);
                     if(Locale.getDefault().getLanguage().equals("fr")){
                         ResourceBundle rb = ResourceBundle.getBundle("sample/translt", Locale.getDefault());
                         alert.setHeaderText(rb.getString("noappointmentsalert"));
                     }

                     else
                         alert.setHeaderText ("You don't have upcoming appointments!");
                     alert.showAndWait();
                     System.out.println ("In the else");
                 }

                 else {
                     do {
                         int appointmentID = resultSet.getInt("Appointment_ID");
                         Timestamp start = resultSet.getTimestamp("Start");


                         ZonedDateTime utcmStart = start.toInstant().atZone(utcZoneID);
                         ZonedDateTime mystartZDT;

                         mystartZDT = ZonedDateTime.ofInstant(utcmStart.toInstant(), ZoneId.systemDefault());
                         String mystartZDTstring = mystartZDT.format(formatter);

                         Timestamp end = resultSet.getTimestamp("End");
                         ZonedDateTime utcmEnd = end.toInstant().atZone(utcZoneID);
                         ZonedDateTime myendZDT;
                         myendZDT = ZonedDateTime.ofInstant(utcmEnd.toInstant(), ZoneId.systemDefault());
                         String myendZDTstring = myendZDT.format(formatter);

                         Alert alert = new Alert(Alert.AlertType.WARNING);
                         if (Locale.getDefault().getLanguage().equals("fr")) {
                             ResourceBundle rb = ResourceBundle.getBundle("sample/translt", Locale.getDefault());
                             System.out.println("inside french alert");
                             alert.setHeaderText(rb.getString("alertpart") + " " + appointmentID + " " +  rb.getString("alertpart2") + " " +  mystartZDTstring +
                                    " " +  rb.getString("alertpart3") + " " + myendZDTstring);


                         } else
                             alert.setHeaderText("You have upcoming appointment! Appointment ID: " + appointmentID + " starting " + mystartZDTstring + " and ending " + myendZDTstring);
                         alert.showAndWait();


                     } while (resultSet.next());}}

              catch (Exception e){

                  System.err.println (e.getMessage());
              }

              Stage stage;
              Parent scene;
              stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
              scene = FXMLLoader.load(getClass().getResource("/sample/View/Customers.fxml"));
              stage.setScene(new Scene(scene));
              stage.show();

          }
          else
          {   Alert alert = new Alert(Alert.AlertType.ERROR);

              if(Locale.getDefault().getLanguage().equals("fr"))

              {    ResourceBundle rb = ResourceBundle.getBundle("sample/translt", Locale.getDefault());
                  alert.setHeaderText(rb.getString("alert.text"));

              }
              else
              alert.setHeaderText("Please enter valid credentials!");
              alert.showAndWait();
        }}

}}
