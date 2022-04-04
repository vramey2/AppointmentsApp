package sample.Controller;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.Optional;

/**This class is to create additional methods for controller classes in order to increase code re-usability.
 * @author Veronika Ramey
 */
public class Utility {

    /**Displays error alert. Method displays error alert and uses switch cases to show different error text.
     * @param error number of error message*/
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


    /**Displays confirmation alert. Method is used to ask for confirmation, different content text is displayed using switch.
     * @param numberAlert number of confirmation message*/
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

    /**Displays information type alert. Method displays information with different text depending on switch case.
     * @param infoNumber number of informational message*/
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

    /**Displays warning alert. Method displays warning alert and uses switch cases to show different warning text.
     * @param warningNumber number of warning message*/
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

/**Method for current UTC time. Method is used to format current UTC time
 * @return String  formatted current UTC time*/
    public static String utcTimeNow (){
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        ZonedDateTime utcTimeNow  = ZonedDateTime.now(ZoneOffset.UTC);

        return utcTimeNow.format(timeFormatter);
    }


    /**Method validates date and time. Method validates date and time to be within business hours using EST timezone
     *
     * @param utcStartZDT starting date and time UTC
     * @param utcEndZDT ending date and time UTC
     * @return boolean returns true if not validated and false if it is within business hours
     */
    public static boolean validateBusinessHours (ZonedDateTime utcStartZDT, ZonedDateTime utcEndZDT){
        ZoneId estZoneId = ZoneId.of("America/New_York");
        ZonedDateTime estStartZDT = ZonedDateTime.ofInstant(utcStartZDT.toInstant(), estZoneId);
        ZonedDateTime estEndZDT = ZonedDateTime.ofInstant(utcEndZDT.toInstant(), estZoneId);
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

    /**Method validates customer input. Method is used to validate input for the customer.
     *
     * @param customerName Customer name
     * @param customerAddress Customer address
     * @param zipCode  Customer's zip code
     * @param phoneNumber Customer's phone number
     * @param division Customer's division
     * @param country Customer's country
     * @return boolean returns true if any field is empty for the customer
     */
    public static boolean validateCustomerInput (String customerName, String customerAddress, String zipCode, String phoneNumber, String division, String country ){
        return customerName.isEmpty() || customerAddress.isEmpty() || zipCode.isEmpty() || phoneNumber.isEmpty() ||
                division == null || country == null;
    }

    /**Method to convert time. Method returns utc date and time as a string
     *
     * @param hour hour for appointment
     * @param minute minute for appointment
     * @param startdt Date and time for appointment
     * @return returns formatted utc date and time as a string
     */
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

    /**Method to convert to UTC date and time as ZonedDateTime. Method is used to convert to UTC ZonedDateTime
     *
     * @param hour hour of appointment
     * @param minute minute of appointment
     * @param startdt date and time for appointment
     * @return ZonedDateTime UTC date and time
     */
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


