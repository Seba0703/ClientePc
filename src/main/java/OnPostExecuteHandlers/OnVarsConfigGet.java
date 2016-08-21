package OnPostExecuteHandlers;

import Builders.ConfigStockVarsBuilders;
import Common.AlertBox;
import Common.Consts;
import Common.LoadingBox;
import OnPostExecuteHandlers.OnPostExecute;
import javafx.application.Platform;
import org.json.JSONObject;


public class OnVarsConfigGet implements OnPostExecute{

    private ConfigStockVarsBuilders configStockVarsBuilders;
    private LoadingBox loadingBox;

    public OnVarsConfigGet(ConfigStockVarsBuilders configStockVarsBuilders, LoadingBox loadingBox) {
        this.configStockVarsBuilders = configStockVarsBuilders;
        this.loadingBox = loadingBox;
    }

    @Override
    public void onSucced(String json) {
        JSONObject jsonObject = new JSONObject(json);
        int stockMin = jsonObject.getInt(Consts.STOCK_MIN);
        int safeVar = jsonObject.getInt(Consts.STOCK_SAFE);
        int multiply = jsonObject.getInt(Consts.STOCK_MULTIPLY);
        int max = jsonObject.getInt(Consts.STOCK_MAX);
        Platform.runLater(()-> {
            loadingBox.close();
            configStockVarsBuilders.setAll(max, stockMin, safeVar, multiply);
        });
    }

    @Override
    public void onSucced() {}

    @Override
    public void onFail(int statusCode) {
        Platform.runLater(()-> {
            loadingBox.close();
            AlertBox.display("ERROR", "No se pudo realizar la operación.");
        });
    }
}
