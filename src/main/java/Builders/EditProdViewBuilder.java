package Builders;

import Common.AlertBox;
import Common.Consts;
import Common.LoadingBox;
import Common.TaskCreator;
import InternetTools.InternetClient;
import OnPostExecuteHandlers.OnEditDataResult;
import OnPostExecuteHandlers.OnUsersIDsResult;
import PriceTextField.PriceTextField;
import PropertyDate.PropertyDayDueDate;
import PropertyDate.PropertyMonthDueDate;
import PropertyDate.PropertyYearBuy;
import PropertyDate.PropertyYearDueDate;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import org.controlsfx.control.textfield.TextFields;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static Common.DateParse.correctYear;
import static Common.DateParse.nonZeroDate;
import static Common.DateParse.parse;

public class EditProdViewBuilder implements Builder{

    private VBox mainPane;
    private TextField nameField, priceField, dayField, monthField, yearField, buyDayField, buyMonthField, buyYearField,
    userField;
    private List<String> suggestions;

    public EditProdViewBuilder(VBox mainPane) {

        this.mainPane = mainPane;
    }

    @Override
    public void build() {

        VBox vBox = new VBox();
        vBox.setPadding(new Insets(10, 12, 10, 12));
        vBox.setSpacing(10);

        HBox titleBox = new HBox();
        titleBox.setPadding(new Insets(5, 12, 5, 12));
        titleBox.setSpacing(6);
        titleBox.setAlignment(Pos.CENTER);

        //TITULO
        Text scenetitle = new Text("Editar un producto agregado.");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
        titleBox.getChildren().add(scenetitle);

        HBox hNamePriceUser = new HBox();
        hNamePriceUser.setPadding(new Insets(5, 12, 5, 12));
        hNamePriceUser.setSpacing(6);
        hNamePriceUser.setAlignment(Pos.CENTER);

        // NOMBRE
        Label nameProd = new Label("Nombre: ");
        nameField = new TextField();

        // PRECIO
        Label price = new Label("Precio: ");
        priceField = new PriceTextField();

        //USUARIO
        Label user = new Label("Usuario: ");
        userField = new TextField();

        List<String> userSuggestions = new ArrayList<>();

        InternetClient clientUsers = new InternetClient(Consts.USERS_ID, null, Consts.GET, null,
                new OnUsersIDsResult(userField, userSuggestions), true);
        try {
            clientUsers.connect();
        }catch (IOException e) {
            Platform.runLater(AlertBox::displayServerOffError);
        }

        // HBOX de precio, nombre y usuario agrego
        hNamePriceUser.getChildren().addAll(nameProd, nameField, price, priceField, user, userField);

        HBox dueDateBox = new HBox();
        dueDateBox.setPadding(new Insets(5, 12, 5, 12));
        dueDateBox.setSpacing(6);
        dueDateBox.setAlignment(Pos.CENTER);

        // FECHA DE VENCIMIENTO
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

        // HBOX de fecha de vencimiento agrego
        dueDateBox.getChildren().addAll(dueDate, dayField, separatorD_M, monthField, separatorM_Y, yearField);

        HBox buyDateBox = new HBox();
        buyDateBox.setPadding(new Insets(5, 12, 5, 12));
        buyDateBox.setSpacing(6);
        buyDateBox.setAlignment(Pos.CENTER);

        //FECHA DE COMPRA
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

        buyDateBox.getChildren().addAll(buyDate, buyDayField, separatorD_M_Buy, buyMonthField, separatorM_Y_Buy, buyYearField);

        HBox buttonBox = new HBox();
        buttonBox.setPadding(new Insets(5, 12, 5, 12));
        buttonBox.setSpacing(6);

        buttonBox.setAlignment(Pos.CENTER);
        Button goEditBtn = new Button("Ir a edición");
        buttonBox.getChildren().add(goEditBtn);

        goEditBtn.setOnAction( e -> {

            if (notEmpty(nameField) ) {
                if (suggestions.contains(nameField.getText())) {
                    if (correctDate(dayField, monthField, yearField)) {
                        if (correctDate(buyDayField, buyMonthField, buyYearField)) {
                            if (notEmpty(priceField) && nonZeroDouble(priceField)) {
                                if (notEmpty(userField)) {
                                    if (userSuggestions.contains(userField.getText())) {

                                        JSONObject jsonO = getRequest();

                                        LoadingBox loadingBox = new LoadingBox();

                                        AddProductsBuilder addTB = new AddProductsBuilder(vBox, Consts.DELETE_PROD);
                                        addTB.setUser(userField.getText());

                                        InternetClient client = new InternetClient(Consts.EDIT_PROD, null, Consts.PUT, jsonO.toString(),
                                                new OnEditDataResult(addTB, suggestions, loadingBox), true);
                                        new TaskCreator(client, loadingBox).start();

                                        loadingBox.display("Descargando", "Espere un momento por favor", "miloStock.gif");

                                    } else {
                                        AlertBox.display("ERROR", "Ingrese un usuario válido, por favor.");
                                    }
                                } else {
                                    AlertBox.display("ERROR", "Ingrese un nombre de usuario, por favor.");
                                }
                            } else {
                                AlertBox.display("ERROR", "Ingrese un precio válido, por favor.");
                            }
                        } else {
                            AlertBox.display("ERROR", "Ingrese una fecha de compra válida, por favor.");
                        }
                    } else {
                        AlertBox.display("ERROR", "Ingrese una fecha de vencimiento válida, por favor.");
                    }
                } else {
                    AlertBox.display("ERROR", "Ingrese un producto existente, por favor.");
                }
            } else {
                AlertBox.display("ERROR", "Ingrese el nombre del insumo, por favor.");
            }

        });

        Separator sepHor = new Separator();
        sepHor.setValignment(VPos.CENTER);

        vBox.getChildren().addAll(titleBox, hNamePriceUser, dueDateBox, buyDateBox , buttonBox, sepHor);

        if (mainPane.getChildren().size() == 2) {
            mainPane.getChildren().remove(1);
        }

        mainPane.getChildren().add(vBox);
    }

    private boolean nonZeroDouble(TextField priceField) {
        return Double.parseDouble(priceField.getText()) != 0d;
    }

    private JSONObject getRequest() {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Consts.MATERIALS, nameField.getText());
        jsonObject.put(Consts.DUE_DATE, parse(dayField.getText(), monthField.getText(), yearField.getText()));
        jsonObject.put(Consts.PRICE, Double.parseDouble(priceField.getText()));
        jsonObject.put(Consts.TRANSACTION_DATE, parse(buyDayField.getText(), buyMonthField.getText(), buyYearField.getText()));
        return jsonObject;
    }

    private boolean correctDate(TextField dayField, TextField monthField, TextField yearField) {
        return notEmpty(dayField) && notEmpty(monthField) && notEmpty(yearField) && nonZeroDate(dayField, monthField, yearField) && correctYear(yearField);
    }

    private boolean notEmpty(TextField field) {
        return !field.getText().isEmpty();
    }

    @Override
    public void bindSuggestions(List<String> suggestions) {
        this.suggestions = suggestions;
        TextFields.bindAutoCompletion(nameField, suggestions);

    }
}
