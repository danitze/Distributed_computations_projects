package com.example.lab1;

import javafx.scene.control.Slider;

public class SliderThread extends Thread {
    private final int sliderPosition;
    private final Slider slider;

    private int counter = 0;

    public SliderThread(int sliderPosition, Slider slider, int priority) {
        this.sliderPosition = sliderPosition;
        this.slider = slider;
        setPriority(priority);
    }

    @Override
    public void run() {
        super.run();
        while (!interrupted()) {
            synchronized (slider) {
                System.out.println(Thread.currentThread().getName() + " " + Thread.currentThread().getPriority());
                int value = (int) slider.getValue();
                if (value < sliderPosition) {
                    ++value;
                } else if (value > sliderPosition) {
                    --value;
                }
                slider.setValue(value);
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                return;
            }
        }
    }
}
