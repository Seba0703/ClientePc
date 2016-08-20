package Builders;

import Common.*;
import InternetTools.InternetClient;
import OS_Command.WindowsCommand;
import OnPostExecuteHandlers.OnSetUserResult;
import PropertyUsersField.PropertyAddUsersField;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.json.JSONObject;

public class AddUserBuilder {

    private Stage window;
    private UsersPropertiesList userProperties;
    private CheckBox addUser;
    private CheckBox extract;
    private CheckBox editProd;
    private CheckBox stockVar;
    private CheckBox pcIn;
    private CheckBox editPass;
    private TextField userNameField, textField, textField2;
    private PasswordField pwBox, newPwBox;
    private PropertyAddUsersField propertyAddUsersField;

    public AddUserBuilder() {
        window = new Stage();
    }

    public void build() {

        //Block events to other windows
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Agregar usuarios");
        window.setMinWidth(900);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text scenetitle = new Text("Ingresar o editar un usuario, por favor");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

        Label userName = new Label("Usuario:");
        grid.add(userName, 0, 1);

        userNameField = new TextField();
        grid.add(userNameField, 1, 1);

        editPass = new CheckBox("¿Editar contraseña?");
        grid.add(editPass, 2, 2);

        Hyperlink help = new Hyperlink("¿Ayuda puntual?");
        help.setOnAction(e-> WindowsCommand.goPDF(7));
        grid.add(help, 4, 0);

        Label newPass = new Label("Nueva contraseña");
        textField2 = new TextField();
        textField2.setManaged(false);
        textField2.setVisible(false);
        newPwBox = new PasswordField();

        addUser = new CheckBox("Agregar usuarios");
        extract = new CheckBox("Sacar insumos (Celular)");
        editProd = new CheckBox("Editar insumos");
        stockVar = new CheckBox("Editar variables de stock");
        pcIn = new CheckBox("Entrar programa PC");

        HBox hBox = new HBox(10);
        hBox.getChildren().addAll(pcIn, extract, editProd, stockVar, addUser);
        grid.add(hBox, 0, 4, 5, 1);

        propertyAddUsersField = new PropertyAddUsersField(userNameField, userProperties, addUser, extract, editProd,
                stockVar, pcIn);
        userNameField.focusedProperty().addListener(propertyAddUsersField);


        Label pw = new Label("Contraseña:");
        grid.add(pw, 0, 2);

        // para desenmascarar el password
        textField = new TextField();
        textField.setManaged(false);
        textField.setVisible(false);
        grid.add(textField, 1, 2);

        // password enmascarado
        pwBox = new PasswordField();
        grid.add(pwBox, 1, 2);

        CheckBox checkBox = new CheckBox("Mostrar contraseña");
        grid.add(checkBox, 1, 3);

        //base password
        textField.managedProperty().bind(checkBox.selectedProperty());
        textField.visibleProperty().bind(checkBox.selectedProperty());

        pwBox.managedProperty().bind(checkBox.selectedProperty().not());
        pwBox.visibleProperty().bind(checkBox.selectedProperty().not());

        textField.textProperty().bindBidirectional(pwBox.textProperty());

        // confirmar password
        textField2.managedProperty().bind(checkBox.selectedProperty());
        textField2.visibleProperty().bind(checkBox.selectedProperty());

        newPwBox.managedProperty().bind(checkBox.selectedProperty().not());
        newPwBox.visibleProperty().bind(checkBox.selectedProperty().not());

        textField2.textProperty().bindBidirectional(newPwBox.textProperty());

        // para alertas o texto informativo
        Text actiontarget = new Text();
        grid.add(actiontarget, 1, 8);

        Button btnAdd = new Button("Configurar");
        btnAdd.setOnAction(e ->
                handle(actiontarget));
        Button btnClose = new Button("Cerrar");
        btnClose.setOnAction(e -> window.close());
        grid.add(btnClose, 0, 5);

        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btnAdd);
        grid.add(hbBtn, 1, 5);

        editPass.setOnAction(e-> {
            if (editPass.isSelected()) {
                grid.getChildren().remove(checkBox);
                grid.getChildren().remove(actiontarget);
                grid.getChildren().remove(hBox);
                grid.getChildren().remove(hbBtn);
                grid.getChildren().remove(btnClose);
                grid.add(newPass, 0, 3); grid.add(textField2, 1, 3); grid.add(newPwBox, 1, 3);
                grid.add(checkBox, 0, 4);
                grid.add(hBox, 0, 5);
                grid.add(btnClose, 0, 6); grid.add(hbBtn, 1, 6);
                grid.add(actiontarget, 1, 9);
            } else {
                grid.getChildren().remove(newPass);
                grid.getChildren().remove(textField2);
                grid.getChildren().remove(newPwBox);
                grid.getChildren().remove(checkBox);
                grid.getChildren().remove(actiontarget);
                grid.getChildren().remove(hBox);
                grid.getChildren().remove(hbBtn);
                grid.getChildren().remove(btnClose);
                grid.add(checkBox, 0, 3);
                grid.add(hBox, 0, 4);
                grid.add(btnClose, 0, 5); grid.add(hbBtn, 1, 5);
                grid.add(actiontarget, 1, 8);
            }
        });

        Scene loginScene = new Scene(grid, 300, 275);
        window.setScene(loginScene);

        window.show();
    }

    private void handle(Text actiontarget) {

        boolean noneSelected = !addUser.isSelected() &&
                !extract.isSelected() && !editProd.isSelected() && !stockVar.isSelected() && !pcIn.isSelected();

        String userNameFinal =  userNameField.getText().replaceAll("\\s+$", "");
        String userPassFinal = pwBox.getText().replaceAll("\\s+$", "");
        String userNewPassFinal = newPwBox.getText().replaceAll("\\s+$", "");

        if (noneSelected || userNameFinal.isEmpty() || (!propertyAddUsersField.contain() && userPassFinal.isEmpty()) ||
                (editPass.isSelected() && userNewPassFinal.isEmpty()) ) {
            if (noneSelected) {
                actiontarget.setText("Seleccione algún permiso para el usuario.");
            }else {
                actiontarget.setText("Algunos campos estan vacios.");
            }
            actiontarget.setFill(Color.FIREBRICK);
        } else {

            LoadingBox loadingBox = new LoadingBox();

            actiontarget.setText("");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Consts.USER, userNameFinal);
            jsonObject.put(Consts.PROP_PC_IN, pcIn.isSelected());
            jsonObject.put(Consts.PROP_EXTRACT, extract.isSelected());
            jsonObject.put(Consts.PROP_CONFIG_STOCK_VARS, stockVar.isSelected());
            jsonObject.put(Consts.PROP_ADD_LOGON, addUser.isSelected());
            jsonObject.put(Consts.PROP_EDIT, editProd.isSelected());

            if (propertyAddUsersField.contain() && editPass.isSelected()) {
                jsonObject.put(Consts.PASS, userPassFinal);
                jsonObject.put(Consts.PASS_NEW, userNewPassFinal);
                jsonObject.put(Consts.CHECK_PASS, true);
            } else if (propertyAddUsersField.contain()) {
                jsonObject.put(Consts.CHECK_PASS, false);
            } else if (editPass.isSelected() || (!propertyAddUsersField.contain() && !editPass.isSelected())) {
                jsonObject.put(Consts.PASS, userPassFinal);
                jsonObject.put(Consts.CHECK_PASS, false);
            }

            InternetClient client = new InternetClient(Consts.SET_USERS, null, Consts.POST, jsonObject.toString(),
                    new OnSetUserResult(propertyAddUsersField, actiontarget, this, loadingBox), false);

            new TaskCreator(client, loadingBox).start();
            loadingBox.display("Procesando...", "Espere un momento, por favor.", "loading.gif");
        }
    }

    public void addUsersProps(UsersPropertiesList userProperties) {
        this.userProperties = userProperties;
    }

    public void clear() {
        if (editPass.isSelected()) {
            editPass.fire();
        }
        pcIn.setSelected(false);
        userNameField.clear();
        addUser.setSelected(false);
        editProd.setSelected(false);
        extract.setSelected(false);
        stockVar.setSelected(false);
        pwBox.clear();
        newPwBox.clear();
    }
}
