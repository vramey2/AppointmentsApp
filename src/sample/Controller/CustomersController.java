package sample.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sample.Model.Customer;
import sample.helper.JDBC;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ResourceBundle;

public class CustomersController implements Initializable {

    public TableView <Customer> TableCustomers;
    public TableColumn <Customer,  Integer> Customer_ID;
    public TableColumn <Customer, String> Customer_Name;
    public TableColumn <Customer, String> Address;
    public TableColumn <Customer, String> Postal_Code;
    public TableColumn <Customer, String> Phone;
    public Button addButton;
    private Object Button;


    //To load from the database

    public void loadCustomers() throws SQLException {
        ObservableList<Customer> customers = FXCollections.observableArrayList();

        /**Connection connection = null;
         Statement statement = null;
         ResultSet resultSet = null;*/

        try {
            JDBC.openConnection();
            // assert false;
            String sql = " SELECT Customer_ID, Customer_Name, Address, Postal_Code, Phone FROM CUSTOMERS";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ResultSet resultSet = ps.executeQuery();
            //  Statement statement = JDBC.connection connection.createStatement();

            // resultSet = statement.executeQuery("SELECT * FROM CUSTOMERS");

            //CREATE CUSTOMER OBJECT FROM EACH RECORD
            while (resultSet.next()){

                int customerID = resultSet.getInt("Customer_ID");
                String customerName = resultSet.getString("Customer_Name");
                String customerAddress = resultSet.getString ("Address");
                String zipCode = resultSet.getString ("Postal_Code");
                String phoneNumber = resultSet.getString ("Phone");

                Customer newCustomer = new Customer (customerID, customerName, customerAddress, zipCode, phoneNumber);
                customers.add (newCustomer);

/**
 Customer newCustomer = new Customer(resultSet.getInt ("Customer_ID"), resultSet.getString ("Customer_Name"),
 resultSet.getString("Address"), resultSet.getInt("Postal_Code"), resultSet.getString("Phone"));
 customers.add(newCustomer);
 System.out.println(newCustomer);*/
                System.out.println (customerID + customerName + customerAddress + zipCode + phoneNumber);
                System.out.println ("");
                System.out.println ("in the while");
            }
            TableCustomers.getItems().addAll(customers);

        }
        catch (Exception e){

            System.err.println (e.getMessage());
        }

        /**  finally{
         if (connection!=null)
         JDBC.closeConnection();
         if (statement !=null)
         statement.close();
         if (resultSet != null)
         resultSet.close();
*/}

         @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

/**        ZoneId myZoneID = ZoneId.systemDefault();
             ZonedDateTime myZDT = ZonedDateTime.of(myLDT, myZoneID);
        System.out.println (myLDT);

        //convert to utc
             ZoneId utcZoneID = ZoneId.of("UTC");
             ZonedDateTime utcZDT = ZonedDateTime.ofInstant(myZDT.toInstant(), utcZoneID);

             try {
                 JDBC.openConnection();
                 String sql = "SELECT Start, End from Appointments WHERE Start BETWEEN ? AND ? ";
                 PreparedStatement ps = JDBC.connection.prepareStatement(sql);
                 ps.setObject(1, utcZDT);
                 ps.setObject(2, utcZDT.plusMinutes(15));
                 ResultSet resultSet = ps.executeQuery();
                 Alert alert = new Alert(Alert.AlertType.ERROR);

                 //CREATE CUSTOMER OBJECT FROM EACH RECORD
                if  (resultSet.next()) {
                     int appointmentID = resultSet.getInt("Appointment_ID");
                     Timestamp start = resultSet.getTimestamp("Start");
                     Timestamp end = resultSet.getTimestamp("End");

                    alert.setHeaderText("You have upcoming appointment! Appointment ID: " + appointmentID + " starting " + start + " and ending " + end);
                    alert.showAndWait();


                 }
                else {
                     alert.setHeaderText ("You don't have upcoming appointments!");
                     System.out.println ("In the else");
                }
             }
            catch (Exception e){

                 System.err.println (e.getMessage());
             }
*/

        //columns configuration
        Customer_ID.setCellValueFactory(new PropertyValueFactory<Customer, Integer>("customerId"));
        Customer_Name.setCellValueFactory(new PropertyValueFactory<Customer, String>("customerName"));
        Address.setCellValueFactory(new PropertyValueFactory<Customer, String>("customerAddress"));
        Postal_Code.setCellValueFactory(new PropertyValueFactory <Customer, String> ("zipCode"));
        Phone.setCellValueFactory(new PropertyValueFactory<Customer, String>("phoneNumber"));

        try{
            loadCustomers();
        }
        catch(SQLException e){

            System.out.println (e.getMessage());
        }



    }

    public void appointmentsButtonClicked(ActionEvent event) throws IOException {

        Stage stage;
        Parent scene;
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/sample/View/Appointments.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    public void addButtonPushed(ActionEvent event) throws IOException {
        Stage stage;
        Parent scene;
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/sample/View/addCustomerScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }
}








