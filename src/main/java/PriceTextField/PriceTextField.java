package PriceTextField;

import javafx.scene.control.TextField;

import java.util.regex.Pattern;

public class PriceTextField extends TextField {

    private final Pattern pattern = Pattern.compile("^\\d*\\.?\\d*$");

    @Override
    public void replaceText(int start, int end, String text) {
        String newText = getText().substring(0, start)+text+getText().substring(end);
        if (pattern.matcher(newText).matches()) {
            super.replaceText(start, end, text);
        }
    }

    @Override
    public void replaceSelection(String text) {
        int start = getSelection().getStart();
        int end = getSelection().getEnd();
        String newText = getText().substring(0, start)+text+getText().substring(end);
        if (pattern.matcher(newText).matches()) {
            super.replaceSelection(text);
        }
    }

}
