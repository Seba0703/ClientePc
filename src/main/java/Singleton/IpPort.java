package Singleton;

/**
 * Created by Sebastian on 21/08/2016.
 */
public class IpPort {

    private static IpPort instance = null;
    private String ipPort;

    protected IpPort() {
        // Exists only to defeat instantiation.
    }

    public static IpPort getInstance() {
        if(instance == null) {
            instance = new IpPort();
        }
        return instance;
    }

    public void setIpPort(String ip) {
        ipPort = ip;
    }

    public String getIpPort() {
        return ipPort;
    }
}
