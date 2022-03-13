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
import sample.helper.JDBC;
import sample.helper.Query;



import java.io.IOException;
import java.net.URL;
import java.sql.*;
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
   // private EventQueueItem Node;

    public void loadAppointments() throws SQLException {
     ObservableList <Appointment> appointments = FXCollections.observableArrayList();
        try {
            JDBC.openConnection();
           /** String sql = "SELECT Appointment_ID, Title, Description, Location, Type, Contact_ID, Start, End, Customer_ID, User_ID from  APPOINTMENTS";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {

                int id = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                int contact = rs.getInt("Contact_ID");
                String type = rs.getString("Type");
                Timestamp startDateTime = rs.getTimestamp("Start");
                Timestamp endDateTime = rs.getTimestamp("End");
                int customerID = rs.getInt("Customer_ID");
                int userID = rs.getInt("User_ID");

                Appointment newAppointment = new Appointment(id, title, description, contact, location, type,
                        startDateTime, endDateTime, customerID, userID);
                appointments.add(newAppointment);*/
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

    public void updateButtonPushed(ActionEvent event) {
    }

    public void deleteButtonPushed(ActionEvent event) {
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
        Parent tableParent = FXMLLoader.load(getClass().getResource("/sample/View/filteredMonthlyViewController.fxml"));
        Scene newScene = new Scene(tableParent);

        Stage window = (Stage)((Node) event.getSource()).getScene().getWindow();
        window.setScene(newScene);
        window.show();
    }

    public void viewByWeekSelected(ActionEvent event) throws IOException {
    /**    Stage stage;
        Parent scene;
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();*/
Parent tableParent = FXMLLoader.load(getClass().getResource("/sample/View/filteredWeeklyAppView.fxml"));
Scene newScene = new Scene(tableParent);

Stage window = (Stage)((Node) event.getSource()).getScene().getWindow();
window.setScene(newScene);
window.show();
      //FXMLLoader loader = new FXMLLoader (getClass().getResource("/sample/View/filteredWeeklyAppView.fxml"));
        /**stage.setScene(new Scene(scene));
        stage.show();*/
      //  loader.load();


    }
}


