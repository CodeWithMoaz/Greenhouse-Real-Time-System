package model;

import view.Camera_GUI;
import view.Humidity_GUI;
import view.Light_GUI;
import view.PH_GUI;
import view.Temperature_GUI;
import view.Water_GUI;
import view.Home_GUI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Controller {

    private boolean state = false;
    private String currentCrop = "Default";
    private CropSettings cropSettings;

    private Beeper beeper;
    private TemperatureSensor tempSensor;
    private HVAC tempActuator;
    private HumiditySensor humiditySensor;
    private Humidifier humidifier;
    private pHSensor phSensor;
    private NutrientDispenser nutrientDispenser;
    private WaterLevelSensor waterLevelSensor;
    private WaterFlowControl waterFlowControl;
    private LightSensor lightSensor;
    private LightControl lightControl;
    private CameraSensor cameraSensor;

    private Home_GUI homeGUI;
    private PH_GUI phGUI;
    private Humidity_GUI humidityGUI;
    private Light_GUI lightGUI;
    private Water_GUI waterGUI;
    private Temperature_GUI tempGUI;
    private Camera_GUI cameraGUI;

    public Controller() {
        try {
            initializeGUIs();

            beeper = new Beeper();

            cropSettings = CropSettings.getSettingsForCrop("Default");

            initializeComponents();

            setSystemRunningState(false);

        } catch (Exception e) {
            System.err.println("Exception during Controller initialization: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void initializeComponents() {
        tempSensor = new TemperatureSensor(this,
                (int)cropSettings.getTempMinLevel(),
                (int)cropSettings.getTempMaxLevel(),
                101, "Temperature");
        tempActuator = new HVAC(101, this, 2);

        humiditySensor = new HumiditySensor(2, 
                (int)cropSettings.getHumidityMinLevel(),
                (int)cropSettings.getHumidityMaxLevel());
        humidifier = new Humidifier(2, this, 5);

        phSensor = new pHSensor(3,
                cropSettings.getPhMinLevel(),
                cropSettings.getPhMaxLevel(), "pH");
        nutrientDispenser = new NutrientDispenser(3, this, 5);

        waterLevelSensor = new WaterLevelSensor(3,
                cropSettings.getWaterMinLevel(),
                cropSettings.getWaterMaxLevel());
        waterFlowControl = new WaterFlowControl(3, this, 5);

        lightSensor = new LightSensor(4,
                cropSettings.getLightMinLevel(),
                cropSettings.getLightMaxLevel());
        lightControl = new LightControl(4, this, 5);
        
        cameraSensor = new CameraSensor(5, currentCrop);

        startThreads();
    }

    private void startThreads() {
        if (tempSensor != null) {
            tempSensor.start();
        }

        if (tempActuator != null) {
            tempActuator.start();
        }

        if (humiditySensor != null) {
            humiditySensor.start();
        }

        if (humidifier != null) {
            humidifier.start();
        }

        if (phSensor != null) {
            phSensor.start();
        }

        if (nutrientDispenser != null) {
            nutrientDispenser.start();
        }

        if (waterLevelSensor != null && waterFlowControl != null) {
            Thread waterSensorThread = new Thread(waterLevelSensor);
            Thread waterControlThread = new Thread(waterFlowControl);
            waterSensorThread.start();
            waterControlThread.start();
        }

        if (lightSensor != null && lightControl != null) {
            Thread lightSensorThread = new Thread(lightSensor);
            Thread lightControlThread = new Thread(lightControl);
            lightSensorThread.start();
            lightControlThread.start();
        }
        
        if (cameraSensor != null) {
            Thread cameraSensorThread = new Thread(cameraSensor);
            cameraSensorThread.start();
        }
    }

    public void updateCropSettings(String cropName) {
        if (cropName == null) {
            return; 
        }
        
        currentCrop = cropName;
        cropSettings = CropSettings.getSettingsForCrop(cropName);
        
        if (waterLevelSensor != null) {
            waterLevelSensor.setMinLevel(cropSettings.getWaterMinLevel());
            waterLevelSensor.setMaxLevel(cropSettings.getWaterMaxLevel());
            
            if (waterGUI != null) {
                waterGUI.getMin().setText(String.format("%.1f", cropSettings.getWaterMinLevel()));
                waterGUI.getMax().setText(String.format("%.1f", cropSettings.getWaterMaxLevel()));
            }
        }
        
        if (tempSensor != null) {
            tempSensor.setMinTemp((int) cropSettings.getTempMinLevel());
            tempSensor.setMaxTemp((int) cropSettings.getTempMaxLevel());
        }
        
        if (humiditySensor != null) {
            humiditySensor.setMinHumidity((int) cropSettings.getHumidityMinLevel());
            humiditySensor.setMaxHumidity((int) cropSettings.getHumidityMaxLevel());
        }
        
        if (phSensor != null) {
            phSensor.setMinPh(cropSettings.getPhMinLevel());
            phSensor.setMaxPh(cropSettings.getPhMaxLevel());
        }
        
        if (lightSensor != null) {
            lightSensor.setMinLight(cropSettings.getLightMinLevel());
            lightSensor.setMaxLight(cropSettings.getLightMaxLevel());
        }
        
        if (cameraSensor != null) {
            cameraSensor.setCropType(cropName);
        }
    }

    public void setSystemRunningState(boolean isRunning) {
        state = isRunning;

        if (waterLevelSensor != null) {
            waterLevelSensor.setIsRunning(isRunning);
        }

        if (tempSensor != null) {
            tempSensor.setIsRunning(isRunning);
        }

        if (humiditySensor != null) {
            humiditySensor.setIsRunning(isRunning);
        }

        if (phSensor != null) {
            phSensor.setIsRunning(isRunning);
        }

        if (lightSensor != null) {
            lightSensor.setIsRunning(isRunning);
        }
        
        if (cameraSensor != null) {
            cameraSensor.setIsRunning(isRunning);
        }
    }

    private void initializeGUIs() {
        try {
            homeGUI = new Home_GUI(this);
            phGUI = new PH_GUI(this);
            humidityGUI = new Humidity_GUI(this);
            lightGUI = new Light_GUI(this);
            waterGUI = new Water_GUI(this);
            tempGUI = new Temperature_GUI(this);
            cameraGUI = new Camera_GUI(this);
        } catch (Exception e) {
            System.err.println("Error initializing GUIs: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void showHomeGUI() {
        homeGUI.setVisible(true);
        phGUI.setVisible(false);
        humidityGUI.setVisible(false);
        lightGUI.setVisible(false);
        waterGUI.setVisible(false);
        tempGUI.setVisible(false);
        cameraGUI.setVisible(false);
    }

    public void showPHGUI() {
        if (phGUI != null) {
            phGUI.setVisible(true);
            homeGUI.setVisible(false);
        } else {
            System.err.println("pH GUI is null");
        }
    }

    public void showHumidityGUI() {
        if (humidityGUI != null) {
            humidityGUI.setVisible(true);
            homeGUI.setVisible(false);
        } else {
            System.err.println("Humidity GUI is null");
        }
    }

    public void showLightGUI() {
        lightGUI.setVisible(true);
        homeGUI.setVisible(false);
    }

    public void showWaterGUI() {
        waterGUI.setVisible(true);
        homeGUI.setVisible(false);
    }

    public void showTemperatureGUI() {
        try {
            if (tempGUI != null) {
                homeGUI.setVisible(false);
                tempGUI.setVisible(true);

                tempGUI.repaint();

                if (tempSensor != null) {
                    System.out.println("Current temperature: " + tempSensor.getCurrent());
                }
                if (tempActuator != null) {
                    System.out.println("Checking HVAC state: " + (tempActuator != null ? "exists" : "null"));
                }
            } else {
                System.err.println("Temperature GUI is null, cannot show it");
                System.err.println("Stack trace for debugging:");
                new Exception().printStackTrace();
            }
        } catch (Exception e) {
            System.err.println("Error showing temperature GUI: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void showCameraGUI() {
        cameraGUI.setVisible(true);
        homeGUI.setVisible(false);
        updateCameraGUI();
    }

    public void updateCameraGUI() {
        if (cameraGUI != null && cameraSensor != null) {
            if (cameraSensor.isIsRunning()) {
                cameraGUI.getStatus().setText("نشط");
            } else {
                cameraGUI.getStatus().setText("إيقاف");
            }
            
            if (cameraSensor.isCropReady()) {
                cameraGUI.getCurrent_value().setText("جاهز");
            } else {
                cameraGUI.getCurrent_value().setText("غير جاهز");
            }
            
            cameraGUI.getTime_left().setText(String.valueOf(cameraSensor.getRemainingTime()));
        }
    }

    public void tempSignal(int temp) throws InterruptedException {
        if (tempGUI != null) {
            tempGUI.updateTemperature(temp);
        }
    }

    public void lightSignal(float light) {
        if (lightGUI != null) {
            lightGUI.updateLight(light);
        }
    }

    public void humiditySignal(int humidity) {
        if (humidityGUI != null) {
            humidityGUI.updateHumidity(humidity);
        }
    }

    public void waterSignal(float water) {
        if (waterGUI != null) {
            waterGUI.updateWater(water);
        }
    }

    public void pHSignal(float pH) {
        if (phGUI != null) {
            phGUI.updatePH(pH);
        }
    }

    private ArrayList<Actuator> actuators = new ArrayList<>();
    private Map<Integer, Sensor> sensors = new HashMap<>();

    public void addSensor(Sensor sensor) {
        sensors.put(sensor.getSensorID(), sensor);
    }

    public void addActuator(Actuator actuator) {
        actuators.add(actuator);
    }

    public ArrayList<Actuator> getActuators() {
        return actuators;
    }

    public Map<Integer, Sensor> getSensors() {
        return sensors;
    }

    public void setActuators(ArrayList<Actuator> actuators) {
        this.actuators = actuators;
    }

    public void notifyController(Sensor s, boolean b) {
    }

    public boolean isControllerOn() {
        return state;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public Beeper getBeeper() {
        return beeper;
    }

    public void setBeeper(Beeper beeper) {
        this.beeper = beeper;
    }

    public TemperatureSensor getTempSensor() {
        return tempSensor;
    }

    public void setTempSensor(TemperatureSensor tempSensor) {
        this.tempSensor = tempSensor;
    }

    public HVAC getTempActuator() {
        return tempActuator;
    }

    public void setTempActuator(HVAC tempActuator) {
        this.tempActuator = tempActuator;
    }

    public HumiditySensor getHumiditySensor() {
        return humiditySensor;
    }

    public Humidifier getHumidifier() {
        return humidifier;
    }

    public pHSensor getpHSensor() {
        return phSensor;
    }

    public NutrientDispenser getNutrientDispenser() {
        return nutrientDispenser;
    }

    public WaterLevelSensor getWaterLevelSensor() {
        return waterLevelSensor;
    }

    public WaterFlowControl getWaterFlowControl() {
        return waterFlowControl;
    }

    public LightSensor getLightSensor() {
        return lightSensor;
    }

    public LightControl getLightControl() {
        return lightControl;
    }
    
    public CameraSensor getCameraSensor() {
        return cameraSensor;
    }

    public String getCurrentCrop() {
        return currentCrop;
    }

    public CropSettings getCropSettings() {
        return cropSettings;
    }

    public void resetSystem() {
        setSystemRunningState(false);
        
        cropSettings = CropSettings.getSettingsForCrop("Default");
        currentCrop = "Default";
        
        if (waterLevelSensor != null) {
            waterLevelSensor.setMinLevel(cropSettings.getWaterMinLevel());
            waterLevelSensor.setMaxLevel(cropSettings.getWaterMaxLevel());
            
            if (waterGUI != null) {
                waterGUI.getMin().setText(String.format("%.1f", cropSettings.getWaterMinLevel()));
                waterGUI.getMax().setText(String.format("%.1f", cropSettings.getWaterMaxLevel()));
            }
        }
        
        if (tempSensor != null) {
            tempSensor.setMinTemp((int) cropSettings.getTempMinLevel());
            tempSensor.setMaxTemp((int) cropSettings.getTempMaxLevel());
        }
        
        if (humiditySensor != null) {
            humiditySensor.setMinHumidity((int) cropSettings.getHumidityMinLevel());
            humiditySensor.setMaxHumidity((int) cropSettings.getHumidityMaxLevel());
        }
        
        if (phSensor != null) {
            phSensor.setMinPh(cropSettings.getPhMinLevel());
            phSensor.setMaxPh(cropSettings.getPhMaxLevel());
        }
        
        if (lightSensor != null) {
            lightSensor.setMinLight(cropSettings.getLightMinLevel());
            lightSensor.setMaxLight(cropSettings.getLightMaxLevel());
        }
        
        if (cameraSensor != null) {
            cameraSensor.setCropType("Default");
            cameraSensor.resetCropReadyState();
        }
    }
    
    public void harvestCrop() {
        if (cameraSensor != null && cameraSensor.isCropReady()) {
            cameraSensor.resetCropReadyState();
            
            setSystemRunningState(false);
            
            if (homeGUI != null) {
                homeGUI.resetCropSelection();
                showHomeGUI();
            }
        }
    }
}
