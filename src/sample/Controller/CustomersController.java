package sample.Controller;

import com.mysql.cj.xdevapi.Table;
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
import sample.Controller.UpdateCustomerController;
import sample.helper.Query;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;
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
    /** Object of customer that was selected by the user */
    private static Customer selectedCustomer;


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

        if (TableCustomers.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert ( Alert.AlertType.WARNING);
            alert.setHeaderText("Please select a customer to delete!");
            alert.showAndWait();
        }
        else {
            Alert confirm = new Alert ( Alert.AlertType.CONFIRMATION);
           confirm.setHeaderText("Do you want to delete customer?");
             Optional<ButtonType> result = confirm.showAndWait();
            // alert.showAndWait();
            if (result.get() == ButtonType.OK) {

            Customer customerDelete = TableCustomers.getSelectionModel().getSelectedItem();
            int customerID = customerDelete.getCustomerId();

            int rowsAffected = Query.checkForAppointments(customerID);
            if (rowsAffected > 0) {
                Alert alert = new Alert ( Alert.AlertType.WARNING);
                alert.setHeaderText("Please delete appointment first!");
                alert.showAndWait();
            } else {

                Query.deleteCustomer(customerID);

            Alert alert = new Alert (Alert.AlertType.INFORMATION);
            alert.setContentText("Customer deleted");
            alert.showAndWait();}

            TableCustomers.setItems(Query.selectCustomers());
        }

    }}

    public void updateButtonPushed(ActionEvent event) throws IOException, SQLException {
        if (TableCustomers.getSelectionModel().getSelectedItem() == null){
            Alert alert = new Alert ( Alert.AlertType.WARNING);
            alert.setHeaderText("Please select a customer to update!");
            alert.showAndWait();
        }
        else {


FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/sample/View/UpdateCustomer.fxml"));

            Parent tableViewParent = loader.load();
            Scene tableViewScene = new Scene(tableViewParent);
            UpdateCustomerController controller = loader.getController();

            controller.populateData(TableCustomers.getSelectionModel().getSelectedItem());

            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(tableViewScene);
            window.show();
        }
    }
}









