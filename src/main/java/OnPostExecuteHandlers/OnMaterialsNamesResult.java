package OnPostExecuteHandlers;

import Builders.Builder;
import Common.AlertBox;
import Common.Consts;
import Common.LoadingBox;
import javafx.application.Platform;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OnMaterialsNamesResult implements OnPostExecute {

    private Builder addProduct;
    private LoadingBox loading;

    public OnMaterialsNamesResult(Builder addProductsBuilder, LoadingBox loadingBox ) {
        addProduct = addProductsBuilder;
        loading = loadingBox;
    }

    @Override
    public void onSucced(String json) {
        JSONObject jsonO = new JSONObject(json);
        JSONArray jsonArray = jsonO.getJSONArray(Consts.RESULT);

        List<String> suggestions = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject matID = (JSONObject) jsonArray.get(i);
            suggestions.add((String) matID.get(Consts.MATERIALS));
        }

        Platform.runLater(() -> {
            addProduct.build();
            addProduct.bindSuggestions(suggestions);
            loading.close();
        });
    }

    @Override
    public void onSucced() {}

    @Override
    public void onFail(int statusCode) {
        Platform.runLater(() -> {
            loading.close();
            AlertBox.display("ERROR", "No se pudo conectar con el servidor.");
        });
    }
}
