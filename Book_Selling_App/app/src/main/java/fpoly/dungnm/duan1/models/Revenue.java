package fpoly.dungnm.duan1.models;

import com.google.gson.annotations.SerializedName;

public class Revenue {
    @SerializedName("_id")
    private String id;
    private double totalRevenue;
    private int orderCount;

    public Revenue() {
    }

    public Revenue(String id, double totalRevenue, int orderCount) {
        this.id = id;
        this.totalRevenue = totalRevenue;
        this.orderCount = orderCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public int getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(int orderCount) {
        this.orderCount = orderCount;
    }
}
