package sample.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sample.Model.Appointment;
import sample.helper.QueryAppointment;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.text.ParseException;
import java.util.ResourceBundle;


/**This is a controller class initiating functionality of Appointments.fxml.
 * @author  Veronika Ramey
 * */
public class AppointmentsController implements Initializable {

    /**Column in appointments table for appointment id*/
    public TableColumn <Appointment, Integer> appointment_ID;

    /**Table view for appointments*/
    public TableView <Appointment> appointmentsTable;

    /**Column in appointments table for title*/
    public TableColumn <Appointment, String> titleColumn;

    /**Column in appointments table for description*/
    public TableColumn <Appointment, String> descriptionColumn;

    /**Column in appointments table for location*/
    public TableColumn <Appointment, String> locationColumn;


    /**Column in appointments table for contact*/
    public TableColumn <Appointment, Integer> contactColumn;

    /**Column in appointments table for type of appointment*/
    public TableColumn <Appointment, String> typeColumn;

    /**Column in appointments table for start of appointment*/
    public TableColumn<Appointment, String> startColumn;

    /**Column for appointment for end of appointment*/
    public TableColumn<Appointment, String> endColumn;

    /**Column for customer's id in the appointments table*/
    public TableColumn <Appointment, Integer> customerIdColumn;

    /**Column for user's id in the appointments table*/
    public TableColumn <Appointment, Integer> userIdColumn;

    /**Button used to add a new appointment*/
    public Button addAppButton;

    /**Button to update appointment*/
    public Button updateButton;

    /**Button to delete appointment*/
    public Button deleteButton;

    /**Button to go back to customer's screen*/
    public Button goBackButton;

    /**Toggle group to view appointments by month or week*/
    public ToggleGroup toggleGroup;

    /**Radio button to view appointments for a month*/
    public RadioButton viewByMonthRadio;

    /**Radio button to view appointments for a week*/
    public RadioButton viewByWeekRadio;

    /**Button used to generate reports*/
    public Button generateReportsButton;

    /**Button to go back to viewing unfiltered appointments*/
    public Button vewAll;


    /**Method is to populate appointments table. Method is used to populate appointments table with existing appointments from the database.
     */
    public void loadAppointments()  {
     ObservableList <Appointment> appointments = FXCollections.observableArrayList();
        try {
              appointments = QueryAppointment.selectAppointments();

        } catch (Exception e) {

            System.err.println(e.getMessage());
        }
        appointmentsTable.getItems().addAll(appointments);
    }


    /**Initializes controller. Method is used to initialize controller for appointments scene.
     * Method includes lambda expression for radio buttons, which are used to filter appointments table to show appointments for a month or a week.
     * @param url Describes resolving relative paths for the root object
     * @param resourceBundle The root object's localization resources, if root object is not localized - null.
     */
        @Override
        public void initialize (URL url, ResourceBundle resourceBundle){
            //columns configuration
            appointment_ID.setCellValueFactory(new PropertyValueFactory<>("id"));
            titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
            descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
            locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
            contactColumn.setCellValueFactory(new PropertyValueFactory<>("contact"));
            typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
            startColumn.setCellValueFactory(new PropertyValueFactory<>("startString"));
            endColumn.setCellValueFactory(new PropertyValueFactory<>("endString"));
            customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
            userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userID"));

        viewByMonthRadio.setOnAction((event) -> {
    try {
        appointmentsTable.setItems(QueryAppointment.selectMonthlyAppointments());
    } catch (SQLException throwables) {
        throwables.printStackTrace();
    } catch (ParseException e) {
        e.printStackTrace();
    }
        });

    viewByWeekRadio.setOnAction (event -> {
    try {
        appointmentsTable.setItems(QueryAppointment.selectWeeklyAppointments());
    }
    catch (SQLException | ParseException e){

        System.out.println (e.getMessage());
    }
    });
                loadAppointments();
        }


/**Method changes scene to add appointment scene. Method changes appointments scene to addAppointmentScreen.fxml.
 * @param event Action on add appointment button*/
      public void addAppButtonPushed(ActionEvent event) throws IOException {
        Stage stage;
        Parent scene;
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/sample/View/addAppointmentScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

/**Method is used to change the scene to update appointment scene. This method changes scene to updateAppointment.fxml when update button is pushed.
 * If no appointment is selected a warning alert is displayed.
 * @param event Action on update button pushed.*/
    public void updateButtonPushed(ActionEvent event)throws IOException, SQLException
    { if (appointmentsTable.getSelectionModel().getSelectedItem() == null){
        Utility.displayWarning(2);
    }
    else {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/sample/View/UpdateAppointment.fxml"));

        Parent tableViewParent = loader.load();
        Scene tableViewScene = new Scene(tableViewParent);
        UpdateAppointmentController controller = loader.getController();

        controller.populateData(appointmentsTable.getSelectionModel().getSelectedItem());

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(tableViewScene);
        window.show();
    }
    }

/**Method is for deleting an appointment. Method is used to delete an appointment upon delete button pushed.
 * If no appointment is selected a warning alert is displayed. When appointment is deleted an information alert is displayed.
 * @param event Action on delete button*/
    public void deleteButtonPushed(ActionEvent event) throws SQLException, ParseException {

        if (appointmentsTable.getSelectionModel().getSelectedItem() == null) {
            Utility.displayWarning(3);

        }
        else if (Utility.displayConfirmation(5)){
            Appointment appointmentDelete = appointmentsTable.getSelectionModel().getSelectedItem();
            int appointmentID = appointmentDelete.getId();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Deleted appointment ID: " + appointmentID + " type: " + QueryAppointment.appointmentType(appointmentID));
            alert.showAndWait();

            QueryAppointment.deleteAppointment(appointmentID);

            appointmentsTable.setItems(QueryAppointment.selectAppointments());

        }}

    /**Method is to change the scene back to customers view. Method is used to go back to Customers.fxml witout saving changes.
     * A confirmation alert is displayed before changing the scene.
     * @param event Action on go back button*/
    public void goBackPushed(ActionEvent event) throws IOException {
      if (Utility.displayConfirmation(2))  {
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Object scene = FXMLLoader.load(getClass().getResource("/sample/View/Customers.fxml"));
            stage.setScene(new Scene((Parent) scene));
            stage.show();
        }
    }


/**Method to change to reports screen and generate reports. Method is used to go to reports.fxml and view reports.
 * @param event Action on generate reports button*/
    public void generateReportsButtonPushed(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("/sample/View/reports.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }

    /***Method resets appointments table to view unfiltered appointments. Method is used to reset the table to view all appointments.
     *
     * @param event Action on view all button
     * @throws SQLException
     * @throws ParseException
     */
    public void viewAllPushed(ActionEvent event) throws SQLException, ParseException {
        appointmentsTable.setItems(QueryAppointment.selectAppointments());
    }
}


