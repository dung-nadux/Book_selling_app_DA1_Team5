package fpoly.dungnm.duan1.models;

import com.google.gson.annotations.SerializedName;

public class Favourite {
    @SerializedName("_id")
    private String id;
    private String username;
    private Product productID;
    private String createdAt;
    private String updatedAt;

    public Favourite() {
    }

    public Favourite(String id, String username, Product productID, String createdAt, String updatedAt) {
        this.id = id;
        this.username = username;
        this.productID = productID;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Product getProductID() {
        return productID;
    }

    public void setProductID(Product productID) {
        this.productID = productID;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
