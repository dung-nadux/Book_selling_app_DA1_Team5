package fpoly.dungnm.book_selling_app.models;

import java.io.Serializable;

public class ModelCart implements Serializable {
    private int userID;
    private int bookID;
    private int quantity;
    private double amount;
    private int status;

    public ModelCart() {
    }

    public ModelCart(int userID, int bookID, int quantity, double amount, int status) {
        this.userID = userID;
        this.bookID = bookID;
        this.quantity = quantity;
        this.amount = amount;
        this.status = status;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getBookID() {
        return bookID;
    }

    public void setBookID(int bookID) {
        this.bookID = bookID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
