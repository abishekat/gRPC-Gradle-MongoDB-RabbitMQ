package cu.dssassignment2.mongospringutil.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "EduCostStatQueryTwo")
public class EduCostStatQueryTwo {
    private String id;
    private String year;
    private String state;
    private String type;
    private String length;
    private String expense;
    private String value;

    public EduCostStatQueryTwo(String id, String year, String state, String type, String length, String expense, String value) {
        this.id = id;
        this.year = year;
        this.state = state;
        this.type = type;
        this.length = length;
        this.expense = expense;
        this.value = value;
    }

    public EduCostStatQueryTwo() {
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

    @Override
    public String toString() {
        return "EduCostStat{" +
                "id='" + id + '\'' +
                ", year='" + year + '\'' +
                ", state='" + state + '\'' +
                ", type='" + type + '\'' +
                ", length='" + length + '\'' +
                ", expense='" + expense + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
