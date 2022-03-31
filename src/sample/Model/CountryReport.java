package sample.Model;

public class CountryReport {
    private int countPerCountry;
    private String country;


    public int getCountPerCountry() {
        return countPerCountry;
    }

    public void setCountPerCountry(int countPerCountry) {
        this.countPerCountry = countPerCountry;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public CountryReport (int countPerCountry, String country){
        this.countPerCountry = countPerCountry;
        this.country = country;
    }

}
