package PropertyDate;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

public class PropertyDayDueDate implements ChangeListener<String> {

    private TextField dayField;

    public PropertyDayDueDate(TextField dayField) {
        this.dayField = dayField;
    }

    @Override
    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if (!newValue.matches("\\d*")) {
            dayField.setText(newValue.replaceAll("[^\\d]", ""));
        } else if(!newValue.isEmpty() && Integer.parseInt(newValue) > 31) {
            dayField.setText(newValue.substring(0, newValue.length() - 1));
        }
    }
}
