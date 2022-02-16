package sample.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
import java.util.ResourceBundle;

public class CustomersController implements Initializable {

    public TableView <Customer> TableCustomers;
    public TableColumn <Customer,  Integer> Customer_ID;
    public TableColumn <Customer, String> Customer_Name;
    public TableColumn <Customer, String> Address;
    public TableColumn <Customer, String> Postal_Code;
    public TableColumn <Customer, String> Phone;


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
}








