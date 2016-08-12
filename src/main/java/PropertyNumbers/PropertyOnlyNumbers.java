package PropertyNumbers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

/**
 * Created by Sebastian on 07/08/2016.
 */
public class PropertyOnlyNumbers implements ChangeListener<String> {

    private TextField numberField;

    public PropertyOnlyNumbers(TextField field) {
        numberField = field;
    }

    @Override
    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if (!newValue.matches("\\d*")) {
            numberField.setText(newValue.replaceAll("[^\\d]", ""));
        }
    }
}
