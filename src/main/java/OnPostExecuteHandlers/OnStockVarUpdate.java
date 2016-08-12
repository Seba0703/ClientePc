package OnPostExecuteHandlers;

import Builders.ConfigStockVarsBuilders;
import Common.AlertBox;
import Common.LoadingBox;
import javafx.application.Platform;

public class OnStockVarUpdate implements OnPostExecute {
    private ConfigStockVarsBuilders configStockVarsBuilders;
    private LoadingBox loadingBox;

    public OnStockVarUpdate(ConfigStockVarsBuilders configStockVarsBuilders, LoadingBox loadingBox) {
        this.configStockVarsBuilders = configStockVarsBuilders;
        this.loadingBox = loadingBox;
    }

    @Override
    public void onSucced(String json) {}

    @Override
    public void onSucced() {
        Platform.runLater(()->{
            loadingBox.close();
            configStockVarsBuilders.clear();
        });

    }

    @Override
    public void onFail(int statusCode) {
        Platform.runLater(()->{
            loadingBox.close();
            AlertBox.display("ERROR", "No se pudo realizar la operación.");
        });
    }
}
