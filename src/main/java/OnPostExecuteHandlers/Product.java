package OnPostExecuteHandlers;

public class Product {

    private String name;
    private int toBuy;
    private int stockMax;

    private int result;

    private int quantity;

    public Product(String name, int toBuy, int stockMax, int result, int quantity) {
        this.name = name;
        this.toBuy = toBuy;
        this.stockMax = stockMax;
        this.result = result;
        this.quantity = quantity;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getToBuy() {
        return toBuy;
    }

    public void setToBuy(int toBuy) {
        this.toBuy = toBuy;
    }

    public int getStockMax() {
        return stockMax;
    }

    public void setStockMax(int stockMax) {
        this.stockMax = stockMax;
    }





}
