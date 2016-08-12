package OnPostExecuteHandlers;

import Builders.ProductFieldsBuilder;
import Common.*;
import InternetTools.InternetClient;
import javafx.application.Platform;
import org.json.JSONObject;

import java.util.concurrent.atomic.AtomicBoolean;

public class OnAddProductResult implements OnPostExecute{

    private ProductFieldsBuilder prodFields;
    private LoadingBox loadingBox;
    private AtomicBoolean hasToUpdateSV;

    public OnAddProductResult(ProductFieldsBuilder productFieldsBuilder, LoadingBox load, AtomicBoolean hasToUpdateSV) {
        this.prodFields = productFieldsBuilder;
        loadingBox = load;
        this.hasToUpdateSV = hasToUpdateSV;
    }

    @Override
    public void onSucced(String json) {}

    @Override
    public void onSucced() {
        System.out.println("Exito");
        if (hasToUpdateSV.get()) {
            JSONObject jsonO = new JSONObject();
            jsonO.put(Consts.MATERIALS, prodFields.getNameFieldText());
            jsonO.put(Consts.STOCK_MAX, prodFields.getMaxFieldInt());
            jsonO.put(Consts.STOCK_MIN, prodFields.getMinFieldInt());
            jsonO.put(Consts.STOCK_SAFE, prodFields.getSafeFieldInt());
            jsonO.put(Consts.STOCK_MULTIPLY, prodFields.getMultiplyFieldInt());
            InternetClient client = new InternetClient(Consts.VARS_CONFIG, null, Consts.POST, jsonO.toString(),
                    new OnPostStockVars(prodFields, loadingBox, hasToUpdateSV), false);
            new TaskCreator(client, loadingBox).start();
        } else {

            Platform.runLater(() -> {
                if(prodFields.hasToDelete()) {
                    prodFields.delete();
                } else {
                    prodFields.clearProdFields();
                }
                loadingBox.close();
            });
        }
    }

    @Override
    public void onFail(int statusCode) {

        Platform.runLater(() -> {
            loadingBox.close();
            AlertBox.display("ERROR", "No se pudo realizar la operación.");
        });
    }

}
