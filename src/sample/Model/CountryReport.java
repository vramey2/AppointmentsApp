package sample.Model;

/** This class is for Country Report object of Appointments Scheduling application.
        * @author Veronika Ramey
        * */
public class CountryReport {

    /**Total number of customers per country*/
    private int countPerCountry;

    /** Country*/
    private String country;


    /**Method creates instance of Country Report. This is a constructor method for CountryReport with corresponding fields.
     *
     * @param countPerCountry Total number by country
     * @param country Country
     */
    public CountryReport (int countPerCountry, String country){
        this.countPerCountry = countPerCountry;
        this.country = country;
    }

    /**Getter method for total by country. Method is a getter for total number of customers per country.
     * @return total by country*/

    public int getCountPerCountry() {
        return countPerCountry;
    }


    /**Setter method for total by country. Method is a setter for total number of customers per country.
     * @param countPerCountry Total by country*/
    public void setCountPerCountry(int countPerCountry) {
        this.countPerCountry = countPerCountry;
    }


    /**Getter method for country. Method is a getter for country.
     * @return Country*/
    public String getCountry() {
        return country;
    }

    /**Setter for country. Method is a setter for country.
     * @param country Country*/
    public void setCountry(String country) {
        this.country = country;
    }


}
