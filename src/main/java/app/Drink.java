package app;

public class Drink {
    public Drink(Integer id, String itemNumber, String name, Float size, Integer price, String category) {
        this.id = id;
        this.itemNumber = itemNumber;
        this.name = name;
        this.size = size;
        this.price = price;
        this.category = category;
    }

    public Drink(Integer id, String itemNumber, String name, Float size, Integer price, String category, Integer ownerId) {
        this.id = id;
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

    private Integer id;
    private String itemNumber;
    private String name;
    private Float size;
    private Integer price;
    private String category;

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    private Integer ownerId;

}
