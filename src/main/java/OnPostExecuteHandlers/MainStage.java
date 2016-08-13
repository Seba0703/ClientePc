package OnPostExecuteHandlers;

import Builders.*;
import Common.Consts;
import Common.LoadingBox;
import Common.TaskCreator;
import InternetTools.InternetClient;
import OnPostExecuteHandlers.*;
import Singleton.UserSingleton;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.json.JSONObject;


public class MainStage {

    VBox mainPane;

    public MainStage() {

    }

    public void start(Stage primaryStage) throws Exception {

        primaryStage.getIcons().add(new Image("file:logoCopa.png"));

        ImageView listImage = new ImageView(new Image("file:list.png"));
        ImageView addProdImage = new ImageView(new Image("file:addProd.png"));

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
            JSONObject permissionRequest = new JSONObject();
            permissionRequest.put(Consts.USER, UserSingleton.getInstance().getUserName());
            permissionRequest.put(Consts.PROP_EDIT, true);
            LoadingBox loadingPermi = new LoadingBox();
            InternetClient permission = new InternetClient(Consts.HAS_PERMISSION, null, Consts.PUT, permissionRequest.toString(),
                    new OnPermissionEditProdResult(loadingPermi, mainPane),false);
            new TaskCreator(permission,loadingPermi).start();
            loadingPermi.display("Buscando...", "Obteniendo permisos. Por favor espere", "loading.gif");
        });

        btnAddUser.setOnAction( e -> {
            JSONObject permissionRequest = new JSONObject();
            permissionRequest.put(Consts.USER, UserSingleton.getInstance().getUserName());
            permissionRequest.put(Consts.PROP_ADD_LOGON, true);
            LoadingBox loadingPermi = new LoadingBox();
            InternetClient permission = new InternetClient(Consts.HAS_PERMISSION, null, Consts.PUT, permissionRequest.toString(),
                    new OnPermissionAddUserResult(loadingPermi),false);
            new TaskCreator(permission,loadingPermi).start();
            loadingPermi.display("Buscando...", "Obteniendo permisos. Por favor espere", "loading.gif");
        });

        btnConfig.setOnAction( e-> {
            JSONObject permissionRequest = new JSONObject();
            permissionRequest.put(Consts.USER, UserSingleton.getInstance().getUserName());
            permissionRequest.put(Consts.PROP_CONFIG_STOCK_VARS, true);
            LoadingBox loadingPermi = new LoadingBox();
            InternetClient permission = new InternetClient(Consts.HAS_PERMISSION, null, Consts.PUT, permissionRequest.toString(),
                    new OnPermissionConfigVarsResult(mainPane, loadingPermi),false);
            new TaskCreator(permission,loadingPermi).start();
            loadingPermi.display("Buscando...", "Obteniendo permisos. Por favor espere", "loading.gif");
        });

        primaryStage.show();
    }
}
