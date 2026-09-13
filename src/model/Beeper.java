/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Youssef Negm
 */
public class Beeper {

    public void beep() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                
                    System.out.println("Beep!");
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Beeper.class.getName()).log(Level.SEVERE, null, ex);
                    }
                
            }
        }).start();
    }

}
