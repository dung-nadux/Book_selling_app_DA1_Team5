package fpoly.dungnm.duan1.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Objects;

public class Product implements Serializable {
    @SerializedName("_id")
    private String id;
    private String productname;
    private String image;
    private String author;
    private  String description;
    private double price;
    private int stock;
    private String status;
    private String cateID;
    private String createdAt;
    private String updatedAt;

    public Product() {
    }

    public Product(String id, String productname, String image, String author, String description, double price, int stock, String status, String cateID, String createdAt, String updatedAt) {
        this.id = id;
        this.productname = productname;
        this.image = image;
        this.author = author;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.status = status;
        this.cateID = cateID;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Product(String productname, String image, String author, String description, double price, int stock, String status, String cateID) {
        this.productname = productname;
        this.image = image;
        this.author = author;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.status = status;
        this.cateID = cateID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCateID() {
        return cateID;
    }

    public void setCateID(String cateID) {
        this.cateID = cateID;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id.equals(product.id); // So sánh dựa trên id
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
