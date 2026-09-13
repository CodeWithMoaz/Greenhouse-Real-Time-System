package model;

/**
 * Stores optimal settings for different crops
 */
public class CropSettings {
    private String cropName;
    private float waterMinLevel;
    private float waterMaxLevel;
    private float tempMinLevel;
    private float tempMaxLevel;
    private float humidityMinLevel;
    private float humidityMaxLevel;
    private float phMinLevel;
    private float phMaxLevel;
    private float lightMinLevel;
    private float lightMaxLevel;
    
    public CropSettings(String cropName, 
                        float waterMinLevel, float waterMaxLevel, 
                        float tempMinLevel, float tempMaxLevel, 
                        float humidityMinLevel, float humidityMaxLevel, 
                        float phMinLevel, float phMaxLevel, 
                        float lightMinLevel, float lightMaxLevel) {
        this.cropName = cropName;
        this.waterMinLevel = waterMinLevel;
        this.waterMaxLevel = waterMaxLevel;
        this.tempMinLevel = tempMinLevel;
        this.tempMaxLevel = tempMaxLevel;
        this.humidityMinLevel = humidityMinLevel;
        this.humidityMaxLevel = humidityMaxLevel;
        this.phMinLevel = phMinLevel;
        this.phMaxLevel = phMaxLevel;
        this.lightMinLevel = lightMinLevel;
        this.lightMaxLevel = lightMaxLevel;
    }
    
    public String getCropName() {
        return cropName;
    }
    
    public float getWaterMinLevel() {
        return waterMinLevel;
    }
    
    public float getWaterMaxLevel() {
        return waterMaxLevel;
    }
    
    public float getTempMinLevel() {
        return tempMinLevel;
    }
    
    public float getTempMaxLevel() {
        return tempMaxLevel;
    }
    
    public float getHumidityMinLevel() {
        return humidityMinLevel;
    }
    
    public float getHumidityMaxLevel() {
        return humidityMaxLevel;
    }
    
    public float getPhMinLevel() {
        return phMinLevel;
    }
    
    public float getPhMaxLevel() {
        return phMaxLevel;
    }
    
    public float getLightMinLevel() {
        return lightMinLevel;
    }
    
    public float getLightMaxLevel() {
        return lightMaxLevel;
    }
    
    /**
     * Factory method to get settings for a specific crop
     */
    public static CropSettings getSettingsForCrop(String cropName) {
        switch (cropName) {
            case "بطيخ":
                return new CropSettings("بطيخ", 
                                        15, 70,     // Water min/max
                                        25, 35,     // Temperature min/max
                                        60, 80,     // Humidity min/max 
                                        5.8f, 6.8f, // pH min/max
                                        5, 8);      // Light min/max hours
            case "بطاطس":
                return new CropSettings("بطاطس", 
                                        20, 75,     // Water min/max
                                        15, 25,     // Temperature min/max
                                        70, 85,     // Humidity min/max
                                        5.0f, 6.0f, // pH min/max
                                        6, 9);      // Light min/max hours
            case "شمام":
                return new CropSettings("شمام",
                                        15, 65,     // Water min/max
                                        20, 32,     // Temperature min/max  
                                        65, 75,     // Humidity min/max
                                        6.0f, 7.0f, // pH min/max
                                        6, 8);      // Light min/max hours
            case "كيوي":
                return new CropSettings("كيوي",
                                        25, 80,     // Water min/max
                                        15, 30,     // Temperature min/max
                                        75, 90,     // Humidity min/max
                                        5.5f, 6.5f, // pH min/max
                                        8, 12);     // Light min/max hours
            case "اوقف النظام":
                // For stopping the system, return default settings
                return new CropSettings("Default",
                                        20, 80,     // Water min/max
                                        25, 35,     // Temperature min/max
                                        60, 80,     // Humidity min/max
                                        5.5f, 6.5f, // pH min/max
                                        6, 10);     // Light min/max hours
            default:
                // Default settings if no crop selected or unknown crop
                return new CropSettings("Default",
                                        20, 80,     // Water min/max
                                        25, 35,     // Temperature min/max
                                        60, 80,     // Humidity min/max
                                        5.5f, 6.5f, // pH min/max
                                        6, 10);     // Light min/max hours
        }
    }
} 