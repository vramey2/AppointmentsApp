package sample.Model;


/** This class is for Customer object of Appointments Scheduling application.
 * @author Veronika Ramey
 * */
public class Customer

{

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


    /**Method creates customer instance. Method is used to create instance of a customer.
     *
     * @param customerId ID of customer
     * @param customerName Name of customer
     * @param customerAddress Address of the customer
     * @param zipCode Zip code of the customer
     * @param phoneNumber Phone number of the customer
     * @param country CountrY of the customer
     * @param division Division of the customer
     * @param divisionId Division's ID
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

    /**Getter method for division ID. Method is a getter for division's id.
     * @return Division's id */
    public int getDivisionId(){return divisionId;}

    /**Setter method for division ID. Method is a setter for division's ID
     * @param divisionId ID of division*/
    public void setDivisionId (int divisionId){
        this.divisionId = divisionId;
    }

    /**Getter method for Division. Method is a getter for division.
     * @return Division name*/
    public String getDivision(){return division;}

    /**Setter method for Division. Method is a setter for division of the customer.
     * @param division Name of division */
    public void setDivision(String division) {
        this.division = division;
    }

    /**Getter method for country. Method is a getter for customer's country.
     * @return Country of the customer*/
    public String getCountry(){return country; }


    /**Setter method for country. Method is a setter for customer's country.
     * @param country Customer's country */
    public void setCountry(String country){

        this.country = country;
    }

    /**Getter method for customer's id.  Method is a getter for customer's id.
     * @return ID of the customer*/
    public int getCustomerId() {
        return customerId;
    }

    /**Setter method for customer's id. Method is a setter for customer's id.
     * @param customerId Setter method for customer's id*/
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    /**Getter method for customer's name. Method is a getter for customer's name.
     * @return Name of customer*/
    public String getCustomerName() {
        return customerName;
    }

    /**Setter method for customer's name. Method is a setter for customer's name.
     * @param customerName Name of the customer*/
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }


    /**Getter for customer's address. Method is a getter for customer's address.
     * @return Address of the customer*/
    public String getCustomerAddress() {
        return customerAddress;
    }


    /**Setter for customers address. Method is a setter for customer's address.
     * @param customerAddress Address of the customer*/
    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }


    /**Getter for zip code. Method is a getter for customer's zip code.
     * @return Zip code of the customer*/
    public String getZipCode() {
        return zipCode;
    }

    /**Setter fro the zip code. Method is a setter for customers' zip code.
     * @param zipCode Zip code of the customer*/
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    /**Getter for the phone number. Method is a getter for the customer's phone number.
     * @return Customer's phone number*/
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**Setter for the phone number. Method is a setter for the customer's phone number.
     * @param phoneNumber Customer's phone number*/
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    }
