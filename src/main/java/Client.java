import Common.Consts;
import InternetTools.InternetClient;
import OnPostExecuteHandlers.OnLoginResult;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Client extends Application {

    Stage window;

    Scene scene1, mainScene;

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) throws Exception {

        window = primaryStage;
        window.setTitle(Consts.LOGIN_TITLE);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text scenetitle = new Text(Consts.WELCOME_T);
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

        Label userName = new Label("Usuario:");
        grid.add(userName, 0, 1);

        TextField userTextField = new TextField();
        grid.add(userTextField, 1, 1);

        Label pw = new Label("Contraseña:");
        grid.add(pw, 0, 2);

        // para desenmascarar el password
        final TextField textField = new TextField();
        textField.setManaged(false);
        textField.setVisible(false);
        grid.add(textField, 1, 2);

        // password enmascarado
        PasswordField pwBox = new PasswordField();
        grid.add(pwBox, 1, 2);

        CheckBox checkBox = new CheckBox("Mostrar contraseña");
        grid.add(checkBox,1, 3);

        textField.managedProperty().bind(checkBox.selectedProperty());
        textField.visibleProperty().bind(checkBox.selectedProperty());

        pwBox.managedProperty().bind(checkBox.selectedProperty().not());
        pwBox.visibleProperty().bind(checkBox.selectedProperty().not());

        textField.textProperty().bindBidirectional(pwBox.textProperty());

        // para alertas o texto informativo
        Text actiontarget = new Text();
        grid.add(actiontarget, 1, 7);

        Button btn = new Button("Ingresar");
        btn.setOnAction(e ->
                handle(userTextField, pwBox, actiontarget, "Usuario no existe o contraseña erronea.", Consts.LOGIN, Consts.GET ));

        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 4);

        Hyperlink link = new Hyperlink();
        link.setText("¿Ingresar un nuevo usuario?");
        link.setOnAction(e ->
                handle(userTextField, pwBox, actiontarget, "Usuario ya existente. Ingrese uno distinto", Consts.LOGON, Consts.POST));

        grid.add(link, 1, 6);

        Scene loginScene = new Scene(grid, 300, 275);
        window.setScene(loginScene);

        Button button2 = new Button("This sucks, go back to scene 1");
        ToolBar toolBar = new ToolBar(button2);

        mainScene = new Scene(toolBar, 600, 300);


        window.show();
    }

    private void handle(TextField userTextField, PasswordField pwBox, Text actiontarget, String sysResponse,
                        String path, String method ) {

        if (userTextField.getText().isEmpty() || pwBox.getText().isEmpty()) {

            actiontarget.setFill(Color.FIREBRICK);
            actiontarget.setText("Algunos campos estan vacios.");

        } else {
            actiontarget.setText("");
            Map<String, String> headers = new HashMap<>();
            headers.put(Consts.USER, userTextField.getText());
            headers.put(Consts.PASS, pwBox.getText());
            InternetClient client = new InternetClient(path, headers, method, null,
                    new OnLoginResult(window, mainScene, actiontarget, sysResponse, userTextField.getText()), false);
            try {
                client.connect();
            }catch (IOException er) {
                actiontarget.setFill(Color.FIREBRICK);
                actiontarget.setText("No hay conexión de red.");
            }
        }
    }

}
