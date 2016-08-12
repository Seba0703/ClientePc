package Common;

import javafx.scene.control.TextField;

public class DateField {

    public TextField day, month, year;

    public DateField(TextField d, TextField m, TextField y) {
         day = d;
         month = m;
         year = y;
    }

    public void clear() {
        System.out.println("borra fecha");
        day.clear();
        month.clear();
        year.clear();
    }
}
