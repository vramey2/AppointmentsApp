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

public class AddCustomerController implements Initializable {
    public ComboBox selectCountryCombobox;
    public TextField customerIdTextfield;
    public TextField customerNameTextField;
    public TextField customerAddressTextfield;
    public TextField postalCodeTextfield;
    public TextField phoneNumberTextfield;

    public Button saveButton;
    public Button cancelButton;
    public ComboBox selectDivisionCombobox;

    ObservableList<String> selectCountryList = FXCollections.observableArrayList("Canada", "UK", "U.S.");

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


        public void cancelButtonPushed (ActionEvent event) throws IOException {

             if (Utility.displayConfirmation(2))
 {
                Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                Object scene = FXMLLoader.load(getClass().getResource("/sample/View/Customers.fxml"));
                stage.setScene(new Scene((Parent) scene));
                stage.show();
            }


        }
    }

