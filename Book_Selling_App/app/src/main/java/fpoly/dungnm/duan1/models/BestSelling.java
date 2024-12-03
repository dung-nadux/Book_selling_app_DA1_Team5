package fpoly.dungnm.duan1.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BestSelling implements Serializable {
    @SerializedName("_id")
    private String id;
    private int totalQuantity;
    private Product productInfo;

    public BestSelling() {
    }

    public BestSelling(String id, int totalQuantity, Product productInfo) {
        this.id = id;
        this.totalQuantity = totalQuantity;
        this.productInfo = productInfo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public Product getProductInfo() {
        return productInfo;
    }

    public void setProductInfo(Product productInfo) {
        this.productInfo = productInfo;
    }
}
