package PropertyDate;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

import java.util.Calendar;

public class PropertyYearDueDate implements ChangeListener<String> {

    private TextField yearField;
    private int currentYear;

    public PropertyYearDueDate(TextField year) {
        yearField = year;
        currentYear = Calendar.getInstance().get(Calendar.YEAR);
    }

    @Override
    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if (!newValue.isEmpty()) {
            if (!newValue.matches("\\d*")) {
                yearField.setText(newValue.replaceAll("[^\\d]", ""));
            } else if (newValue.length() >= 4 &&
                    (Integer.parseInt(newValue) < currentYear || Integer.parseInt(newValue) > currentYear + 50)) {
                yearField.setText(newValue.substring(0, newValue.length() - 1));
            }
        }
    }
}
