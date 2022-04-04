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
import java.util.ResourceBundle;



/**This is a controller class initiating functionality of updateAppointment.fxml.
 * @author  Veronika Ramey
 * */
public class UpdateAppointmentController implements Initializable {

    /**Date picker to chose appointment's start date*/
    public DatePicker startUpdDT;

    /**Spinner to select appointment's start hour*/
    public Spinner hourStartSpinneUpd;

    /**Spinner to select appointment's start minute*/
    public Spinner minuteStartSpinnerUpd;

    /**Text field for appointment's id*/
    public TextField appointmentIdUpdTextfield;

    /**Text field for appointment's title*/
    public TextField titleUpdTextField;

    /**Text field for appointment's description*/
    public TextField descriptionUpdTextfield;

    /**Text field for appointment's location*/
    public TextField locationUpdTextfield;

    /**Text field for appointment's type*/
    public TextField typeUpdTextfield;

    /**Text field for user's id*/
    public TextField userIdUpdTextfield;

    /**Combo box to select name of contact for the appointment*/
    public ComboBox selectContactNameUpd;

    /**Text field for customer's id*/
    public TextField customerUpdTextfield;

    /**Date picker to chose appointment's end date*/
    public DatePicker endDTUpd;

    /**Spinner to select appointment's end hour*/
    public Spinner hourEndSpinnerUpd;

    /**Spinner to select appointment's end minute*/
    public Spinner minuteEndSpinnerUpd;

    /**Button to save updated appointment*/
    public Button saveButtonUpd;

    /**Button to go back without saving*/
    public Button cancelButtonUpd;

    /**Selected appointment*/
    public Appointment selectedAppointment;


    /**Method saves updated appointment. This method saves edited appointment after input validation.
     * Upon saving redirects to appointments main screen
     * @param event Action on save button*/
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


             if (title.isEmpty() || description.isEmpty() || location.isEmpty() || type.isEmpty()
                    || customerUpdTextfield.getText().isEmpty() || userIdUpdTextfield.getText().isEmpty() || contactName == null ||
            startdt == null || enddt ==null ||startHour.isEmpty() || endHour.isEmpty()) {
                Utility.displayErrorAlert(1);

            }

            else if (Utility.validateBusinessHours(utcStartZDT, utcEndZDT)){
                Utility.displayWarning(4);

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


    /**Method redirects back to appointments vew without saving. Method is used to go back to appointments.fxml scene without saving after confirmation alert is displayed.
     * @param event Action on cancel button
     * @throws IOException*/
    public void cancelButtonPushed(ActionEvent event) throws IOException
    {  if ( Utility.displayConfirmation(1)){
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Object scene = FXMLLoader.load(getClass().getResource("/sample/View/Appointments.fxml"));
            stage.setScene(new Scene((Parent) scene));
            stage.show();
        }
    }


    /**Initializes controller. Method is used to initialize controller for update appointment scene.
     * @param url Describes resolving relative paths for the root object
     * @param resourceBundle The root object's localization resources, if root object is not localized - null.
     */
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

    /**Method to initialize the view. The method populates data to initialize the view.
     * @param appointment Selected appointment
     * @throws SQLException */
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
