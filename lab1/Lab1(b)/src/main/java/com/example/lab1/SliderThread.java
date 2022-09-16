package com.example.lab1;

import javafx.scene.control.Slider;

import java.util.concurrent.atomic.AtomicInteger;

public class SliderThread extends Thread {
    private final int sliderPosition;
    private final Slider slider;

    private final AtomicInteger semaphore;

    public SliderThread(int sliderPosition, Slider slider, int priority, AtomicInteger semaphore) {
        this.sliderPosition = sliderPosition;
        this.slider = slider;
        this.semaphore = semaphore;
        setPriority(priority);
    }

    @Override
    public void run() {
        super.run();

        System.out.println(Thread.currentThread().getPriority() + " " + semaphore.get());
        while (semaphore.get() == 1) {}
        semaphore.set(1);

        while (!interrupted()) {
            int value = (int) slider.getValue();
            if (value < sliderPosition) {
                ++value;
            } else if (value > sliderPosition) {
                --value;
            }
            slider.setValue(value);
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                return;
            }
        }
    }

    @Override
    public void interrupt() {
        semaphore.set(0);
        super.interrupt();
    }
}
