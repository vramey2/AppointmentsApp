package sample.Controller;

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
import java.util.ResourceBundle;

/**This is a controller class initiating functionality of updateCustomer.fxml.
 * @author  Veronika Ramey
 * */
public class UpdateCustomerController implements Initializable {

    /**Combobox to select customer's country*/
    public ComboBox countryComboboxUpdateCustomer;

    /**Combobox to select customer's division*/
    public ComboBox selectDivisionUpdateCCombobox;

    /**Text field for customer's ID*/
    public TextField customerUpdIdTextfield;

    /**Text field for customer's address*/
    public TextField customerUpdAddressTextfield;

    /**Text field for customer's zip code*/
    public TextField postalCodeUpdTextfield;

    /**Text field for customer's phone number*/
    public TextField phoneNumberUpdTextfield;

    /**Button to save updated customer*/
    public Button saveUpdButton;

    /**Button to go back to customers without saving*/
    public Button cancelUpdButton;

    /**Selected customer*/
    public Customer selectedCustomer;

    /**Text field for customer's name*/
    public TextField customerUpdNameTextField;

    /**Initializes controller. Method is used to initialize controller for update customer scene.
     * Method includes lambda expression for country's combobox listener, which is used to set up division combo based on country combobox input.
     * @param url Describes resolving relative paths for the root object
     * @param resourceBundle The root object's localization resources, if root object is not localized - null.
     */
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

    /**Method to initialize the view. The method populates data to initialize the view.
     * @param customer Selected customer
     * @throws SQLException */
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


    /**Method saves updated customer. This method saves edited customer after input validation.
     * Upon saving redirects to customers main screen
     * @param event Action on save button*/
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

    /**Method redirects back to customers vew without saving. Method is used to go back to Customers.fxml scene without saving after confirmation alert is displayed.
     * @param event Action on cancel button
     * @throws IOException*/
    public void cancelUpdButtonPushed(ActionEvent event) throws IOException {
       if ( Utility.displayConfirmation(1)){

            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Object scene = FXMLLoader.load(getClass().getResource("/sample/View/Customers.fxml"));
            stage.setScene(new Scene((Parent) scene));
            stage.show();
        }
    }

    }

