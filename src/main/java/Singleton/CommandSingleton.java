package Singleton;

/**
 * Created by Sebastian on 21/08/2016.
 */
public class CommandSingleton {

    private static CommandSingleton instance = null;
    private String command;

    protected CommandSingleton() {
        // Exists only to defeat instantiation.
    }

    public static CommandSingleton getInstance() {
        if(instance == null) {
            instance = new CommandSingleton();
        }
        return instance;
    }

    public void setCommand(String comm) {
        command = comm;
    }

    public String getCommand() {
        return command;
    }

}
