package OnPostExecuteHandlers;

import Builders.*;
import Common.Consts;
import Common.LoadingBox;
import Common.TaskCreator;
import InternetTools.InternetClient;
import OnPostExecuteHandlers.*;
import Singleton.UserSingleton;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
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
        ImageView addProdImage = new ImageView(new Image("file:addProd4.png"));
        ImageView furnitureImage = new ImageView(new Image("file:sheet.png"));
        ImageView addUserImage = new ImageView(new Image("file:virden.png"));
        ImageView configImage = new ImageView(new Image("file:config.png"));
        ImageView searchImage = new ImageView(new Image("file:search3.png"));
        ImageView editProdImage = new ImageView(new Image("file:edit_prod.png"));

        Button btnAdd = new Button("Agregar producto", addProdImage);
        Button btnListStock = new Button("Ver listado productos", listImage);
        Button btnListFurniture = new Button("Exportar listado muebles", furnitureImage);
        Button btnSearch = new Button("Buscar", searchImage);
        Button btnConfig = new Button("Configurar", configImage);
        Button btnAddUser = new Button("Agregar usuario", addUserImage);
        Button btnEditProd = new Button("Editar producto", editProdImage);

        Separator sep1 = new Separator(); sep1.setOrientation(Orientation.VERTICAL);
        Separator sep2 = new Separator(); sep2.setOrientation(Orientation.VERTICAL);
        Separator sep3 = new Separator(); sep3.setOrientation(Orientation.VERTICAL);
        Separator sep4 = new Separator(); sep4.setOrientation(Orientation.VERTICAL);
        Separator sep5 = new Separator(); sep5.setOrientation(Orientation.VERTICAL);
        sep1.setStyle("-fx-background-color: #b1b1b1 ;");
        sep2.setStyle("-fx-background-color: #b1b1b1 ;");
        sep3.setStyle("-fx-background-color: #b1b1b1 ;");
        sep4.setStyle("-fx-background-color: #b1b1b1 ;");
        sep5.setStyle("-fx-background-color: #b1b1b1 ;");

        HBox customToolbar = new HBox(10);
        customToolbar.setPadding(new Insets(15, 10, 15, 10));
        customToolbar.getChildren().addAll(
                sep1,
                btnAdd,
                btnAddUser,
                sep2,
                btnEditProd,
                sep3,
                btnSearch,
                btnListStock,
                btnListFurniture,
                sep4,
                btnConfig,
                sep5
        );
        customToolbar.setAlignment(Pos.CENTER);
        customToolbar.setStyle("-fx-background-color: lightgrey ");

        mainPane = new VBox();
        mainPane.setStyle("-fx-background-color:  #e5e5e5 ");
        mainPane.getChildren().add(customToolbar);
        Scene scene = new Scene(mainPane, 1150, 600);
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

        btnConfig.setOnAction( e -> {
            JSONObject permissionRequest = new JSONObject();
            permissionRequest.put(Consts.USER, UserSingleton.getInstance().getUserName());
            permissionRequest.put(Consts.PROP_CONFIG_STOCK_VARS, true);
            LoadingBox loadingPermi = new LoadingBox();
            InternetClient permission = new InternetClient(Consts.HAS_PERMISSION, null, Consts.PUT, permissionRequest.toString(),
                    new OnPermissionConfigVarsResult(mainPane, loadingPermi),false);
            new TaskCreator(permission,loadingPermi).start();
            loadingPermi.display("Buscando...", "Obteniendo permisos. Por favor espere", "loading.gif");
        });

        btnListFurniture.setOnAction( e -> {
            HBox allNotUpd = new HBox(10);
            allNotUpd.setPadding(new Insets(20,20,20,20));
            allNotUpd.setAlignment(Pos.CENTER);
            ImageView allImage = new ImageView(new Image("file:allMuebles.png"));
            ImageView allImage2 = new ImageView(new Image("file:allMuebles.png"));

            Button allFurniture = new Button("Todos los bienes", allImage2);
            Button notUpdFurniture = new Button("No actualizados", allImage);

            allFurniture.setOnAction( ev -> {
                allFurniture.setDisable(true);
                LoadingBox loadingBox = new LoadingBox();
                loadingBox.display("Exportando...", "Por favor espere un momento.", "miloStock.gif");
                InternetClient client = new InternetClient(Consts.FURNITURE, null, Consts.GET, null, new OnAllFurniResult(loadingBox, allFurniture), true);
                new TaskCreator(client, loadingBox).start();
            });

            notUpdFurniture.setOnAction( ev -> {
                notUpdFurniture.setDisable(true);
                LoadingBox loadingBox = new LoadingBox();
                loadingBox.display("Exportando...", "Por favor espere un momento.", "miloStock.gif");
                InternetClient client = new InternetClient(Consts.FURNITURE_NOT_UPDATED, null, Consts.GET, null,
                        new OnNotUpdFurniResult(loadingBox, notUpdFurniture), true);
                new TaskCreator(client, loadingBox).start();
            });

            allNotUpd.getChildren().addAll(allFurniture, notUpdFurniture);

            if (mainPane.getChildren().size() == 2) {
                mainPane.getChildren().remove(1);
            }

            mainPane.getChildren().add(allNotUpd);

        });

        primaryStage.show();
    }
}
