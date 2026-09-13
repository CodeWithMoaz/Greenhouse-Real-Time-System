package model;

public class Camera extends Sensor {
    private Boolean ready;
    
    public Camera(int sensorID) {
        super(sensorID, "Camera");
        this.ready = false;
     
    }
    
    @Override
    public int checkValue() {
        // For a camera, this might check if the camera is ready and operational
        return ready ? 1 : 0;
    }
    
    // Getters and setters
    public Boolean isReady() {
        return ready;
    }
    
    public void setReady(Boolean ready) {
        this.ready = ready;
    }
    
    

    @Override
    protected int generateInitialValue() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    protected void updateValue() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}

