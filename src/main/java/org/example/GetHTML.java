package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetHTML {
    private String OverlayTheme;
    private String ColorScheme;
    public GetHTML(String theme, String color){
        this.ColorScheme = color;
        this.OverlayTheme = theme;
    }
    public String GetNow(){
        String fileURL = "";

        if(OverlayTheme.equals("default") && ColorScheme.equals("dark")){
            fileURL = "https://streamoverlay.vercel.app/defaultDark.html";
        }else if(OverlayTheme.equals("default") && ColorScheme.equals("light")){
            fileURL = "https://streamoverlay.vercel.app/defaultLight.html";
        }else if(OverlayTheme.equals("default") && ColorScheme.equals("transparent")){
            fileURL = "https://streamoverlay.vercel.app/defaultTransparent.html";
        }else {
            return "<h1>Select valid theme</h1>";
        }

        StringBuilder content = new StringBuilder();

        try {
            URL url = new URL(fileURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }

            reader.close();
            connection.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return content.toString();
    }
}
