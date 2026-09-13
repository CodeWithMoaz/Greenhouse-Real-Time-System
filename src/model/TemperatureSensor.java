package model;

import java.util.Random;
import esper.Config;
import events.TempSensorReading;

public class TemperatureSensor extends Sensor {
    
    
    private Controller c;
    
    
    private int minTemp;
    private int maxTemp;
    private int current;

    public TemperatureSensor(Controller c, int minTemp, int maxTemp, int sensorID, String sensorType) {
        super(sensorID, sensorType);
        this.c = c;
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.current= 20;
    }
    private int random(int min, int max) {
        
        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }
        
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }
    
    
     public void idle() {
        try {
            if (current > 5) {
                int decrease = random(2, 5);
                current -= decrease;
            } else {
                int increase = random(2, 5);
                current += increase;
            }
            // Send event to Esper
            Config.sendEvent(new TempSensorReading(current));
        } catch (Exception e) {
            System.err.println("Error in idle: " + e.getMessage());
            e.printStackTrace();
        }
    }

     public void raiseTemp(int freq) {
        try {
            current += freq;
            // Send event to Esper
            Config.sendEvent(new TempSensorReading(current));
        } catch (Exception e) {
            System.err.println("Error in raiseTemp: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Controller getC() {
        return c;
    }

    public void setC(Controller c) {
        this.c = c;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getSensorID() {
        return sensorID;
    }

    public void setSensorID(int sensorID) {
        this.sensorID = sensorID;
    }

    public String getSensorType() {
        return sensorType;
    }

    public void setSensorType(String sensorType) {
        this.sensorType = sensorType;
    }

    public int getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(int currentValue) {
        this.currentValue = currentValue;
    }

    public boolean isIsRunning() {
        return isRunning;
    }

    public void setIsRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }

    public Thread getSensorThread() {
        return sensorThread;
    }

    public void setSensorThread(Thread sensorThread) {
        this.sensorThread = sensorThread;
    }

    public Random getRandom() {
        return random;
    }

    public void setRandom(Random random) {
        this.random = random;
    }
    
    
    
    @Override
    protected int generateInitialValue() {
        // Generate an initial temperature value between min and max
        return minTemp + random.nextInt(maxTemp - minTemp + 1);
    }
    
    @Override
    protected void updateValue() {
        try {
            // Simulate temperature fluctuation
            int fluctuation = random.nextInt(5) - 2; // -2 to +2 degrees
            current = Math.max(minTemp, Math.min(maxTemp, current + fluctuation));
            
            // Send event to Esper
            Config.sendEvent(new TempSensorReading(current));
        } catch (Exception e) {
            System.err.println("Error in updateValue: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    public int checkValue() {
        return currentValue;
    }
    
    public int getMinTemp() {
        return minTemp;
    }
    
    public void setMinTemp(int minTemp) {
        this.minTemp = minTemp;
    }
    
    public int getMaxTemp() {
        return maxTemp;
    }
    
    public void setMaxTemp(int maxTemp) {
        this.maxTemp = maxTemp;
    }
    
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                if (!isRunning) {
                    Thread.sleep(1000);
                    continue;
                }
                
                // Temperature is now controlled by HVAC
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                System.err.println("Error in temperature sensor run: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}