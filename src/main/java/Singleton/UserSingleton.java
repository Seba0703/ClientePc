package Singleton;

public class UserSingleton {

    private static UserSingleton instance = null;
    private String userName;

    protected UserSingleton() {
        // Exists only to defeat instantiation.
    }

    public static UserSingleton getInstance() {
        if(instance == null) {
            instance = new UserSingleton();
        }
        return instance;
    }

    public void setUserName(String user) {
        userName = user;
    }

    public String getUserName() {
        return userName;
    }



}
