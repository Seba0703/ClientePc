package Builders;

import Common.AlertBox;
import Common.Consts;
import Common.LoadingBox;
import Common.TaskCreator;
import InternetTools.InternetClient;
import OnPostExecuteHandlers.OnStockVarUpdate;
import OnPostExecuteHandlers.OnVarsConfigGet;
import PropertyNumbers.PropertyOnlyNumbers;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import org.controlsfx.control.textfield.TextFields;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigStockVarsBuilders implements Builder{

    private  TextField nameField;
    private List<String> suggestions;
    private TextField minField;
    private TextField safeField;
    private TextField multiplyField;
    private VBox mainPane;
    private Button save;

    public ConfigStockVarsBuilders(VBox mainPane) {
        this.mainPane = mainPane;
    }

    @Override
    public void build() {

        GridPane grid = new GridPane();

        Text scenetitle = new Text("Ingrese un insumo existente para configurar las variables de stock.");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
        grid.add(scenetitle, 0, 0, 2, 1);

        Label nameProd = new Label("Nombre: ");
        nameField = new TextField();
        grid.add(nameProd, 0, 1);
        grid.add(nameField, 1, 1);

        HBox box = new HBox(10);
        Button search = new Button("Buscar");
        box.setAlignment(Pos.CENTER);
        box.getChildren().add(search);
        grid.add(box, 0, 2, 7, 1);          // TODO: verrrrr

        search.setOnAction(e-> {
            if (suggestions.contains(nameField.getText())) {
                LoadingBox loadingBox = new LoadingBox();
                Map<String, String> header = new HashMap<>();
                header.put(Consts.MATERIALS, nameField.getText());
                InternetClient client = new InternetClient(Consts.VARS_CONFIG, header, Consts.GET, null,
                        new OnVarsConfigGet(this,loadingBox), true);
                new TaskCreator(client, loadingBox).start();
                loadingBox.display("Descarndo...", "Espere un momento, por favor.", "miloStock.gif");
            } else {
                AlertBox.display("INVÁLIDO", "Ingrese un insumo válido, por favor.");
            }
        });

        Separator sepHor = new Separator();
        sepHor.setValignment(VPos.CENTER);
        GridPane.setConstraints(sepHor, 0, 3);
        GridPane.setColumnSpan(sepHor, 7);
        grid.getChildren().add(sepHor);

        //Label maxLabel = new Label("Stock maximo: ");
        //TextField maxField = new TextField();
        //maxField.textProperty().addListener(new PropertyOnlyNumbers(maxField));
        Label minLabel = new Label("Stock minimo: ");
        minField = new TextField();
        minField.textProperty().addListener(new PropertyOnlyNumbers(minField));
        Label safeLabel = new Label("Zona de riesgo: ");
        safeField = new TextField();
        safeField.textProperty().addListener(new PropertyOnlyNumbers(safeField));
        Label multiplyLabel = new Label("Multiplicador de Zona de Riesgo: ");
        multiplyField = new TextField();
        multiplyField.textProperty().addListener(new PropertyOnlyNumbers(multiplyField));

        ImageView graphStock = new ImageView(
                new Image("file:stockVarsGraph.png")
        );
        GridPane.setConstraints(graphStock, 5, 7);
        GridPane.setRowSpan(graphStock, 5);
        grid.getChildren().add(graphStock);

        HBox saveBox = new HBox(10);
        save = new Button("Guardar");
        save.setVisible(false);
        save.setDisable(true);
        box.setAlignment(Pos.CENTER);
        box.getChildren().add(search);
        grid.add(saveBox, 0, 7, 7, 1);

        save.setOnAction(e-> {
            JSONObject request = buildRequest();
            LoadingBox loadingBox = new LoadingBox();

            InternetClient client = new InternetClient(Consts.VARS_CONFIG, null, Consts.PUT, request.toString(),
                    new OnStockVarUpdate(this, loadingBox), false);
            new TaskCreator(client, loadingBox).start();
            loadingBox.display("Guardando...", "Por favor espere un momento.", "miloStock.gif");
        });

        //grid.add(maxLabel, 0, 4);
        //grid.add(maxField, 1, 4);
        grid.add(minLabel, 0, 4);
        grid.add(minField, 1, 4);
        grid.add(safeLabel, 0, 5);
        grid.add(safeField, 1, 5);
        grid.add(multiplyLabel, 0, 6);
        grid.add(multiplyField, 1, 6);

    }

    @Override
    public void bindSuggestions(List<String> suggestions) {
        this.suggestions = suggestions;
        TextFields.bindAutoCompletion(nameField, suggestions);
    }

    public void setAll(int stockMin, int safeVar, int multiply) {
        minField.setText(String.valueOf(stockMin));
        safeField.setText(String.valueOf(safeVar));
        multiplyField.setText(String.valueOf(multiply));
        save.setVisible(true);
        save.setDisable(false);
    }

    public void clear() {
        minField.clear();
        safeField.clear();
        multiplyField.clear();
        save.setVisible(false);
        save.setDisable(true);
        nameField.clear();
    }
}
