/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package esper;


import model.Controller;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import view.Greenhouse_Intro_GUI;
import com.espertech.esper.client.EPStatement;

/**
 *
 * @author Youssef Negm
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException {
            
            // Disable logging
            Logger.getRootLogger().setLevel(Level.OFF);
            
            // Register events
            Config.registerEvents();
            
            // Create Controller
            final Controller c = new Controller();

            // Create EPL statements
            Config.createStatement("select temp from TempSensorReading")
                    .setSubscriber(new Object() {
                        public void update(int temp) throws InterruptedException  {
                            c.tempSignal(temp);
                        }
                    });

            Config.createStatement("select state from PowerEvent")
                    .setSubscriber(new Object() {
                        public void update(boolean state) {
                            c.setState(state);
                        }
                    });

            // Light sensor EPL statement
            Config.createStatement("select intensity from LightSensorReading")
                    .setSubscriber(new Object() {
                        public void update(float intensity) {
                            c.lightSignal(intensity);
                        }
                    });

            // Humidity sensor EPL statement
            Config.createStatement("select humidity from HumiditySensorReading")
                    .setSubscriber(new Object() {
                        public void update(int humidity) {
                            c.humiditySignal(humidity);
                        }
                    });

            // Water sensor EPL statement
            Config.createStatement("select level from WaterSensorReading")
                    .setSubscriber(new Object() {
                        public void update(float level) {
                            c.waterSignal(level);
                        }
                    });

            // pH sensor EPL statement
            EPStatement pHStatement = Config.createStatement("select pH from pHSensorReading");
            pHStatement.addListener((newData, oldData) -> {
                float pH = (float) newData[0].get("pH");
                c.pHSignal(pH);
            });
                
            // Show loading screen
            double numDots;
            String loadingText;
            Greenhouse_Intro_GUI gf = new Greenhouse_Intro_GUI();
            gf.setVisible(true);
            Thread.sleep(100);
            for (int i = 0; i <= 100; i++) {
                Thread.sleep(40);
                gf.ProgressBar.setValue(i);

                numDots = Math.floor(i / 10) % 4;
                loadingText = "تحميل" + ".".repeat((int) numDots);
                gf.Loading.setText(loadingText);

                if (i == 100) {
                    Thread.sleep(800);
                    gf.dispose();  // First dispose the loading screen
                    c.showHomeGUI(); // Then show the home GUI
                }
            }
    }
}
