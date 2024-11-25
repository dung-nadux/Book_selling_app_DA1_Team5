package fpoly.dungnm.book_selling_app.models;

public class ModelWallet {
    private int userId;
    private double balance;

    public ModelWallet() {
    }
    public ModelWallet(int userId, double balance) {
        this.userId = userId;
        this.balance = balance;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
