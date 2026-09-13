package model;

import java.util.logging.Level;
import java.util.logging.Logger;

public class HumiditySensor extends Sensor {
    private int minHumidity;
    private int maxHumidity;
    private Thread sensorThread;
    private boolean isRunning;
    private int currentHumidity;

    public HumiditySensor(int sensorID, int minHumidity, int maxHumidity) {
        super(sensorID, "Humidity");
        this.minHumidity = minHumidity;
        this.maxHumidity = maxHumidity;
        this.isRunning = false;
        this.currentHumidity = 10; // Start at 10%
        System.out.println("DEBUG: HumiditySensor initialized - ID: " + sensorID + 
                         ", Min: " + minHumidity + ", Max: " + maxHumidity);
    }

    @Override
    public void run() {
        System.out.println("DEBUG: HumiditySensor run method started");
        sensorThread = Thread.currentThread();
        
        while (!Thread.currentThread().isInterrupted()) {
            try {
                if (!isRunning) {
                    Thread.sleep(1000);
                    continue;
                }
                
                // Let the Humidifier control the humidity
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("DEBUG: HumiditySensor thread interrupted");
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                System.err.println("DEBUG: Error in HumiditySensor run: " + e.getMessage());
                e.printStackTrace();
            }
        }
        System.out.println("DEBUG: HumiditySensor run method exited");
    }

    public int getMinHumidity() {
        return minHumidity;
    }

    public void setMinHumidity(int minHumidity) {
        this.minHumidity = minHumidity;
    }

    public int getMaxHumidity() {
        return maxHumidity;
    }

    public void setMaxHumidity(int maxHumidity) {
        this.maxHumidity = maxHumidity;
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
        return "Humidity";
    }

    @Override
    protected int generateInitialValue() {
        return 10; // Start at 10%
    }

    @Override
    protected void updateValue() {
        // This method is not used as the Humidifier controls the value
    }

    public int getCurrent() {
        return currentHumidity;
    }

    public void setCurrent(int humidity) {
        this.currentHumidity = humidity;
    }
}