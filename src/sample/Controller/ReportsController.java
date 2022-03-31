package sample.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.Model.Appointment;
import sample.Model.CountryReport;
import sample.Model.Report;
import sample.helper.JDBC;
import sample.helper.Query;

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
        yearColumn.setCellValueFactory(new PropertyValueFactory<Report, String>("year"));
        monthColumn.setCellValueFactory(new PropertyValueFactory<Report, String>("month"));
        countColumn.setCellValueFactory(new PropertyValueFactory<Report, Integer>("count"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<Report, String>("type"));
        monthByTypeColumn.setCellValueFactory(new PropertyValueFactory<Report, String>("month"));
        totalColumn.setCellValueFactory(new PropertyValueFactory<Report, Integer>("countType"));
        appIdColumn.setCellValueFactory(new PropertyValueFactory<Appointment, Integer>("id"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("title"));
        typeContColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("type"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("description"));
        startColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("startString"));
        endColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("endString"));
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<Appointment, Integer>("CustomerID"));
        countryColumn.setCellValueFactory(new PropertyValueFactory<CountryReport, String >("country"));
        totalPerCountry.setCellValueFactory(new PropertyValueFactory<CountryReport, Integer> ("countPerCountry"));

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
    }