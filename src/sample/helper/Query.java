package sample.helper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import sample.Controller.Utility;
import sample.Model.*;

import javax.xml.transform.Result;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;

public class Query {

    //  private static Object ZonedDateTime;

    public Query() throws SQLException {
    }


    public static int deleteCustomer(int customerID) throws SQLException {

        String sql = "DELETE FROM CUSTOMERS WHERE Customer_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, customerID);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }


    public static int checkForAppointments(int customerID) throws SQLException {

        String sql = "SELECT Appointment_ID  FROM APPOINTMENTS WHERE Customer_ID = ? ";

        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, customerID);
        ResultSet resultSet = ps.executeQuery();
        int rowsAffected = 0;
        while (resultSet.next()) {
            rowsAffected += 1;
        }
        return rowsAffected;


    }
    public static ObservableList<Integer> checkforOverlaps(int customerID, String startDate, String endDate) throws SQLException {
    ObservableList <Integer> overlapID = FXCollections.observableArrayList();
        String sql = "SELECT Appointment_ID from APPOINTMENTS WHERE Customer_ID = ? AND (START BETWEEN ? AND ?) " +
                "OR (END BETWEEN ? AND ?)" +
                " or (START <   ? AND END  > ?)";

        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, customerID);
        ps.setString(2, startDate);
        ps.setString(3, endDate);
        ps.setString(4, startDate);
        ps.setString(5, endDate);
        ps.setString(6, startDate);
        ps.setString(7, endDate);
        ResultSet rs = ps.executeQuery();
       // int rowAffected = 0;
        while (rs.next()) {
           int appointmentId = rs.getInt("Appointment_ID");
           overlapID.add(appointmentId);
        }
        return overlapID;
    }

    public static ObservableList<String> selectContacts() throws SQLException {
        ObservableList<String> contacts = FXCollections.observableArrayList();
        JDBC.openConnection();
        String sql = "SELECT Contact_Name FROM contacts";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            String name = rs.getString("Contact_Name");
            contacts.add(name);
        }
        return contacts;
    }

    public static Integer contactID(String contactName) throws SQLException {
        Integer contactID = 0;
        String sql = "SELECT Contact_ID FROM contacts WHERE Contact_Name = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, contactName);
        ResultSet resultSet = ps.executeQuery();
        while (resultSet.next()) {
            contactID = resultSet.getInt("Contact_ID");

        }

        return contactID;
    }

    public static String contactName (int contactID) throws SQLException {
        String contactName = null;
        String sql = "SELECT Contact_Name FROM contacts WHERE Contact_ID= ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt (1, contactID);
        ResultSet resultSet = ps.executeQuery();
        while (resultSet.next()) {
            contactName = resultSet.getString("Contact_Name");

        }

        return contactName;
    }

    public static boolean customerExists(int customerId) throws SQLException {

        int rowsAffected = 0;
        String sql = "SELECT Customer_Name FROM customers Where Customer_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, customerId);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            rowsAffected += 1;
        }
        return (rowsAffected > 0);
    }

    public static boolean userExists(int userId) throws SQLException {

        int rowsAffected = 0;
        String sql = "SELECT User_Name FROM users Where User_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, userId);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            rowsAffected += 1;
        }
        return (rowsAffected > 0);
    }
public static ObservableList selectCountMonth()throws SQLException{
        ObservableList <Report> reports = FXCollections.observableArrayList();

   String sql = "SELECT year(Start) as 'Year', MONTHNAME(Start) as 'Month', count(Description) as 'count'  from appointments group by year(Start), MONTH(Start)";

    PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()){
            int count = rs.getInt ("count");
            String year = rs.getString ("Year");
            String month = rs.getString ("Month");
            String type = "";
           int countType = 0;
            Report newReport = new Report(count, month, year, type,  countType);
            reports.add(newReport);

        }

         return reports;
}

public static ObservableList countByCountry() throws SQLException {
        ObservableList <CountryReport> newReports = FXCollections.observableArrayList();

        String sql = "SELECT customers.Customer_Name, customers.Customer_ID, countries.Country, count(Customer_ID) as 'count' from  customers\n" +
                " INNER JOIN first_level_divisions ON customers.Division_ID = first_level_divisions.Division_ID \n" +
                "INNER JOIN countries ON first_level_divisions.Country_ID = countries.Country_ID group by countries.Country";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()){

            int countPerCountry = rs.getInt("count");
            String country = rs.getString("countries.Country");
            CountryReport newReport = new CountryReport(countPerCountry, country);
            newReports.add(newReport);
        }

        return newReports;

}
public static ObservableList selectCountType ()throws SQLException{
        ObservableList <Report> reporttype = FXCollections.observableArrayList();

        String sql = "SELECT Type, month(start) as 'Month', count(Type) as 'Total' from appointments group by month(start), Type";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()){
            String type = rs.getString("Type");
            String month = rs.getString("Month");
            int countType = rs.getInt("Total");
            String year = "";
            int count = 0;
            Report newReport = new Report (count, month, year, type, countType);
            reporttype.add(newReport);

        }

        return reporttype;
}

    public static ObservableList selectAppointments() throws SQLException, ParseException {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        String sql = "SELECT Appointment_ID, Title, Description, Location, Type, Contact_ID, Start, End, Customer_ID, User_ID from  APPOINTMENTS";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {

            int id = rs.getInt("Appointment_ID");
            String title = rs.getString("Title");
            String description = rs.getString("Description");
            String location = rs.getString("Location");
            int contact = rs.getInt("Contact_ID");
            String type = rs.getString("Type");
            Timestamp startDateTime = rs.getTimestamp("Start");

            Timestamp endDateTime = rs.getTimestamp("End");
            int customerID = rs.getInt("Customer_ID");
            int userID = rs.getInt("User_ID");

            ZoneId utcZoneId = ZoneId.of("UTC");


            System.out.println(startDateTime + " timestamp");
            ZoneId myZoneID = ZoneId.systemDefault();

            ZonedDateTime utcStartZDT = ZonedDateTime.ofInstant(startDateTime.toInstant(), utcZoneId);
            ZonedDateTime utcEndZDT = ZonedDateTime.ofInstant(endDateTime.toInstant(), utcZoneId);

            ZonedDateTime myStartDateTime = ZonedDateTime.ofInstant(utcStartZDT.toInstant(), myZoneID);

            //  ZonedDateTime startToLocalInstat = utcStartZDT.withZoneSameInstant(myZoneID);

            //  ZonedDateTime startLocal = startDateTime.toInstant().atZone(myZoneID);

            ZonedDateTime myEndDateTime = ZonedDateTime.ofInstant(utcEndZDT.toInstant(), myZoneID);
            String startTime = String.valueOf(myStartDateTime.toLocalTime());
            String endTime = String.valueOf(myEndDateTime.toLocalTime());
            String startDate = String.valueOf(myStartDateTime.toLocalDate());
            String endDate = String.valueOf(myEndDateTime.toLocalDate());

            String formattedStartDateTime = startTime + " " + startDate;
            String formattedEndDateTime = endTime + " " + endDate;


            Appointment newAppointment = new Appointment(id, title, description, contact, location, type,
                    startDateTime, endDateTime, customerID, userID, formattedStartDateTime, formattedEndDateTime);
            appointments.add(newAppointment);

        }
        return appointments;


    }

    //To load from the database

    public static ObservableList selectCustomers() throws SQLException {
        ObservableList<Customer> customers = FXCollections.observableArrayList();

        String sql = "   SELECT c.Customer_ID, c.Customer_Name, c.Address, c.Postal_Code, c.Phone, c.Division_ID, fld.Division, co.Country FROM Customers AS c INNER JOIN first_level_divisions as fld ON c.Division_ID = fld.Division_ID INNER JOIN countries as co ON fld.Country_ID = co.Country_ID";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet resultSet = ps.executeQuery();

        while (resultSet.next()) {
            System.out.println("Inside rsult set");

            int customerID = resultSet.getInt("Customer_ID");
            System.out.println(customerID);
            String customerName = resultSet.getString("Customer_Name");

            String zipCode = resultSet.getString("Postal_Code");
            String phoneNumber = resultSet.getString("Phone");
            System.out.println(phoneNumber);
            String division = resultSet.getString("Division");
            int divisionId = resultSet.getInt("Division_ID");
            String address = resultSet.getString("Address");

            String country = resultSet.getString("Country");
            //  String customerAddress = address + ", " + division + ", " + country;
            String customerAddress = address;
            Customer newCustomer = new Customer(customerID, customerName, customerAddress, zipCode, phoneNumber, country, division, divisionId);
            customers.add(newCustomer);


        }
        return customers;
    }


    public static Integer DivisionID(String Division) throws SQLException {
        Integer divisionID = 0;


        String sql = "SELECT Division, Division_ID FROM first_level_divisions WHERE Division =  ? ";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, Division);
        ResultSet resultSet = ps.executeQuery();
        while (resultSet.next()) {
            divisionID = resultSet.getInt("Division_ID");
            System.out.println("Division ID is " + divisionID);
        }


        System.out.println(divisionID);
        return divisionID;
    }


    public static Integer CountryID(String Country) throws SQLException {
        Integer countryID = 0;


        String sql = "SELECT Country_ID FROM countries WHERE Country =  ? ";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, Country);
        ResultSet resultSet = ps.executeQuery();
        while (resultSet.next()) {
            countryID = resultSet.getInt("Country_ID");

        }


        return countryID;
    }

    public static String appointmentType(int appointmentID) throws SQLException {
        String type = null;


        String sql = "SELECT Type, Description from appointments WHERE Appointment_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, appointmentID);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            type = rs.getString("Type");


        }
        return type;
    }

    public static ObservableList<String> selectCountry(int Division_ID) throws SQLException {
        ObservableList<String> selectCountry = FXCollections.observableArrayList();
        try {
            JDBC.openConnection();

            String sql = " SELECT Country FROM countries INNER JOIN first_level_divisions ON countries.Country_ID = first_level_divisions.Country_ID WHERE Division_ID = ?";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setInt(1, Division_ID);
            ResultSet resultSet = ps.executeQuery();

            //CREATE CUSTOMER OBJECT FROM EACH RECORD
            while (resultSet.next()) {

                String country = resultSet.getString("Country");
                selectCountry.add(country);


            }
        } catch (Exception e) {//
        }
        return selectCountry;
    }

    //add customer to DB
    public static int insertCustomer(String customerName, String customerAddress, String zipCode, String phoneNumber,
                                     int divisionId)
            throws SQLException {

        String sql = "INSERT INTO CUSTOMERS (Customer_Name,  Address, Postal_Code, Phone, Create_Date, Created_by, Last_Update, Last_Updated_By, Division_ID ) VALUES (?, ?, ?, ?,  ?, ?, ?, ?, ?)";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, customerName);
        ps.setString(2, customerAddress);
        ps.setString(3, zipCode);
        ps.setString(4, phoneNumber);
        ps.setString(5, Utility.utcTimeNow());
        ps.setString(6, UserLogged.getSignedName());
        ps.setString(7, Utility.utcTimeNow());
        ps.setString(8, UserLogged.getSignedName());
        ps.setInt(9, divisionId);


        int rowsAffected = ps.executeUpdate();
        return rowsAffected;


    }

    //update customer in DB
    public static int updateCustomer(String customerName, String customerAddress, String zipCode, String phoneNumber,
                                     int divisionId, int customerID)
            throws SQLException {

        String sql = "UPDATE CUSTOMERS SET Customer_Name = ?,  Address = ?, Postal_Code = ?, Phone = ?, Last_Update = ?, Last_Updated_By = ?, Division_ID  = ? WHERE Customer_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, customerName);
        ps.setString(2, customerAddress);
        ps.setString(3, zipCode);
        ps.setString(4, phoneNumber);
        ps.setString(5, Utility.utcTimeNow());
        ps.setString(6, UserLogged.getSignedName());
        ps.setInt(7, divisionId);

        ps.setInt(8, customerID);

        int rowsAffected = ps.executeUpdate();
        return rowsAffected;


    }

    public static int updateAppointment(String title, String description, String location, String type, String startDT, String endDT, int customerID, int userID, int contactID, int appointmentID) throws SQLException {


    String sql = "Update appointments SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ?, Last_Update = ?, Last_Updated_By = ? Where Appointment_ID = ?";
    PreparedStatement ps =JDBC.connection.prepareStatement(sql);
    ps.setString(1, title);
    ps.setString(2, description);
    ps.setString(3, location);
    ps.setString(4, type);
    ps.setString(5, startDT);
    ps.setString(6, endDT);
    ps.setInt(7, customerID);
    ps.setInt(8, userID);
    ps.setInt(9, contactID);
    ps.setString(10, Utility.utcTimeNow());
    ps.setString(11,  UserLogged.getSignedName());
    ps.setInt(12, appointmentID);

    int rowsAffected = ps.executeUpdate();
    return rowsAffected;
}

    public static ObservableList <String> loadDivisions(int Country_ID) throws SQLException

    {

    {


        ObservableList<String> selectDivisionList = FXCollections.observableArrayList();
        try {
            JDBC.openConnection();

            String sql = " SELECT Division FROM first_level_divisions WHERE Country_ID = ?";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setInt(1, Country_ID);
            ResultSet resultSet = ps.executeQuery();

            //CREATE CUSTOMER OBJECT FROM EACH RECORD
            while (resultSet.next()) {

                String division = resultSet.getString("Division");
                selectDivisionList.add(division);

            }
        } catch (Exception e) {//
        }
        return selectDivisionList;
    }





    }

    public static ObservableList <String> loadAllCountries() throws SQLException  {

        ObservableList <String> allCountries = FXCollections.observableArrayList();
        String sql = "SELECT COUNTRY FROM countries";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet resultSet = ps.executeQuery();
        while (resultSet.next()) {

            String country = resultSet.getString("Country");
            allCountries.add(country);

        }
        return allCountries;
    }
    public static ObservableList <String> loadAllDivisions() throws SQLException  {

        ObservableList <String> allDivisions = FXCollections.observableArrayList();
        String sql = "SELECT Division FROM first_level_divisions";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet resultSet = ps.executeQuery();
        while (resultSet.next()) {

            String division = resultSet.getString("Division");
            allDivisions.add(division);

        }
        return allDivisions;
    }

    //add appointment to DB
    public static int insertAppointment  ( String title, String description,  String location, String type,
                                          String startDateTime, String endDateTime, int customerID, int userID, int contact )
            throws SQLException {

        String sql = "INSERT INTO APPOINTMENTS (Title, Description, Location, Type, Start, End,Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID)" +
                "VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ,?)";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, title);
        ps.setString(2,description);

        ps.setString(3, location);
        ps.setString(4, type);
        ps.setString(5, startDateTime);
        ps.setString (6, endDateTime);
        ps.setString(7, Utility.utcTimeNow());
        ps.setString(8, UserLogged.getSignedName());
        ps.setInt (9, customerID);
        ps.setInt (10, userID);
        ps.setInt(11, contact);



        int rowsAffected = ps.executeUpdate();
        return rowsAffected;


    }

    public static int deleteAppointment(int appointmentID) throws SQLException {

        String sql = "DELETE FROM APPOINTMENTS WHERE Appointment_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, appointmentID);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }

public static  ObservableList selectAppointmentsByContact (int contactId) throws SQLException{
        ObservableList<Appointment> byContact = FXCollections.observableArrayList();
        String sql = "SELECT * FROM appointments where Contact_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, contactId);
        ResultSet rs = ps.executeQuery();
        while (rs.next()){
            int id = rs.getInt("Appointment_ID");
            String title = rs.getString("Title");
            String description = rs.getString("Description");
            String location = rs.getString("Location");
            int contact = rs.getInt("Contact_ID");
            String type = rs.getString("Type");
            Timestamp startDateTime= rs.getTimestamp("Start");

            Timestamp endDateTime = rs.getTimestamp("End");
            int customerID = rs.getInt("Customer_ID");
            int userID = rs.getInt("User_ID");

            System.out.println (startDateTime + " timestamp");
            ZoneId myZoneID = ZoneId.systemDefault();
            ZoneId utcZoneId = ZoneId.of("UTC");

            ZonedDateTime utcStartZDT = ZonedDateTime.ofInstant (startDateTime.toInstant(), utcZoneId);
            ZonedDateTime utcEndZDT = ZonedDateTime.ofInstant (endDateTime.toInstant(), utcZoneId);
            System.out.println (utcStartZDT + "utcStartZDT");
            ZonedDateTime myStartDateTime = ZonedDateTime.ofInstant (utcStartZDT.toInstant(), myZoneID);
            System.out.println (myStartDateTime + "myStartDT");
            ZonedDateTime startToLocalInstat = utcStartZDT.withZoneSameInstant(myZoneID);
            System.out.println (startToLocalInstat + "instant");
            ZonedDateTime startLocal = startDateTime.toInstant().atZone(myZoneID);
            System.out.println (startLocal + "startLocal");
            ZonedDateTime myEndDateTime = ZonedDateTime.ofInstant(utcEndZDT.toInstant(), myZoneID);
            String startTime = String.valueOf (myStartDateTime.toLocalTime());
            String endTime = String.valueOf (myEndDateTime.toLocalTime());
            String startDate = String.valueOf (myStartDateTime.toLocalDate());
            String endDate = String.valueOf(myEndDateTime.toLocalDate());

            String formattedStartDateTime = startTime + " " + startDate;
            String formattedEndDateTime = endTime + " " + endDate;

            Appointment newAppointment = new Appointment(id, title, description, contact, location, type,
                    startDateTime, endDateTime, customerID, userID, formattedStartDateTime, formattedEndDateTime);
            byContact.add(newAppointment);

        }
        return byContact;

}


   public static <LodalDate> ObservableList selectWeeklyAppointments() throws SQLException, ParseException {
       ObservableList<Appointment> appByWeek = FXCollections.observableArrayList();
       LocalDateTime myLDT = LocalDateTime.now();
       System.out.println(myLDT + " my time now");
       ZoneId myZoneId = ZoneId.systemDefault();
       ZonedDateTime myZDT = ZonedDateTime.of(myLDT, myZoneId);

       ZoneId utcZoneId = ZoneId.of("UTC");
       ZonedDateTime utcZDT = ZonedDateTime.ofInstant(myZDT.toInstant(), utcZoneId);
       System.out.println(utcZDT + "my time now in UTC");
       LocalDateTime localUTC = utcZDT.toLocalDateTime();
       System.out.println (localUTC + " local UTC DATE TIME");
       LocalDateTime localWeekAhead = localUTC.plusHours(168);
       System.out.println (localWeekAhead + "plus week");

       String sql = "SELECT Appointment_ID, Title, Description, Location, Type, Contact_ID, Start, End, Customer_ID, User_ID from  APPOINTMENTS WHERE Start between ? and  ?";
       PreparedStatement ps = JDBC.connection.prepareStatement(sql);
       ps.setObject(1, localUTC);
       ps.setObject(2, localWeekAhead);
       ResultSet rs = ps.executeQuery();
       while (rs.next()) {

           int id = rs.getInt("Appointment_ID");
           String title = rs.getString("Title");
           String description = rs.getString("Description");
           String location = rs.getString("Location");
           int contact = rs.getInt("Contact_ID");
           String type = rs.getString("Type");
           Timestamp startDateTime= rs.getTimestamp("Start");

           Timestamp endDateTime = rs.getTimestamp("End");
           int customerID = rs.getInt("Customer_ID");
           int userID = rs.getInt("User_ID");

           System.out.println (startDateTime + " timestamp");
           ZoneId myZoneID = ZoneId.systemDefault();


           ZonedDateTime utcStartZDT = ZonedDateTime.ofInstant (startDateTime.toInstant(), utcZoneId);
           ZonedDateTime utcEndZDT = ZonedDateTime.ofInstant (endDateTime.toInstant(), utcZoneId);
           System.out.println (utcStartZDT + "utcStartZDT");
           ZonedDateTime myStartDateTime = ZonedDateTime.ofInstant (utcStartZDT.toInstant(), myZoneID);
           System.out.println (myStartDateTime + "myStartDT");
           ZonedDateTime startToLocalInstat = utcStartZDT.withZoneSameInstant(myZoneID);
           System.out.println (startToLocalInstat + "instant");
           ZonedDateTime startLocal = startDateTime.toInstant().atZone(myZoneID);
           System.out.println (startLocal + "startLocal");
           ZonedDateTime myEndDateTime = ZonedDateTime.ofInstant(utcEndZDT.toInstant(), myZoneID);
           String startTime = String.valueOf (myStartDateTime.toLocalTime());
           String endTime = String.valueOf (myEndDateTime.toLocalTime());
           String startDate = String.valueOf (myStartDateTime.toLocalDate());
           String endDate = String.valueOf(myEndDateTime.toLocalDate());

           String formattedStartDateTime = startTime + " " + startDate;
           String formattedEndDateTime = endTime + " " + endDate;


           Appointment newAppointment = new Appointment(id, title, description, contact, location, type,
                   startDateTime, endDateTime, customerID, userID, formattedStartDateTime, formattedEndDateTime);
           appByWeek.add(newAppointment);

       }
       return appByWeek;


   }

    public static ObservableList selectPreviousWeeklyAppointments(int count) throws SQLException, ParseException {
        ObservableList<Appointment> appByPrevWeek = FXCollections.observableArrayList();
        LocalDateTime myLDT = LocalDateTime.now();
        System.out.println(myLDT + " my time now");
        ZoneId myZoneId = ZoneId.systemDefault();
        ZonedDateTime myZDT = ZonedDateTime.of(myLDT, myZoneId);

        ZoneId utcZoneId = ZoneId.of("UTC");
        ZonedDateTime utcZDT = ZonedDateTime.ofInstant(myZDT.toInstant(), utcZoneId);
        System.out.println(utcZDT + "my time now in UTC");
        LocalDateTime localUTC = utcZDT.toLocalDateTime();
        localUTC = localUTC.minusWeeks(count);
        System.out.println (localUTC + " local UTC DATE TIME");
        LocalDateTime localWeekPrev = localUTC.minusWeeks(count + 1);
        System.out.println (localWeekPrev+ "minus week");

        String sql = "SELECT Appointment_ID, Title, Description, Location, Type, Contact_ID, Start, End, Customer_ID, User_ID from  APPOINTMENTS WHERE Start between ? and  ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setObject(1, localWeekPrev);
        ps.setObject(2, localUTC);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {

            int id = rs.getInt("Appointment_ID");
            String title = rs.getString("Title");
            String description = rs.getString("Description");
            String location = rs.getString("Location");
            int contact = rs.getInt("Contact_ID");
            String type = rs.getString("Type");
            Timestamp startDateTime= rs.getTimestamp("Start");

            Timestamp endDateTime = rs.getTimestamp("End");
            int customerID = rs.getInt("Customer_ID");
            int userID = rs.getInt("User_ID");

            System.out.println (startDateTime + " timestamp");
            ZoneId myZoneID = ZoneId.systemDefault();


            ZonedDateTime utcStartZDT = ZonedDateTime.ofInstant (startDateTime.toInstant(), utcZoneId);
            ZonedDateTime utcEndZDT = ZonedDateTime.ofInstant (endDateTime.toInstant(), utcZoneId);
            System.out.println (utcStartZDT + "utcStartZDT");
            ZonedDateTime myStartDateTime = ZonedDateTime.ofInstant (utcStartZDT.toInstant(), myZoneID);
            System.out.println (myStartDateTime + "myStartDT");
            ZonedDateTime startToLocalInstat = utcStartZDT.withZoneSameInstant(myZoneID);
            System.out.println (startToLocalInstat + "instant");
            ZonedDateTime startLocal = startDateTime.toInstant().atZone(myZoneID);
            System.out.println (startLocal + "startLocal");
            ZonedDateTime myEndDateTime = ZonedDateTime.ofInstant(utcEndZDT.toInstant(), myZoneID);
            String startTime = String.valueOf (myStartDateTime.toLocalTime());
            String endTime = String.valueOf (myEndDateTime.toLocalTime());
            String startDate = String.valueOf (myStartDateTime.toLocalDate());
            String endDate = String.valueOf(myEndDateTime.toLocalDate());

            String formattedStartDateTime = startTime + " " + startDate;
            String formattedEndDateTime = endTime + " " + endDate;


            Appointment newAppointment = new Appointment(id, title, description, contact, location, type,
                    startDateTime, endDateTime, customerID, userID, formattedStartDateTime, formattedEndDateTime);
            appByPrevWeek.add(newAppointment);

        }
        return appByPrevWeek;


    }
    public static <LodalDate> ObservableList selectNextWeeklyAppointments() throws SQLException, ParseException {
        ObservableList<Appointment> appByNextWeek = FXCollections.observableArrayList();
        LocalDateTime myLDT = LocalDateTime.now();
        System.out.println(myLDT + " my time now");
        ZoneId myZoneId = ZoneId.systemDefault();
        ZonedDateTime myZDT = ZonedDateTime.of(myLDT, myZoneId);

        ZoneId utcZoneId = ZoneId.of("UTC");
        ZonedDateTime utcZDT = ZonedDateTime.ofInstant(myZDT.toInstant(), utcZoneId);
        System.out.println(utcZDT + "my time now in UTC");
        LocalDateTime localUTC = utcZDT.toLocalDateTime().plusWeeks(1);
        System.out.println(localUTC + " local UTC DATE TIME");
        LocalDateTime localWeekNext = localUTC.plusWeeks(2);
        //System.out.println(localWeekNext + "minus week");

        String sql = "SELECT Appointment_ID, Title, Description, Location, Type, Contact_ID, Start, End, Customer_ID, User_ID from  APPOINTMENTS WHERE Start between ? and  ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setObject(1, localUTC);
        ps.setObject(2, localWeekNext);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {

            int id = rs.getInt("Appointment_ID");
            String title = rs.getString("Title");
            String description = rs.getString("Description");
            String location = rs.getString("Location");
            int contact = rs.getInt("Contact_ID");
            String type = rs.getString("Type");
            Timestamp startDateTime = rs.getTimestamp("Start");

            Timestamp endDateTime = rs.getTimestamp("End");
            int customerID = rs.getInt("Customer_ID");
            int userID = rs.getInt("User_ID");

            System.out.println(startDateTime + " timestamp");
            ZoneId myZoneID = ZoneId.systemDefault();


            ZonedDateTime utcStartZDT = ZonedDateTime.ofInstant(startDateTime.toInstant(), utcZoneId);
            ZonedDateTime utcEndZDT = ZonedDateTime.ofInstant(endDateTime.toInstant(), utcZoneId);
            System.out.println(utcStartZDT + "utcStartZDT");
            ZonedDateTime myStartDateTime = ZonedDateTime.ofInstant(utcStartZDT.toInstant(), myZoneID);
            System.out.println(myStartDateTime + "myStartDT");
            ZonedDateTime startToLocalInstat = utcStartZDT.withZoneSameInstant(myZoneID);
            System.out.println(startToLocalInstat + "instant");
            ZonedDateTime startLocal = startDateTime.toInstant().atZone(myZoneID);
            System.out.println(startLocal + "startLocal");
            ZonedDateTime myEndDateTime = ZonedDateTime.ofInstant(utcEndZDT.toInstant(), myZoneID);
            String startTime = String.valueOf(myStartDateTime.toLocalTime());
            String endTime = String.valueOf(myEndDateTime.toLocalTime());
            String startDate = String.valueOf(myStartDateTime.toLocalDate());
            String endDate = String.valueOf(myEndDateTime.toLocalDate());

            String formattedStartDateTime = startTime + " " + startDate;
            String formattedEndDateTime = endTime + " " + endDate;


            Appointment newAppointment = new Appointment(id, title, description, contact, location, type,
                    startDateTime, endDateTime, customerID, userID, formattedStartDateTime, formattedEndDateTime);
            appByNextWeek.add(newAppointment);

        }
        return appByNextWeek;
    }
    public static ObservableList selectMonthlyAppointments() throws SQLException, ParseException {
        ObservableList<Appointment> appByMonth = FXCollections.observableArrayList();
        LocalDateTime myLDT = LocalDateTime.now();
        System.out.println(myLDT + " my time now");
        ZoneId myZoneId = ZoneId.systemDefault();
        ZonedDateTime myZDT = ZonedDateTime.of(myLDT, myZoneId);

        ZoneId utcZoneId = ZoneId.of("UTC");
        ZonedDateTime utcZDT = ZonedDateTime.ofInstant(myZDT.toInstant(), utcZoneId);
        System.out.println(utcZDT + "my time now in UTC");
        LocalDateTime localUTC = utcZDT.toLocalDateTime();
        System.out.println (localUTC + " local UTC DATE TIME");
        LocalDateTime localWeekAhead = localUTC.plusMonths(1);
        System.out.println (localWeekAhead + "plus week");

        String sql = "SELECT Appointment_ID, Title, Description, Location, Type, Contact_ID, Start, End, Customer_ID, User_ID from  APPOINTMENTS WHERE Start between ? and  ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setObject(1, localUTC);
        ps.setObject(2, localWeekAhead);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {

            int id = rs.getInt("Appointment_ID");
            String title = rs.getString("Title");
            String description = rs.getString("Description");
            String location = rs.getString("Location");
            int contact = rs.getInt("Contact_ID");
            String type = rs.getString("Type");
            Timestamp startDateTime= rs.getTimestamp("Start");

            Timestamp endDateTime = rs.getTimestamp("End");
            int customerID = rs.getInt("Customer_ID");
            int userID = rs.getInt("User_ID");

            System.out.println (startDateTime + " timestamp");
            ZoneId myZoneID = ZoneId.systemDefault();


            ZonedDateTime utcStartZDT = ZonedDateTime.ofInstant (startDateTime.toInstant(), utcZoneId);
            ZonedDateTime utcEndZDT = ZonedDateTime.ofInstant (endDateTime.toInstant(), utcZoneId);
            System.out.println (utcStartZDT + "utcStartZDT");
            ZonedDateTime myStartDateTime = ZonedDateTime.ofInstant (utcStartZDT.toInstant(), myZoneID);
            System.out.println (myStartDateTime + "myStartDT");
            ZonedDateTime startToLocalInstat = utcStartZDT.withZoneSameInstant(myZoneID);
            System.out.println (startToLocalInstat + "instant");
            ZonedDateTime startLocal = startDateTime.toInstant().atZone(myZoneID);
            System.out.println (startLocal + "startLocal");
            ZonedDateTime myEndDateTime = ZonedDateTime.ofInstant(utcEndZDT.toInstant(), myZoneID);
            String startTime = String.valueOf (myStartDateTime.toLocalTime());
            String endTime = String.valueOf (myEndDateTime.toLocalTime());
            String startDate = String.valueOf (myStartDateTime.toLocalDate());
            String endDate = String.valueOf(myEndDateTime.toLocalDate());

            String formattedStartDateTime = startTime + " " + startDate;
            String formattedEndDateTime = endTime + " " + endDate;


            Appointment newAppointment = new Appointment(id, title, description, contact, location, type,
                    startDateTime, endDateTime, customerID, userID, formattedStartDateTime, formattedEndDateTime);
            appByMonth.add(newAppointment);

        }
        return appByMonth;


    }
    }



