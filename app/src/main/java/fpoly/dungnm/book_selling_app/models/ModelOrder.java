package fpoly.dungnm.book_selling_app.models;

public class ModelOrder {
    private int id;
    private int userId;
    private int discountVoucherID;
    private int shippingVoucherID;
    private String status;
    private double totalPrice;
    private String orderDate;
    private int address;

    public ModelOrder() {
    }

    public ModelOrder(int id, int userId, int discountVoucherID, int shippingVoucherID, String status, double totalPrice, String orderDate, int address) {
        this.id = id;
        this.userId = userId;
        this.discountVoucherID = discountVoucherID;
        this.shippingVoucherID = shippingVoucherID;
        this.status = status;
        this.totalPrice = totalPrice;
        this.orderDate = orderDate;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getDiscountVoucherID() {
        return discountVoucherID;
    }

    public void setDiscountVoucherID(int discountVoucherID) {
        this.discountVoucherID = discountVoucherID;
    }

    public int getShippingVoucherID() {
        return shippingVoucherID;
    }

    public void setShippingVoucherID(int shippingVoucherID) {
        this.shippingVoucherID = shippingVoucherID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public int getAddress() {
        return address;
    }

    public void setAddress(int address) {
        this.address = address;
    }
}
