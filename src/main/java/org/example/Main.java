package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    static boolean STATUS = false;
    static String selectedDesign = "default";
    static String selectedColor = "dark";



    public static void main(String[] args) {





        JFrame frame = new JFrame("Ninjabrain Stream Overlay v1.0.0");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 250);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        int verticalSpacing = 10;

        JLabel statusLabel = new JLabel("Trying to find Ninjabrain...");
        statusLabel.setFont(new Font("Sans-Serif", Font.ITALIC, 12));
        statusLabel.setForeground(new Color(100, 100, 100));
        statusLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(statusLabel);
        mainPanel.add(Box.createVerticalStrut(verticalSpacing));

        JPanel designPanel = new JPanel(new BorderLayout(5, 5));
        JLabel designLabel = new JLabel("Design:");
        designLabel.setFont(new Font("Sans-Serif", Font.PLAIN, 14));
        JComboBox<String> designDropdown = new JComboBox<>(new String[]{"Default"});
        designDropdown.setFont(new Font("Sans-Serif", Font.PLAIN, 14));
        designPanel.add(designLabel, BorderLayout.WEST);
        designPanel.add(designDropdown, BorderLayout.CENTER);
        designPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        designDropdown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedDesign = designDropdown.getSelectedItem().toString().toLowerCase();
            }
        });
        mainPanel.add(designPanel);
        mainPanel.add(Box.createVerticalStrut(verticalSpacing));

        JPanel colorPanel = new JPanel(new BorderLayout(5, 5));
        JLabel colorLabel = new JLabel("Color Scheme:");
        colorLabel.setFont(new Font("Sans-Serif", Font.PLAIN, 14));
        JComboBox<String> colorDropdown = new JComboBox<>(new String[]{"Dark", "Light", "Transparent"});
        colorDropdown.setFont(new Font("Sans-Serif", Font.PLAIN, 14));
        colorPanel.add(colorLabel, BorderLayout.WEST);
        colorPanel.add(colorDropdown, BorderLayout.CENTER);
        colorPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        colorDropdown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedColor = colorDropdown.getSelectedItem().toString().toLowerCase();
            }
        });
        mainPanel.add(colorPanel);
        mainPanel.add(Box.createVerticalStrut(verticalSpacing));

        JPanel urlPanel = new JPanel(new BorderLayout(5, 5));
        JLabel urlLabel = new JLabel("URL:");
        urlLabel.setFont(new Font("Sans-Serif", Font.PLAIN, 14));
        JTextField urlField = new JTextField("http://localhost:787");
        urlField.setEditable(false);
        urlField.setFont(new Font("Sans-Serif", Font.PLAIN, 14));
        JButton copyButton = new JButton("Copy");
        copyButton.setFont(new Font("Sans-Serif", Font.PLAIN, 12));
        copyButton.addActionListener((ActionEvent e) -> {
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(
                    new StringSelection(urlField.getText()), null);
            JOptionPane.showMessageDialog(frame, "URL copied to clipboard!", "Info", JOptionPane.INFORMATION_MESSAGE);
        });
        urlPanel.add(urlLabel, BorderLayout.WEST);
        urlPanel.add(urlField, BorderLayout.CENTER);
        urlPanel.add(copyButton, BorderLayout.EAST);
        urlPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(urlPanel);

        frame.add(mainPanel);
        frame.setVisible(true);

        HTTPServer server = new HTTPServer();
        server.startAsThread();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                ServiceTester serviceTester = new ServiceTester();
                boolean nbb = serviceTester.SearchForNinjabrain();
                if(nbb){
                    statusLabel.setText("Ninjabrain Bot is running!");
                    statusLabel.setFont(new Font("Arial", Font.BOLD, 12));
                    STATUS = true;


                } else {
                    statusLabel.setText("Trying to find Ninjabrain...");
                    statusLabel.setFont(new Font("Arial", Font.ITALIC, 12));
                    STATUS = false;
                }
            }
        };

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(runnable, 2, 1, TimeUnit.SECONDS);

    }

    public static boolean isSTATUS() {
        return STATUS;
    }
}
