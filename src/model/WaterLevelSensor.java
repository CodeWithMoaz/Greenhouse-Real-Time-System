package model;

import java.util.logging.Level;
import java.util.logging.Logger;

public class WaterLevelSensor extends Sensor {
    private float minLevel;
    private float maxLevel;
    private Thread sensorThread;
    private boolean isRunning;
    private float currentLevel;

    public WaterLevelSensor(int sensorID, float minLevel, float maxLevel) {
        super(sensorID, "WaterLevel");
        this.minLevel = minLevel;
        this.maxLevel = maxLevel;
        this.isRunning = false;
        this.currentLevel = 0.0f; // Start at empty
        System.out.println("DEBUG: WaterLevelSensor initialized - ID: " + sensorID + 
                         ", Min: " + minLevel + ", Max: " + maxLevel);
    }

    @Override
    public void run() {
        System.out.println("DEBUG: WaterLevelSensor run method started");
        sensorThread = Thread.currentThread();
        
        while (!Thread.currentThread().isInterrupted()) {
            try {
                if (!isRunning) {
                    Thread.sleep(1000);
                    continue;
                }
                
                // Let the WaterFlowControl control the water level
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("DEBUG: WaterLevelSensor thread interrupted");
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                System.err.println("DEBUG: Error in WaterLevelSensor run: " + e.getMessage());
                e.printStackTrace();
            }
        }
        System.out.println("DEBUG: WaterLevelSensor run method exited");
    }

    public float getMinLevel() {
        return minLevel;
    }

    public void setMinLevel(float minLevel) {
        this.minLevel = minLevel;
    }

    public float getMaxLevel() {
        return maxLevel;
    }

    public void setMaxLevel(float maxLevel) {
        this.maxLevel = maxLevel;
    }

    public Thread getSensorThread() {
        return sensorThread;
    }

    public boolean isIsRunning() {
        return isRunning;
    }

    public void setIsRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }

    @Override
    public String getSensorType() {
        return "WaterLevel";
    }

    @Override
    protected int generateInitialValue() {
        return 0; // Start at empty
    }

    @Override
    protected void updateValue() {
        // This method is not used as the WaterFlowControl controls the value
    }

    public float getCurrent() {
        return currentLevel;
    }

    public void setCurrent(float level) {
        this.currentLevel = level;
    }
}