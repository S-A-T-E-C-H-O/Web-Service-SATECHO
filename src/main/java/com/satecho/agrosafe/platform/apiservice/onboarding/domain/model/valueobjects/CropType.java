package com.satecho.agrosafe.platform.apiservice.onboarding.domain.model.valueobjects;

public enum CropType {

    CORN("CORN", "Corn / Maiz", 20.0, 80.0, 0.5, 3.0, 5.5, 7.0, 10.0, 35.0),
    BLUEBERRY("BLUEBERRY", "Blueberry / Arandano", 30.0, 70.0, 0.3, 1.5, 4.0, 5.5, 5.0, 32.0),
    AVOCADO("AVOCADO", "Avocado / Aguacate", 35.0, 75.0, 0.5, 2.5, 5.0, 6.5, 10.0, 30.0),
    WATERMELON("WATERMELON", "Watermelon / Sandia", 25.0, 85.0, 0.5, 3.0, 5.5, 7.0, 18.0, 35.0),
    GRAPE("GRAPE", "Grape / Uva", 25.0, 65.0, 0.5, 2.5, 5.5, 7.0, 10.0, 35.0),
    COTTON("COTTON", "Cotton / Algodon", 20.0, 75.0, 0.5, 4.0, 5.5, 8.0, 15.0, 38.0),
    TOMATO("TOMATO", "Tomato / Tomate", 30.0, 80.0, 1.0, 3.5, 5.5, 7.0, 15.0, 30.0),
    POTATO("POTATO", "Potato / Papa", 25.0, 75.0, 0.5, 2.5, 4.8, 6.5, 7.0, 25.0),
    SOYBEAN("SOYBEAN", "Soybean / Soja", 20.0, 80.0, 0.5, 3.0, 5.5, 7.5, 10.0, 35.0),
    WHEAT("WHEAT", "Wheat / Trigo", 15.0, 70.0, 0.3, 2.5, 5.5, 7.5, 5.0, 30.0),
    RICE("RICE", "Rice / Arroz", 60.0, 100.0, 0.5, 2.0, 5.0, 7.0, 20.0, 38.0),
    LETTUCE("LETTUCE", "Lettuce / Lechuga", 25.0, 75.0, 0.5, 2.5, 6.0, 7.0, 10.0, 25.0),
    COFFEE("COFFEE", "Coffee / Cafe", 35.0, 80.0, 0.5, 2.5, 5.0, 6.5, 15.0, 28.0),
    SUGARCANE("SUGARCANE", "Sugarcane / Cana de azucar", 25.0, 80.0, 0.5, 3.0, 5.0, 8.0, 20.0, 38.0),
    SUNFLOWER("SUNFLOWER", "Sunflower / Girasol", 20.0, 75.0, 0.5, 3.0, 6.0, 7.5, 10.0, 35.0);

    private final String name;
    private final String displayName;
    private final double minMoisture;
    private final double maxMoisture;
    private final double minEc;
    private final double maxEc;
    private final double minPh;
    private final double maxPh;
    private final double minTemperature;
    private final double maxTemperature;

    CropType(String name, String displayName, double minMoisture, double maxMoisture,
             double minEc, double maxEc, double minPh, double maxPh,
             double minTemperature, double maxTemperature) {
        this.name = name;
        this.displayName = displayName;
        this.minMoisture = minMoisture;
        this.maxMoisture = maxMoisture;
        this.minEc = minEc;
        this.maxEc = maxEc;
        this.minPh = minPh;
        this.maxPh = maxPh;
        this.minTemperature = minTemperature;
        this.maxTemperature = maxTemperature;
    }

    public String getName() { return name; }
    public String getDisplayName() { return displayName; }
    public double getMinMoisture() { return minMoisture; }
    public double getMaxMoisture() { return maxMoisture; }
    public double getMinEc() { return minEc; }
    public double getMaxEc() { return maxEc; }
    public double getMinPh() { return minPh; }
    public double getMaxPh() { return maxPh; }
    public double getMinTemperature() { return minTemperature; }
    public double getMaxTemperature() { return maxTemperature; }

    public static ThresholdLimits getDefaultThresholdsFor(CropType cropType) {
        if (cropType == null) throw new IllegalArgumentException("Crop type cannot be null");
        return new ThresholdLimits(
                cropType.minMoisture, cropType.maxMoisture,
                cropType.minEc, cropType.maxEc,
                cropType.minPh, cropType.maxPh,
                cropType.minTemperature, cropType.maxTemperature);
    }
}