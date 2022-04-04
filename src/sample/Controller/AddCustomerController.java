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
import sample.helper.Query;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;


/**This is a controller class initiating functionality of addCustomerScreen.fxml.
 * @author  Veronika Ramey
 * */
public class AddCustomerController implements Initializable {

    /**Combobox to select country of customer*/
    public ComboBox selectCountryCombobox;

    /**Text field for customer's id*/
    public TextField customerIdTextfield;

    /**Text field for customer's name*/
    public TextField customerNameTextField;

    /**Text field for customer's address*/
    public TextField customerAddressTextfield;

    /**Text field for customer's zip code*/
    public TextField postalCodeTextfield;

    /**Text field for customer's phone number*/
    public TextField phoneNumberTextfield;

    /**Button to save new customer*/
    public Button saveButton;

    /**Button to cancel and go back to main customer's screen*/
    public Button cancelButton;

    /**Combobox to select customer's division*/
    public ComboBox selectDivisionCombobox;

    /**Observable list of available countries*/
    ObservableList<String> selectCountryList = FXCollections.observableArrayList("Canada", "UK", "U.S.");


    /**Initializes controller. Method is used to initialize controller for add customer scene.
     * Method includes lambda expression for country's combobox listener, which is used to set up division combo based on country combobox input.
     * @param url Describes resolving relative paths for the root object
     * @param resourceBundle The root object's localization resources, if root object is not localized - null.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        customerIdTextfield.setEditable(false);
        selectCountryCombobox.setItems(selectCountryList);


        selectCountryCombobox.valueProperty().addListener((observableValue, o, t1) -> {
            String selectedCountry = (String) selectCountryCombobox.getValue();
            int Country_ID;
            if (selectedCountry.equals("U.S."))
                Country_ID = 1;
            else if (selectedCountry.equals("UK"))
                Country_ID = 2;
            else
                Country_ID = 3;

            try {

                selectDivisionCombobox.setItems(Query.loadDivisions(Country_ID));

                System.out.println(Country_ID);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
    }

    /**Method to create a new instance of a customer. Method adds new customer and takes back to customers main screen.
     * Before customer is saved all input is validated and appropriate alerts are displayed.
     * @param event action on save button*/
        public void saveButtonPushed (ActionEvent event) throws SQLException, IOException {

            String customerName = customerNameTextField.getText();
            String customerAddress = customerAddressTextfield.getText();
            String zipCode = postalCodeTextfield.getText();
            String phoneNumber = phoneNumberTextfield.getText();
            String division = (String) selectDivisionCombobox.getValue();
            String country = (String) selectCountryCombobox.getValue();

            if (Utility.validateCustomerInput(customerName, customerAddress, zipCode, phoneNumber, division, country))
              {
                Utility.displayErrorAlert(6);


            } else {

                int rowsAffected = Query.insertCustomer(customerName, customerAddress, zipCode, phoneNumber, Query.DivisionID(division));
                if (rowsAffected > 0) {
                    Utility.displayInformation(1);

                    Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                    Object scene = FXMLLoader.load(getClass().getResource("/sample/View/Customers.fxml"));
                    stage.setScene(new Scene((Parent) scene));
                    stage.show();
                }
            }
        }

    /**Changes scene to customers screen - Customers.fxml.
     * This method is used to go back to customers screen without saving changes once the user confirms the action.
     * @param  event Action on cancel button
     * @throws IOException
     */
        public void cancelButtonPushed (ActionEvent event) throws IOException {

             if (Utility.displayConfirmation(2))  {
                Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                Object scene = FXMLLoader.load(getClass().getResource("/sample/View/Customers.fxml"));
                stage.setScene(new Scene((Parent) scene));
                stage.show();
            }
        }
    }

