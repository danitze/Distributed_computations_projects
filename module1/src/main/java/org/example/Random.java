package org.example;

import java.nio.ByteBuffer;
import java.security.SecureRandom;

public class Random {
    private final SecureRandom secureRandom;

    public Random() {
        secureRandom = new SecureRandom(ByteBuffer.allocate(Long.BYTES).putLong(System.currentTimeMillis()).array());
    }

    public boolean getBoolean() {
        return secureRandom.nextBoolean();
    }

    public int getInt(int bound) {
        return secureRandom.nextInt(bound);
    }
}
