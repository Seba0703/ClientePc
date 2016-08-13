package Builders;

import Common.Consts;
import Common.DateParse;
import PriceTextField.PriceTextField;
import PropertyDate.PropertyDayDueDate;
import PropertyDate.PropertyMonthDueDate;
import PropertyDate.PropertyYearBuy;
import PropertyDate.PropertyYearDueDate;
import PropertyNumbers.PropertyOnlyNumbers;
import Singleton.UserSingleton;
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
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import org.controlsfx.control.textfield.TextFields;
import org.json.JSONObject;

import java.util.List;
import java.util.regex.Pattern;

import static Common.DateParse.correctYear;

public class ProductFieldsBuilder {

    private TextField nameField, cantField, priceField, yearField, monthField, dayField, buyYearField, buyDayField, buyMonthField;
    private TextField maxField, minField,safeField,multiplyField;

    private Label maxLabel, minLabel, safeLabel, multiplyLabel;

    private Separator sepHor;

    private ImageView graphStock;

    private String path;
    private GridPane grid;
    private Button saveButton;

    public ProductFieldsBuilder(String path, GridPane gridPane, Button save) {
        this.path = path;

        grid = gridPane;
        saveButton = save;

        // -> Ingresar nombre del producto
        Label nameProd = new Label("Nombre: ");
        nameField = new TextField();

        // -> Ingresar Cantidad
        Label cant = new Label("Cantidad: ");
        cantField = new TextField();
        cantField.textProperty().addListener(new PropertyOnlyNumbers(cantField));

        // -> Ingresar precio
        Label price = new Label("Precio: ");
        final Pattern pattern = Pattern.compile("^\\d*\\.?\\d*$");
        priceField = new PriceTextField();

        //  Ingresar fecha de vencimiento dia mes año
        Label dueDate = new Label("Fecha de vencimiento: ");
        yearField = new TextField();
        yearField.setPromptText("Año");
        yearField.textProperty().addListener(new PropertyYearDueDate(yearField));

        monthField = new TextField();
        monthField.setPromptText("Mes");
        monthField.textProperty().addListener(new PropertyMonthDueDate(monthField));

        dayField = new TextField();
        dayField.setPromptText("Día");
        dayField.textProperty().addListener(new PropertyDayDueDate(dayField));

        Label separatorD_M = new Label("/");
        Label separatorM_Y = new Label("/");

        // -> Ingresar fecha de compra dia mes año TODO: agregar properties reales
        Label buyDate = new Label("Fecha de compra: ");
        buyYearField = new TextField();
        buyYearField.setPromptText("Año");
        buyYearField.textProperty().addListener(new PropertyYearBuy(buyYearField));

        buyMonthField = new TextField();
        buyMonthField.setPromptText("Mes");
        buyMonthField.textProperty().addListener(new PropertyMonthDueDate(buyMonthField));

        buyDayField = new TextField();
        buyDayField.setPromptText("Día");
        buyDayField.textProperty().addListener(new PropertyDayDueDate(buyDayField));

        Label separatorD_M_Buy = new Label("/");
        Label separatorM_Y_Buy = new Label("/");

        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text scenetitle = new Text("Agregar un producto nuevo o ya existente.");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
        grid.add(scenetitle, 0, 0, 2, 1);

        grid.add(nameProd, 0, 1);
        grid.add(nameField, 1, 1);

        grid.add(cant, 0, 2);
        grid.add(cantField, 1, 2);

        grid.add(price, 0, 3);
        grid.add(priceField, 1, 3);

        grid.add(dueDate, 0, 4);
        grid.add(dayField, 1, 4);
        grid.add(separatorD_M, 2, 4);
        grid.add(monthField, 3, 4);
        grid.add(separatorM_Y, 4, 4);
        grid.add(yearField, 5, 4);

        grid.add(buyDate, 0, 5);
        grid.add(buyDayField, 1, 5);
        grid.add(separatorD_M_Buy, 2, 5);
        grid.add(buyMonthField, 3, 5);
        grid.add(separatorM_Y_Buy, 4, 5);
        grid.add(buyYearField, 5, 5);
    }

    //Se contruyen en la instancia de producto nuevo, que no estan en la lista de la base de datos.
    public void addStockVarsFields() {

        maxLabel = new Label("Stock maximo: ");
        maxField = new TextField();
        maxField.textProperty().addListener(new PropertyOnlyNumbers(maxField));
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
        GridPane.setConstraints(graphStock, 5, 7);
        GridPane.setRowSpan(graphStock, 5);
        grid.getChildren().add(graphStock);

        sepHor = new Separator();
        sepHor.setValignment(VPos.CENTER);
        GridPane.setConstraints(sepHor, 0, 6);
        GridPane.setColumnSpan(sepHor, 7);
        grid.getChildren().add(sepHor);

        grid.add(maxLabel, 0, 7);
        grid.add(maxField, 1, 7);
        grid.add(minLabel, 0, 8);
        grid.add(minField, 1, 8);
        grid.add(safeLabel, 0, 9);
        grid.add(safeField, 1, 9);
        grid.add(multiplyLabel, 0, 10);
        grid.add(multiplyField, 1, 10);
    }

    public void addNewSuggestion(List<String> suggestions) {
        String name = nameField.getText().toUpperCase();
        grid.getChildren().remove(nameField);
        nameField = new TextField();
        TextFields.bindAutoCompletion(nameField, suggestions);
        grid.add(nameField, 1, 1);
        nameField.setText(name);
    }

    public boolean correctInput(boolean hasUpdateStockVars) {
        boolean correctCant;
        boolean correctStockBar = false;

        try {
            correctCant = nonZeroFieldInt(cantField);
            if (hasUpdateStockVars) {
                correctStockBar = nonZeroFieldInt(safeField) && nonZeroFieldInt(maxField) && nonZeroFieldInt(minField) && nonZeroFieldInt(multiplyField);
            }
        }catch (NumberFormatException e) {
            correctCant = false;
        }

        if (hasUpdateStockVars) {
            return correctCant && correctStockBar && nonZeroFieldDouble(priceField) && correctYear(yearField) &&
                    nonZeroFieldInt(dayField) && nonZeroFieldInt(monthField) && correctYear(buyYearField) && nonZeroFieldInt(buyMonthField) && nonZeroFieldInt(buyDayField);
        } else {
            return correctCant && nonZeroFieldDouble(priceField) && correctYear(yearField) && nonZeroFieldInt(dayField)
                    && nonZeroFieldInt(monthField) && correctYear(buyYearField) && nonZeroFieldInt(buyMonthField) && nonZeroFieldInt(buyDayField);
        }
    }

    public boolean allFieldsFull(boolean hasUpdateStockVars) {

        boolean oldProd = fieldFull(nameField) && fieldFull(priceField) && fieldFull(cantField) && fieldFull(yearField) &&
                fieldFull(monthField) && fieldFull(dayField) && fieldFull(buyDayField) && fieldFull(buyMonthField) && fieldFull(buyYearField);

        if (hasUpdateStockVars) {
            return oldProd && fieldFull(safeField) && fieldFull(maxField) && fieldFull(minField) && fieldFull(multiplyField);
        }
        return oldProd;
    }

    private boolean fieldFull(TextField field) {
        return !field.getText().isEmpty();
    }

    private boolean nonZeroFieldInt(TextField field) {
        return Integer.parseInt(field.getText()) != 0;
    }

    private boolean nonZeroFieldDouble(TextField field) {
        return Double.parseDouble(field.getText()) != 0D;
    }

    /**Correr en la thread de javaFX
     * */
    public void clearProdFields() {
        nameField.clear();
        cantField.clear();
        priceField.clear();
        dayField.clear();
        monthField.clear();
        yearField.clear();
        buyDayField.clear();
        buyMonthField.clear();
        buyYearField.clear();
    }

    /**Correr en la thread de javaFX
     * */
    public void clearStockFields() {
        maxField.clear();
        minField.clear();
        safeField.clear();
        multiplyField.clear();
    }

    public TextField getNameField() {
        return nameField;
    }

    public String getNameFieldText() {
        return nameField.getText().toUpperCase();
    }

    public int getCantFieldInt() {
        return Integer.parseInt(cantField.getText());
    }

    public double getPriceFieldDouble() {
        return Double.parseDouble(priceField.getText());
    }

    public int getMaxFieldInt() {
        return Integer.parseInt(maxField.getText());
    }

    public int getMinFieldInt() {
        return Integer.parseInt(minField.getText());
    }

    public int getSafeFieldInt() {
        return Integer.parseInt(safeField.getText());
    }

    public int getMultiplyFieldInt() {
        return Integer.parseInt(multiplyField.getText());
    }

    public void setAll(String name, int quantity, double price, int dueDate, int buyDate) {
        nameField.setText(name);
        cantField.setText(String.valueOf(quantity));
        priceField.setText(String.valueOf(price));

        String dueDateS = String.valueOf(dueDate);
        dayField.setText(dueDateS.substring(6));
        monthField.setText(dueDateS.substring(4, 6));
        yearField.setText(dueDateS.substring(0, 4));

        String buyDateS = String.valueOf(buyDate);
        buyDayField.setText(buyDateS.substring(6));
        buyMonthField.setText(buyDateS.substring(4, 6));
        buyYearField.setText(buyDateS.substring(0, 4));

    }

    /**Correr en la thread de javaFX
     * */
    public void removeFieldsStock() {
        grid.getChildren().remove(saveButton);
        grid.getChildren().remove(graphStock);
        grid.getChildren().remove(maxField);
        grid.getChildren().remove(minField);
        grid.getChildren().remove(safeField);
        grid.getChildren().remove(multiplyField);
        grid.getChildren().remove(maxLabel);
        grid.getChildren().remove(minLabel);
        grid.getChildren().remove(safeLabel);
        grid.getChildren().remove(multiplyLabel);
        grid.getChildren().remove(sepHor);
        grid.add(saveButton, 3, 7);
    }

    public JSONObject buildRequest() {
        JSONObject request = new JSONObject();
        request.put(Consts.MATERIALS, nameField.getText().toUpperCase());
        request.put(Consts.QUANTITY, getCantFieldInt());
        request.put(Consts.PRICE, getPriceFieldDouble());

        int dueDateInt = DateParse.parse(dayField.getText(), monthField.getText(), yearField.getText());
        request.put(Consts.DUE_DATE, dueDateInt);

        int buyDateInt = DateParse.parse(buyDayField.getText(), buyMonthField.getText(), buyYearField.getText());
        request.put(Consts.TRANSACTION_DATE, buyDateInt);
        request.put(Consts.USER, UserSingleton.getInstance().getUserName().toUpperCase());

        return request;

    }

    public boolean hasToDelete() {
        return path.equals(Consts.DELETE_PROD);
    }

    public void delete() {
        grid.getChildren().clear();
    }
}
