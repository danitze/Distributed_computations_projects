package com.example.lab1;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;

import java.util.concurrent.atomic.AtomicInteger;

public class MainController {

    private SliderThread thread1 = null;
    private SliderThread thread2 = null;

    @FXML
    private Slider slider;

    @FXML
    private Button start1Button;

    @FXML
    private Button start2Button;

    @FXML
    private Button stop1Button;

    @FXML
    private Button stop2Button;

    private final AtomicInteger semaphore = new AtomicInteger(0);

    @FXML
    public void initialize() {
        start1Button.setOnMouseClicked(e -> {
            start1Button.setDisable(true);
            stop1Button.setDisable(false);
            thread1 = new SliderThread(10, slider, Thread.MIN_PRIORITY, semaphore);
            thread1.start();
        });
        
        start2Button.setOnMouseClicked(e -> {
            start2Button.setDisable(true);
            stop2Button.setDisable(false);
            thread2 = new SliderThread(90, slider, Thread.MAX_PRIORITY, semaphore);
            thread2.start();
        });
        
        stop1Button.setOnMouseClicked(e -> {
            thread1.interrupt();
            thread1 = null;
            stop1Button.setDisable(true);
            start1Button.setDisable(false);
        });

        stop2Button.setOnMouseClicked(e -> {
            thread2.interrupt();
            thread2 = null;
            stop2Button.setDisable(true);
            start2Button.setDisable(false);
        });
    }

    public void onClose() {
        if(thread1 != null) {
            thread1.interrupt();
            thread1 = null;
        }
        if(thread2 != null) {
            thread2.interrupt();
            thread2 = null;
        }
    }

}