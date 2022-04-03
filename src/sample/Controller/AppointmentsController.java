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
import sample.helper.QueryAppointment;

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
    public Button vewAll;
    // private EventQueueItem Node;

    public void loadAppointments()  {
     ObservableList <Appointment> appointments = FXCollections.observableArrayList();
        try {


               appointments = QueryAppointment.selectAppointments();

        } catch (Exception e) {

            System.err.println(e.getMessage());
        }
        appointmentsTable.getItems().addAll(appointments);
    }

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
    public void goBackPushed(ActionEvent event) throws IOException {
      if (Utility.displayConfirmation(2))  {
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Object scene = FXMLLoader.load(getClass().getResource("/sample/View/Customers.fxml"));
            stage.setScene(new Scene((Parent) scene));
            stage.show();
        }
    }




    public void generateReportsButtonPushed(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("/sample/View/reports.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }

    public void viewAllPushed(ActionEvent event) throws SQLException, ParseException {
        appointmentsTable.setItems(QueryAppointment.selectAppointments());
    }
}


