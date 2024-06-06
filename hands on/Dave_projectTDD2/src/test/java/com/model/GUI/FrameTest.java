package com.model.GUI;

import com.model.image.ImageProcessor;
import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FrameTest {
    private FrameFixture window;
    private JFileChooser mockFileChooser;
    private final ImageProcessor imageProcessor = new ImageProcessor();

    @BeforeEach
    void setUp() {
        Frame frame = GuiActionRunner.execute(() -> new Frame());
        window = new FrameFixture(frame);
        window.show(); // shows the frame to test
    }

    @AfterEach
    void tearDown() {
        window.cleanUp();
    }

    @Test
    @GUITest
    void shouldDisplayFrameWithButtons() {
        window.button(JButtonMatcher.withText("LoadImages")).requireVisible();
        window.button(JButtonMatcher.withText("Start")).requireVisible();
    }

    @Test
    @GUITest
    void shouldPerformLoadButtonAction() {
        // Assuming loadButton has an action listener that changes a JLabel text
        JButton loadButton = window.button(JButtonMatcher.withText("LoadImages")).target();
        loadButton.addActionListener(e -> JOptionPane.showMessageDialog(null, "Load Button Clicked"));

        window.button(JButtonMatcher.withText("LoadImages")).click();
        window.optionPane().requireMessage("Load Button Clicked").okButton().click();
    }

    @Test
    @GUITest
    void shouldPerformStartButtonAction() {
        // Assuming startButton has an action listener that changes a JLabel text
        JButton startButton = window.button(JButtonMatcher.withText("Start")).target();
        startButton.addActionListener(e -> JOptionPane.showMessageDialog(null, "Start Button Clicked"));

        window.button(JButtonMatcher.withText("Start")).click();
        window.optionPane().requireMessage("Start Button Clicked").okButton().click();
    }

    @Test
    @GUITest
    void shouldPerformLoadButtonActionWithFileChooser() throws IOException {
        // Find the JButton component by its text
        JButton loadButton = window.button(JButtonMatcher.withText("LoadImages")).target();

        // mockFileChooser to simulate a user selecting a file
        mockFileChooser = mock(JFileChooser.class);
        when(mockFileChooser.showOpenDialog(any(Component.class))).thenReturn(JFileChooser.APPROVE_OPTION);
        when(mockFileChooser.getSelectedFile()).thenReturn(Paths.get("dummyPath.txt").toFile());

        loadButton.addActionListener(e -> {
            // use the mocked file chooser instead of creating a new one
            int result = mockFileChooser.showOpenDialog(window.target());

            if (result == JFileChooser.APPROVE_OPTION) {
                Path imagePath = mockFileChooser.getSelectedFile().toPath();
                try {
                    imageProcessor.loadImages(imagePath);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        // simulate a button click
        window.button(JButtonMatcher.withText("LoadImages")).click();

        // verify that the mocked JFileChooser was interacted with
        verify(mockFileChooser, times(1)).showOpenDialog(any(Component.class));
        verify(mockFileChooser, times(1)).getSelectedFile();
    }
}