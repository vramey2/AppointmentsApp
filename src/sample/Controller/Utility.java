package sample.Controller;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.Optional;


public class Utility {
    public static void displayErrorAlert (int error){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error dialog");
        switch (error){
            case 1:
                alert.setContentText("Please enter valid value for each field!");
                break;
            case 2:
                alert.setContentText("Appointment start time cannnot be after end time!");
                break;
            case 3:
                alert.setContentText("Please enter existing customer ID or add a new customer first");
                break;
            case 4:
                alert.setContentText("Please enter existing user ID!");
                break;
            case 5:
                alert.setContentText("Appointment overlapps with a different appointment!");
                break;
            case 6:
                alert.setContentText("Please fill out all fields to enter customer!");
                break;
            case 7:
                alert.setContentText("Please enter existing customer ID or add a new customer first");
                break;
            case 8:
                alert.setContentText("Please enter existing user ID!");
        }


        alert.showAndWait();
    }

public static boolean displayConfirmation (int numberAlert){

        Alert alert = new Alert (Alert.AlertType.CONFIRMATION);
        switch (numberAlert){
            case 1:
                alert.setContentText("Do you want to go back without saving?");
                break;

            case 2:
                alert.setContentText("Do you want to go back?");
                break;

            case 3:
                alert.setContentText("Do you want to exit application?");
                break;

            case 4:
                alert.setContentText("Do you want to delete customer?");
                break;

            case 5:
                alert.setContentText("Do you want to delete appointment?");

        }
        Optional<ButtonType> rs = alert.showAndWait();
        return rs.get()== ButtonType.OK;
}

public static void displayInformation (int infoNumber){

        Alert alert = new Alert (Alert.AlertType.INFORMATION);
        switch (infoNumber){
            case 1:
                alert.setContentText("Customer Added!");
                break;

            case 2:
                alert.setContentText("Appointment Updated!");
                break;

            case 3:
                alert.setContentText("Customer Updated!");

        }
        alert.showAndWait();
}

public static void displayWarning (int warningNumber){

        Alert alert = new Alert (Alert.AlertType.WARNING);
        switch (warningNumber){
            case 1:
                alert.setContentText("Cannot schedule outside business hours!");
                break;
            case 2:
                alert.setContentText("Please select an appointment to update!");
                break;
            case 3:
                alert.setContentText("Please select appointment to delete!");
                break;
            case 4:
                alert.setContentText("Cannot schedule outside business hours!");
        }
        alert.showAndWait();
}

    public static String utcTimeNow (){

      //  ZonedDateTime myDT = ZonedDateTime.now();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        ZonedDateTime utcTimeNow  = ZonedDateTime.now(ZoneOffset.UTC);

        return utcTimeNow.format(timeFormatter);
    }




    public static boolean validateBusinessHours (ZonedDateTime utcStartZDT, ZonedDateTime utcEndZDT){
        ZoneId estZoneId = ZoneId.of("America/New_York");
        ZonedDateTime estStartZDT = ZonedDateTime.ofInstant(utcStartZDT.toInstant(), estZoneId);
        ZonedDateTime estEndZDT = ZonedDateTime.ofInstant(utcEndZDT.toInstant(), estZoneId);
        System.out.println("est start zdt: " + estStartZDT);
        System.out.println("est end zdt: " + estEndZDT);

        //compare input to business hours for overlap
        LocalTime startEST = estStartZDT.toLocalTime();
        LocalTime endEST = estEndZDT.toLocalTime();
        LocalDate dateSEST = estStartZDT.toLocalDate();
        LocalDate dateEEST = estEndZDT.toLocalDate();
        DayOfWeek dayStart = DayOfWeek.of(dateSEST.get(ChronoField.DAY_OF_WEEK));


        DayOfWeek dayEnd = DayOfWeek.of(dateEEST.get(ChronoField.DAY_OF_WEEK));
        LocalTime businessStart = LocalTime.of(8, 00);
        LocalTime busienssEnd = LocalTime.of(22, 00);
        if (startEST.isAfter(busienssEnd) || startEST.isBefore(businessStart) || (endEST.isAfter(busienssEnd)
                || endEST.isBefore(businessStart)) || dayStart.equals(DayOfWeek.SATURDAY) ||dayStart.equals(DayOfWeek.SUNDAY)
                || dayEnd.equals(DayOfWeek.SATURDAY) || dayEnd.equals(DayOfWeek.SUNDAY))
            return true;
        else
            return false;
    }

    public static boolean validateCustomerInput (String customerName, String customerAddress, String zipCode, String phoneNumber, String division, String country ){
        return customerName.isEmpty() || customerAddress.isEmpty() || zipCode.isEmpty() || phoneNumber.isEmpty() ||
                division == null || country == null;
    }





    public static String convertTime (String hour, String minute, LocalDate startdt){

        String startTime = hour + ":" + minute;
        ZoneId myZDoneId = ZoneId.systemDefault();
        DateTimeFormatter parser = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime localStartTime = LocalTime.parse(startTime, parser);
        LocalDateTime localStartDateTime = LocalDateTime.of(startdt, localStartTime);
        ZonedDateTime startmyZDT = ZonedDateTime.of(LocalDateTime.from(localStartDateTime), myZDoneId);
        ZoneId utcZneId = ZoneId.of("UTC");
        ZonedDateTime utcStartZDT = ZonedDateTime.ofInstant(startmyZDT.toInstant(), utcZneId);
        String startDate = java.lang.String.valueOf(utcStartZDT.toLocalDate());
        String startUTCTime = java.lang.String.valueOf((utcStartZDT.toLocalTime()));
        String startDT = startDate + " " + startUTCTime;

        return startDT;
    }

    public static ZonedDateTime convertUTCTime (String hour, String minute, LocalDate startdt){

        String startTime = hour + ":" + minute;
        ZoneId myZDoneId = ZoneId.systemDefault();
        DateTimeFormatter parser = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime localStartTime = LocalTime.parse(startTime, parser);
        LocalDateTime localStartDateTime = LocalDateTime.of(startdt, localStartTime);
        ZonedDateTime startmyZDT = ZonedDateTime.of(LocalDateTime.from(localStartDateTime), myZDoneId);
        ZoneId utcZneId = ZoneId.of("UTC");
        ZonedDateTime utcZDT = ZonedDateTime.ofInstant(startmyZDT.toInstant(), utcZneId);

        return utcZDT;
    }
}


