package Common;

import javafx.scene.control.TextField;

public class StockVarFields {

    public TextField stockMin;
    public TextField stockMax;
    public TextField stockSafe;
    public TextField stockMultiplier;

    public StockVarFields(TextField stockMin, TextField stockMax, TextField stockSafe, TextField stockMultiplier) {
        this.stockMin = stockMin;
        this.stockMax = stockMax;
        this.stockSafe = stockSafe;
        this.stockMultiplier = stockMultiplier;
    }

    public void clear() {
        stockMin.clear();
        stockMax.clear();
        stockSafe.clear();
        stockMultiplier.clear();
    }


}
