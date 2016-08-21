import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class IpReader {

    public static String read() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("ipPort.txt"));
            return reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
