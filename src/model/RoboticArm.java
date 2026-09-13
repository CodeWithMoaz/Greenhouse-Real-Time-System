package model;



public class RoboticArm extends Actuator {
    private int robotID;
    private String taskType;

    // Constructor
    public RoboticArm(int actuatorID, String actuatorType, int robotID, String taskType 
            
            ) {
        super(actuatorID);
        this.robotID = robotID;
        this.taskType = taskType;
    }

    // Getters and Setters
    public int getRobotID() {
        return robotID;
    }

    public void setRobotID(int robotID) {
        this.robotID = robotID;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

  

    
    public void harvestCrop() {
        //System.out.println("Harvesting crop: " + crop.getName());
    }

    @Override
    public void adjustSetting(boolean state) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void check(boolean state) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}