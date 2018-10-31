public class Product {
    private int cod;
    private String name;
    private int price;
    private int size;

    public Product(int cod, String name, int price, int size){
        this.cod = cod;
        this.name = name;
        this.price = price;
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getSize() {
        return size;
    }

    public int getCod() {
        return cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }

}
