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
import sample.Model.Customer;
import sample.helper.JDBC;
import sample.helper.Query;



import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.text.ParseException;
import java.util.Optional;
import java.util.ResourceBundle;

public class AppointmentsController implements Initializable {


    public TableColumn <Appointment, Integer> appointment_ID;
    public TableView <Appointment> appointmentsTable;
    public TableColumn <Appointment, String> titleColumn;
    public TableColumn <Appointment, String> descriptionColumn;
    public TableColumn <Appointment, String> locationColumn;
    public TableColumn <Appointment, Integer> contactColumn;
    public TableColumn <Appointment, String> typeColumn;
    public TableColumn<Appointment, String> startColumn;
    public TableColumn<Appointment, String> endColumn;
    public TableColumn <Appointment, Integer> customerIdColumn;
    public TableColumn <Appointment, Integer> userIdColumn;
    public Button addAppButton;
    public Button updateButton;
    public Button deleteButton;
    public Button goBackButton;
    public ToggleGroup toggleGroup;
    public RadioButton viewByMonthRadio;
    public RadioButton viewByWeekRadio;
    public Label previousButton;
    public Button generateReportsButton;
    // private EventQueueItem Node;

    public void loadAppointments() throws SQLException {
     ObservableList <Appointment> appointments = FXCollections.observableArrayList();
        try {
            JDBC.openConnection();

               appointments = Query.selectAppointments();

        } catch (Exception e) {

            System.err.println(e.getMessage());
        }
        appointmentsTable.getItems().addAll(appointments);
    }

        @Override
        public void initialize (URL url, ResourceBundle resourceBundle){
            //columns configuration
            appointment_ID.setCellValueFactory(new PropertyValueFactory<Appointment, Integer>("id"));
            titleColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("title"));
            descriptionColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("description"));
            locationColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("location"));
            contactColumn.setCellValueFactory(new PropertyValueFactory<Appointment, Integer>("contact"));
            typeColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("type"));
            startColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("startString"));
            endColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("endString"));
            customerIdColumn.setCellValueFactory(new PropertyValueFactory<Appointment, Integer>("customerID"));
            userIdColumn.setCellValueFactory(new PropertyValueFactory<Appointment, Integer>("userID"));


            try {
                loadAppointments();
            } catch (SQLException e) {

                System.out.println(e.getMessage());
            }
        }




    public void addAppButtonPushed(ActionEvent event) throws IOException {
        Stage stage;
        Parent scene;
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/sample/View/addAppointmentScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }


    public void updateButtonPushed(ActionEvent event)throws IOException, SQLException
    { if (appointmentsTable.getSelectionModel().getSelectedItem() == null){
        Alert alert = new Alert ( Alert.AlertType.WARNING);
        alert.setHeaderText("Please select an appointment to update!");
        alert.showAndWait();
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


    public void deleteButtonPushed(ActionEvent event) throws SQLException, ParseException {

        if (appointmentsTable.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert ( Alert.AlertType.WARNING);
            alert.setHeaderText("Please select appointment to delete!");
            alert.showAndWait();
           // System.out.println("Please select first!");
        }
        else {
            Appointment appointmentDelete = appointmentsTable.getSelectionModel().getSelectedItem();
            int appointmentID = appointmentDelete.getId();
            Alert alert = new Alert (Alert.AlertType.INFORMATION);
            alert.setContentText("Deleted appointment ID: " + appointmentID +" type: " + Query.appointmentType(appointmentID));
            alert.showAndWait();

            Query.deleteAppointment (appointmentID);

            appointmentsTable.setItems(Query.selectAppointments());
        }

    }


    public void goBackPushed(ActionEvent event) throws IOException {

        Alert alert = new Alert (Alert.AlertType.CONFIRMATION);
        alert.setContentText("Do you want to go back without saving?");
        Optional<ButtonType> result = alert.showAndWait();
        // alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Object scene = FXMLLoader.load(getClass().getResource("/sample/View/Customers.fxml"));
            stage.setScene(new Scene((Parent) scene));
            stage.show();
        }
    }

    public void viewByMonthSelected(ActionEvent event) throws IOException {

        try {
            appointmentsTable.setItems(Query.selectMonthlyAppointments());
           // previousButton.setVisible(true);
        } catch (SQLException | ParseException e) {

            System.out.println(e.getMessage());
        }
    }


    public void viewByWeekSelected(ActionEvent event) throws IOException {

    try {
        appointmentsTable.setItems(Query.selectWeeklyAppointments());
    }
    catch (SQLException | ParseException e){

        System.out.println (e.getMessage());
    }
    }

    public void generateReportsButtonPushed(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("/sample/View/reports.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }
}


