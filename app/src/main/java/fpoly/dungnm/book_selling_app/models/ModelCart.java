package fpoly.dungnm.book_selling_app.models;

public class ModelCart {
    private int userID;
    private int bookID;
    private int quantity;
    private double amount;

    public ModelCart() {
    }

    public ModelCart(int userID, int bookID, int quantity, double amount) {
        this.userID = userID;
        this.bookID = bookID;
        this.quantity = quantity;
        this.amount = amount;
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
}
