package cu.dssassignment2.mongospringutil.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "EduCostStatQueryFive")
public class EduCostStatQueryFive {
    private String region;
    private String expense;

    public EduCostStatQueryFive() {
    }

    public EduCostStatQueryFive(String expense, String region) {

        this.expense = expense;
        this.region = region;
    }

    @Override
    public String toString() {
        return "EduCostStatQueryFive{" +
                "region='" + region + '\'' +
                ", expense='" + expense + '\'' +
                '}';
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getExpense() {
        return expense;
    }

    public void setExpense(String expense) {
        this.expense = expense;
    }
}
