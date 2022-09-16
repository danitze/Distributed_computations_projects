package com.example.lab1;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

public class MainController {

    private SliderThread thread1 = null;
    private SliderThread thread2 = null;

    @FXML
    private Slider slider;

    @FXML
    private Spinner<Integer> firstThreadPrioritySpinner;

    @FXML
    private Spinner<Integer> secondThreadPrioritySpinner;

    @FXML
    private Button startButton;

    @FXML
    public void initialize() {
        firstThreadPrioritySpinner.setValueFactory(createValueFactory());
        secondThreadPrioritySpinner.setValueFactory(createValueFactory());

        firstThreadPrioritySpinner.getEditor()
                .textProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if(thread1 != null) {
                        thread1.setPriority(Integer.parseInt(newValue));
                    }
                });

        secondThreadPrioritySpinner.getEditor()
                .textProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if(thread2 != null) {
                        thread2.setPriority(Integer.parseInt(newValue));
                    }
                });

        startButton.setOnMouseClicked((event) -> {
            thread1 = new SliderThread(10, slider, firstThreadPrioritySpinner.getValue());
            thread2 = new SliderThread(90, slider, secondThreadPrioritySpinner.getValue());

            thread1.start();
            thread2.start();

            startButton.setDisable(true);
        });
    }

    private SpinnerValueFactory<Integer> createValueFactory() {
        return new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1);
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