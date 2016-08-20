package OS_Command;

import Common.AlertBox;

import java.io.IOException;

public class WindowsCommand {

    public static void goPDF(int page) {

        try {
            Process process = Runtime.getRuntime().exec("cmd /C \"\"C:\\Program Files\\Adobe\\Acrobat Reader DC\\Reader\\AcroRd32.exe\" /A \"page=" + page + "\" \"Manual de usuario.pdf\"\"");
        } catch (IOException e) {
            AlertBox.display("ERROR", "No se pudo abrir el pdf.");
        }
    }

}
