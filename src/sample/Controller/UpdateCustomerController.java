package sample.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import sample.Model.Customer;
import sample.helper.Query;


import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class UpdateCustomerController implements Initializable {
    public ComboBox countryComboboxUpdateCustomer;
    public ComboBox selectDivisionUpdateCCombobox;
    public TextField customerUpdIdTextfield;
    public TextField customerUpdAddressTextfield;
    public TextField postalCodeUpdTextfield;
    public TextField phoneNumberUpdTextfield;
    public Button saveUpdButton;
    public Button cancelUpdButton;
    public Customer selectedCustomer;
    public TextField customerUpdNameTextField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Initialized");
        customerUpdIdTextfield.setEditable(false);
        selectDivisionUpdateCCombobox.setDisable(true);

        countryComboboxUpdateCustomer.valueProperty().addListener((observableValue, o, t1) -> {
            String selectedCountry = (String) countryComboboxUpdateCustomer.getValue();

            if (selectedCountry != null)
                selectDivisionUpdateCCombobox.setDisable(false);

            try {


                int countryId = Query.CountryID(selectedCountry);
                System.out.println("Country ID is " + countryId);
                selectDivisionUpdateCCombobox.setItems(Query.loadDivisions(countryId));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }


        });
            }

//to populate the view.
    public void populateData (Customer customer) throws SQLException {
        selectedCustomer = customer;

        customerUpdIdTextfield.setText(String.valueOf(selectedCustomer.getCustomerId()));
        customerUpdAddressTextfield.setText(selectedCustomer.getCustomerAddress());
        customerUpdNameTextField.setText(selectedCustomer.getCustomerName());
       phoneNumberUpdTextfield.setText(selectedCustomer.getPhoneNumber());
        postalCodeUpdTextfield.setText(selectedCustomer.getZipCode());


        countryComboboxUpdateCustomer.setItems(Query.loadAllCountries());
        countryComboboxUpdateCustomer.setValue (selectedCustomer.getCountry());
        int country_ID = Query.CountryID(selectedCustomer.getCountry());



        selectDivisionUpdateCCombobox.setItems(Query.loadAllDivisions());
        selectDivisionUpdateCCombobox.setValue(selectedCustomer.getDivision());

    }



   public void saveUpdButtonPushed(ActionEvent event) throws SQLException, IOException {
        String customerName = customerUpdNameTextField.getText();
        String customerAddress = customerUpdAddressTextfield.getText();
        String zipCode = postalCodeUpdTextfield.getText();
        String phoneNumber = phoneNumberUpdTextfield.getText();
        String division = (String) selectDivisionUpdateCCombobox.getValue();
       int customerId = Integer.parseInt (customerUpdIdTextfield.getText());
       String country = (String) countryComboboxUpdateCustomer.getValue();

       if (Utility.validateCustomerInput (customerName, customerAddress, zipCode, phoneNumber, division, country) ) {
           Utility.displayErrorAlert(1);

       }
       else {

           int rowsAffected =  Query.updateCustomer(customerName, customerAddress, zipCode, phoneNumber, Query.DivisionID(division), customerId);
        if (rowsAffected >0){
            Utility.displayInformation(3);


            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Object scene = FXMLLoader.load(getClass().getResource("/sample/View/Customers.fxml"));
            stage.setScene(new Scene((Parent) scene));
            stage.show();
        }}
    }

    public void cancelUpdButtonPushed(ActionEvent event) throws IOException {
       if ( Utility.displayConfirmation(1)){

            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Object scene = FXMLLoader.load(getClass().getResource("/sample/View/Customers.fxml"));
            stage.setScene(new Scene((Parent) scene));
            stage.show();
        }
    }

    }

