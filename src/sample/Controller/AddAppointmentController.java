package sample.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.converter.LocalTimeStringConverter;
import sample.helper.Query;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Optional;
import java.util.ResourceBundle;

public class AddAppointmentController implements Initializable {
    public TextField appointmentIdTextfield;
    public TextField titleTextField;
    public TextField descriptionTextfield;
    public TextField locationTextfield;
    public TextField contactTextfield;
    public TextField typeTextfield;
    public TextField customerTextfield;
    public TextField userIdTextfield;
    public Button saveButton;
    public Button cancelButton;
    public DatePicker endDT;
    public DatePicker startDT;
    public TextField startTText;
    public TextField endTText;
    public Spinner hourStartSpinner;
    public Spinner minuteStartSpinner;
    public Spinner hourEndSpinner;
    public Spinner minuteEndSpinner;

    public void saveButtonPushed(ActionEvent event) throws SQLException, IOException {
        String title = titleTextField.getText();
        String description = descriptionTextfield.getText();
        int contact = Integer.parseInt(contactTextfield.getText());
        String location = locationTextfield.getText();
        String type = typeTextfield.getText();
        int customerID = Integer.parseInt(customerTextfield.getText());
        int userID = Integer.parseInt(userIdTextfield.getText());
        LocalDate startdt = startDT.getValue();
        LocalDate enddt = endDT.getValue();
        String startHour = hourStartSpinner.getValue().toString();
        if (startHour.length() == 1)
            startHour = "0" + startHour;
        String endHour = hourEndSpinner.getValue().toString();
        if (endHour.length() == 1)
            endHour = "0" + endHour;

        String startMinute = minuteStartSpinner.getValue().toString();
        if (startMinute.length() == 1)
            startMinute = "0" + startMinute;

        String endMinute = minuteEndSpinner.getValue().toString();
        if (endMinute.length() == 1)
            endMinute = "0" + endMinute;
        String endTime = endHour + ":" + endMinute;
        String startTime = startHour + ":" + startMinute;
        ZoneId myZDoneId = ZoneId.systemDefault();
        DateTimeFormatter parser = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime localStartTime = LocalTime.parse(startTime, parser);
        LocalDateTime localStartDateTime = LocalDateTime.of (startdt, localStartTime);
        LocalTime localEndTime = LocalTime.parse(endTime, parser);
        System.out.println (localStartTime + " local start time!");
        LocalDateTime localEndDateTime = LocalDateTime.of (enddt, localEndTime);
        ZonedDateTime startmyZDT = ZonedDateTime.of(LocalDateTime.from(localStartDateTime), myZDoneId);
        ZonedDateTime endmyZDT = ZonedDateTime.of(LocalDateTime.from(localEndDateTime), myZDoneId);
        ZoneId utcZneId = ZoneId.of("UTC");
        ZonedDateTime utcStartZDT = ZonedDateTime.ofInstant(startmyZDT.toInstant(), utcZneId);
        ZonedDateTime utcEndZDT = ZonedDateTime.ofInstant(endmyZDT.toInstant(), utcZneId);
      String startDate = String.valueOf(utcStartZDT.toLocalDate());
     String startUTCTime = String.valueOf((utcStartZDT.toLocalTime()));
      String startDT = startDate + " " + startUTCTime;
      String endDate = String.valueOf(utcEndZDT.toLocalDate ());
      String endutcTime = String.valueOf(utcEndZDT.toLocalTime());
      String endDT = endDate + " " + endutcTime;
      System.out.println (endDate + " end date time");
      //  String endDT = String.valueOf (utcEndZDT);
        int rowsAffected =  Query.insertAppointment (title, description,  location, type, startDT, endDT, customerID, userID, contact);
        if (rowsAffected >0){
            Alert alert = new Alert (Alert.AlertType.INFORMATION);
            alert.setContentText("Appointment Added!");

            alert.showAndWait();

            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Object scene = FXMLLoader.load(getClass().getResource("/sample/View/Appointments.fxml"));
            stage.setScene(new Scene((Parent) scene));
            stage.show();
        }



    }

    public void cancelButtonPushed(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Do you want to go back without saving?");
        Optional<ButtonType> result = alert.showAndWait();
        // alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Object scene = FXMLLoader.load(getClass().getResource("/sample/View/Appointments.fxml"));
            stage.setScene(new Scene((Parent) scene));
            stage.show();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        appointmentIdTextfield.setEditable(false);

        //configure the spinner with values of 0-24
        SpinnerValueFactory <Integer> starthourValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 24);
        this.hourStartSpinner.setValueFactory(starthourValueFactory);

    SpinnerValueFactory <Integer> startminuteValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59);
        this.minuteStartSpinner.setValueFactory(startminuteValueFactory);


        SpinnerValueFactory <Integer> endhourValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 24);
        this.hourEndSpinner.setValueFactory(endhourValueFactory);

        SpinnerValueFactory <Integer> endtminuteValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59);
        this.minuteEndSpinner.setValueFactory(endtminuteValueFactory);
/**
        SpinnerValueFactory starthourValueFactory = new SpinnerValueFactory<LocalTime>() {
            {
                setConverter(new LocalTimeStringConverter(FormatStyle.MEDIUM));
            }
            @Override
            public void decrement(int steps) {
                if (getValue() == null)

                    setValue(LocalTime.now());
                else {
                    LocalTime time =  getValue();
                    setValue(time.minusMinutes(steps));
                }
            }

            @Override
            public void increment(int steps) {
                if (this.getValue() == null)
                    setValue(LocalTime.now());
                else {
                    LocalTime time = getValue();
                    setValue(time.plusMinutes(steps));
                }
            }
        };*/
}


    }