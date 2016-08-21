import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class OneLineReader {

    public static String read(String file) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            return reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
