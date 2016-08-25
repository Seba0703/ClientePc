package OS_Command;

import Common.AlertBox;
import Common.Consts;
import Singleton.CommandSingleton;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class WindowsCommand {

    public static void goPDF(int page) {

        try {
            Process process = Runtime.getRuntime().exec("cmd /C \"\"" + CommandSingleton.getInstance().getPDFCommand()+ "\" /A \"page=" + page + "\" \"Manual de usuario.pdf\"\"");
        } catch (IOException e) {
            AlertBox.display("ERROR", "No se pudo abrir el pdf.");
        }
    }

    public static void goExcel(String path) {

        Desktop dt = Desktop.getDesktop();
        try {
            dt.open(new File(path));
        } catch (IOException e) {
            AlertBox.display("ERROR", "No se pudo abrir el excel. ¿Existe la caperta SalidaExcel en el escritorio?");
        }
    }

}
