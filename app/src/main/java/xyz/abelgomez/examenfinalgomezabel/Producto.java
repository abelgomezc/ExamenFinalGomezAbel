package xyz.abelgomez.examenfinalgomezabel;

public class Producto {
    private int id;
    private String title;//
    private double price;//
    private String description;//
    private String category;//
    private String image;//
    private double rate;
    private int count;

    public Producto(int id, String title, double price, String description, String category, String image, double rate, int count) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.description = description;
        this.category = category;
        this.image = image;
        this.rate = rate;
        this.count = count;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public String getImage() {
        return image;
    }

    public double getRate() {
        return rate;
    }

    public int getCount() {
        return count;
    }
}
