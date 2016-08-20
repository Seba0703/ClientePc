package Builders;

import Common.AlertBox;
import Common.Consts;
import Common.LoadingBox;
import Common.TaskCreator;
import InternetTools.InternetClient;
import OS_Command.WindowsCommand;
import OnPostExecuteHandlers.OnSearchResult;
import OnPostExecuteHandlers.OnUsersIDsResult;
import PropertyDate.PropertyDayDueDate;
import PropertyDate.PropertyMonthDueDate;
import PropertyDate.PropertyYearBuy;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.controlsfx.control.textfield.TextFields;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static Common.DateParse.correctYear;
import static Common.DateParse.nonZeroDate;
import static Common.DateParse.parse;

public class SearchViewBuilder implements Builder {

    private VBox mainPane;
    private TextField prodField;
    private List<String> suggestions;
    private List<String> userSuggestions;

    public SearchViewBuilder(VBox pane) {
        mainPane = pane;
    }

    public void build() {
        VBox searchV = new VBox();
        searchV.setPadding(new Insets(10, 12, 10, 12));
        searchV.setSpacing(10);

        HBox hTexBox = new HBox();
        hTexBox.setPadding(new Insets(5, 12, 5, 12));
        hTexBox.setSpacing(6);
        hTexBox.setAlignment(Pos.CENTER);

        Label prodLabel = new Label("Insumo: ");
        prodField = new TextField();
        hTexBox.getChildren().add(prodLabel);
        hTexBox.getChildren().add(prodField);

        Label transTypeLabel = new Label("Tipo de transacción: ");
        ComboBox<String> transTyp = new ComboBox<>();
        transTyp.getItems().addAll(Consts.IN, Consts.OUT, Consts.CHANGEold, Consts.CHANGEnew, Consts.EMPTY);
        hTexBox.getChildren().add(transTypeLabel);
        hTexBox.getChildren().add(transTyp);

        Label sucursalLabel = new Label("Sucursal");
        ComboBox<String> sucursal = new ComboBox<>();
        sucursal.getItems().addAll( Consts.CELINA, Consts.EVA, Consts.NAZCA, Consts.EMPTY );
        hTexBox.getChildren().add(sucursalLabel);
        hTexBox.getChildren().add(sucursal);

        Label userLabel = new Label("Usuario: ");
        TextField userField = new TextField();
        hTexBox.getChildren().add(userLabel);
        hTexBox.getChildren().add(userField);

        userSuggestions = new ArrayList<>();

        InternetClient clientUsers = new InternetClient(Consts.USERS_ID, null, Consts.GET, null,
                new OnUsersIDsResult(userField, userSuggestions), true);
        try {
            clientUsers.connect();
        }catch (IOException e) {
            Platform.runLater(AlertBox::displayServerOffError);
        }

        HBox hFromDateBox = new HBox();
        hFromDateBox.setPadding(new Insets(5, 12, 5, 12));
        hFromDateBox.setSpacing(13);
        hFromDateBox.setAlignment(Pos.CENTER);

        // -> Ingresar fecha de vencimiento dia mes año
        Label fromDate = new Label("Desde: ");
        hFromDateBox.getChildren().add(fromDate);

        TextField fromDayField = new TextField();
        fromDayField.setPromptText("Día");
        fromDayField.textProperty().addListener(new PropertyDayDueDate(fromDayField));
        hFromDateBox.getChildren().add(fromDayField);

        Label separatorD_M = new Label("/");
        hFromDateBox.getChildren().add(separatorD_M);

        TextField fromMonthField = new TextField();
        fromMonthField.setPromptText("Mes");
        fromMonthField.textProperty().addListener(new PropertyMonthDueDate(fromMonthField));
        hFromDateBox.getChildren().add(fromMonthField);

        Label separatorM_Y = new Label("/");
        hFromDateBox.getChildren().add(separatorM_Y);

        TextField fromYearField = new TextField();
        fromYearField.setPromptText("Año");
        fromYearField.textProperty().addListener(new PropertyYearBuy(fromYearField));
        hFromDateBox.getChildren().add(fromYearField);

        HBox hToDateBox = new HBox();
        hToDateBox.setPadding(new Insets(5, 12, 5, 12));
        hToDateBox.setSpacing(13);
        hToDateBox.setAlignment(Pos.CENTER);

        // -> Ingresar fecha de vencimiento dia mes año
        Label toDate = new Label("Hasta: ");
        hToDateBox.getChildren().add(toDate);

        TextField toDayField = new TextField();
        toDayField.setPromptText("Día");
        toDayField.textProperty().addListener(new PropertyDayDueDate(toDayField));
        hToDateBox.getChildren().add(toDayField);

        Label separatorD_M_Buy = new Label("/");
        hToDateBox.getChildren().add(separatorD_M_Buy);

        TextField toMonthField = new TextField();
        toMonthField.setPromptText("Mes");
        toMonthField.textProperty().addListener(new PropertyMonthDueDate(toMonthField));
        hToDateBox.getChildren().add(toMonthField);

        Label separatorM_Y_Buy = new Label("/");
        hToDateBox.getChildren().add(separatorM_Y_Buy);

        TextField toYearField = new TextField();
        toYearField.setPromptText("Año");
        toYearField.textProperty().addListener(new PropertyYearBuy(toYearField));
        hToDateBox.getChildren().add(toYearField);

        Button searchBttn = new Button("Buscar");
        HBox hBttnBox = new HBox();
        hBttnBox.setSpacing(10);
        hBttnBox.getChildren().add(searchBttn);

        searchBttn.setOnAction( e -> {

            JSONObject jsonO = buildRequest( transTyp, sucursal, userField, fromDayField, fromMonthField, fromYearField, toDayField, toMonthField, toYearField );

            if (!jsonO.toString().equals("{}")) {
                LoadingBox loadingBox = new LoadingBox();

                String path;
                if ( transTyp.getValue()!= null && (transTyp.getValue().equals(Consts.CHANGEnew) || transTyp.getValue().equals(Consts.CHANGEold) )) {
                    path = Consts.MAT_TRANSACTIONS_CHANGE;
                } else {
                    path = Consts.MAT_TRANSACTIONS;
                }
                InternetClient client = new InternetClient(path, null, Consts.PUT, jsonO.toString(),
                        new OnSearchResult(searchV, loadingBox, path), true);
                new TaskCreator(client, loadingBox).start();
                loadingBox.display("Buscando...", "Por favor espere un momento.", "miloStock.gif");
            }
        });

        Button clearBttn = new Button("Limpiar");
        clearBttn.setOnAction( e -> {
            prodField.clear(); transTyp.setValue(null); sucursal.setValue(null); userField.clear();
            fromDayField.clear(); fromMonthField.clear(); fromYearField.clear();
            toDayField.clear(); toMonthField.clear(); toYearField.clear();
        });

        Hyperlink help = new Hyperlink("¿Ayuda puntual?");
        help.setOnAction(e-> WindowsCommand.goPDF(15));

        hBttnBox.getChildren().addAll(clearBttn, help);
        hBttnBox.setAlignment(Pos.CENTER);


        Separator sepHor = new Separator();
        sepHor.setValignment(VPos.CENTER);

        searchV.getChildren().addAll(hTexBox, hFromDateBox, hToDateBox, hBttnBox, sepHor);

        if (mainPane.getChildren().size() == 2) {
            mainPane.getChildren().remove(1);
        }

        mainPane.getChildren().add(searchV);
    }

    private JSONObject buildRequest(ComboBox<String> transTyp, ComboBox<String> sucursal, TextField userName, TextField fromDayField, TextField fromMonthField, TextField fromYearField, TextField toDayField, TextField toMonthField, TextField toYearField) {

        JSONObject jsonO = new JSONObject();

        if (notEmpty(prodField)) {
            if (suggestions.contains(prodField.getText().toUpperCase())) {
                jsonO.put(Consts.MATERIALS, prodField.getText().toUpperCase());
            }else {
                AlertBox.display("INVALIDO", "Se ingresó un INSUMO invalido. Se ignora esta entrada.");
            }
        }

        if (notEmpty(userName)) {
            if (userSuggestions.contains(userName.getText().toUpperCase())) {
                jsonO.put(Consts.USER, userName.getText().toUpperCase());
            } else {
                AlertBox.display("INVALIDO", "Se ingresó un USUARIO invalido. Se ignora esta entrada.");
            }
        }

        if (diffNull(transTyp)) {
            jsonO.put(Consts.TRANSACTION_TYPE, transTyp.getValue());
        }

        if (diffNull(sucursal)) {
            jsonO.put(Consts.DESTINY, Consts.getMapNameSuc(sucursal.getValue()));
        }

        JSONObject jsonDate = new JSONObject();

        boolean correctDateFrom = correctDate(fromDayField,fromMonthField,fromYearField);

        if (correctDateFrom) {
            jsonDate.put("$gte", parse(fromDayField.getText(), fromMonthField.getText(), fromYearField.getText()));
        }

        boolean correctDateTo = correctDate(toDayField,toMonthField,toYearField);

        if (correctDateTo) {
            jsonDate.put("$lte", parse(toDayField.getText(), toMonthField.getText(), toYearField.getText()));
        }

        if (correctDateFrom || correctDateTo) {
            jsonO.put(Consts.TRANSACTION_DATE, jsonDate);
        }

        return jsonO;
    }

    private boolean correctDate(TextField dayField, TextField monthField, TextField yearField) {
        return notEmpty(dayField) && notEmpty(monthField) && notEmpty(yearField) && nonZeroDate(dayField, monthField, yearField) && correctYear(yearField);
    }

    private boolean diffNull(ComboBox<String> combo) {
        return combo.getValue() != null;
    }

    private boolean notEmpty(TextField field) {
        return !field.getText().isEmpty();
    }

    @Override
    public void bindSuggestions(List<String> suggestions) {
        this.suggestions = suggestions;
        TextFields.bindAutoCompletion(prodField, suggestions);

    }

}
