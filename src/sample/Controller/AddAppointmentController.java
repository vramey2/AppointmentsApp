package sample.Controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import sample.helper.Query;
import sample.helper.QueryAppointment;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.*;
import java.util.ResourceBundle;

/**This is a controller class initiating functionality of addAppointmentScreen.fxml.
 *
 * @author Veronika Ramey
 */
public class AddAppointmentController implements Initializable {

    /**Text field for appointment's id*/
    public TextField appointmentIdTextfield;

    /**Text field for appointment's title*/
    public TextField titleTextField;

    /**Text field for appointment's description*/
    public TextField descriptionTextfield;

    /**Text field for appointment's location*/
    public TextField locationTextfield;

    /**Text field for type of appointment*/
     public TextField typeTextfield;

     /**Text field for customer's id*/
    public TextField customerTextfield;

    /**Text fiedl for user's id*/
    public TextField userIdTextfield;

    /**Button to save the new customer*/
    public Button saveButton;

    /**Button to cancel and go back to main appointments screen*/
    public Button cancelButton;

    /**Date picker to chose end date for appointment*/
    public DatePicker endDT;

    /**Date picker to chose start date for appointment*/
    public DatePicker startDT;

    /**Spinner to chose starting hour for appointment*/
     public Spinner hourStartSpinner;

     /**Spinner to chose starting minute for appointment*/
    public Spinner minuteStartSpinner;

    /**Spinner to chose ending hour for appointment*/
    public Spinner hourEndSpinner;

    /**Spinner to chose endign minute for appointment*/
    public Spinner minuteEndSpinner;

    /**Combobox to select name of appointment's contact*/
    public ComboBox <String>  selectContactName;


    /**Changes scene to appointments scree - Appointments.fxml.
     * This method is used to go back to appointments screen without saving changes once the user confirms the action.
     * @param  event Action on cancel button
     * @throws IOException
     */
    public void cancelButtonPushed(ActionEvent event) throws IOException {

      if (Utility.displayConfirmation(1)){
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Object scene = FXMLLoader.load(getClass().getResource("/sample/View/Appointments.fxml"));
            stage.setScene(new Scene((Parent) scene));
            stage.show();
        }
    }

    /**Initializes controller. Method is used to initialize controller for add apoointment scene.
     *
     * @param url Describes resolving relative paths for the root object
     * @param resourceBundle The root object's localization resources, if root object is not localized null."/
     */

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        appointmentIdTextfield.setEditable(false);

        try {
            selectContactName.setItems(Query.selectContacts());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


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

    String startDT = Utility.convertTime(startHour, startMinute, startdt);
    String endDT = Utility.convertTime (endHour, endMinute, enddt);
    ZonedDateTime utcStartZDT = Utility.convertUTCTime(startHour, startMinute, startdt);
    ZonedDateTime utcEndZDT = Utility.convertUTCTime (endHour, endMinute, enddt);

            if ( Utility.validateBusinessHours(utcStartZDT, utcEndZDT)){
                Utility.displayWarning(1);
            }
            else if (title.isEmpty() || description.isEmpty() || location.isEmpty() || type.isEmpty()
            || customerTextfield.getText().isEmpty() || userIdTextfield.getText().isEmpty() || contactName == null  ) {
              Utility.displayErrorAlert(1);

            }
            else if (utcStartZDT.isAfter(utcEndZDT)){
               Utility.displayErrorAlert(2);
            }
            else if (Query.customerExists(customerID)){

              Utility.displayErrorAlert(3);

    }
            else if (Query.userExists(userID)){

                Utility.displayErrorAlert(4);

            }
            else  {
                ObservableList<Integer> overlapID  = QueryAppointment.checkforOverlaps(customerID, startDT, endDT);
                if (!overlapID.isEmpty()){
                  Utility.displayErrorAlert(5);
                }
                else {

                int rowsAffected = QueryAppointment.insertAppointment(title, description, location, type, startDT, endDT, customerID, userID, Query.contactID(contactName));
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

            Utility.displayErrorAlert(1);


        }
    }}
