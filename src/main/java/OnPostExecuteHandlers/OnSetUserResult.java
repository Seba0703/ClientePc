package OnPostExecuteHandlers;

import Builders.AddUserBuilder;
import Common.LoadingBox;
import PropertyUsersField.PropertyAddUsersField;
import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class OnSetUserResult implements OnPostExecute {


    private PropertyAddUsersField propertyAddUsersField;
    private final Text actiontarget;
    private final AddUserBuilder addUserBuilder;
    private LoadingBox loadingBox;

    public OnSetUserResult(PropertyAddUsersField propertyAddUsersField, Text actiontarget, AddUserBuilder addUserBuilder, LoadingBox loadingBox) {
        this.propertyAddUsersField = propertyAddUsersField;
        this.actiontarget = actiontarget;
        this.addUserBuilder = addUserBuilder;
        this.loadingBox = loadingBox;
    }

    @Override
    public void onSucced(String json) {}

    @Override
    public void onSucced() {
        Platform.runLater(()-> {
            loadingBox.close();
            propertyAddUsersField.set();
            addUserBuilder.clear();
            actiontarget.setFill(Color.FORESTGREEN);
            actiontarget.setText("Usuario configurado con éxito.");
        });
    }

    @Override
    public void onFail() {
        Platform.runLater(()-> {
            loadingBox.close();
            actiontarget.setFill(Color.FIREBRICK);
            actiontarget.setText("Error al configurar al usuario.");
        });
    }
}
