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
import sample.helper.JDBC;
import sample.helper.Query;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Locale;
import java.util.ResourceBundle;

public class LoginController implements Initializable  {

    public TextField Userloginname;
    public TextField Password;
    public Label locationLabel;
    public Label UsernameLabel;
    public Label PasswordLabel;

    @FXML
    private Button submitButton; 



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println ("Initialized!");
       locationLabel.setText("Your location: " + ZoneId.systemDefault());
        System.out.println("Your location: " + ZoneId.systemDefault());
     //   Locale.setDefault(new Locale("fr"));
        ResourceBundle rb = ResourceBundle.getBundle("sample/translt", Locale.getDefault());
//        locationLabel.setText(ZoneId.systemDefault().toString());
    if(Locale.getDefault().getLanguage().equals("fr")){
        UsernameLabel.setText (rb.getString("UsernameLabel.text"));
        PasswordLabel.setText(rb.getString("PasswordLabel.text"));
        submitButton.setText(rb.getString("submitButton.text"));
       // locationLabel.setText (rb.getString("locationLabel.text"));
    }
    }

    public void submitButtonClicked(ActionEvent event) throws SQLException, IOException {

        String userName = Userloginname.getText();
        String password = Password.getText();

        if (userName.isEmpty() || password.isEmpty()){

            System.out.println ("Please enter your user name ans password!");
        }

        else
        {
            JDBC.openConnection();
          boolean validate =  Query.select(userName, password);
          if (validate) {
              System.out.println("Success!");
              LocalDateTime myLDT = LocalDateTime.now();
              //LocalDateTime myLDT = LocalDateTime.of(2020, 05, 28, 11, 55);
              ZoneId myZoneID = ZoneId.systemDefault();
              ZonedDateTime myZDT = ZonedDateTime.of(myLDT, myZoneID);
              System.out.println ("myZDT " +  myZDT);


              //convert to utc
              ZoneId utcZoneID = ZoneId.of("UTC");
              ZonedDateTime utcZDT = ZonedDateTime.ofInstant(myZDT.toInstant(), utcZoneID);
              System.out.println ("UTC tme: " + utcZDT);

              try {
                  JDBC.openConnection();
                  String sql = "SELECT Appointment_ID, Start, End from appointments WHERE Start BETWEEN ? AND ? ";
                  PreparedStatement ps = JDBC.connection.prepareStatement(sql);
                  ps.setObject(1, utcZDT);
                  ps.setObject(2, utcZDT.plusMinutes(15));
                  ResultSet resultSet = ps.executeQuery();



                  //CREATE CUSTOMER OBJECT FROM EACH RECORD
                  if  (resultSet.next()) {

                      int appointmentID = resultSet.getInt("Appointment_ID");
                      Timestamp start = resultSet.getTimestamp("Start");
                      Timestamp end = resultSet.getTimestamp("End");
                      System.out.println("inside if");
                      System.out.println (appointmentID);
                      Alert alert = new Alert(Alert.AlertType.WARNING);
                      alert.setHeaderText("You have upcoming appointment! Appointment ID: " + appointmentID + " starting " + start + " and ending " + end);
                      alert.showAndWait();


                  }
                  else {
                      Alert alert = new Alert(Alert.AlertType.WARNING);
                      alert.setHeaderText ("You don't have upcoming appointments!");
                      alert.showAndWait();
                      System.out.println ("In the else");
                  }
              }
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
             // Locale.setDefault(new Locale("fr"));
              ResourceBundle rb = ResourceBundle.getBundle("sample/translt", Locale.getDefault());
              if(Locale.getDefault().getLanguage().equals("fr"))

              {
                  alert.setHeaderText (rb.getString("alert.text"));

              }
              else
              alert.setHeaderText("Please enter valid credentials!");
              alert.showAndWait();
             // System.out.println("Enter valid credentials!");}

        }



    }
}}
