package OnPostExecuteHandlers;

import Builders.AddUserBuilder;
import Common.AlertBox;
import Common.Consts;
import Common.LoadingBox;
import Common.TaskCreator;
import InternetTools.InternetClient;
import javafx.application.Platform;

public class OnPermissionAddUserResult implements OnPostExecute {
    private LoadingBox loadingPermi;

    public OnPermissionAddUserResult(LoadingBox loadingPermi) {
        this.loadingPermi = loadingPermi;
    }

    @Override
    public void onSucced(String json) {}

    @Override
    public void onSucced() {
        Platform.runLater(() -> {
            loadingPermi.close();
            AddUserBuilder addUserBuilder = new AddUserBuilder();
            LoadingBox loadingBox = new LoadingBox();
            InternetClient clientUsers = new InternetClient(Consts.USERS_ID, null, Consts.GET, null,
                    new OnUsersPropertiesResult(addUserBuilder, loadingBox), true);
            new TaskCreator(clientUsers, loadingBox).start();
            loadingBox.display("Descargando...",  "Espere un momento por favor.", "loading.gif");
        });

    }

    @Override
    public void onFail(int statusCode) {
        if (statusCode == 411) {
            System.out.println("no permi");
            Platform.runLater(() -> {
                loadingPermi.close();
                AlertBox.display("ERROR permisos", "No posee los permisos correspondientes.");
            });
        } else {
            System.out.println("error");
            Platform.runLater(() -> {
                loadingPermi.close();
                AlertBox.display("ERROR", "No se pudo realizar la operación.");
            });
        }
    }
}
