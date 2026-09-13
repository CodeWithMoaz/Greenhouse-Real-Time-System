package model;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Random;
import esper.Config;
import events.WaterSensorReading;

public class WaterFlowControl extends Actuator {
    private int freq;
    private Controller c;
    
    public WaterFlowControl(int actuatorID, Controller c, int freq) {
        super(actuatorID);
        this.freq = freq;
        this.c = c;
        System.out.println("DEBUG: WaterFlowControl initialized - ID: " + actuatorID + ", freq: " + freq);
    }

    public void setController(Controller c) {
        this.c = c;
        System.out.println("DEBUG: Controller reference set in WaterFlowControl");
    }

    @Override
    public void run() {
        System.out.println("DEBUG: WaterFlowControl run method started");
        boolean isIncreasing = true;  // Track current direction
        boolean hasBeeped = false;    // Track if we've beeped at current threshold
        float lastBeepLevel = 0;      // Track the level at last beep
        
        while (!Thread.currentThread().isInterrupted()) {
            try {
                if (c == null) {
                    System.err.println("DEBUG: WaterFlowControl controller reference is null");
                    Thread.sleep(1000);
                    continue;
                }
                
                if (c.getWaterLevelSensor() == null) {
                    System.err.println("DEBUG: Water level sensor in controller is null");
                    Thread.sleep(1000);
                    continue;
                }
                
                WaterLevelSensor sensor = c.getWaterLevelSensor();
                float currentLevel = sensor.getCurrent();
                float minLevel = sensor.getMinLevel();  // Should be 20
                float maxLevel = sensor.getMaxLevel();  // Should be 80
                
                if (sensor.isIsRunning()) {
                    // System is ON - WaterFlowControl controls level
                    if (isIncreasing) {
                        // Currently increasing
                        if (currentLevel >= maxLevel && !hasBeeped) {
                            // Reached max threshold - beep but continue increasing
                            System.out.println("DEBUG: Water level reached max (" + maxLevel + "), beeping");
                            c.getBeeper().beep();
                            hasBeeped = true;
                            lastBeepLevel = currentLevel;
                            // Only increase by 1 to reach exactly 81
                            sensor.setCurrent(maxLevel + 1);
                        } else if (currentLevel >= maxLevel + 1) {
                            // Reached 81 - start decreasing
                            System.out.println("DEBUG: Water level reached 81, starting to decrease");
                            isIncreasing = false;
                            hasBeeped = false;
                            float decrease = random(1, 3);
                            sensor.setCurrent(currentLevel - decrease);
                        } else {
                            // Normal increase
                            float increase = random(1, freq);
                            sensor.setCurrent(currentLevel + increase);
                        }
                    } else {
                        // Currently decreasing
                        if (currentLevel <= minLevel && !hasBeeped) {
                            // Reached min threshold - beep but continue decreasing
                            System.out.println("DEBUG: Water level reached min (" + minLevel + "), beeping");
                            c.getBeeper().beep();
                            hasBeeped = true;
                            lastBeepLevel = currentLevel;
                            // Only decrease by 1 to reach exactly 19
                            sensor.setCurrent(minLevel - 1);
                        } else if (currentLevel <= minLevel - 1) {
                            // Reached 19 - start increasing
                            System.out.println("DEBUG: Water level reached 19, starting to increase");
                            isIncreasing = true;
                            hasBeeped = false;
                            float increase = random(1, 3);
                            sensor.setCurrent(currentLevel + increase);
                        } else {
                            // Normal decrease
                            float decrease = random(1, freq);
                            sensor.setCurrent(currentLevel - decrease);
                        }
                    }
                } else {
                    // System is OFF - level gradually returns to empty
                    if (currentLevel > 0) {
                        float decrease = random(1, freq);
                        sensor.setCurrent(Math.max(0, currentLevel - decrease));
                    }
                    // Reset direction tracking when system is turned off
                    isIncreasing = true;
                    hasBeeped = false;
                    lastBeepLevel = 0;
                }
                
                // Always send event to Esper to update the display
                Config.sendEvent(new WaterSensorReading(sensor.getCurrent()));
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("DEBUG: WaterFlowControl thread interrupted");
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                System.err.println("DEBUG: Error in WaterFlowControl run: " + e.getMessage());
                e.printStackTrace();
            }
        }
        System.out.println("DEBUG: WaterFlowControl run method exited");
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
        System.out.println("DEBUG: WaterFlowControl frequency updated to " + freq);
    }

    @Override
    public void adjustSetting(boolean state) {
        System.out.println("DEBUG: WaterFlowControl adjustSetting called with state: " + state);
    }

    @Override
    public void check(boolean state) {
        System.out.println("DEBUG: WaterFlowControl check called with state: " + state);
    }
}