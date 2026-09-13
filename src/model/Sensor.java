package model;

import java.util.Random;

public abstract class Sensor implements Runnable {
    protected int sensorID;
    protected String sensorType;
    protected int currentValue;
    protected boolean isRunning;
    protected Thread sensorThread;
    protected Random random;
    
    public Sensor(int sensorID, String sensorType) {
        this.sensorID = sensorID;
        this.sensorType = sensorType;
        this.random = new Random();
        this.currentValue = generateInitialValue();
        this.isRunning = false;
        System.out.println("DEBUG: Sensor base class initialized - ID: " + sensorID + ", Type: " + sensorType);
    }
    
    public void start() {
        try {
            System.out.println("DEBUG: Attempting to start sensor " + sensorType + " (ID: " + sensorID + ")");
            if (sensorThread == null) {
                isRunning = true;
                sensorThread = new Thread(this);
                sensorThread.setName(sensorType + "-" + sensorID);
                sensorThread.start();
                System.out.println("DEBUG: " + sensorType + " sensor " + sensorID + " thread started successfully");
            } else {
                System.out.println("DEBUG: " + sensorType + " sensor " + sensorID + " thread already exists");
                if (!sensorThread.isAlive()) {
                    System.out.println("DEBUG: " + sensorType + " sensor " + sensorID + " thread is not alive, restarting");
                    isRunning = true;
                    sensorThread = new Thread(this);
                    sensorThread.setName(sensorType + "-" + sensorID);
                    sensorThread.start();
                }
            }
        } catch (Exception e) {
            System.err.println("DEBUG: Error starting sensor " + sensorType + " (ID: " + sensorID + "): " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void stop() {
        try {
            System.out.println("DEBUG: Attempting to stop sensor " + sensorType + " (ID: " + sensorID + ")");
            isRunning = false;
            if (sensorThread != null) {
                sensorThread.interrupt();
                System.out.println("DEBUG: " + sensorType + " sensor " + sensorID + " stopped");
            } else {
                System.out.println("DEBUG: " + sensorType + " sensor " + sensorID + " thread was null, nothing to stop");
            }
        } catch (Exception e) {
            System.err.println("DEBUG: Error stopping sensor " + sensorType + " (ID: " + sensorID + "): " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    public void run() {
        System.out.println("DEBUG: " + sensorType + " sensor " + sensorID + " run method started");
        while (isRunning) {
            try {
                // Generate a new sensor reading
                System.out.println("DEBUG: " + sensorType + " sensor " + sensorID + " updating value");
                updateValue();
                
                // Notify the controller
                System.out.println("DEBUG: " + sensorType + " sensor " + sensorID + " current value: " + currentValue);
                
                // Sleep for some time before the next reading
                System.out.println("DEBUG: " + sensorType + " sensor " + sensorID + " sleeping for 2 seconds");
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                System.out.println("DEBUG: " + sensorType + " sensor " + sensorID + " interrupted");
                break;
            } catch (Exception e) {
                System.err.println("DEBUG: Error in " + sensorType + " sensor " + sensorID + " run method: " + e.getMessage());
                e.printStackTrace();
                // Sleep briefly to avoid tight loop in case of persistent error
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                    break;
                }
            }
        }
        System.out.println("DEBUG: " + sensorType + " sensor " + sensorID + " run method exited");
    }
    
    public int getSensorID() {
        return sensorID;
    }
    
    public String getSensorType() {
        return sensorType;
    }
    
    public int checkValue() {
        return currentValue;
    }
    
    protected abstract int generateInitialValue();
    
    protected abstract void updateValue();
    
    public void calibrate() {
        System.out.println("Calibrating " + sensorType + " sensor " + sensorID);
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
    
    
    
    
}