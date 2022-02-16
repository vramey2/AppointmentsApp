package sample.Model;

import java.sql.Time;
import java.sql.Timestamp;

public class Appointment {

    private int id;
    private String title;
    private String description;
    private int contact;
    private String location;

    private String type;
    private int customerID;
    private final int userID;
   // private Timestamp startTime;
    private Timestamp startDateTime;
    private final Timestamp endDateTime;

    public Appointment (int id, String title, String description, int contact, String location, String type,
                        Timestamp startDateTime, Timestamp endDateTime, int customerID, int userID ){
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

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getContact() {
        return contact;
    }

    public void setContact(int contact) {
        this.contact = contact;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public int getUserID() {
        return userID;
    }

    public Timestamp getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(Timestamp startDateTime) {
        this.startDateTime = startDateTime;
    }

    public Timestamp getEndDateTime() {
        return endDateTime;
    }
}