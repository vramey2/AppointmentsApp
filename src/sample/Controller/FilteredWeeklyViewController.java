package sample.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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
import java.sql.SQLException;
import java.time.*;
import java.util.Optional;
import java.util.ResourceBundle;

public class FilteredWeeklyViewController implements Initializable {
    public Label weeklyLabel;
    public TableView <Appointment> appointmentsWeeklyTable;
    public TableColumn <Appointment, String> titleColumn;
    public TableColumn <Appointment, String> descriptionColumn;
    public TableColumn <Appointment, String> locationColumn;
    public TableColumn <Appointment, Integer> contactColumn;
    public TableColumn <Appointment, String> typeColumn;
    public TableColumn<Appointment, String> startColumn;
    public TableColumn<Appointment, String> endColumn;
    public TableColumn <Appointment, Integer> customerIdColumn;
    public TableColumn <Appointment, Integer> userIdColumn;
    public TableColumn <Appointment, Integer> appointment_ID;
    public Button prevWeekButton;
    public Button nextWeekButton;
    public Button goBackToAllButton;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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

            loadWeeklyAppointments();
        } catch (SQLException e) {

            System.out.println(e.getMessage());
        }
    }

        public void loadWeeklyAppointments () throws SQLException {
            ObservableList<Appointment> appByWeek = FXCollections.observableArrayList();
            try {
                JDBC.openConnection();
                appByWeek = Query.selectWeeklyAppointments();

            } catch (Exception e) {

                System.err.println(e.getMessage());
            }
            appointmentsWeeklyTable.getItems().addAll(appByWeek);
        }

public void loadPrevWeeklyAppointments () throws SQLException

    {
        ObservableList<Appointment> appPrevByWeek = FXCollections.observableArrayList();
   /**     ObservableList <Appointment> visibleAppointments = FXCollections.observableArrayList();
        visibleAppointments = appointmentsWeeklyTable.getItems();
        LocalDateTime startDT;
        while (visibleAppointments.iterator().hasNext()){



        }*/
        try {
            JDBC.openConnection();
            appPrevByWeek = Query.selectPreviousWeeklyAppointments();

        } catch (Exception e) {

            System.err.println(e.getMessage());
        }
        appointmentsWeeklyTable.setItems(appPrevByWeek);

    }

    public void prevWeekButtonPushed(ActionEvent event) throws IOException {
         
        try {
            loadPrevWeeklyAppointments();
        } catch (SQLException e) {

            System.out.println(e.getMessage());
        }

        }

    public void loadNextWeeklyAppointments () throws SQLException

    {
        ObservableList<Appointment> appNextByWeek = FXCollections.observableArrayList();
        try {
            JDBC.openConnection();
            appNextByWeek = Query.selectNextWeeklyAppointments();

        } catch (Exception e) {

            System.err.println(e.getMessage());
        }
        appointmentsWeeklyTable.setItems(appNextByWeek);
    }

    public void nextWeekButtonPushed(ActionEvent event) {
        try {
            loadNextWeeklyAppointments();
        } catch (SQLException e) {

            System.out.println(e.getMessage());
        }

    }

    public void goBackToAllPushed(ActionEvent event) throws IOException {
        Alert alert = new Alert (Alert.AlertType.CONFIRMATION);
        alert.setContentText("Do you want to go back to view unfiltered appointments?");
        Optional<ButtonType> result = alert.showAndWait();
        // alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Object scene = FXMLLoader.load(getClass().getResource("/sample/View/Appointments.fxml"));
            stage.setScene(new Scene((Parent) scene));
            stage.show();
        }
    }
}
