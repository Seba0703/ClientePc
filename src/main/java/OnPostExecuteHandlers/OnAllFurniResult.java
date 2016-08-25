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

public class OnAllFurniResult implements OnPostExecute {

    private LoadingBox loadingBox;
    private Button allFurniture;

    public OnAllFurniResult(LoadingBox loadingBox, Button allFurniture) {
        this.loadingBox = loadingBox;
        this.allFurniture = allFurniture;
    }

    @Override
    public void onSucced(String json) {
        JSONObject jsonO = new JSONObject(json);
        JSONArray jsonA = jsonO.getJSONArray(Consts.RESULT);

        try {
            String title = "Sucursal;Número de producto;Precio;Fecha de compra;Última actualización;Estado\n";
            String username = System.getProperty("user.name");
            String path = Consts.ROOT_PATH + username + Consts.NEXT_ROOT_PATH + Consts.ALL_FURNI_CSV;
            FileOutputStream out = new FileOutputStream(path);
            out.write(title.getBytes());

            System.out.println(jsonA.length());

            for (int i = 0; i < jsonA.length(); i++) {
                JSONObject furniture = jsonA.getJSONObject(i);
                int suc = furniture.getInt(Consts.N_SUC);
                int member = furniture.getInt(Consts.N_MEMBER);
                String lastUpdate = Consts.format(furniture.getInt(Consts.LAST_UPDATE));
                String state = Consts.getMapState(furniture.getInt(Consts.STATE));
                double price = furniture.getDouble(Consts.FINAL_PRICE);
                String buyDate = Consts.format(furniture.getInt(Consts.BUY_DATE));
                String row = suc + ";" + member + ";" + price + ";" + buyDate + ";" + lastUpdate + ";" + state + "\n";
                out.write(row.getBytes());
            }
            out.close();
            WindowsCommand.goExcel(path);
            Platform.runLater(() -> {
                loadingBox.close();
                allFurniture.setDisable(false);
            });

        } catch (java.io.IOException e) {
           onFail(2);
        }
    }

    @Override
    public void onSucced() {}

    @Override
    public void onFail(int statusCode) {
        Platform.runLater(() -> {
            loadingBox.close();
            allFurniture.setDisable(false);
            AlertBox.display("ERROR", "No se pudo realizar la operación.");
        });

    }
}
