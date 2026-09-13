package model;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Random;
import esper.Config;
import events.TempSensorReading;

public class HVAC extends Actuator {
    private int freq;

    private Controller c;
    public HVAC(int actuatorID, Controller c, int freq) {
        super(actuatorID);
        this.freq = freq;
        this.c = c;
    }

    public void setController(Controller c) {
        this.c = c;
    }

    
    @Override
    public void run() {
        boolean isIncreasing = true;  // Track current direction
        boolean hasBeeped = false;    // Track if we've beeped at current threshold
        int lastBeepTemp = 0;         // Track the temperature at last beep
        
        while (!Thread.currentThread().isInterrupted()) {
            try {
                if (c == null) {
                    System.err.println("HVAC controller reference is null");
                    Thread.sleep(1000);
                    continue;
                }
                
                if (c.getTempSensor() == null) {
                    System.err.println("Temperature sensor in controller is null");
                    Thread.sleep(1000);
                    continue;
                }
                
                TemperatureSensor sensor = c.getTempSensor();
                int currentTemp = sensor.getCurrent();
                int minTemp = sensor.getMinTemp();  // Should be 30
                int maxTemp = sensor.getMaxTemp();  // Should be 40
                
                if (sensor.isIsRunning()) {
                    // System is ON - HVAC controls temperature
                    if (isIncreasing) {
                        // Currently increasing
                        if (currentTemp >= maxTemp && !hasBeeped) {
                            // Reached max threshold - beep but continue increasing
                            c.getBeeper().beep();
                            hasBeeped = true;
                            lastBeepTemp = currentTemp;
                            // Only increase by 1 to reach exactly 41
                            sensor.setCurrent(maxTemp + 1);
                        } else if (currentTemp >= maxTemp + 1) {
                            // Reached 41 - start decreasing
                            isIncreasing = false;
                            hasBeeped = false;
                            int decrease = random(1, freq);
                            sensor.setCurrent(currentTemp - decrease);
                        } else {
                            // Normal increase
                            int increase = random(1, freq);
                            sensor.setCurrent(currentTemp + increase);
                        }
                    } else {
                        // Currently decreasing
                        if (currentTemp <= minTemp && !hasBeeped) {
                            // Reached min threshold - beep but continue decreasing
                            c.getBeeper().beep();
                            hasBeeped = true;
                            lastBeepTemp = currentTemp;
                            // Only decrease by 1 to reach exactly 29
                            sensor.setCurrent(minTemp - 1);
                        } else if (currentTemp <= minTemp - 1) {
                            // Reached 29 - start increasing
                            isIncreasing = true;
                            hasBeeped = false;
                            int increase = random(1, freq);
                            sensor.setCurrent(currentTemp + increase);
                        } else {
                            // Normal decrease
                            int decrease = random(1, freq);
                            sensor.setCurrent(currentTemp - decrease);
                        }
                    }
                } else {
                    // System is OFF - temperature gradually decreases to 10
                    if (currentTemp > 10) {
                        int decrease = random(1, freq);
                        sensor.setCurrent(Math.max(10, currentTemp - decrease));
                    }
                    // Reset direction tracking when system is turned off
                    isIncreasing = true;
                    hasBeeped = false;
                    lastBeepTemp = 0;
                }
                
                // Always send event to Esper to update the display
                Config.sendEvent(new TempSensorReading(sensor.getCurrent()));
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                System.err.println("Error in HVAC run: " + e.getMessage());
                e.printStackTrace();
            }
        }
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
    }


    // Additional methods
    public void decreaseTemp(Sensor s) {
        try {
            s.setCurrentValue((s.getCurrentValue() - freq));
        } catch (Exception e) {
            System.err.println("Error decreasing temperature: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void increaseTemp(Sensor s) {


    }

    @Override
    public void adjustSetting(boolean state) {
        // Implementation
    }    
    
    @Override
    public void check(boolean state) {
        // Implementation
    }
}