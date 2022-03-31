package sample.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.converter.LocalTimeStringConverter;
import sample.helper.Query;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoField;
import java.util.Date;
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
    public ComboBox <String>  selectContactName;
    //ObservableList<String> selectContactList = FXCollections.observableArrayList();


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
        try {
            selectContactName.setItems(Query.selectContacts());
        } catch (SQLException throwables) {
            throwables.printStackTrace();}

        endDT.getEditor().setDisable(true);
        startDT.getEditor().setDisable(true);
hourStartSpinner.getEditor().setDisable(true);
hourEndSpinner.getEditor().setDisable(true);
minuteStartSpinner.getEditor().setDisable(true);
minuteEndSpinner.getEditor().setDisable(true);




        //configure the spinner with values of 0-24
        SpinnerValueFactory <Integer> starthourValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23);
        this.hourStartSpinner.setValueFactory(starthourValueFactory);

    SpinnerValueFactory <Integer> startminuteValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59);
        this.minuteStartSpinner.setValueFactory(startminuteValueFactory);


        SpinnerValueFactory <Integer> endhourValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23);
        this.hourEndSpinner.setValueFactory(endhourValueFactory);

        SpinnerValueFactory <Integer> endtminuteValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59);
        this.minuteEndSpinner.setValueFactory(endtminuteValueFactory);

}
    public void saveButtonPushed(ActionEvent event) throws SQLException, IOException {

try {

            String title = titleTextField.getText();

            String description = descriptionTextfield.getText();
            //int contact = Integer.parseInt(contactTextfield.getText());
            String location = locationTextfield.getText();
            String type = typeTextfield.getText();
            int customerID = Integer.parseInt(customerTextfield.getText());
            int userID = Integer.parseInt(userIdTextfield.getText());
            System.out.println (userID);
            String contactName = selectContactName.getValue();

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
            || customerTextfield.getText().isEmpty() || userIdTextfield.getText().isEmpty() || contactName == null  ) {
              Utility.displayErrorAlert();

            }
            else if (localStartDateTime.isAfter(localEndDateTime)){
                Alert alert= new Alert (Alert.AlertType.ERROR);
                alert.setContentText ("Appointment start time cannnot be after end time!");
                alert.showAndWait();
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
                ObservableList<Integer> overlapID = FXCollections.observableArrayList();
               overlapID = Query.checkforOverlaps(customerID, startDT, endDT);
                if (!overlapID.isEmpty()){
                    Alert alert = new Alert (Alert.AlertType.ERROR);
                    alert.setContentText("Appointment overlapps with a different appointment!");
                    alert.showAndWait();
                }
                else {

                int rowsAffected = Query.insertAppointment(title, description, location, type, startDT, endDT, customerID, userID, Query.contactID(contactName));
                if (rowsAffected > 0) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("Appointment Added!");

                    alert.showAndWait();

                    Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                    Object scene = FXMLLoader.load(getClass().getResource("/sample/View/Appointments.fxml"));
                    stage.setScene(new Scene((Parent) scene));
                    stage.show();
                }}

        } } catch (NumberFormatException e) {

            Utility.displayErrorAlert();


        }
    }}
