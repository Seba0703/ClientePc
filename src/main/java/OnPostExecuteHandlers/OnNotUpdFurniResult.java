package OnPostExecuteHandlers;


import Common.AlertBox;
import Common.Consts;
import Common.LoadingBox;
import OS_Command.WindowsCommand;
import javafx.application.Platform;
import javafx.scene.control.Button;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;

public class OnNotUpdFurniResult implements OnPostExecute {

    private LoadingBox loadingBox;
    private Button notUpdFurniture;

    public OnNotUpdFurniResult(LoadingBox loadingBox, Button notUpdFurniture) {
        this.loadingBox = loadingBox;
        this.notUpdFurniture = notUpdFurniture;
    }

    @Override
    public void onSucced(String json) {
        JSONObject jsonO = new JSONObject(json);
        JSONArray jsonA = jsonO.getJSONArray(Consts.RESULT);

        try {
            String title = "Número de producto\n";
            String username = System.getProperty("user.name");
            String path = Consts.ROOT_PATH + username + Consts.NEXT_ROOT_PATH + Consts.NOT_UPD_FURNI_CSV;
            FileOutputStream out = new FileOutputStream(path);
            out.write(title.getBytes());

            for (int i = 0; i < jsonA.length(); i++) {
                String member = String.valueOf(jsonA.getInt(i)) + "\n";
                out.write(member.getBytes());
            }
            out.close();
            WindowsCommand.goExcel(path);
            Platform.runLater(() -> {
                loadingBox.close();
                notUpdFurniture.setDisable(false);
            });
        } catch (IOException e) {
            onFail(2);
        }
    }

    @Override
    public void onSucced() {}

    @Override
    public void onFail(int statusCode) {
        Platform.runLater(() -> {
            loadingBox.close();
            notUpdFurniture.setDisable(false);
            AlertBox.display("ERROR", "No se pudo realizar la operación.");
        });
    }
}
