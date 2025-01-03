package org.example;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServiceTester {

    public ServiceTester(){

    }


    public boolean SearchForNinjabrain() {
        HttpURLConnection connection = null;
        try {
            URL url = new URL("http://localhost:52533/api/v1/ping");
            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                String responseMessage = connection.getResponseMessage();
                if ("OK".equalsIgnoreCase(responseMessage)) {
                    return true;
                }
            }
        } catch (IOException e) {

        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return false;
    }
}
