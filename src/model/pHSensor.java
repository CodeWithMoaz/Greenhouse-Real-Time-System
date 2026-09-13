package model;

import java.util.logging.Level;
import java.util.logging.Logger;

public class pHSensor extends Sensor {
    private float min_pH;
    private float max_pH;
    private Thread sensorThread;
    private boolean isRunning;
    private float current_pH;

    public pHSensor(int sensorID, float min_pH, float max_pH, String type) {
        super(sensorID, "pH");
        this.min_pH = min_pH;
        this.max_pH = max_pH;
        this.isRunning = false;
        this.current_pH = 7.0f; // Start at neutral pH
        System.out.println("DEBUG: pHSensor initialized - ID: " + sensorID + 
                         ", Min: " + min_pH + ", Max: " + max_pH);
    }

    @Override
    public void run() {
        System.out.println("DEBUG: pHSensor run method started");
        sensorThread = Thread.currentThread();
        
        while (!Thread.currentThread().isInterrupted()) {
            try {
                if (!isRunning) {
                    Thread.sleep(1000);
                    continue;
                }
                
                // Let the NutrientDispenser control the pH
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("DEBUG: pHSensor thread interrupted");
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                System.err.println("DEBUG: Error in pHSensor run: " + e.getMessage());
                e.printStackTrace();
            }
        }
        System.out.println("DEBUG: pHSensor run method exited");
    }

    public float getMin_pH() {
        return min_pH;
    }

    public void setMin_pH(float min_pH) {
        this.min_pH = min_pH;
    }

    public float getMax_pH() {
        return max_pH;
    }

    public void setMax_pH(float max_pH) {
        this.max_pH = max_pH;
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
        return "pH";
    }

    @Override
    protected int generateInitialValue() {
        return 7; // Start at neutral pH
    }

    @Override
    protected void updateValue() {
        // This method is not used as the NutrientDispenser controls the value
    }

    public float getCurrent() {
        return current_pH;
    }

    public void setCurrent(float pH) {
        this.current_pH = pH;
    }

    public void setMinPh(float minPh) {
        this.min_pH = minPh;
        System.out.println("DEBUG: pH sensor min value updated to: " + minPh);
    }
    
    public void setMaxPh(float maxPh) {
        this.max_pH = maxPh;
        System.out.println("DEBUG: pH sensor max value updated to: " + maxPh);
    }
}