package org.example;

public class Dock implements Runnable {

    private final Port port;

    public Dock(Port port) {
        this.port = port;
    }

    @Override
    public void run() {
        Random random = new Random();
        while (true) {
            boolean shouldReload = random.getBoolean();
            int shipCapacity = random.getInt(port.getCapacity() - 1) + 1;
            boolean isEmpty = random.getBoolean();
            if(!isEmpty) {
                if(shipCapacity == 1) {
                    port.addContainers(1);
                } else {
                    port.addContainers(random.getInt(shipCapacity - 1) + 1);
                }
            }
            if(shouldReload) {
                port.getContainers(shipCapacity);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
