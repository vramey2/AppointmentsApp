package sample.Model;


/** This class is for Report object of Appointments Scheduling application.
 * @author Veronika Ramey
 * */
public class Report {

    /**Total appointments count by month*/
    private int count;

    /**Month*/
    private String month;


    /**Year*/
    private String year;

    /**Type*/
    private String type;

    /**Total number by type*/
    private int countType;

    /**Method creates instance of Report. This is a constructor method for Report with corresponding fields.
     *
     * @param count Total appointments by month
     * @param month Month of appointment
     * @param year Year of appointment
     * @param type Type of appointment
     * @param countType Total appointments by type
     */
    public Report (int count, String month, String year, String type, int countType){

        this.count = count;
        this.month = month;
        this.year = year;
        this.type = type;
        this.countType = countType;
    }

    /**Getter method for total by month. Method is a getter for total appointments by month.
     * @return Total in each month*/
    public int getCount() {
        return count;
    }

    /**Setter method for total by month. Method is a setter for total appointments by month.
     * @param count Total by month*/
     public void setCount(int count) {
        this.count = count;
    }

    /**Getter for month. Method is getter for month of appointment.
     * @return Month of appointment*/
    public String getMonth() {
        return month;
    }


    /**Setter for month. Method is setter for month of appointment.
     * @param month Month of appointment*/

    public void setMonth(String month) {
        this.month = month;
    }

    /**Getter for year. Method is getter for year of appointment.
     * @return Year of appointment*/
    public String getYear() {
        return year;
    }

    /**Setter for year. Method is setter for year of appointment.
     * @param year Year of appointment*/
    public void setYear(String year) {
        this.year = year;
    }

    /**Getter for type of appointment. Method is a getter for appointment's type.
     *  @return Type of appointment*/
    public String getType() {
        return type;
    }

    /**Setter for type of appointment. Method is a setter for appointment's type.
     * @param type Type of appointment */
    public void setType(String type) {
        this.type = type;
    }

    /**Getter for total by type. Method is a getter for total of appointments by type.
     *  @return Total by type*/
    public int getCountType() {
        return countType;
    }

    /**Setter for total by type. Method is a setter for total of appointments by type.
     * @param countType Total by Type*/
    public void setCountType(int countType) {
        this.countType = countType;
    }


}
