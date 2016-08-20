package OnPostExecuteHandlers;

import Singleton.UserSingleton;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class OnLoginResult implements OnPostExecute {

    private Stage window;
    private Text actionTarget;
    private String userName;

    public OnLoginResult(Stage window, Text action, String userName) {
        this.window = window;
        actionTarget = action;
        this.userName = userName;

    }

    @Override
    public void onSucced(String json) {}

    @Override
    public void onSucced() {
        //el header elimina los espacios al final
        UserSingleton.getInstance().setUserName(userName.toUpperCase().replaceAll("\\s+$", ""));
        window.close();
        MainStage stage = new MainStage();
        try {
            stage.start(window);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFail(int statusCode) {
        actionTarget.setFill(Color.FIREBRICK);
        actionTarget.setText("Usuario no existe o contraseña errónea.");
    }
}

