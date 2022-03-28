package sample.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import sample.Model.Appointment;
import sample.Model.Customer;
import sample.helper.Query;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.Optional;
import java.util.ResourceBundle;

public class UpdateAppointmentController implements Initializable {
    public DatePicker startUpdDT;
    public Spinner hourStartSpinneUpd;
    public Spinner minuteStartSpinnerUpd;
    public TextField appointmentIdUpdTextfield;
    public TextField titleUpdTextField;
    public TextField descriptionUpdTextfield;
    public TextField locationUpdTextfield;
    public TextField typeUpdTextfield;
    public TextField userIdUpdTextfield;
    public ComboBox selectContactNameUpd;
    public TextField customerUpdTextfield;
    public DatePicker endDTUpd;
    public Spinner hourEndSpinnerUpd;
    public Spinner minuteEndSpinnerUpd;
    public Button saveButtonUpd;
    public Button cancelButtonUpd;
    public Appointment selectedAppointment;

    public void saveButtonPushed(ActionEvent event) {

        try {

            String title = titleUpdTextField.getText();

            String description = descriptionUpdTextfield.getText();

            String location = locationUpdTextfield.getText();
            String type = typeUpdTextfield.getText();
            int customerID = Integer.parseInt(customerUpdTextfield.getText());
            int userID = Integer.parseInt(userIdUpdTextfield.getText());
            System.out.println (userID);
            String contactName = (String) selectContactNameUpd.getValue();
            int appointmentID = Integer.parseInt(appointmentIdUpdTextfield.getText());

            LocalDate startdt = startUpdDT.getValue();
            LocalDate enddt = endDTUpd.getValue();
            String startHour = hourStartSpinneUpd.getValue().toString();
            if (startHour.length() == 1)
                startHour = "0" + startHour;
            String endHour = hourEndSpinnerUpd.getValue().toString();
            if (endHour.length() == 1)
                endHour = "0" + endHour;

            String startMinute = minuteStartSpinnerUpd.getValue().toString();
            if (startMinute.length() == 1)
                startMinute = "0" + startMinute;

            String endMinute = minuteEndSpinnerUpd.getValue().toString();
            if (endMinute.length() == 1)
                endMinute = "0" + endMinute;

            String endTime = endHour + ":" + endMinute;
            String startTime = startHour + ":" + startMinute;
            ZoneId myZDoneId = ZoneId.systemDefault();
            DateTimeFormatter parser = DateTimeFormatter.ofPattern("HH:mm");
            LocalTime localStartTime = LocalTime.parse(startTime, parser);
            LocalDateTime localStartDateTime = LocalDateTime.of(startdt, localStartTime);
            LocalTime localEndTime = LocalTime.parse(endTime, parser);

            LocalDateTime localEndDateTime = LocalDateTime.of(enddt, localEndTime);
            ZonedDateTime startmyZDT = ZonedDateTime.of(LocalDateTime.from(localStartDateTime), myZDoneId);
            ZonedDateTime endmyZDT = ZonedDateTime.of(LocalDateTime.from(localEndDateTime), myZDoneId);
            ZoneId utcZneId = ZoneId.of("UTC");
            ZonedDateTime utcStartZDT = ZonedDateTime.ofInstant(startmyZDT.toInstant(), utcZneId);
            ZonedDateTime utcEndZDT = ZonedDateTime.ofInstant(endmyZDT.toInstant(), utcZneId);
            String startDate = String.valueOf(utcStartZDT.toLocalDate());
            String startUTCTime = String.valueOf((utcStartZDT.toLocalTime()));
            String startDT = startDate + " " + startUTCTime;
            String endDate = String.valueOf(utcEndZDT.toLocalDate());
            String endutcTime = String.valueOf(utcEndZDT.toLocalTime());
            String endDT = endDate + " " + endutcTime;

            //convert to est to validate
            ZoneId estZoneId = ZoneId.of("America/New_York");
            ZonedDateTime estStartZDT = ZonedDateTime.ofInstant(utcStartZDT.toInstant(), estZoneId);
            ZonedDateTime estEndZDT = ZonedDateTime.ofInstant(utcEndZDT.toInstant(), estZoneId);
            System.out.println("est start zdt: " + estStartZDT);
            System.out.println("est end zdt: " + estEndZDT);
//compare input to business hours for overlap
            LocalTime startEST = estStartZDT.toLocalTime();
            LocalTime endEST = estEndZDT.toLocalTime();
            LocalDate dateSEST = estStartZDT.toLocalDate();
            LocalDate dateEEST = estEndZDT.toLocalDate();
            DayOfWeek dayStart = DayOfWeek.of(dateSEST.get(ChronoField.DAY_OF_WEEK));


            DayOfWeek dayEnd = DayOfWeek.of(dateEEST.get(ChronoField.DAY_OF_WEEK));
            LocalTime businessStart = LocalTime.of(8, 00);
            LocalTime busienssEnd = LocalTime.of(22, 00);
            if (startEST.isAfter(busienssEnd) || startEST.isBefore(businessStart) || (endEST.isAfter(busienssEnd)
                    || endEST.isBefore(businessStart)) || dayStart.equals(DayOfWeek.SATURDAY) ||dayStart.equals(DayOfWeek.SUNDAY)
                    || dayEnd.equals(DayOfWeek.SATURDAY) || dayEnd.equals(DayOfWeek.SUNDAY)) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText("Cannot schedule outside business hours!");
                alert.showAndWait();
            } else if (title.isEmpty() || description.isEmpty() || location.isEmpty() || type.isEmpty()
                    || customerUpdTextfield.getText().isEmpty() || userIdUpdTextfield.getText().isEmpty() || contactName == null  ) {
                Utility.displayErrorAlert();

            }
            else if (!Query.customerExists(customerID)){

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Please enter existing customer ID or add a new customer first");
                alert.showAndWait();

            }
            else if (!Query.userExists(userID)){

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Please enter existing user ID!");
                alert.showAndWait();

            }
            else  {
                int overlapps = Query.checkforOverlaps(customerID, startDT, endDT);
                if (overlapps > 0  ){
                    Alert alert = new Alert (Alert.AlertType.ERROR);
                    alert.setContentText("Appointment overlapps with a different appointment!");
                    alert.showAndWait();
                }
                else {

                    int rowsAffected = Query.updateAppointment(title, description, location, type, startDT, endDT, customerID, userID, Query.contactID(contactName), appointmentID);
                    if (rowsAffected > 0) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setContentText("Appointment Updated!");

                        alert.showAndWait();

                        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                        Object scene = FXMLLoader.load(getClass().getResource("/sample/View/Appointments.fxml"));
                        stage.setScene(new Scene((Parent) scene));
                        stage.show();
                    }}

            } } catch (NumberFormatException | IOException | SQLException e) {

            Utility.displayErrorAlert();


        }
    }



    public void cancelButtonPushed(ActionEvent event) throws IOException
    {Alert alert = new Alert (Alert.AlertType.CONFIRMATION);
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
        appointmentIdUpdTextfield.setEditable(false);
        endDTUpd.getEditor().setDisable(true);
        startUpdDT.getEditor().setDisable(true);
       hourStartSpinneUpd.getEditor().setDisable(true);
        hourEndSpinnerUpd.getEditor().setDisable(true);
        minuteStartSpinnerUpd.getEditor().setDisable(true);
        minuteEndSpinnerUpd.getEditor().setDisable(true);




        //configure the spinner with values of 0-24



    }

    //to populate the view.
    public void populateData (Appointment appointment) throws SQLException {
         selectedAppointment = appointment;

        appointmentIdUpdTextfield.setText(String.valueOf(selectedAppointment.getId()));
        titleUpdTextField.setText(selectedAppointment.getTitle());
        descriptionUpdTextfield.setText(selectedAppointment.getDescription());
        locationUpdTextfield.setText (selectedAppointment.getLocation());
        typeUpdTextfield.setText(selectedAppointment.getType());
        userIdUpdTextfield.setText(String.valueOf (selectedAppointment.getUserID()));
        selectContactNameUpd.setItems (Query.selectContacts());
         int conactId = selectedAppointment.getContact();
        selectContactNameUpd.setValue (Query.contactName(conactId));
        customerUpdTextfield.setText(String.valueOf (selectedAppointment.getCustomerID()));
        Timestamp startDate = selectedAppointment.getStartDateTime();
        ZoneId utcZoneId = ZoneId.of("UTC");
        ZonedDateTime utcStartZDT = ZonedDateTime.ofInstant(startDate.toInstant(), utcZoneId);
        ZonedDateTime myStartZDT = ZonedDateTime.ofInstant(utcStartZDT.toInstant(), ZoneId.systemDefault());
        String startDateUpd = String.valueOf((myStartZDT.toLocalDate()));
        startUpdDT.setValue(LocalDate.parse(startDateUpd));
        String startTimeUpd = String.valueOf((myStartZDT.toLocalTime()));
        System.out.println (startTimeUpd);
        String [] substrings = startTimeUpd.split(":");
        String hours = substrings[0];
        String minutes = substrings[1];
        System.out.println (hours);
        System.out.println (minutes);
        Timestamp endDate = selectedAppointment.getEndDateTime();
        ZonedDateTime utcEndZDT = ZonedDateTime.of (endDate.toLocalDateTime(), utcZoneId);
        ZonedDateTime myEndZDT = ZonedDateTime.ofInstant(utcEndZDT.toInstant(), ZoneId.systemDefault());
        String endDateUpd = String.valueOf(myEndZDT.toLocalDate());
        endDTUpd.setValue(LocalDate.parse(endDateUpd));
        String endTimeUpd = String.valueOf((myEndZDT.toLocalTime()));
        String [] parts = endTimeUpd.split (":");
        String endHours = parts[0];
        String endMinutes = parts [1];
        SpinnerValueFactory <Integer> starthourValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, Integer.parseInt(hours));
        this.hourStartSpinneUpd.setValueFactory(starthourValueFactory);

        SpinnerValueFactory <Integer> startminuteValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, Integer.parseInt(minutes));
        this.minuteStartSpinnerUpd.setValueFactory(startminuteValueFactory);


        SpinnerValueFactory <Integer> endhourValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, Integer.parseInt(endHours));
        this.hourEndSpinnerUpd.setValueFactory(endhourValueFactory);

        SpinnerValueFactory <Integer> endtminuteValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, Integer.parseInt(endMinutes));
        this.minuteEndSpinnerUpd.setValueFactory(endtminuteValueFactory);



    }
}
