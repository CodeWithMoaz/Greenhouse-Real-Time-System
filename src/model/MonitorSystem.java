/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import model.Sensor;
import java.util.ArrayList;

/**
 *
 * @author Moaz
 */
public class MonitorSystem {
    
    private int monitorID;
    private String status;
    private ArrayList<Sensor> sensors;
    
    
    public void analyzeData(){};
    public void sendAlert(Sensor s){};
    public void notifyController(Sensor s,boolean b){};

    public MonitorSystem(int monitorID, String status, ArrayList<Sensor> sensors) {
        this.monitorID = monitorID;
        this.status = status;
        this.sensors = sensors;
    }

    public int getMonitorID() {
        return monitorID;
    }

    public void setMonitorID(int monitorID) {
        this.monitorID = monitorID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<Sensor> getSensors() {
        return sensors;
    }

    public void setSensors(ArrayList<Sensor> sensors) {
        this.sensors = sensors;
    }
    
    
}
