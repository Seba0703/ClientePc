package Builders;

import Common.*;
import InternetTools.InternetClient;
import OnPostExecuteHandlers.OnAddProductResult;
import Singleton.UserSingleton;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.controlsfx.control.textfield.TextFields;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;


public class AddProductsBuilder implements Builder{

    private List<String> suggestions;

    private VBox boxAdd;
    private GridPane grid;
    private AtomicBoolean hasUpdateStockVars;
    private ProductFieldsBuilder prodFields;

    private VBox mainPane;
    private String path;
    private OldProduct oldProd;
    private String user;

    public AddProductsBuilder(VBox main, String path ) {
        mainPane = main;
        this.path = path;
        grid = new GridPane();
    }

    public void build() {

        boxAdd = new VBox();
        hasUpdateStockVars = new AtomicBoolean(false);
        Button save = new Button("Guardar producto");
        prodFields = new ProductFieldsBuilder(path, grid, save);

        save.setOnAction(e -> {

            boolean allFieldsFull = prodFields.allFieldsFull(hasUpdateStockVars.get());
            boolean correctInput = prodFields.correctInput(hasUpdateStockVars.get());
            boolean suggestionsContains = suggestions.contains(prodFields.getNameFieldText().toUpperCase());

            String requestMethod;
            if (suggestionsContains && !hasUpdateStockVars.get() ) {
                requestMethod = Consts.PUT;
            } else {
                requestMethod = Consts.POST;
            }

            System.out.println("Request: "+ requestMethod + " Full: " + allFieldsFull+ " correc: " + correctInput + " Sugg: " + suggestionsContains);

            if ( allFieldsFull && correctInput && suggestionsContains ) {

                JSONObject request = prodFields.buildRequest();
                JSONObject requestGlob;

                if (path.equals(Consts.DELETE_PROD)) {
                    requestGlob = addOldProd();
                    requestGlob.put(Consts.NEW_VALUE, request);
                } else {
                    requestGlob = request;
                }

                LoadingBox loadingBox = new LoadingBox();

                InternetClient client = new InternetClient(path, null, requestMethod, requestGlob.toString(),
                        new OnAddProductResult(prodFields, loadingBox, hasUpdateStockVars), false);

                new TaskCreator(client, loadingBox).start();

                loadingBox.display("Guardando...", "Espere un momento, por favor.", "miloStock.gif");

            } else if (!allFieldsFull) {
                AlertBox.display("NULL", "Hay campos sin llenar.");
            } else if (!correctInput) {
                AlertBox.display("Entrada invalida", "El formato de algunos campos son incorrectos.");
            } else {

                hasUpdateStockVars.set(true);

                prodFields.addStockVarsFields();

                suggestions.add(prodFields.getNameFieldText().toUpperCase());
                prodFields.addNewSuggestion(suggestions);

                grid.getChildren().remove(save);
                grid.add(save, 3, 11);
            }
        });

        grid.add(save, 3, 7);

        if(mainPane.getChildren().size() == 2 && !path.equals(Consts.DELETE_PROD)) {
            mainPane.getChildren().remove(1);
        }else if (mainPane.getChildren().size() == 7 && path.equals(Consts.DELETE_PROD)) {
            mainPane.getChildren().remove(6);
        }

        if (path.equals(Consts.DELETE_PROD)) {
            ScrollPane sp = new ScrollPane();
            VBox.setVgrow(sp, Priority.ALWAYS);
            sp.setContent(grid);
            mainPane.getChildren().add(sp);
        } else {
            boxAdd.getChildren().add(grid);
            mainPane.getChildren().add(boxAdd);
        }
    }

    private JSONObject addOldProd() {
        JSONObject requestGlob = new JSONObject();
        requestGlob.put(Consts.MATERIALS, oldProd.name);
        requestGlob.put(Consts.QUANTITY, oldProd.quantity);
        requestGlob.put(Consts.PRICE, oldProd.price);
        requestGlob.put(Consts.DUE_DATE, oldProd.dueDate);
        requestGlob.put(Consts.TRANSACTION_DATE, oldProd.buyDate);
        requestGlob.put(Consts.USER, user);
        return requestGlob;
    }

    @Override
    public void bindSuggestions(List<String> suggestions) {
        this.suggestions = suggestions;
        TextFields.bindAutoCompletion(prodFields.getNameField(), suggestions);
    }

    public void setAll(String name, int quantity, double price, int dueDate, int buyDate) {
        oldProd = new OldProduct(name, quantity, price, dueDate, buyDate);
        prodFields.setAll(name, quantity, price, dueDate, buyDate);
    }

    public void setUser(String user) {
        this.user = user;
    }
}
