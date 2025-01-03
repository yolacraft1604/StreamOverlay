package org.example;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;
import java.time.Duration;

public class ApiCalls {
    private final String url;

    public ApiCalls(String Endpoint) {
        String baseurl = "http://localhost:52533";
        this.url = baseurl + Endpoint;
    }

    public String Call() {
        String responseContent = "";
        try {
            HttpClient client = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(5)) // Connection timeout
                    .build();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .header("Content-Type", "application/json")
                    .timeout(Duration.ofSeconds(10)) // Request timeout
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                responseContent = response.body();
            } else {
                System.err.println("HTTP Error: " + response.statusCode());
            }

        } catch (HttpTimeoutException e) {
            System.err.println("Request timeout: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error during API call: " + e.getMessage());
            e.printStackTrace();
        }
        return responseContent;
    }

    public Object getData(String jsonPath) {
        String response = Call();
        if (response.isEmpty()) {
            return null; // Return null if no response was received
        }

        try {
            JSONObject jsonObject = new JSONObject(response);
            String[] keys = jsonPath.split("\\.");
            Object current = jsonObject;

            for (String key : keys) {
                if (current instanceof JSONObject) {
                    JSONObject currentObject = (JSONObject) current;

                    if (!currentObject.has(key)) {
                        return null;
                    }
                    current = currentObject.get(key);

                } else if (current instanceof JSONArray) {
                    try {
                        int index = Integer.parseInt(key);
                        JSONArray currentArray = (JSONArray) current;

                        if (index < 0 || index >= currentArray.length()) {
                            return null;
                        }
                        current = currentArray.get(index);
                    } catch (NumberFormatException e) {
                        return null;
                    }
                } else {
                    return null;
                }
            }
            return current;

        } catch (Exception e) {
            System.err.println("Error during JSON parsing: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
