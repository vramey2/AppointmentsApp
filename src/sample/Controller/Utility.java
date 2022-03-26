package sample.Controller;

import javafx.scene.control.Alert;
import sample.Model.User;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Utility {
    public static void displayErrorAlert (){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error dialog");
        alert.setContentText("Please enter valid value for each field!");
        alert.showAndWait();
    }


    public static String utcTimeNow (){

      //  ZonedDateTime myDT = ZonedDateTime.now();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        ZonedDateTime utcTimeNow  = ZonedDateTime.now(ZoneOffset.UTC);

        return utcTimeNow.format(timeFormatter);
    }


}
