package model;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LightSensor extends Sensor {
    private float minIntensity;
    private float maxIntensity;
    private Thread sensorThread;
    private boolean isRunning;
    private float currentIntensity;

    public LightSensor(int sensorID, float minIntensity, float maxIntensity) {
        super(sensorID, "Light");
        this.minIntensity = minIntensity;
        this.maxIntensity = maxIntensity;
        this.isRunning = false;
        this.currentIntensity = 0.0f; // Start at dark
        System.out.println("DEBUG: LightSensor initialized - ID: " + sensorID + 
                         ", Min: " + minIntensity + ", Max: " + maxIntensity);
    }

    @Override
    public void run() {
        System.out.println("DEBUG: LightSensor run method started");
        sensorThread = Thread.currentThread();
        
        while (!Thread.currentThread().isInterrupted()) {
            try {
                if (!isRunning) {
                    Thread.sleep(1000);
                    continue;
                }
                
                // Let the LightControl control the light intensity
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("DEBUG: LightSensor thread interrupted");
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                System.err.println("DEBUG: Error in LightSensor run: " + e.getMessage());
                e.printStackTrace();
            }
        }
        System.out.println("DEBUG: LightSensor run method exited");
    }

    public float getMinIntensity() {
        return minIntensity;
    }

    public void setMinIntensity(float minIntensity) {
        this.minIntensity = minIntensity;
    }

    public float getMaxIntensity() {
        return maxIntensity;
    }

    public void setMaxIntensity(float maxIntensity) {
        this.maxIntensity = maxIntensity;
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
        return "Light";
    }

    @Override
    protected int generateInitialValue() {
        return 0; // Start at dark
    }

    @Override
    protected void updateValue() {
        // This method is not used as the LightControl controls the value
    }

    public float getCurrent() {
        return currentIntensity;
    }

    public void setCurrent(float intensity) {
        this.currentIntensity = intensity;
    }

    public void setMinLight(float minLight) {
        this.minIntensity = minLight;
        System.out.println("DEBUG: Light sensor min value updated to: " + minLight);
    }
    
    public void setMaxLight(float maxLight) {
        this.maxIntensity = maxLight;
        System.out.println("DEBUG: Light sensor max value updated to: " + maxLight);
    }
}