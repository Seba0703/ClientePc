package OnPostExecuteHandlers;

import Builders.AddProductsBuilder;
import Common.AlertBox;
import Common.Consts;
import Common.LoadingBox;
import javafx.application.Platform;
import org.json.JSONObject;

import java.util.List;

import static javafx.application.Platform.runLater;

public class OnEditDataResult implements OnPostExecute {

    private final AddProductsBuilder addTB;
    private final List<String> suggestions;
    private final LoadingBox loadingBox;

    public OnEditDataResult(AddProductsBuilder addTB, List<String> suggestions, LoadingBox loadingBox) {

        this.addTB = addTB;
        this.suggestions = suggestions;
        this.loadingBox = loadingBox;
    }

    @Override
    public void onSucced(String json) {
        JSONObject jsonObject = new JSONObject(json);
        String name = jsonObject.getString(Consts.MATERIALS);
        int quantity = jsonObject.getInt(Consts.QUANTITY);
        double price = jsonObject.getDouble(Consts.PRICE);
        int dueDate = jsonObject.getInt(Consts.DUE_DATE);
        int buyDate = jsonObject.getInt(Consts.TRANSACTION_DATE);

        runLater(() -> {
            addTB.build();
            addTB.bindSuggestions(suggestions);
            addTB.setAll(name, quantity, price, dueDate, buyDate);
            loadingBox.close();
        });
    }

    @Override
    public void onSucced() {}

    @Override
    public void onFail() {
        Platform.runLater(()-> {
            loadingBox.close();
            AlertBox.display("ERROR", "No se puedo realizar la operación.");
        });
    }
}
