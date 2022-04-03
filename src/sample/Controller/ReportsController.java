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

public class ReportsController implements Initializable {
    public TableColumn <Report, String> yearColumn;
    public TableColumn <Report, String> monthColumn;
    public TableColumn <Report, Integer> countColumn;
    public TableView countByMonth;
    public TableView typeByMonthTable;
    public TableColumn <Report, String> typeColumn;
    public TableColumn <Report, String> monthByTypeColumn;
    public TableColumn <Report, Integer> totalColumn;
    public ComboBox selectContactCombo;
    public TableView contactScheduleTable;
    public TableColumn <Appointment, Integer> appIdColumn;
    public TableColumn <Appointment, String> titleColumn;
    public TableColumn <Appointment, String> typeContColumn;
    public TableColumn <Appointment, String> descriptionColumn;
    public TableColumn <Appointment, String>  startColumn;
    public TableColumn <Appointment, String> endColumn;
    public TableColumn <Appointment, Integer> customerIdColumn;
    public TableView custByCountryTrable;
    public TableColumn <CountryReport, String> countryColumn;
    public TableColumn <CountryReport, Integer> totalPerCountry;
    public Button goBackButton;

    public void loadCountMonth() throws SQLException {
        ObservableList<Report> reports = FXCollections.observableArrayList();
        try {


            reports = Query.selectCountMonth();

        } catch (Exception e) {

            System.err.println(e.getMessage());
        }
        countByMonth.getItems().addAll(reports);
    }

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

    public void goBackPushed(ActionEvent event) throws IOException {
        if (Utility.displayConfirmation(2))  {
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Object scene = FXMLLoader.load(getClass().getResource("/sample/View/Appointments.fxml"));
            stage.setScene(new Scene((Parent) scene));
            stage.show();
        }
    }
}