package OnPostExecuteHandlers;

import Builders.ProductFieldsBuilder;
import Common.AlertBox;
import Common.LoadingBox;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;

import java.util.concurrent.atomic.AtomicBoolean;

public class OnPostStockVars implements OnPostExecute {

    private ProductFieldsBuilder prodFields;
    private LoadingBox loadingBox;
    private AtomicBoolean hasToUpdateSV;

    public OnPostStockVars(ProductFieldsBuilder productFieldsBuilder, LoadingBox loadingBox, AtomicBoolean hasToUptateSV) {
        prodFields = productFieldsBuilder;
        this.loadingBox = loadingBox;
        this.hasToUpdateSV = hasToUptateSV;

    }

    @Override
    public void onSucced(String json) {}

    @Override
    public void onSucced() {
        hasToUpdateSV.set(false);
        Platform.runLater(() -> {
            if (prodFields.hasToDelete()) {
                prodFields.delete();
            } else {
                prodFields.clearProdFields();
                prodFields.clearStockFields();
                prodFields.removeFieldsStock();
            }
            loadingBox.close();
        });
    }

    @Override
    public void onFail() {
        Platform.runLater(() -> {
            loadingBox.close();
            AlertBox.display("ERROR", "No se pudo realizar la operación.");
        });
    }
}
