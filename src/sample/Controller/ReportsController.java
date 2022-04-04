package sample.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sample.Model.Appointment;
import sample.Model.CountryReport;
import sample.Model.Report;
import sample.helper.Query;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;


/**This is a controller class initiating functionality of reports.fxml.
 * @author  Veronika Ramey
 * */
public class ReportsController implements Initializable {

    /**Column of countByMonth table for years*/
    public TableColumn <Report, String> yearColumn;

    /**Column of countByMonth table for month*/
    public TableColumn <Report, String> monthColumn;

    /**Column of countByMonth table for total*/
    public TableColumn <Report, Integer> countColumn;

    /**Table view to show total of appointments per month*/
    public TableView countByMonth;

    /**Table view to show total of appointments by type*/
    public TableView typeByMonthTable;

    /**Column of type by Month table for type*/
    public TableColumn <Report, String> typeColumn;

    /**Column of type by month table for month*/
    public TableColumn <Report, String> monthByTypeColumn;

    /**Column of type by month table for total*/
    public TableColumn <Report, Integer> totalColumn;

    /**Combobox for selecting contact name*/
    public ComboBox selectContactCombo;

    /**This is a table view to displa schedule by contact*/
    public TableView contactScheduleTable;

    /** Column in the contact schedule table for appointment ID*/
    public TableColumn <Appointment, Integer> appIdColumn;

    /**Column in the contact schedule table for title*/
    public TableColumn <Appointment, String> titleColumn;

    /**Column in the contact schedule table for appointment's type*/
    public TableColumn <Appointment, String> typeContColumn;

    /**Column in the contact schedule table for appointment's description*/
    public TableColumn <Appointment, String> descriptionColumn;

    /**Column in the contact schedule table for appointment's start date and time*/
    public TableColumn <Appointment, String>  startColumn;

    /**Column in the contact schedule table for appointment's end date and time*/
    public TableColumn <Appointment, String> endColumn;

    /**Column in the contact's schedule table for customer's id*/
    public TableColumn <Appointment, Integer> customerIdColumn;

    /**Table view to display number of customers per country*/
    public TableView custByCountryTrable;

    /**Column from by country table for country*/
    public TableColumn <CountryReport, String> countryColumn;

    /**Column from by country table for total number of appointments*/
    public TableColumn <CountryReport, Integer> totalPerCountry;

    /**Button to go back to appointments view screen*/
    public Button goBackButton;



    /**Method is to populate count by month table. Method is used to populate count by month table with existing data from the database.
     */
    public void loadCountMonth() throws SQLException {
        ObservableList<Report> reports = FXCollections.observableArrayList();
        try {

           reports = Query.selectCountMonth();

        } catch (Exception e) {

            System.err.println(e.getMessage());
        }
        countByMonth.getItems().addAll(reports);
    }

    /**Method is to populate type by month table. Method is used to populate type by month table with existing data from the database.
     */
    public void loadTypeMonth () throws SQLException {
        ObservableList <Report> reporttype = FXCollections.observableArrayList();
        try {
               reporttype = Query.selectCountType();

        }
        catch (Exception e){
            System.err.println (e.getMessage());
        }
        typeByMonthTable.getItems().addAll(reporttype);
    }

    /**Method is to populate customer by country table. Method is used to populate customer by country table with existing data from the database.
     */
    public void loadByCountry () throws SQLException {
        ObservableList <CountryReport> newReports = FXCollections.observableArrayList();
        try {
            newReports = Query.countByCountry();
        }
        catch (Exception e){
            System.err.println (e.getMessage());
        }
        custByCountryTrable.getItems().addAll (newReports);
    }

    /**Method is to populate contact schedule table. Method is used to populate contact schedule table with existing data from the database.
     */
    public void loadByContact (int selectedContId)  {
        ObservableList <Appointment> byContact = FXCollections.observableArrayList();
        try {

            byContact = Query.selectAppointmentsByContact(selectedContId);
        }
        catch (Exception e){
            System.err.println (e.getMessage());
        }
        contactScheduleTable.setItems(byContact);
    }

    /**Initializes controller for reports view scene. Method is used to initialize controller for reports -reports.fxml.
     * Uses lambda expression to populate contacts schedule table depending on combobox selection.
     * @param url Describes resolving relative paths for the root object
     * @param resourceBundle The root object's localization resources, if root object is not localized - null.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
        monthColumn.setCellValueFactory(new PropertyValueFactory<>("month"));
        countColumn.setCellValueFactory(new PropertyValueFactory<>("count"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        monthByTypeColumn.setCellValueFactory(new PropertyValueFactory<>("month"));
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("countType"));
        appIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        typeContColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        startColumn.setCellValueFactory(new PropertyValueFactory<>("startString"));
        endColumn.setCellValueFactory(new PropertyValueFactory<>("endString"));
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("CustomerID"));
        countryColumn.setCellValueFactory(new PropertyValueFactory< >("country"));
        totalPerCountry.setCellValueFactory(new PropertyValueFactory<> ("countPerCountry"));

        try {
            selectContactCombo.setItems(Query.selectContacts());
        } catch (SQLException throwables) {
            throwables.printStackTrace();}
        try {
            loadCountMonth();
            loadTypeMonth();
            loadByCountry();

        } catch (SQLException e) {

            System.out.println(e.getMessage());
        }

        selectContactCombo.valueProperty().addListener((observableValue, o, t1) -> {
            String selectedContact = (String) selectContactCombo.getValue();
            try {
                int selectedContId = Query.contactID(selectedContact);
                loadByContact(selectedContId);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }});
 }

    /**Method is to change the scene back to appointments view. Method is used to go back to Appointments.fxml
     * A confirmation alert is displayed before changing the scene.
     * @param event Action on go back button*/
    public void goBackPushed(ActionEvent event) throws IOException {
        if (Utility.displayConfirmation(2))  {
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Object scene = FXMLLoader.load(getClass().getResource("/sample/View/Appointments.fxml"));
            stage.setScene(new Scene((Parent) scene));
            stage.show();
        }
    }
}