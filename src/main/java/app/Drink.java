package app;

import com.google.gson.annotations.SerializedName;

public class Drink {
    public Drink(Integer id,Integer quantity, String itemNumber, String name, Float size, Integer price, String category) {
        this.id = id;
        this.quantity=quantity;
        this.itemNumber = itemNumber;
        this.name = name;
        this.size = size;
        this.price = price;
        this.category = category;
    }

    public Drink(Integer id,Integer quantity, String itemNumber, String name, Float size, Integer price, String category, Integer ownerId) {
        this.id = id;
        this.quantity=quantity;
        this.itemNumber = itemNumber;
        this.name = name;
        this.size = size;
        this.price = price;
        this.category = category;
        this.ownerId = ownerId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(String itemNumber) {
        this.itemNumber = itemNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getSize() {
        return size;
    }

    public void setSize(Float size) {
        this.size = size;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @SerializedName("id")
    private Integer id;
    @SerializedName("item_number")
    private String itemNumber;
    @SerializedName("name")
    private String name;
    @SerializedName("size")
    private Float size;
    @SerializedName("price")
    private Integer price;

    private String categoryName;
    @SerializedName("category_id")
    private String category;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @SerializedName("quantity")
    private Integer quantity;

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    @SerializedName("owner_id")
    private Integer ownerId;

}
