package OnPostExecuteHandlers;

import Common.LoadingBox;
import Singleton.UserSingleton;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class OnLoginResult implements OnPostExecute {

    private Stage window;
    private Text actionTarget;
    private String userName;
    private LoadingBox loadingBox;

    public OnLoginResult(Stage window, Text action, String userName, LoadingBox loadingBox) {
        this.window = window;
        actionTarget = action;
        this.userName = userName;

        this.loadingBox = loadingBox;
    }

    @Override
    public void onSucced(String json) {}

    @Override
    public void onSucced() {
        //el header elimina los espacios al final
        UserSingleton.getInstance().setUserName(userName.toUpperCase().replaceAll("\\s+$", ""));

        Platform.runLater(() ->{
            loadingBox.close();
            window.close();
            MainStage stage = new MainStage();
            try {
                stage.start(window);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void onFail(int statusCode) {

        Platform.runLater(() ->{
            actionTarget.setFill(Color.FIREBRICK);
            actionTarget.setText("Usuario no existe o contraseña errónea.");
            loadingBox.close();
        });
    }
}

