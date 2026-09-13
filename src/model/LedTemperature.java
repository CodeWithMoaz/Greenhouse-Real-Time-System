/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Moaz
 */
public class LedTemperature {
    private boolean state;
    private Controller controller;

    public LedTemperature(Controller kettle) {
        this.state = false;
        this.controller = kettle;
    }

    public void setState(boolean state) {
        this.state = state;
//        if (state) {
//            controller.getTgui().getjLabel13().setText("ON");
//        } else {
//            controller.getTgui().getjLabel13().setText("OFF");
//        }

    }
}
