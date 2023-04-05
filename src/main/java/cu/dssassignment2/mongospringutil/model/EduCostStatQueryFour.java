package cu.dssassignment2.mongospringutil.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "EduCostStatQueryFour")
public class EduCostStatQueryFour {
    private String id;
    private String year;
    private String state;
    private String type;
    private String length;
    private String expense;
    private String value;

    private String growthRate;
    private String pastYear;

    public EduCostStatQueryFour() {
    }

    public String getGrowthRate() {
        return growthRate;
    }

    public void setGrowthRate(String growthRate) {
        this.growthRate = growthRate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getExpense() {
        return expense;
    }

    public void setExpense(String expense) {
        this.expense = expense;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getPastYear() {
        return pastYear;
    }

    public void setPastYear(String pastYear) {
        this.pastYear = pastYear;
    }

}
