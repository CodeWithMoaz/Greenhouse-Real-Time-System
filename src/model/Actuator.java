package model;


public abstract class Actuator extends Thread  {
    
    private int actuatorId;
    
    
    public Actuator(int actuatorId) {
        this.actuatorId = actuatorId;
        System.out.println("DEBUG: Actuator base class initialized - ID: " + actuatorId);
    }
    
    @Override
    public void start() {
        try {
            System.out.println("DEBUG: Starting actuator " + getClass().getSimpleName() + " (ID: " + actuatorId + ")");
            super.start();
            System.out.println("DEBUG: Actuator " + getClass().getSimpleName() + " (ID: " + actuatorId + ") started successfully");
        } catch (Exception e) {
            System.err.println("DEBUG: Error starting actuator " + getClass().getSimpleName() + " (ID: " + actuatorId + "): " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    public void run() {
        System.out.println("DEBUG: Actuator " + getClass().getSimpleName() + " (ID: " + actuatorId + ") run method started");
        try {
            // Base class implementation should be overridden
            System.out.println("DEBUG: Actuator base run method was not overridden!");
        } catch (Exception e) {
            System.err.println("DEBUG: Error in actuator " + getClass().getSimpleName() + " (ID: " + actuatorId + ") run method: " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println("DEBUG: Actuator " + getClass().getSimpleName() + " (ID: " + actuatorId + ") run method exited");
    }
    
    public abstract void adjustSetting(boolean state);
    public abstract void check(boolean state);
    
   
    public int getActuatorId() {
        return actuatorId;
    }
    
   
   
    public void setActuatorId(int actuatorId) {
        this.actuatorId = actuatorId;
    }
    
   
  
}