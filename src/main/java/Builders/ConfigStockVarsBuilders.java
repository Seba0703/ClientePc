package Builders;

import Common.AlertBox;
import Common.Consts;
import Common.LoadingBox;
import Common.TaskCreator;
import InternetTools.InternetClient;
import OnPostExecuteHandlers.OnStockVarUpdate;
import OnPostExecuteHandlers.OnVarsConfigGet;
import PropertyNumbers.PropertyOnlyNumbers;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
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
    private TextField minField, safeField, multiplyField;
    private VBox mainPane;
    private Button save, cancel;
    private Label minLabel, multiplyLabel, safeLabel;
    private ImageView graphStock;
    private String prodSearched;

    public ConfigStockVarsBuilders(VBox mainPane) {
        this.mainPane = mainPane;
    }

    @Override
    public void build() {

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

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
        grid.add(box, 3, 1, 7, 1);

        search.setOnAction(e-> {
            if (suggestions.contains(nameField.getText().toUpperCase())) {
                prodSearched = nameField.getText().toUpperCase();
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
        GridPane.setConstraints(sepHor, 0, 2);
        GridPane.setColumnSpan(sepHor, 7);
        grid.getChildren().add(sepHor);

        //Label maxLabel = new Label("Stock maximo: ");
        //TextField maxField = new TextField();
        //maxField.textProperty().addListener(new PropertyOnlyNumbers(maxField));
        minLabel = new Label("Stock minimo: ");
        minField = new TextField();
        minField.textProperty().addListener(new PropertyOnlyNumbers(minField));
        safeLabel = new Label("Zona de riesgo: ");
        safeField = new TextField();
        safeField.textProperty().addListener(new PropertyOnlyNumbers(safeField));
        multiplyLabel = new Label("Multiplicador de Zona de Riesgo: ");
        multiplyField = new TextField();
        multiplyField.textProperty().addListener(new PropertyOnlyNumbers(multiplyField));

        graphStock = new ImageView(
                new Image("file:stockVarsGraph.png")
        );
        GridPane.setConstraints(graphStock, 5, 3);
        GridPane.setRowSpan(graphStock, 5);
        grid.getChildren().add(graphStock);

        HBox saveBox = new HBox(10);
        save = new Button("Guardar");

        cancel = new Button("Cancelar");
        saveBox.setAlignment(Pos.CENTER);
        saveBox.getChildren().add(save);
        saveBox.getChildren().add(cancel);
        grid.add(saveBox, 0, 6, 7, 1);

        cancel.setOnAction(e-> {
            clear();
        });

        save.setOnAction(e-> {
            if (fieldOK(minField) && fieldOK(safeField) && fieldOK(multiplyField) &&
                    prodSearched.equals(nameField.getText().toUpperCase())) {
                JSONObject request = buildRequest();
                LoadingBox loadingBox = new LoadingBox();
                InternetClient client = new InternetClient(Consts.VARS_CONFIG, null, Consts.PUT, request.toString(),
                        new OnStockVarUpdate(this, loadingBox), false);
                new TaskCreator(client, loadingBox).start();
                loadingBox.display("Guardando...", "Por favor espere un momento.", "miloStock.gif");
            } else {
                AlertBox.display("INVÁLIDO", "Campos vacíos, con cero o el insumo buscado es otro.");
            }
        });

        //grid.add(maxLabel, 0, 4);
        //grid.add(maxField, 1, 4);
        grid.add(minLabel, 0, 3);
        grid.add(minField, 1, 3);
        grid.add(safeLabel, 0, 4);
        grid.add(safeField, 1, 4);
        grid.add(multiplyLabel, 0, 5);
        grid.add(multiplyField, 1, 5);
        grid.setAlignment(Pos.CENTER);
        disableNotVIsibleEdition();

        if (mainPane.getChildren().size() == 2) {
            mainPane.getChildren().remove(1);
        }

        mainPane.getChildren().add(grid);
    }

    private boolean fieldOK(TextField field) {
        boolean nonZero;
        try {
            nonZero = Integer.parseInt(field.getText()) != 0;
        }catch (NumberFormatException e) {
            nonZero = false;
        }

       return !field.getText().isEmpty() && nonZero;
    }

    private void disableNotVIsibleEdition() {
        disableNotVisible(save); disableNotVisible(cancel);
        disableNotVisible(minField); disableNotVisible(minLabel);
        disableNotVisible(multiplyField); disableNotVisible(multiplyLabel);
        disableNotVisible(safeField); disableNotVisible(safeLabel);
        disableNotVisible(graphStock);
    }

    private void ableVIsibleEdition() {
        ableVisible(save); ableVisible(cancel);
        ableVisible(minField); ableVisible(minLabel);
        ableVisible(multiplyField); ableVisible(multiplyLabel);
        ableVisible(safeField); ableVisible(safeLabel);
        ableVisible(graphStock);
    }

    private void disableNotVisible(Node node) {
        node.setDisable(true);
        node.setVisible(false);
    }
    private void ableVisible(Node node) {
        node.setDisable(false);
        node.setVisible(true);
    }

    private JSONObject buildRequest() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Consts.MATERIALS, nameField.getText());
        jsonObject.put(Consts.STOCK_MULTIPLY, Integer.parseInt(multiplyField.getText()));
        jsonObject.put(Consts.STOCK_SAFE, Integer.parseInt(safeField.getText()));
        jsonObject.put(Consts.STOCK_MIN, Integer.parseInt(minField.getText()));
        return jsonObject;
    }

    @Override
    public void bindSuggestions(List<String> suggestions) {
        this.suggestions = suggestions;
        TextFields.bindAutoCompletion(nameField, suggestions);
    }

    public void setAll(int stockMin, int safeVar, int multiply) {
        ableVIsibleEdition();
        minField.setText(String.valueOf(stockMin));
        safeField.setText(String.valueOf(safeVar));
        multiplyField.setText(String.valueOf(multiply));
    }

    public void clear() {
        disableNotVIsibleEdition();
        minField.clear();
        safeField.clear();
        multiplyField.clear();
        nameField.clear();
    }
}
