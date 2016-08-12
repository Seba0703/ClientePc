import Builders.*;
import Common.AlertBox;
import Common.Consts;
import Common.LoadingBox;
import Common.TaskCreator;
import InternetTools.InternetClient;
import OnPostExecuteHandlers.*;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;


public class tes extends Application {

    VBox mainPane;

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) throws Exception {

        ImageView listImage = new ImageView(
                new Image("file:list.png")
        );

        ImageView addProdImage = new ImageView(
                new Image("file:addProd.png")
        );

        Button btnAdd = new Button("Agregar producto", addProdImage);
        Button btnListStock = new Button("Ver listado", listImage);
        Button btnSearch = new Button("Buscar");
        Button btnConfig = new Button("Configurar");
        Button btnAddUser = new Button("Agregar usuario");
        Button btnEditProd = new Button("Editar producto");

        ToolBar mainToolBar = new ToolBar();
        mainToolBar.getItems().addAll(
                new Separator(),
                btnAdd,
                btnAddUser,
                new Separator(),
                btnEditProd,
                new Separator(),
                btnSearch,
                btnListStock,
                new Separator(),
                btnConfig,
                new Separator()
        );

        mainPane = new VBox();
        mainPane.getChildren().add(mainToolBar);
        Scene scene = new Scene(mainPane, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Copacabana Stock");

        btnAdd.setOnAction(e -> {

            AddProductsBuilder addTB = new AddProductsBuilder(mainPane, Consts.MAT_ADD);

            LoadingBox loading = new LoadingBox();

            InternetClient client = new InternetClient(Consts.MATERIALS_ID, null, Consts.GET, null,
                    new OnMaterialsNamesResult(addTB, loading), true);
            new TaskCreator(client, loading).start();
            loading.display("Descargando...", "Espere un momento por favor.", "loading.gif" );
        });

        btnListStock.setOnAction( e -> {

            LoadingBox loading = new LoadingBox();

            ListViewProductsBuilder listViewBuilder = new ListViewProductsBuilder(mainPane);

            InternetClient client = new InternetClient(Consts.MAT_STATUS, null, Consts.GET, null,
                    new OnProductsStatusResult(listViewBuilder, loading), true);
            new TaskCreator(client, loading).start();
            loading.display("Descargando...", "Se esta descargando el listado de productos. Por favor espere un momento", "miloStock.gif");

        });

        btnSearch.setOnAction(e -> {

            SearchViewBuilder searchView = new SearchViewBuilder(mainPane);
            LoadingBox loading = new LoadingBox();
            InternetClient client = new InternetClient(Consts.MATERIALS_ID, null, Consts.GET, null,
                    new OnMaterialsNamesResult(searchView, loading), true );

            new TaskCreator(client, loading).start();
            loading.display("Descargando...", "Espere un momento por favor.", "loading.gif");

        });

        btnEditProd.setOnAction( e -> {
            EditProdViewBuilder editView = new EditProdViewBuilder(mainPane);
            LoadingBox loading = new LoadingBox();
            InternetClient client = new InternetClient(Consts.MATERIALS_ID, null, Consts.GET, null,
                    new OnMaterialsNamesResult(editView, loading), true );
            new TaskCreator(client, loading).start();
            loading.display("Descargando...", "Espere un momento por favor.", "loading.gif");
        });

        btnAddUser.setOnAction( e -> {
            AddUserBuilder addUserBuilder = new AddUserBuilder();
            LoadingBox loadingBox = new LoadingBox();
            InternetClient clientUsers = new InternetClient(Consts.USERS_ID, null, Consts.GET, null,
                    new OnUsersPropertiesResult(addUserBuilder, loadingBox), true);

            new TaskCreator(clientUsers, loadingBox).start();
            loadingBox.display("Descargando...",  "Espere un momento por favor.", "loading.gif");
        });

        primaryStage.show();
    }
}
