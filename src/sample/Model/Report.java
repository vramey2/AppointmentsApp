package sample.Model;

public class Report {
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCountType() {
        return countType;
    }

    public void setCountType(int countType) {
        this.countType = countType;
    }

    private int count;
    private String month;
    private String year;
    private String type;
    private int countType;


    public Report (int count, String month, String year, String type, int countType){

        this.count = count;
        this.month = month;
        this.year = year;
        this.type = type;
        this.countType = countType;
    };

}
