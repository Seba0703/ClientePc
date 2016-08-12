package OnPostExecuteHandlers;

public class ProductTransaction {

    private String name;
    private int quantity;
    private String dueDate;
    private String destiny;
    private String transDate;
    private String transType;
    private double price;
    private int change;
    private String userName;

    public ProductTransaction(String name, String userName, int quantity, String destiny, String transDate, String dueDate, String transType, double price, int change) {
        this.name = name;
        this.userName = userName;
        this.quantity = quantity;
        this.dueDate = dueDate;
        this.destiny = destiny;
        this.transDate = transDate;
        this.transType = transType;
        this.price = price;
        this.change = change;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDestiny() {
        return destiny;
    }

    public void setDestiny(String destiny) {
        this.destiny = destiny;
    }

    public String getTransDate() {
        return transDate;
    }

    public void setTransDate(String transDate) {
        this.transDate = transDate;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }


    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public int getChange() {
        return change;
    }

    public void setChange(int change) {
        this.change = change;
    }
}
