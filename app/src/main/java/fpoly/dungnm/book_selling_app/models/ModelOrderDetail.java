package fpoly.dungnm.book_selling_app.models;

public class ModelOrderDetail {
    private int orderId;
    private int bookingId;
    private int quantity;
    private double unitPrice;
    private double amount;

    public ModelOrderDetail() {
    }

    public ModelOrderDetail(int orderId, int bookingId, int quantity, double unitPrice, double amount) {
        this.orderId = orderId;
        this.bookingId = bookingId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.amount = amount;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
