package PropertyUsersField;

import Common.UserProperties;
import Common.UsersPropertiesList;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

public class PropertyAddUsersField implements ChangeListener<Boolean> {

    private TextField userTextField;
    private UsersPropertiesList userProperties;
    private CheckBox addUser, extract, editProd, stockVar, pcIn;
    private boolean has;
    private UserProperties prop;

    public PropertyAddUsersField(TextField userTextField, UsersPropertiesList userProperties, CheckBox addUser, CheckBox extract, CheckBox editProd, CheckBox stockVar, CheckBox pcIn) {

        this.userTextField = userTextField;
        this.userProperties = userProperties;
        this.addUser = addUser;
        this.extract = extract;
        this.editProd = editProd;
        this.stockVar = stockVar;
        this.pcIn = pcIn;
        has = false;
    }

    @Override
    public void changed(ObservableValue<? extends Boolean> observable, Boolean notFocus, Boolean focus) {

        if (!focus) {
            prop = userProperties.find(userTextField.getText().toUpperCase());
            if (prop != null) {
                addUser.setSelected(prop.addLogon);
                extract.setSelected(prop.extract);
                editProd.setSelected(prop.editProd);
                stockVar.setSelected(prop.configVarStock);
                pcIn.setSelected(prop.pcIn);
                has = true;
            } else {
                addUser.setSelected(false);
                extract.setSelected(false);
                editProd.setSelected(false);
                stockVar.setSelected(false);
                pcIn.setSelected(false);
                has = false;
            }
        }

    }

    public boolean contain() {
        return has;
    }

    public void set() {
        if (has) {
            prop.addLogon = addUser.isSelected();
            prop.extract = extract.isSelected();
            prop.editProd = editProd.isSelected();
            prop.configVarStock = stockVar.isSelected();
            prop.pcIn = pcIn.isSelected();
        } else {
            prop = new UserProperties(addUser.isSelected(), stockVar.isSelected(), editProd.isSelected(),
                    extract.isSelected(), pcIn.isSelected(), userTextField.getText().toUpperCase());
            userProperties.add(prop);
        }
    }
}
