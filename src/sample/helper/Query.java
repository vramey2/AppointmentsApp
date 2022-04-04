package sample.helper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.Controller.Utility;
import sample.Model.*;

import java.sql.*;
import java.time.*;

/**Class for database queries. Class is used for queries to the database.
 * @author Veronika Ramey
 */
public class Query {

    /**Method to delete customer. Deletes customer from the database.
     *
     * @param customerID ID of customer to be deleted
     * @return int number of deletions made
     * @throws SQLException
     */
    public static int deleteCustomer(int customerID) throws SQLException {

        String sql = "DELETE FROM CUSTOMERS WHERE Customer_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, customerID);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }

    /**Method to lookup appointments for a customer. Method is used to search for appointments for a customer.
     *
     * @param customerID Customer's ID for whom the search is made
     * @return int number of found apointments
     * @throws SQLException
     */
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

/**Method to search for contact names. Method is used to find contact names in the db
 * @return ObservableList contacts*/

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


    /**Method to find contact's ID. Method is used to find contact ID for a contact name in the DB.
     *
     * @param contactName Name of contact
     * @return Integer ID of the contact
     * @throws SQLException
     */
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

    /**Method to search for contact's name. Method is used to find contact's name for a specific contact's ID in the db
     *
     * @param contactID contact's ID
     * @return String contact's name
     * @throws SQLException
     */
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

    /**Method to verify that the customer exists. Method is used to verify if the customer exists in the database
     *
     * @param customerId Customer's id
     * @return boolean True if customer doesn't exist in the database
     * @throws SQLException
     */
    public static boolean customerExists(int customerId) throws SQLException {

        int rowsAffected = 0;
        String sql = "SELECT Customer_Name FROM customers Where Customer_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, customerId);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            rowsAffected += 1;
        }
        return (rowsAffected <= 0);
    }

    /**Method to verify if the user exists. Method is used to verify if a specific user exists in the database
     *
     * @param userId User's ID
     * @return boolean True if the user doesn't exist in the db
     * @throws SQLException
     */
    public static boolean userExists(int userId) throws SQLException {

        int rowsAffected = 0;
        String sql = "SELECT User_Name FROM users Where User_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, userId);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            rowsAffected += 1;
        }
        return (rowsAffected <= 0);
    }

    /**Method to count appointments by month. Method is used to count appointments by month
     *
     * @return ObservableList Appointments grouped by month and year
     * @throws SQLException
     */
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

    /**Method to count customers by country. Method is used to count customers for each country
     *
     * @return ObservableList customers by country
     * @throws SQLException
     */
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

    /**Method to count appointments by type. Method is used to count number of appointments for each type in the db
     *
     * @return ObservableList Appointments by type
     * @throws SQLException
     */
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


    /**Method to select customers from database. Method is used to select customers in the database.
     *
     * @return ObservableList List of customers
     * @throws SQLException
     */

    public static ObservableList selectCustomers() throws SQLException {
        ObservableList<Customer> customers = FXCollections.observableArrayList();

        String sql = "   SELECT c.Customer_ID, c.Customer_Name, c.Address, c.Postal_Code, c.Phone, c.Division_ID, fld.Division, co.Country FROM Customers AS c INNER JOIN first_level_divisions as fld ON c.Division_ID = fld.Division_ID INNER JOIN countries as co ON fld.Country_ID = co.Country_ID";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet resultSet = ps.executeQuery();

        while (resultSet.next()) {

            int customerID = resultSet.getInt("Customer_ID");
            String customerName = resultSet.getString("Customer_Name");

            String zipCode = resultSet.getString("Postal_Code");
            String phoneNumber = resultSet.getString("Phone");

            String division = resultSet.getString("Division");
            int divisionId = resultSet.getInt("Division_ID");
            String address = resultSet.getString("Address");

            String country = resultSet.getString("Country");
            String customerAddress = address;
            Customer newCustomer = new Customer(customerID, customerName, customerAddress, zipCode, phoneNumber, country, division, divisionId);
            customers.add(newCustomer);
        }
        return customers;
    }

    /**Method to find division ID. Method is used to find division id for a specific division in the db
     *
     * @param Division Name of division
     * @return Integer Division's ID
     * @throws SQLException
     */
    public static Integer DivisionID(String Division) throws SQLException {
        Integer divisionID = 0;
        String sql = "SELECT Division, Division_ID FROM first_level_divisions WHERE Division =  ? ";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, Division);
        ResultSet resultSet = ps.executeQuery();
        while (resultSet.next()) {
            divisionID = resultSet.getInt("Division_ID");
             }



        return divisionID;
    }


    /**Method to find country's ID. Method is used to find specific country's id in the db
     *
     * @param  Country Name of country
     * @return Integer Country's id
     * @throws SQLException
     */
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


    /**Method to find country for a given Division ID. Method is used to find country for a specific division's id
     *
     * @param Division_ID ID of division
     * @return ObservableList Countries
     * @throws SQLException
     */
    public static ObservableList<String> selectCountry(int Division_ID) throws SQLException {
        ObservableList<String> selectCountry = FXCollections.observableArrayList();
        try {
            JDBC.openConnection();

            String sql = " SELECT Country FROM countries INNER JOIN first_level_divisions ON countries.Country_ID = first_level_divisions.Country_ID WHERE Division_ID = ?";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setInt(1, Division_ID);
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {

                String country = resultSet.getString("Country");
                selectCountry.add(country);
            }
        } catch (Exception e) {//
        }
        return selectCountry;
    }

    /**Method to add customer to database. Method is used to insert new customer's record in the database.
     *
     * @param customerName Customer's name
     * @param customerAddress Customer's address
     * @param zipCode Customer's zip code
     * @param phoneNumber Customer's phone number
     * @param divisionId Customer's division id
     * @return int Number of records inserted
     * @throws SQLException
     */

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

    /**Method to update customer's record in the database. Method is used to edit record for existing customer in the database.
     *
     * @param customerName Customer's name
     * @param customerAddress Customer's address
     * @param zipCode Customer's zip code
     * @param phoneNumber Customer's phone number
     * @param divisionId Customer's division id
     * @param customerID Customer's ID
     * @return int Number of records updated
     * @throws SQLException
     */

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


    /**Method to select divisions from DB. Method is used to select divisions for a specific country's ID
     *
     * @param Country_ID ID of country
     * @return ObservableList divisions
     * @throws SQLException
     */
    public static ObservableList <String> loadDivisions(int Country_ID) throws SQLException

    {
            ObservableList<String> selectDivisionList = FXCollections.observableArrayList();
        try {
            JDBC.openConnection();

            String sql = " SELECT Division FROM first_level_divisions WHERE Country_ID = ?";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setInt(1, Country_ID);
            ResultSet resultSet = ps.executeQuery();


            while (resultSet.next()) {

                String division = resultSet.getString("Division");
                selectDivisionList.add(division);

            }
        } catch (Exception e) {//
        }
        return selectDivisionList;
    }


    /**Method to select all countries. Method is used to load all countries from the db
     *
     * @return ObservableList List of countries
     * @throws SQLException
     */
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

    /**Method to select all division. Method is used to load all divisions from the db
     *
     * @return ObservableList List of divisions
     * @throws SQLException
     */
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

    /**Method to find appointments for a certain contact ID. Method is used to find appointments for a specific contact id in the db.
     *
     * @param contactId ID of contact
     * @return ObservableList Appointments for a specific contact
     * @throws SQLException
     */
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


            ZoneId myZoneID = ZoneId.systemDefault();
            ZoneId utcZoneId = ZoneId.of("UTC");

            ZonedDateTime utcStartZDT = ZonedDateTime.ofInstant (startDateTime.toInstant(), utcZoneId);
            ZonedDateTime utcEndZDT = ZonedDateTime.ofInstant (endDateTime.toInstant(), utcZoneId);
            ZonedDateTime myStartDateTime = ZonedDateTime.ofInstant (utcStartZDT.toInstant(), myZoneID);
            ZonedDateTime startToLocalInstat = utcStartZDT.withZoneSameInstant(myZoneID);
            ZonedDateTime startLocal = startDateTime.toInstant().atZone(myZoneID);

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


        }




