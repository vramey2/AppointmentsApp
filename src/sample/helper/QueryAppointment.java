package sample.helper;



import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.Controller.Utility;
import sample.Model.*;

import java.sql.*;
import java.text.ParseException;
import java.time.*;
public class QueryAppointment {
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



            ZoneId myZoneID = ZoneId.systemDefault();

            ZonedDateTime utcStartZDT = ZonedDateTime.ofInstant(startDateTime.toInstant(), utcZoneId);
            ZonedDateTime utcEndZDT = ZonedDateTime.ofInstant(endDateTime.toInstant(), utcZoneId);

            ZonedDateTime myStartDateTime = ZonedDateTime.ofInstant(utcStartZDT.toInstant(), myZoneID);


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
    //add appointment to DB
    public static int insertAppointment  ( String title, String description,  String location, String type,
                                           String startDateTime, String endDateTime, int customerID, int userID, int contact )
            throws SQLException {

        String sql = "INSERT INTO APPOINTMENTS (Title, Description, Location, Type, Start, End, Create_Date, Created_By, Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID)" +
                "VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ,?, ? , ?)";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, title);
        ps.setString(2,description);

        ps.setString(3, location);
        ps.setString(4, type);
        ps.setString(5, startDateTime);
        ps.setString (6, endDateTime);
        ps.setString (7, Utility.utcTimeNow());
        ps.setString (8, UserLogged.getSignedName());
        ps.setString(9, Utility.utcTimeNow());
        ps.setString(10, UserLogged.getSignedName());
        ps.setInt (11, customerID);
        ps.setInt (12, userID);
        ps.setInt(13, contact);



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



    public static  ObservableList selectWeeklyAppointments() throws SQLException, ParseException {
        ObservableList<Appointment> appByWeek = FXCollections.observableArrayList();
        LocalDateTime myLDT = LocalDateTime.now();

        ZoneId myZoneId = ZoneId.systemDefault();
        ZonedDateTime myZDT = ZonedDateTime.of(myLDT, myZoneId);

        ZoneId utcZoneId = ZoneId.of("UTC");
        ZonedDateTime utcZDT = ZonedDateTime.ofInstant(myZDT.toInstant(), utcZoneId);
        System.out.println(utcZDT + "my time now in UTC");
        LocalDateTime localUTC = utcZDT.toLocalDateTime();

        LocalDateTime localWeekAhead = localUTC.plusHours(168);


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


            ZoneId myZoneID = ZoneId.systemDefault();


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
            appByWeek.add(newAppointment);

        }
        return appByWeek;


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




    }}