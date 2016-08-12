package PropertyDate;

import Common.Consts;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

import java.util.Calendar;

public class PropertyYearBuy implements ChangeListener<String> {

    private TextField yearField;
    private int currentYear;

    public PropertyYearBuy(TextField year) {
        yearField = year;
        currentYear = Calendar.getInstance().get(Calendar.YEAR);
    }

    @Override
    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if (!newValue.isEmpty()) {
            if (!newValue.matches("\\d*")) {
                yearField.setText(newValue.replaceAll("[^\\d]", ""));
            } else if (newValue.length() >= 4 &&  ( (Integer.parseInt(newValue) < Consts.FIRST_BUY_YEAR) ||
                    Integer.parseInt(newValue) > currentYear ) ) {

                yearField.setText(newValue.substring(0, newValue.length() - 1));

            }
        }
    }

}
