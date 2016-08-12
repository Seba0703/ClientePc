package OnPostExecuteHandlers;

import Builders.ListViewProductsBuilder;
import Common.AlertBox;
import Common.Consts;
import Common.LoadingBox;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.json.JSONArray;
import org.json.JSONObject;

public class OnProductsStatusResult implements OnPostExecute {

    private ListViewProductsBuilder listViewProducts;
    private LoadingBox loadingBox;

    public OnProductsStatusResult(ListViewProductsBuilder listViewBuilder, LoadingBox loading) {
        listViewProducts = listViewBuilder;
        loadingBox = loading;
    }

    @Override
    public void onSucced(String json) {
        JSONObject jsonObject =  new JSONObject(json);
        JSONArray jsonArray = (JSONArray) jsonObject.get(Consts.INSUMOS);

        ObservableList<Product> products = FXCollections.observableArrayList();

        for( int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonO =  jsonArray.getJSONObject(i);
            String nameProd = jsonO.getString(Consts.MATERIALS);
            int toBuy = jsonO.getInt(Consts.TO_BUY);
            int stockMax = jsonO.getInt(Consts.STOCK_MAX);
            int result = jsonO.getInt(Consts.RESULT);
            int quantity = jsonO.getInt(Consts.QUANTITY);
            products.add(new Product(nameProd, toBuy, stockMax, result,quantity));
        }

        Platform.runLater(() -> {
            listViewProducts.build(products);
            loadingBox.close();
        });
    }

    @Override
    public void onSucced() {}

    @Override
    public void onFail(int statusCode) {
        Platform.runLater(() -> {
            loadingBox.close();
            AlertBox.display("ERROR", "No se pudo realizar la operación.");
        });
    }
}
