package fpoly.dungnm.book_selling_app.models;

public class ModelProducts {
    int id;
     byte[] image;
    String title;
    String author;
    int price;
    String description;
    String category;

    public ModelProducts() {
    }

//    public ModelProducts(int id, String image, String title, String author, Integer price, String description, String category) {
//        this.id = id;
//        this.image = image;
//        this.title = title;
//        this.author = author;
//        this.price = price;
//        this.description = description;
//        this.category = category;
//    }
//
//    public ModelProducts(String image, String title, String author, Integer price, String description, String category) {
//        this.image = image;
//        this.title = title;
//        this.author = author;
//        this.price = price;
//        this.description = description;
//        this.category = category;
//    }


    public ModelProducts(int id, byte[] image, String title, String author, int price, String description, String category) {
        this.id = id;
        this.image = image;
        this.title = title;
        this.author = author;
        this.price = price;
        this.description = description;
        this.category = category;
    }

    public ModelProducts(byte[] image, String title, String author, int price, String description, String category) {
        this.image = image;
        this.title = title;
        this.author = author;
        this.price = price;
        this.description = description;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
