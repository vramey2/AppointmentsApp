package sample.Model;

import java.sql.Time;
import java.sql.Timestamp;


/** This class is for Appointment object of Appointments Scheduling application.
 * @author Veronika Ramey
 * */

public class Appointment {

    /** Appointment ID*/
    private int id;

    /** Title of appointment*/
    private String title;

    /** Description of appointment*/
    private String description;

    /** ID number of appointment's contact*/
    private int contact;

    /**Location of appointment*/
    private String location;

    /**Type of appointment*/
    private String type;

    /**ID of customer*/
    private int customerID;

    /**ID of user scheduling appointment*/
    private final int userID;

    /**Appointment start date and time*/
    private Timestamp startDateTime;

    /**Appointment end date and time*/
    private Timestamp endDateTime;

    /**Appointment start date and time as a String*/
    private String startString;

   /**Appointment end date and time as a String*/
    private String endString;


    /**Method creates instace of Appoinmtent. This is a constructor method for appointment with corresponding fields.
     * @param id Id number of the appointment
     * @param title  Title of the appointment
     * @param description  Description of the appointment
     * @param contact   Contact for the appointment
     * @param location  Location of the appointment
     * @param type  Type of the appointment
     * @param startDateTime  Start date and time of the appointment
     * @param endDateTime  End date and time of the appointment
     * @param customerID  Id of customer whose appointment is scheduled
     * @param userID  ID of user scheduling or adjusting the appointment
     * @param startString  Start date and time for appointment as a string
     * @param endString  End date and time for appointment as a string*/
    public Appointment (int id, String title, String description, int contact, String location, String type,
                        Timestamp startDateTime, Timestamp endDateTime, int customerID, int userID, String startString, String endString ){
        this.id = id;
        this.title= title;
        this.description = description;
        this.contact = contact;
        this.location = location;
        this.type = type;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.userID = userID;
        this.customerID = customerID;
        this.startString = startString;
        this.endString = endString;

    }

    /**This is getter method for appointment ID. Method is a getter for ID of the appointment.
     * @return ID number of the appointment.
     */
    public int getId() {
        return id;
    }


    /**Setter method for ID of the appointment. Method is a setter for id of the appointment.
     * @param id Identification number of the appointment
     */
    public void setId(int id) {
        this.id = id;
    }

    /** Getter method for Title. Method is a getter for title of the appointment
     *  @return Title of the appointment */
    public String getTitle() {
        return title;
    }


    /**Setter method for Title. Method is a setter for title of the appointment.
     *  @param title  Title of the appointment */
    public void setTitle(String title) {
        this.title = title;
    }

    /**Getter method for Description. Method is a getter for appointment's description.
     * @return description of the appointment */
    public String getDescription() {
        return description;
    }

    /**Setter method for description. Method is a setter for appointment's description.
     * @param  description Description of the appointment*/
    public void setDescription(String description) {
        this.description = description;
    }

    /**Getter method for contact. Method is a getter for appointment's contact.
     * @return Contact for the appointment */
    public int getContact() {
        return contact;
    }

    /**Setter method for contact. Method is a setter for appointment's contact.
     * @param contact Contact for the appointment*/
    public void setContact(int contact) {
        this.contact = contact;
    }

    /**Getter method for location. Method is a getter for appointment's location.
     * @return Location of the appointment*/
    public String getLocation() {
        return location;
    }

    /**Setter method for location. Method is a setter for appointment's location.
     * @param location Location of the appointment */
    public void setLocation(String location) {
        this.location = location;
    }

    /**Getter method for type of appointment. Method is a getter for appointment's type.
     * @return Type of the appointment*/
    public String getType() {
        return type;
    }

    /**Setter method for type of appointment. Method is a setter for appointment's type.
     * @param type Type of appointment*/
    public void setType(String type) {
        this.type = type;
    }

    /**Getter method for cutsomer's id. Method is a getter for appointment's customer id
     *  @return ID of appointment's customer*/
     public int getCustomerID() {
        return customerID;
    }

    /**Setter method for customer's id. Method is a setter for appointment's customer id
     * @param customerID Id of appointment's customer */
    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    /**Getter method for user's id. Method is a getter for id of the user scheduling appointment.
     * @return User's ID */
    public int getUserID() {
        return userID;
    }

    /**Getter method for appointment's start. Method is a getter for appointment's start date and time.
     * @return Start date and time of the appointment*/
    public Timestamp getStartDateTime() {
        return startDateTime;
    }


    /**Setter method for appointment's start. Method is a setter for appointment's start date and time.
     * @param startDateTime Start date and time of the appointment*/
    public void setStartDateTime(Timestamp startDateTime) {
        this.startDateTime = startDateTime;
    }

    /**Getter method for appointment's end. Method is a getter for appointment's end date and time.
     * @return End date and time of the appointment*/
    public Timestamp getEndDateTime() {
        return endDateTime;
    }

    /**Setter method for appointment's end. Method is a setter for appointment's end date and time.
     * @param endDateTime End date and time of teh appointment*/
     public void setEndDateTime(Timestamp endDateTime){
        this.endDateTime = endDateTime;
    }

    /**Getter for appointment's start as a string. Method is a getter for appointment's start date and time as a string.
     * @return Start date and time as a string for the appointment*/
    public String getStartString() {
        return startString;
    }

    /**Setter for appointment's start as a string. Method is a setter for appointment's start date and time as a string.
     *  @param startString Start date and time as a string for the appointment*/
    public void setStartString(String startString) {
        this.startString = startString;
    }

    /**Getter for appointment's end as a string. Method is a getter for appointment's end date and time as a string.
     * @return End date and time as a string for the appointment*/
    public String getEndString() {
        return endString;
    }


    /**Setter for appointment's end as a string. Method is a setter for appointment's end date and time as a string.
     * @param endString End date and time as string for the appointment*/
    public void setEndString(String endString) {
        this.endString = endString;
    }
}
