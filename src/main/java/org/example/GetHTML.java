package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class GetHTML {
    private String OverlayTheme;
    private String ColorScheme;
    public GetHTML(String theme, String color){
        this.ColorScheme = color;
        this.OverlayTheme = theme;
    }
    public String GetNow(){
        String filePath = "defaultDark.html";

        if(OverlayTheme.equals("default") && ColorScheme.equals("dark")){
            filePath = "src/main/resources/defaultDark.html";
        }else if(OverlayTheme.equals("default") && ColorScheme.equals("light")){
            filePath = "src/main/resources/defaultLight.html";
        }else if(OverlayTheme.equals("default") && ColorScheme.equals("transparent")){
            filePath = "src/main/resources/defaultTransparent.html";
        }else {
            return "<h1>Select valid theme</h1>";
        }

        StringBuilder content = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return content.toString();
    }
}
