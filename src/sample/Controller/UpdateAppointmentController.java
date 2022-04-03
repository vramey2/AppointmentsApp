package sample.Controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import sample.Model.Appointment;
import sample.helper.Query;
import sample.helper.QueryAppointment;

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

            String startDT = Utility.convertTime(startHour, startMinute, startdt);
            String endDT = Utility.convertTime (endHour, endMinute, enddt);
            ZonedDateTime utcStartZDT = Utility.convertUTCTime(startHour, startMinute, startdt);
            ZonedDateTime utcEndZDT = Utility.convertUTCTime (endHour, endMinute, enddt);


//check why utc time ?
                if (Utility.validateBusinessHours(utcStartZDT, utcEndZDT)){
                    Utility.displayWarning(4);

            } else if (title.isEmpty() || description.isEmpty() || location.isEmpty() || type.isEmpty()
                    || customerUpdTextfield.getText().isEmpty() || userIdUpdTextfield.getText().isEmpty() || contactName == null  ) {
                Utility.displayErrorAlert(1);

            }
            else if (Query.customerExists(customerID)){
                Utility.displayErrorAlert(7);

            }
            else if (Query.userExists(userID)){

                Utility.displayErrorAlert (8);

            }

            else if (utcStartZDT.isAfter(utcEndZDT)){
                Utility.displayErrorAlert (2);

            }

            else  {
                ObservableList<Integer> overlapID =  QueryAppointment.checkforOverlaps(customerID, startDT, endDT);

                if ((!overlapID.contains(appointmentID)  && overlapID.size() > 0 ) || (overlapID.size()>1)){
                    Utility.displayErrorAlert(5);

                }

                else
                {

                    int rowsAffected = QueryAppointment.updateAppointment(title, description, location, type, startDT, endDT, customerID, userID, Query.contactID(contactName), appointmentID);
                    if (rowsAffected > 0) {
                        Utility.displayInformation(2);

                        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                        Object scene = FXMLLoader.load(getClass().getResource("/sample/View/Appointments.fxml"));
                        stage.setScene(new Scene((Parent) scene));
                        stage.show();
                    }}

            } } catch (NumberFormatException | IOException | SQLException e) {

            Utility.displayErrorAlert (1);


        }
    }



    public void cancelButtonPushed(ActionEvent event) throws IOException
    {  if ( Utility.displayConfirmation(1)){
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
        ZonedDateTime utcEndZDT = ZonedDateTime.ofInstant (endDate.toInstant (), utcZoneId);
        ZonedDateTime myEndZDT = ZonedDateTime.ofInstant(utcEndZDT.toInstant(), ZoneId.systemDefault());
        String endDateUpd = String.valueOf(myEndZDT.toLocalDate());
        endDTUpd.setValue(LocalDate.parse(endDateUpd));
        String endTimeUpd = String.valueOf((myEndZDT.toLocalTime()));
        String [] parts = endTimeUpd.split (":");
        String endHours = parts[0];
        System.out.println (endHours + " end hours");
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
