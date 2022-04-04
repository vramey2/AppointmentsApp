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
import sample.Model.Customer;
import sample.helper.JDBC;
import sample.helper.Query;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;


/**This is a controller class initiating functionality of Customers.fxml.
 * @author  Veronika Ramey
 * */
public class CustomersController implements Initializable {

    /**Table view for customers*/
    public TableView<Customer> TableCustomers;

    /**Column of customers table for customer's ID*/
    public TableColumn<Customer, Integer> Customer_ID;

    /**Column of the customers table for customer's name*/
    public TableColumn<Customer, String> Customer_Name;

    /**Column of the customers table for customer's address*/
    public TableColumn<Customer, String> Address;

    /**Column of the customers table for customer's zip code*/
    public TableColumn<Customer, String> Postal_Code;

    /**Column of the customers table for customer's phone number*/
    public TableColumn<Customer, String> Phone;

    /**Button to add a new customer*/
    public Button addButton;

    /**Column of the customers table for customer's country*/
    public TableColumn<Customer, String> CountryColumn;

    /**Column of the customers table for customer's division*/
    public TableColumn<Customer, String> DivisionColumn;

    /**Button to delete a customer*/
    public javafx.scene.control.Button deleteButton;

    /**Button to exit application*/
    public javafx.scene.control.Button exitButton;

    /**Button to update customer*/
    public Button updateButton;

    /** Object of customer that was selected by the user */
    private static Customer selectedCustomer;



    /**Method is to populate customers table. Method is used to populate customers table with existing customers from the database.
      */
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


    /**Initializes controller for customers view scene. Method is used to initialize controller for customers scene - Customers.fxml.
      * @param url Describes resolving relative paths for the root object
     * @param resourceBundle The root object's localization resources, if root object is not localized - null.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

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

    /**Method changes a scene when appointments button is pushed. The method changes customers scene to appointments scene when appointments button is pushed.
     *
     * @param event Action on vew appointments button
     * @throws IOException
     */
    public void appointmentsButtonClicked(ActionEvent event) throws IOException {

        Stage stage;
        Parent scene;
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/sample/View/Appointments.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }


    /**Method changes scene to add customer scene. Method changes customers scene to addCustomerScreen.fxml.
     * @param event Action on add customer button*/
    public void addButtonPushed(ActionEvent event) throws IOException {
        Stage stage;
        Parent scene;
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/sample/View/addCustomerScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }


    /**Method is for deleting a customer. Method is used to delete a customer after delete button is pushed.
     * If no customer is selected a warning alert is displayed. If a customer has appointments scheduled a warning is displayed to delete existing appointments first.
     *  @param event Action on delete button*/
    public void deleteButtonPushed(ActionEvent event) throws SQLException {

        if (TableCustomers.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert ( Alert.AlertType.WARNING);
            alert.setHeaderText("Please select a customer to delete!");
            alert.showAndWait();
        }
        else {
            if (Utility.displayConfirmation(4)){


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


    /**Method is used to change the scene to update customers scene. This method changes scene to updateCustomer.fxml when update button is pushed.
     * If no customer is selected a warning alert is displayed.
     * @param event Action on update button*/
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

    /**Method is used to exit application. This method is used to excit application and close database connection, a confirmation is asked before closing out
     * @param event Action on exit button*/
    public void exitPushed(ActionEvent event) {
        if (Utility.displayConfirmation(3)){
            JDBC.closeConnection();
            Stage stage = (Stage) exitButton.getScene().getWindow();
            stage.close();
        }
    }
}









