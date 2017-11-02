package InternetTools;

import OnPostExecuteHandlers.OnPostExecute;
import Singleton.IpPort;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class InternetClient {

    private static final int timeOUT_R = 20000;
    private static final int timeOUT_C = 25000;

    private InputStream is;
    private HttpURLConnection connection;
    private String nURL;

    private String jsonBody;
    private String requestMethod;
    private Map<String, String> headers;
    private OnPostExecute onPost;
    private boolean expectResponse;
    private int responseCode;

    public InternetClient(String path, Map<String, String> headerM, String rMethod, String jBody, OnPostExecute exec, boolean response, String ipPort ) {
        nURL = ipPort + path;
        requestMethod = rMethod;
        jsonBody = jBody;
        headers = headerM;
        is = null;
        connection = null;
        onPost = exec;
        expectResponse = response;
        responseCode = 0;
    }

    public InternetClient(String path, Map<String, String> headerM, String rMethod, String jBody, OnPostExecute exec, boolean response ) {
        nURL = IpPort.getInstance().getIpPort() + path;
        requestMethod = rMethod;
        jsonBody = jBody;
        headers = headerM;
        is = null;
        connection = null;
        onPost = exec;
        expectResponse = response;
        responseCode = 0;
    }

    public void connect() throws IOException {

        try {

            URL urlOK = new URL(nURL);
            connection = (HttpURLConnection) urlOK.openConnection();

            connection.setReadTimeout(timeOUT_R /* milliseconds */);
            connection.setConnectTimeout(timeOUT_C /* milliseconds */);
            connection.setRequestMethod(requestMethod);

            if (headers != null) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    connection.addRequestProperty(entry.getKey(), entry.getValue());
                }
            }

            if (jsonBody != null){
                connection.setDoOutput(true);
                byte[] outputInBytes = jsonBody.getBytes("UTF-8");
                OutputStream os = connection.getOutputStream();
                os.write( outputInBytes );
                os.close();
            } else {
                connection.setDoOutput(false);
            }

            connection.connect();

            responseCode = connection.getResponseCode();

            System.out.println(responseCode);

            if ( responseCode < 300 && responseCode >= 200 ) {
                if (expectResponse) {
                    onPost.onSucced(readIt());
                } else {
                    onPost.onSucced();
                }
            }else {
                onPost.onFail(responseCode);
            }
        } finally {
            closeConnection();
        }
    }

    private void closeConnection() throws IOException {
        if (connection != null) {
            connection.disconnect();
        }

        if (is != null) {
            is.close();
        }
    }

    private String readIt() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        return sb.toString();
    }
}
