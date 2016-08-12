package OnPostExecuteHandlers;

import Singleton.UserSingleton;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class OnLoginResult implements OnPostExecute {

    private Stage window;
    private Scene mainScene;
    private Text actionTarget;
    private String sysResponse;
    private String userName;

    public OnLoginResult(Stage window, Scene main, Text action, String sysResponse, String userName) {
        this.window = window;
        mainScene = main;
        actionTarget = action;
        this.sysResponse =  sysResponse;
        this.userName = userName;

    }

    @Override
    public void onSucced(String json) {}
    @Override
    public void onSucced() {
        UserSingleton.getInstance().setUserName(userName);
        window.setScene(mainScene);
    }

    @Override
    public void onFail() {
        actionTarget.setFill(Color.FIREBRICK);
        actionTarget.setText(sysResponse);
    }
}

