package OnPostExecuteHandlers;

import Builders.ConfigStockVarsBuilders;
import Common.AlertBox;
import Common.Consts;
import Common.LoadingBox;
import Common.TaskCreator;
import InternetTools.InternetClient;
import javafx.application.Platform;
import javafx.scene.layout.VBox;

public class OnPermissionConfigVarsResult implements OnPostExecute {

    private VBox mainPane;
    private LoadingBox loadingPermi;

    public OnPermissionConfigVarsResult(VBox mainPane, LoadingBox loadingPermi) {
        this.mainPane = mainPane;
        this.loadingPermi = loadingPermi;
    }

    @Override
    public void onSucced(String json) {}

    @Override
    public void onSucced() {
        Platform.runLater(()-> {
            loadingPermi.close();
            System.out.println("permiso");
            ConfigStockVarsBuilders configStockVar = new ConfigStockVarsBuilders(mainPane);
            LoadingBox loading = new LoadingBox();
            InternetClient client = new InternetClient(Consts.MATERIALS_ID, null, Consts.GET, null,
                    new OnMaterialsNamesResult(configStockVar, loading), true );
            new TaskCreator(client, loading).start();
            loading.display("Descargando...", "Espere un momento por favor.", "loading.gif");
        });
    }

    @Override
    public void onFail(int statusCode) {
        if (statusCode == 411) {
            Platform.runLater(() -> {
                loadingPermi.close();
                AlertBox.display("ERROR permisos", "No posee los permisos correspondientes.");
            });
        } else {
            Platform.runLater(() -> {
                loadingPermi.close();
                AlertBox.display("ERROR", "No se pudo realizar la operación.");
            });
        }
    }
}
