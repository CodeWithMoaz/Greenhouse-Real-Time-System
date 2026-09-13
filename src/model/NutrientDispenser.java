package model;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Random;
import esper.Config;
import events.pHSensorReading;

public class NutrientDispenser extends Actuator {
    private int freq;
    private Controller c;
    
    public NutrientDispenser(int actuatorID, Controller c, int freq) {
        super(actuatorID);
        this.freq = freq;
        this.c = c;
        System.out.println("DEBUG: NutrientDispenser initialized - ID: " + actuatorID + ", freq: " + freq);
    }

    public void setController(Controller c) {
        this.c = c;
        System.out.println("DEBUG: Controller reference set in NutrientDispenser");
    }

    @Override
    public void run() {
        System.out.println("DEBUG: NutrientDispenser run method started");
        boolean isIncreasing = true;  // Track current direction
        boolean hasBeeped = false;    // Track if we've beeped at current threshold
        float lastBeep_pH = 0;        // Track the pH at last beep
        
        while (!Thread.currentThread().isInterrupted()) {
            try {
                if (c == null) {
                    System.err.println("DEBUG: NutrientDispenser controller reference is null");
                    Thread.sleep(1000);
                    continue;
                }
                
                if (c.getpHSensor() == null) {
                    System.err.println("DEBUG: pH sensor in controller is null");
                    Thread.sleep(1000);
                    continue;
                }
                
                pHSensor sensor = c.getpHSensor();
                float current_pH = sensor.getCurrent();
                float min_pH = sensor.getMin_pH();  // Should be 5.5
                float max_pH = sensor.getMax_pH();  // Should be 6.5
                
                if (sensor.isIsRunning()) {
                    // System is ON - NutrientDispenser controls pH
                    if (isIncreasing) {
                        // Currently increasing
                        if (current_pH >= max_pH && !hasBeeped) {
                            // Reached max threshold - beep but continue increasing
                            System.out.println("DEBUG: pH reached max (" + max_pH + "), beeping");
                            c.getBeeper().beep();
                            hasBeeped = true;
                            lastBeep_pH = current_pH;
                            // Only increase by 0.1 to reach exactly 6.6
                            sensor.setCurrent(max_pH + 0.1f);
                        } else if (current_pH >= max_pH + 0.1f) {
                            // Reached 6.6 - start decreasing
                            System.out.println("DEBUG: pH reached 6.6, starting to decrease");
                            isIncreasing = false;
                            hasBeeped = false;
                            float decrease = random(0.1f, 0.3f);
                            sensor.setCurrent(current_pH - decrease);
                        } else {
                            // Normal increase
                            float increase = random(0.1f, freq * 0.1f);
                            sensor.setCurrent(current_pH + increase);
                        }
                    } else {
                        // Currently decreasing
                        if (current_pH <= min_pH && !hasBeeped) {
                            // Reached min threshold - beep but continue decreasing
                            System.out.println("DEBUG: pH reached min (" + min_pH + "), beeping");
                            c.getBeeper().beep();
                            hasBeeped = true;
                            lastBeep_pH = current_pH;
                            // Only decrease by 0.1 to reach exactly 5.4
                            sensor.setCurrent(min_pH - 0.1f);
                        } else if (current_pH <= min_pH - 0.1f) {
                            // Reached 5.4 - start increasing
                            System.out.println("DEBUG: pH reached 5.4, starting to increase");
                            isIncreasing = true;
                            hasBeeped = false;
                            float increase = random(0.1f, freq * 0.1f);
                            sensor.setCurrent(current_pH + increase);
                        } else {
                            // Normal decrease
                            float decrease = random(0.1f, freq * 0.1f);
                            sensor.setCurrent(current_pH - decrease);
                        }
                    }
                } else {
                    // System is OFF - pH gradually returns to neutral
                    if (current_pH > 7.0f) {
                        float decrease = random(0.1f, freq * 0.1f);
                        sensor.setCurrent(Math.max(7.0f, current_pH - decrease));
                    } else if (current_pH < 7.0f) {
                        float increase = random(0.1f, freq * 0.1f);
                        sensor.setCurrent(Math.min(7.0f, current_pH + increase));
                    }
                    // Reset direction tracking when system is turned off
                    isIncreasing = true;
                    hasBeeped = false;
                    lastBeep_pH = 0;
                }
                
                // Always send event to Esper to update the display
                Config.sendEvent(new pHSensorReading(sensor.getCurrent()));
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("DEBUG: NutrientDispenser thread interrupted");
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                System.err.println("DEBUG: Error in NutrientDispenser run: " + e.getMessage());
                e.printStackTrace();
            }
        }
        System.out.println("DEBUG: NutrientDispenser run method exited");
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
        System.out.println("DEBUG: NutrientDispenser frequency updated to " + freq);
    }

    @Override
    public void adjustSetting(boolean state) {
        System.out.println("DEBUG: NutrientDispenser adjustSetting called with state: " + state);
    }

    @Override
    public void check(boolean state) {
        System.out.println("DEBUG: NutrientDispenser check called with state: " + state);
    }
}
