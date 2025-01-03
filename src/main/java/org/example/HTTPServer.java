package org.example;

import java.io.*;
import java.net.*;
import org.json.JSONObject;

public class HTTPServer {
    private ServerSocket serverSocket;

    public HTTPServer() {}

    public void startWebServer() {
        int port = 787;


        CollectData collectData = new CollectData();

        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server läuft auf Port " + port + "...");

            while (true) {
                try (
                        Socket clientSocket = serverSocket.accept();
                        InputStream input = clientSocket.getInputStream();
                        OutputStream output = clientSocket.getOutputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                        PrintWriter writer = new PrintWriter(output, true)
                ) {
                    String requestLine = reader.readLine();
                    if (requestLine != null && requestLine.contains("/api/getData")) {
                        writer.println("HTTP/1.1 200 OK");
                        writer.println("Content-Type: application/json");
                        writer.println("Access-Control-Allow-Origin: *"); // Erlaubt alle Ursprünge
                        writer.println("Access-Control-Allow-Methods: GET, POST, OPTIONS"); // Optional: Erlaubt bestimmte Methoden
                        writer.println("Access-Control-Allow-Headers: Content-Type"); // Optional: Erlaubt bestimmte Header
                        writer.println("Connection: close");
                        writer.println();

                        JSONObject jsonResponse = new JSONObject();

                        if (Main.STATUS) {
                            collectData.UpdateData();
                            jsonResponse.put("distance", collectData.getDistance());
                            jsonResponse.put("percent", collectData.getPercent());
                            jsonResponse.put("isnether", collectData.getNether());
                            jsonResponse.put("eyethrows", collectData.getEyethrows());
                            jsonResponse.put("boat", collectData.getBoatstate());
                            jsonResponse.put("x", collectData.getSHx());
                            jsonResponse.put("z", collectData.getSHz());
                        } else {
                            jsonResponse.put("Ninjabrain", "offline");
                        }

                        writer.println(jsonResponse.toString());
                    } else {
                        GetHTML html = new GetHTML(Main.selectedDesign, Main.selectedColor);
                        String htmlContent = html.GetNow();
                        writer.println("HTTP/1.1 200 OK");
                        writer.println("Content-Type: text/html");
                        writer.println("Access-Control-Allow-Origin: *"); // Auch für HTML-Ressourcen
                        writer.println("Connection: close");
                        writer.println();
                        writer.println(htmlContent);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to stop the web server
    public void stopWebServer() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
                System.out.println("Webserver gestoppt.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to start the server as a thread
    public void startAsThread() {
        Thread serverThread = new Thread(() -> startWebServer());  // Lambda to run startWebServer
        serverThread.start();
    }
}
