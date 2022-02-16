package sample.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.Model.Appointment;
import sample.Model.Customer;
import sample.helper.JDBC;
import sample.helper.Query;


import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class AppointmentsController implements Initializable {


    public TableColumn <Appointment, Integer> appointment_ID;
    public TableView <Appointment> appointmentsTable;
    public TableColumn <Appointment, String> titleColumn;
    public TableColumn <Appointment, String> descriptionColumn;
    public TableColumn <Appointment, String> locationColumn;
    public TableColumn <Appointment, Integer> contactColumn;
    public TableColumn <Appointment, String> typeColumn;
    public TableColumn <Appointment, Timestamp> startColumn;
    public TableColumn <Appointment, Timestamp>  endColumn;
    public TableColumn <Appointment, Integer> customerIdColumn;
    public TableColumn <Appointment, Integer> userIdColumn;

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
            startColumn.setCellValueFactory(new PropertyValueFactory<Appointment, Timestamp>("startDateTime"));
            endColumn.setCellValueFactory(new PropertyValueFactory<Appointment, Timestamp>("endDateTime"));
            customerIdColumn.setCellValueFactory(new PropertyValueFactory<Appointment, Integer>("customerID"));
            userIdColumn.setCellValueFactory(new PropertyValueFactory<Appointment, Integer>("userID"));

            try {
                loadAppointments();
            } catch (SQLException e) {

                System.out.println(e.getMessage());
            }
        }


    }


