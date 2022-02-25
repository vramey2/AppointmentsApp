package sample.helper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.Model.Appointment;
import sample.Model.Customer;

import java.sql.*;

public class Query {

    public Query() throws SQLException {
    }

    public static boolean select(String userName, String password) throws SQLException {
        String sql = " SELECT* FROM USERS WHERE User_Name = ? and Password = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, userName);
        ps.setString(2, password);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return true;

        } else
            return false;
    }

    public static int deleteCustomer (int customerID) throws SQLException {

        String sql = "DELETE FROM CUSTOMERS WHERE Customer_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, customerID);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
            }


            public static int checkForAppointments (int customerID) throws SQLException{

        String sql = "SELECT Appointment_ID  FROM APPOINTMENTS WHERE Customer_ID = ? ";

                PreparedStatement ps = JDBC.connection.prepareStatement(sql);
                ps.setInt (1, customerID);
                ResultSet resultSet = ps.executeQuery();
                int rowsAffected = 0;
                while (resultSet.next() ){
                    rowsAffected +=1;
                }
                return rowsAffected;


            }

    public static ObservableList selectAppointments() throws SQLException {
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

            Appointment newAppointment = new Appointment(id, title, description, contact, location, type,
                    startDateTime, endDateTime, customerID, userID);
            appointments.add(newAppointment);

        }
        return appointments;


    }

    //To load from the database

    public static ObservableList selectCustomers() throws SQLException {
        ObservableList<Customer> customers = FXCollections.observableArrayList();

        String sql = "   SELECT c.Customer_ID, c.Customer_Name, c.Address, c.Postal_Code, c.Phone, c.Division_ID, fld.Division, co.Country FROM Customers AS c INNER JOIN first_level_divisions as fld ON c.Division_ID = fld.Division_ID INNER JOIN countries as co ON fld.Country_ID = co.Country_ID"  ;
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet resultSet = ps.executeQuery();

        while (resultSet.next()){
            System.out.println("Inside rsult set");

            int customerID = resultSet.getInt("Customer_ID");
            System.out.println (customerID);
            String customerName = resultSet.getString("Customer_Name");

            String zipCode = resultSet.getString ("Postal_Code");
            String phoneNumber = resultSet.getString ("Phone");
            System.out.println (phoneNumber);
            String division = resultSet.getString("Division");
            int divisionId = resultSet.getInt("Division_ID");
            //String country = Query.selectCountry (Division_ID);
            // PreparedStatement customertCountry = JDBC.connection.prepareStatement( " SELECT Country FROM countries INNER JOIN first_level_divisions ON countries.Country_ID = first_level_divisions.Country_ID WHERE Division_ID = ?");
            String country =  resultSet.getString ("Country");
            String customerAddress = resultSet.getString ("Address") + ", " + division + ", " + country ;
            Customer newCustomer = new Customer (customerID, customerName, customerAddress, zipCode, phoneNumber, country, division, divisionId);
            customers.add (newCustomer);

/**
 Customer newCustomer = new Customer(resultSet.getInt ("Customer_ID"), resultSet.getString ("Customer_Name"),
 resultSet.getString("Address"), resultSet.getInt("Postal_Code"), resultSet.getString("Phone"));
 customers.add(newCustomer);
 System.out.println(newCustomer);*/
            System.out.println (customerID + customerName + customerAddress + zipCode + phoneNumber);
            System.out.println ("");
            System.out.println ("in the while");
        }
        return customers;
    }


public static Integer DivisionID (String Division) throws SQLException{
 Integer divisionID = 0;


            String sql = "SELECT Division, Division_ID FROM first_level_divisions WHERE Division =  ? ";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setString(1, Division);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next() ){
                 divisionID = resultSet.getInt ("Division_ID");
              System.out.println ("Division ID is " + divisionID);
            }


System.out.println (divisionID);
        return divisionID;
}
    public static ObservableList  <String> selectCountry(int Division_ID)throws SQLException{
        ObservableList <String> selectCountry = FXCollections.observableArrayList();
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

                // selectDivisionList.add(resultSet.getString("Division"));
              //  System.out.println (division);
            }
        } catch (Exception e) {//
        }
        return selectCountry;
    }

    //add customer to DB
    public static int insertCustomer(String customerName, String customerAddress, String zipCode, String phoneNumber,
                                        int divisionId)
      throws SQLException {

        String sql = "INSERT INTO CUSTOMERS (Customer_Name,  Address, Postal_Code, Phone, Division_ID ) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, customerName);
        ps.setString(2, customerAddress);
        ps.setString(3, zipCode);
        ps.setString(4, phoneNumber);
        ps.setInt(5, divisionId);

        int rowsAffected = ps.executeUpdate();
        return rowsAffected;


    }


    public static ObservableList <String> loadDivisions(int Country_ID) throws SQLException

    {

    {

        System.out.println ("Inside loaddivisions");
        ObservableList<String> selectDivisionList = FXCollections.observableArrayList();
        try {
            JDBC.openConnection();

            String sql = " SELECT Division FROM first_level_divisions WHERE Country_ID = ?";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setInt(1, Country_ID);
            ResultSet resultSet = ps.executeQuery();

            //CREATE CUSTOMER OBJECT FROM EACH RECORD
            while (resultSet.next()) {
                System.out.println ("Inside while of loaddivisions");
                String division = resultSet.getString("Division");
                selectDivisionList.add(division);

                // selectDivisionList.add(resultSet.getString("Division"));
                System.out.println (division);
            }
        } catch (Exception e) {//
        }
        return selectDivisionList;
    }


/**
    public ArrayList <Division> getInfo(String Country){


    }*/


    }
    }



