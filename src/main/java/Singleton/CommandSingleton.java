package Singleton;

/**
 * Created by Sebastian on 21/08/2016.
 */
public class CommandSingleton {

    private static CommandSingleton instance = null;
    private String pdfCommand;

    private String excelPath;

    protected CommandSingleton() {
        // Exists only to defeat instantiation.
    }

    public static CommandSingleton getInstance() {
        if(instance == null) {
            instance = new CommandSingleton();
        }
        return instance;
    }

    public void setPDFCommand(String comm) {
        pdfCommand = comm;
    }

    public String getPDFCommand() {
        return pdfCommand;
    }

    public String getExcelPath() {
        return excelPath;
    }

    public void setExcelPath(String excelPath) {
        this.excelPath = excelPath;
    }

}
