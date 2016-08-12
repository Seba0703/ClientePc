package OnPostExecuteHandlers;

import Common.AlertBox;
import Common.Consts;
import javafx.application.Platform;
import javafx.scene.control.TextField;
import org.controlsfx.control.textfield.TextFields;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Sebastian on 09/08/2016.
 */
public class OnUsersIDsResult implements OnPostExecute {


    private TextField userField;
    private List<String> userSuggestions;

    public OnUsersIDsResult(TextField userField, List<String> userSuggestions) {

        this.userField = userField;
        this.userSuggestions = userSuggestions;
    }

    @Override
    public void onSucced(String json) {
        JSONObject jsonO = new JSONObject(json);
        JSONArray jsonArray = jsonO.getJSONArray(Consts.RESULT);

        for(int i = 0; i < jsonArray.length(); i++) {
            userSuggestions.add(jsonArray.getJSONObject(i).getString(Consts.USER));
        }

        TextFields.bindAutoCompletion(userField, userSuggestions);

    }

    @Override
    public void onSucced() {}

    @Override
    public void onFail() {
        Platform.runLater(() -> AlertBox.display("ERROR", "No se pudo realizar la operación."));

    }
}
