package fpoly.dungnm.book_selling_app.models;

import java.io.Serializable;
import java.util.Date;

public class ModelVoucher implements Serializable {
    private int id;
    private String type;
    private int discount;
    private String content;
    private String startDate;
    private String endDate;
    private boolean isChecked;

    public ModelVoucher() {
    }

    public ModelVoucher(int id, String type, int discount, String content, String startDate, String endDate) {
        this.id = id;
        this.type = type;
        this.discount = discount;
        this.content = content;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public ModelVoucher(String type, int discount, String content, String startDate, String endDate) {
        this.type = type;
        this.discount = discount;
        this.content = content;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
