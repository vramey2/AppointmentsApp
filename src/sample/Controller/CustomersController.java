package sample.Controller;

import com.mysql.cj.xdevapi.Table;
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
import sample.Model.Appointment;
import sample.Model.Customer;
import sample.helper.JDBC;
import sample.helper.Query;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ResourceBundle;

public class CustomersController implements Initializable {

    public TableView<Customer> TableCustomers;
    public TableColumn<Customer, Integer> Customer_ID;
    public TableColumn<Customer, String> Customer_Name;
    public TableColumn<Customer, String> Address;
    public TableColumn<Customer, String> Postal_Code;
    public TableColumn<Customer, String> Phone;
    public Button addButton;
    public TableColumn<Customer, String> CountryColumn;
    public TableColumn<Customer, String> DivisionColumn;
    public javafx.scene.control.Button deleteButton;
    private Object Button;
    private int Division_ID;


    //To load from the database
    public void loadCustomers() throws SQLException {
        ObservableList<Customer> customers = FXCollections.observableArrayList();
        try {
            JDBC.openConnection();

            customers = Query.selectCustomers();

        } catch (Exception e) {

            System.err.println(e.getMessage());
        }
        TableCustomers.getItems().addAll(customers);


    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //columns configuration
        Customer_ID.setCellValueFactory(new PropertyValueFactory<Customer, Integer>("customerId"));
        Customer_Name.setCellValueFactory(new PropertyValueFactory<Customer, String>("customerName"));
        Address.setCellValueFactory(new PropertyValueFactory<Customer, String>("customerAddress"));
        Postal_Code.setCellValueFactory(new PropertyValueFactory<Customer, String>("zipCode"));
        Phone.setCellValueFactory(new PropertyValueFactory<Customer, String>("phoneNumber"));
        CountryColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("country"));
        DivisionColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("division"));
        // DivisionIDColumn.setCellValueFactory(new PropertyValueFactory<Customer, Integer>("divisionId"));

        try {
            loadCustomers();
        } catch (SQLException e) {

            System.out.println(e.getMessage());
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

    public void deleteButtonPushed(ActionEvent event) throws SQLException {
        System.out.println ("Delete pushed");
        if (TableCustomers.getSelectionModel().getSelectedItem() == null) {
            System.out.println("Please select first!");
        }
        else {

            Customer customerDelete = TableCustomers.getSelectionModel().getSelectedItem();
            int customerID = customerDelete.getCustomerId();

            int rowsAffected = Query.checkForAppointments(customerID);
            if (rowsAffected > 0) {
                System.out.println("Please delete appointment first!");
            } else
                Query.deleteCustomer(customerID);
            TableCustomers.setItems(Query.selectCustomers());
        }

    }
}









