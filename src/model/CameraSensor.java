package model;

import java.util.logging.Level;
import java.util.logging.Logger;

public class CameraSensor extends Sensor {
    private Thread sensorThread;
    private boolean isRunning;
    private boolean isCropReady;
    private int readyTimeSeconds;
    private long startTime;
    private String cropType;

    public CameraSensor(int sensorID, String cropType) {
        super(sensorID, "Camera");
        this.isRunning = false;
        this.isCropReady = false;
        this.cropType = cropType;
        this.readyTimeSeconds = getReadyTimeForCrop(cropType);
        this.startTime = 0;
    }

    private int getReadyTimeForCrop(String cropType) {
        switch (cropType) {
            case "بطيخ":
                return 25; 
            case "بطاطس":
                return 30; 
            case "شمام":
                return 23;  
            case "كيوي":
                return 27; 
            default:
                return 25; 
        }
    }

    @Override
    public void run() {
        sensorThread = Thread.currentThread();
        
        while (!Thread.currentThread().isInterrupted()) {
            try {
                if (!isRunning) {
                    Thread.sleep(1000);
                    continue;
                }
                
                if (startTime == 0) {
                    startTime = System.currentTimeMillis();
                }
                
                long currentTime = System.currentTimeMillis();
                long elapsedSeconds = (currentTime - startTime) / 1000;
                
                if (elapsedSeconds >= readyTimeSeconds && !isCropReady) {
                    isCropReady = true;
                }
                
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                System.err.println("Error in CameraSensor run: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public Thread getSensorThread() {
        return sensorThread;
    }

    public boolean isIsRunning() {
        return isRunning;
    }

    public void setIsRunning(boolean isRunning) {
        if (this.isRunning != isRunning) {
            this.isRunning = isRunning;
            
            if (isRunning) {
                isCropReady = false;
                startTime = System.currentTimeMillis();
            }
        }
    }
    
    public void setCropType(String cropType) {
        if (!this.cropType.equals(cropType)) {
            this.cropType = cropType;
            this.readyTimeSeconds = getReadyTimeForCrop(cropType);
            isCropReady = false;
            startTime = 0;
        }
    }

    public boolean isCropReady() {
        return isCropReady;
    }
    
    public void resetCropReadyState() {
        isCropReady = false;
        startTime = 0;
    }
    
    public int getReadyTimeSeconds() {
        return readyTimeSeconds;
    }
    
    public long getElapsedTime() {
        if (startTime == 0) {
            return 0;
        }
        return (System.currentTimeMillis() - startTime) / 1000;
    }
    
    public long getRemainingTime() {
        if (isCropReady) {
            return 0;
        }
        long elapsed = getElapsedTime();
        return elapsed >= readyTimeSeconds ? 0 : readyTimeSeconds - elapsed;
    }

    @Override
    public String getSensorType() {
        return "Camera";
    }

    @Override
    protected int generateInitialValue() {
        return 0;
    }

    @Override
    protected void updateValue() {
    }
} 