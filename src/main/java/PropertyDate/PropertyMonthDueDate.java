package PropertyDate;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

/**
 * Created by Sebastian on 06/08/2016.
 */
public class PropertyMonthDueDate implements ChangeListener<String> {

    private TextField monthField;

    public PropertyMonthDueDate(TextField monthField) {
        this.monthField = monthField;
    }

    @Override
    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if (!newValue.matches("\\d*")) {
            monthField.setText(newValue.replaceAll("[^\\d]", ""));
        }else if(!newValue.isEmpty() && Integer.parseInt(newValue) > 12) {
            monthField.setText(newValue.substring(0, newValue.length() - 1));
        }
    }
}
