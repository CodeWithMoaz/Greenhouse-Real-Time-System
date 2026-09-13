package model;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Random;
import esper.Config;
import events.LightSensorReading;

import model.Actuator;

public class LightControl extends Actuator {
    private int freq;
    private Controller c;
    
    public LightControl(int actuatorID, Controller c, int freq) {
        super(actuatorID);
        this.freq = freq;
        this.c = c;
        System.out.println("DEBUG: LightControl initialized - ID: " + actuatorID + ", freq: " + freq);
    }

    public void setController(Controller c) {
        this.c = c;
        System.out.println("DEBUG: Controller reference set in LightControl");
    }

    @Override
    public void run() {
        System.out.println("DEBUG: LightControl run method started");
        boolean isIncreasing = true;  // Track current direction
        boolean hasBeeped = false;    // Track if we've beeped at current threshold
        float lastBeepIntensity = 0;  // Track the intensity at last beep
        
        while (!Thread.currentThread().isInterrupted()) {
            try {
                if (c == null) {
                    System.err.println("DEBUG: LightControl controller reference is null");
                    Thread.sleep(1000);
                    continue;
                }
                
                if (c.getLightSensor() == null) {
                    System.err.println("DEBUG: Light sensor in controller is null");
                    Thread.sleep(1000);
                    continue;
                }
                
                LightSensor sensor = c.getLightSensor();
                float currentIntensity = sensor.getCurrent();
                float minIntensity = sensor.getMinIntensity();  // Should be 20
                float maxIntensity = sensor.getMaxIntensity();  // Should be 80
                
                if (sensor.isIsRunning()) {
                    // System is ON - LightControl controls intensity
                    if (isIncreasing) {
                        // Currently increasing
                        if (currentIntensity >= maxIntensity && !hasBeeped) {
                            // Reached max threshold - beep but continue increasing
                            System.out.println("DEBUG: Light intensity reached max (" + maxIntensity + "), beeping");
                            c.getBeeper().beep();
                            hasBeeped = true;
                            lastBeepIntensity = currentIntensity;
                            // Only increase by 1 to reach exactly 81
                            sensor.setCurrent(maxIntensity + 1);
                        } else if (currentIntensity >= maxIntensity + 1) {
                            // Reached 81 - start decreasing
                            System.out.println("DEBUG: Light intensity reached 81, starting to decrease");
                            isIncreasing = false;
                            hasBeeped = false;
                            float decrease = random(1, 3);
                            sensor.setCurrent(currentIntensity - decrease);
                        } else {
                            // Normal increase
                            float increase = random(1, freq);
                            sensor.setCurrent(currentIntensity + increase);
                        }
                    } else {
                        // Currently decreasing
                        if (currentIntensity <= minIntensity && !hasBeeped) {
                            // Reached min threshold - beep but continue decreasing
                            System.out.println("DEBUG: Light intensity reached min (" + minIntensity + "), beeping");
                            c.getBeeper().beep();
                            hasBeeped = true;
                            lastBeepIntensity = currentIntensity;
                            // Only decrease by 1 to reach exactly 19
                            sensor.setCurrent(minIntensity - 1);
                        } else if (currentIntensity <= minIntensity - 1) {
                            // Reached 19 - start increasing
                            System.out.println("DEBUG: Light intensity reached 19, starting to increase");
                            isIncreasing = true;
                            hasBeeped = false;
                            float increase = random(1, 3);
                            sensor.setCurrent(currentIntensity + increase);
                        } else {
                            // Normal decrease
                            float decrease = random(1, freq);
                            sensor.setCurrent(currentIntensity - decrease);
                        }
                    }
                } else {
                    // System is OFF - intensity gradually returns to dark
                    if (currentIntensity > 0) {
                        float decrease = random(1, freq);
                        sensor.setCurrent(Math.max(0, currentIntensity - decrease));
                    }
                    // Reset direction tracking when system is turned off
                    isIncreasing = true;
                    hasBeeped = false;
                    lastBeepIntensity = 0;
                }
                
                // Always send event to Esper to update the display
                Config.sendEvent(new LightSensorReading(sensor.getCurrent()));
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("DEBUG: LightControl thread interrupted");
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                System.err.println("DEBUG: Error in LightControl run: " + e.getMessage());
                e.printStackTrace();
            }
        }
        System.out.println("DEBUG: LightControl run method exited");
    }

    private float random(float min, float max) {
        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }
        return min + new Random().nextFloat() * (max - min);
    }

    public float getFreq() {
        return freq;
    }

    public void setFreq(int freq) {
        this.freq = freq;
        System.out.println("DEBUG: LightControl frequency updated to " + freq);
    }

    @Override
    public void adjustSetting(boolean state) {
        System.out.println("DEBUG: LightControl adjustSetting called with state: " + state);
    }

    @Override
    public void check(boolean state) {
        System.out.println("DEBUG: LightControl check called with state: " + state);
    }
}