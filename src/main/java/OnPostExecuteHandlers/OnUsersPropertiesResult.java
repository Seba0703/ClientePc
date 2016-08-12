package OnPostExecuteHandlers;

import Builders.AddUserBuilder;
import Common.*;
import javafx.application.Platform;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OnUsersPropertiesResult implements OnPostExecute {


    private final AddUserBuilder addUserBuilder;
    private final LoadingBox loadingBox;

    public OnUsersPropertiesResult(AddUserBuilder addUserBuilder, LoadingBox loadingBox) {
        this.addUserBuilder = addUserBuilder;
        this.loadingBox = loadingBox;
    }

    @Override
    public void onSucced(String json) {
        JSONObject jsonObject = new JSONObject(json);
        JSONArray jsonArray = jsonObject.getJSONArray(Consts.RESULT);

        List<UserProperties> userProperties = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonO = jsonArray.getJSONObject(i);
            UserProperties userProp = new UserProperties(jsonO.getBoolean(Consts.PROP_ADD_LOGON), jsonO.getBoolean(Consts.PROP_CONFIG_STOCK_VARS),
                    jsonO.getBoolean(Consts.PROP_EDIT), jsonO.getBoolean(Consts.PROP_EXTRACT),
                    jsonO.getBoolean(Consts.PROP_PC_IN), jsonO.getString(Consts.USER));
            userProperties.add(userProp);
        }

        UsersPropertiesList usersPropertiesList = new UsersPropertiesList(userProperties);

        addUserBuilder.addUsersProps(usersPropertiesList);

        Platform.runLater(() -> {
            loadingBox.close();
            addUserBuilder.build();
        });

    }

    @Override
    public void onSucced() {}

    @Override
    public void onFail(int statusCode) {
        Platform.runLater(() -> {
            loadingBox.close();
            AlertBox.display("ERROR", "No se pudo descargar usuarios.");
        });
    }
}
