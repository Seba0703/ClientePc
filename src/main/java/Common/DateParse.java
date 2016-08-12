package Common;

import javafx.scene.control.TextField;

public class DateParse {

    public static int parse(String day, String month, String year) {
        return Integer.parseInt(year + parseWithZero(month) + parseWithZero(day));

    }

    private static String parseWithZero(String num) {
        int number = Integer.parseInt(num);
        if (number >= 10) {
            return num;
        } else {
            return "0" + number;
        }
    }

    public static boolean correctYear(TextField yearField) {
        return yearField.getText().length() == 4;
    }

    public static boolean nonZeroDate(TextField day, TextField month, TextField year) {
        return nonZero(day) && nonZero(month) && nonZero(year);

    }

    private static boolean nonZero(TextField field) {
        return Integer.parseInt(field.getText()) != 0;
    }

}
