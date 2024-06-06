package com.model.GUI;

import com.model.image.ImageProcessingException;
import com.model.image.ImageProcessor;
import com.model.image.ImageService;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

public class Frame extends JFrame {
    // Variables declaration - do not modify
    private JPanel jPanel1;
    private JButton loadButton;
    private JButton startButton;
    public Frame() {
        init();
    }
    public final ImageProcessor imageProcessor = new ImageProcessor();
    private void init() {
        jPanel1 = new JPanel();
        loadButton = new JButton();
        startButton = new JButton();


        loadButton.setText("LoadImages");
        startButton.setText("Start");

        loadButton.addActionListener(e-> {
            // Create a file chooser
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fileChooser.setMultiSelectionEnabled(true);

            // show the file chooser dialog
            int result = fileChooser.showOpenDialog(this);

            // Process the result
            if (result == JFileChooser.APPROVE_OPTION) {
                // Get the selected file
                Path imagePath = fileChooser.getSelectedFile().toPath();

                // ImageProcessor is DOC
                Set<Path> pathSet = null;
                try {
                    pathSet = imageProcessor.loadImages(imagePath);
                } catch (IOException ex) {
                    throw new ImageProcessingException("error loadImages");
                }
                // Inform the user that the loading process was successful
                JOptionPane.showMessageDialog(this, "File loaded successfully: " + imagePath, "Success",
                        JOptionPane.INFORMATION_MESSAGE);

            }
        });
        setLayout();

        pack();
        setVisible(true);
        setDefaultCloseOperation(3);
        setLocationRelativeTo(null);

    }

    private void setLayout() {
        var jPanel1Layout = new GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(92, 92, 92)
                                .addComponent(loadButton, GroupLayout.PREFERRED_SIZE, 140, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 229, Short.MAX_VALUE)
                                .addComponent(startButton, GroupLayout.PREFERRED_SIZE, 140, GroupLayout.PREFERRED_SIZE)
                                .addGap(92, 92, 92))
        );

        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(215, 215, 215)
                                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(loadButton, GroupLayout.PREFERRED_SIZE, 69, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(startButton, GroupLayout.PREFERRED_SIZE, 69, GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(218, Short.MAX_VALUE))
        );

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jPanel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jPanel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
        );
    }



}
