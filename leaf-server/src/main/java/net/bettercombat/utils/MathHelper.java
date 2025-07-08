package net.bettercombat.utils;

public class MathHelper {
    // Generic
    public static float clamp(float value, float min, float max) {
        return Math.max(Math.min(value, max), min);
    }
}
