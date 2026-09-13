package model;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Random;
import esper.Config;
import events.HumiditySensorReading;

public class Humidifier extends Actuator {
    private int freq;
    private Controller c;
    
    public Humidifier(int actuatorID, Controller c, int freq) {
        super(actuatorID);
        this.freq = freq;
        this.c = c;
        System.out.println("DEBUG: Humidifier initialized - ID: " + actuatorID + ", freq: " + freq);
    }

    public void setController(Controller c) {
        this.c = c;
        System.out.println("DEBUG: Controller reference set in Humidifier");
    }

    @Override
    public void run() {
        System.out.println("DEBUG: Humidifier run method started");
        boolean isIncreasing = true;  // Track current direction
        boolean hasBeeped = false;    // Track if we've beeped at current threshold
        int lastBeepHumidity = 0;     // Track the humidity at last beep
        
        while (!Thread.currentThread().isInterrupted()) {
            try {
                if (c == null) {
                    System.err.println("DEBUG: Humidifier controller reference is null");
                    Thread.sleep(1000);
                    continue;
                }
                
                if (c.getHumiditySensor() == null) {
                    System.err.println("DEBUG: Humidity sensor in controller is null");
                    Thread.sleep(1000);
                    continue;
                }
                
                HumiditySensor sensor = c.getHumiditySensor();
                int currentHumidity = sensor.getCurrent();
                int minHumidity = sensor.getMinHumidity();  // Should be 30
                int maxHumidity = sensor.getMaxHumidity();  // Should be 40
                
                if (sensor.isIsRunning()) {
                    // System is ON - Humidifier controls humidity
                    if (isIncreasing) {
                        // Currently increasing
                        if (currentHumidity >= maxHumidity && !hasBeeped) {
                            // Reached max threshold - beep but continue increasing
                            System.out.println("DEBUG: Humidity reached max (" + maxHumidity + "), beeping");
                            c.getBeeper().beep();
                            hasBeeped = true;
                            lastBeepHumidity = currentHumidity;
                            // Only increase by 1 to reach exactly 41
                            sensor.setCurrent(maxHumidity + 1);
                        } else if (currentHumidity >= maxHumidity + 1) {
                            // Reached 41 - start decreasing
                            System.out.println("DEBUG: Humidity reached 41, starting to decrease");
                            isIncreasing = false;
                            hasBeeped = false;
                            int decrease = random(1, freq);
                            sensor.setCurrent(currentHumidity - decrease);
                        } else {
                            // Normal increase
                            int increase = random(1, freq);
                            sensor.setCurrent(currentHumidity + increase);
                        }
                    } else {
                        // Currently decreasing
                        if (currentHumidity <= minHumidity && !hasBeeped) {
                            // Reached min threshold - beep but continue decreasing
                            System.out.println("DEBUG: Humidity reached min (" + minHumidity + "), beeping");
                            c.getBeeper().beep();
                            hasBeeped = true;
                            lastBeepHumidity = currentHumidity;
                            // Only decrease by 1 to reach exactly 29
                            sensor.setCurrent(minHumidity - 1);
                        } else if (currentHumidity <= minHumidity - 1) {
                            // Reached 29 - start increasing
                            System.out.println("DEBUG: Humidity reached 29, starting to increase");
                            isIncreasing = true;
                            hasBeeped = false;
                            int increase = random(1, freq);
                            sensor.setCurrent(currentHumidity + increase);
                        } else {
                            // Normal decrease
                            int decrease = random(1, freq);
                            sensor.setCurrent(currentHumidity - decrease);
                        }
                    }
                } else {
                    // System is OFF - humidity gradually decreases to 10
                    if (currentHumidity > 10) {
                        int decrease = random(1, freq);
                        sensor.setCurrent(Math.max(10, currentHumidity - decrease));
                    }
                    // Reset direction tracking when system is turned off
                    isIncreasing = true;
                    hasBeeped = false;
                    lastBeepHumidity = 0;
                }
                
                // Always send event to Esper to update the display
                Config.sendEvent(new HumiditySensorReading(sensor.getCurrent()));
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("DEBUG: Humidifier thread interrupted");
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                System.err.println("DEBUG: Error in Humidifier run: " + e.getMessage());
                e.printStackTrace();
            }
        }
        System.out.println("DEBUG: Humidifier run method exited");
    }

    private int random(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }
        return new Random().nextInt((max - min) + 1) + min;
    }

    public float getFreq() {
        return freq;
    }

    public void setFreq(int freq) {
        this.freq = freq;
        System.out.println("DEBUG: Humidifier frequency updated to " + freq);
    }

    @Override
    public void adjustSetting(boolean state) {
        System.out.println("DEBUG: Humidifier adjustSetting called with state: " + state);
    }

    @Override
    public void check(boolean state) {
        System.out.println("DEBUG: Humidifier check called with state: " + state);
    }
}