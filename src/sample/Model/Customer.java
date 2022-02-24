package sample.Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Customer

{ /**List of appointments of the Customer */
   // private final ObservableList <Appointment> associatedAppointments = FXCollections.observableArrayList();

    /** Customer's ID  */
    private int customerId;

    /** Customer's name */
    private String customerName;

/** Customer's address */
    private String customerAddress;

    /**Customer's postal code */
    private String zipCode;

    /**Customer's phone number */
    private String phoneNumber;

    /**Customer's country */
     private String country;

     /**Customer's Division */
     private String division;

     /**Customer's Division ID*/
     private int divisionId;


//private int divisionID ?

    /**Method creates customer instance.
     *
     *
     */
    public Customer (int customerId, String customerName, String customerAddress, String zipCode, String phoneNumber, String country, String division, int divisionId){
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.zipCode = zipCode;
        this.phoneNumber = phoneNumber;
        this.division = division;
        this.country = country;
        this.divisionId = divisionId;

    }

public int getDivisionId(){return divisionId;}

    public void setDivisionId (int divisionId){
        this.divisionId = divisionId;
    }

    public String getDivision(){return division;}

    public void setDivision(String division) {
        this.division = division;
    }

    public String getCountry(){return country; }

    public void setCountry(String country){

        this.country = country;
    }
    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**Method to add appointments for the Customer.
     * *
     */

    //public void addAssociatedAppointments (Appointment appointment) {
      //  associatedAppointments.add(appointment);
  //  }

    /**Method to get all appoitnemts of the customer.
     *
     */
   // public ObservableList <Appointment> getAllAssociatedAppointments(){
  //      return associatedAppointments;
  //  }
}
