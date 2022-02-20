package sample.helper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.Model.Appointment;

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
    }}



