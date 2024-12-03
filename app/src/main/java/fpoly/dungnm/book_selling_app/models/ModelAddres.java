package fpoly.dungnm.book_selling_app.models;

import java.io.Serializable;

public class ModelAddres implements Serializable {
    private int id;
    private int userId;
    private String fullName;
    private String phone;
    private String address;

    public ModelAddres(int userId, String fullName, String phone, String address) {
        this.userId = userId;
        this.fullName = fullName;
        this.phone = phone;
        this.address = address;
    }

    public ModelAddres(int id, int userId, String fullName, String phone, String address) {
        this.id = id;
        this.userId = userId;
        this.fullName = fullName;
        this.phone = phone;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ModelAddres() {
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
