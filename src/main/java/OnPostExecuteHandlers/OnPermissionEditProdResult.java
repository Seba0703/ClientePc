package OnPostExecuteHandlers;

import Builders.EditProdViewBuilder;
import Common.AlertBox;
import Common.Consts;
import Common.LoadingBox;
import Common.TaskCreator;
import InternetTools.InternetClient;
import javafx.application.Platform;
import javafx.scene.layout.VBox;

public class OnPermissionEditProdResult implements OnPostExecute {

    private final LoadingBox loadingPermi;
    private final VBox mainPane;

    public OnPermissionEditProdResult(LoadingBox loadingPermi, VBox mainPane) {
        this.loadingPermi = loadingPermi;
        this.mainPane = mainPane;
    }

    @Override
    public void onSucced(String json) {}

    @Override
    public void onSucced() {
        Platform.runLater(() -> {
            loadingPermi.close();
            EditProdViewBuilder editView = new EditProdViewBuilder(mainPane);
            LoadingBox loading = new LoadingBox();
            InternetClient client = new InternetClient(Consts.MATERIALS_ID, null, Consts.GET, null,
                    new OnMaterialsNamesResult(editView, loading), true );
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
