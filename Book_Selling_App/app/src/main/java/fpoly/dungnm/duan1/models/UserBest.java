package fpoly.dungnm.duan1.models;

import com.google.gson.annotations.SerializedName;

public class UserBest {
    @SerializedName("_id")
    private String id;
    private double totalSpent;
    private int orderCount;

    public UserBest() {
    }

    public UserBest(String id, double totalSpent, int orderCount) {
        this.id = id;
        this.totalSpent = totalSpent;
        this.orderCount = orderCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getTotalSpent() {
        return totalSpent;
    }

    public void setTotalSpent(double totalSpent) {
        this.totalSpent = totalSpent;
    }

    public int getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(int orderCount) {
        this.orderCount = orderCount;
    }
}
