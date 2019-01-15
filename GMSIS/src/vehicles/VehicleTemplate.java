/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vehicles;

/**
 *
 * @author ariag
 */
public class VehicleTemplate {
    private String model;
    private String make;
    private String classification;
    private String fuelType;
    private String engineSize;
    
    public VehicleTemplate(String model, String make, String classification, String fuelType, String engineSize) {
        this.model = model;
        this.make = make;
        this.classification = classification;
        this.fuelType = fuelType;
        this.engineSize = engineSize;
    }
    
    public String getClassification() {
        return this.classification;
    }
    
    public String getModel() {
        return this.model;
    }
    
    public String getMake() {
        return this.make;
    }
    
    public String getFuelType() {
        return this.fuelType;
    }
    
    public String getEngineSize() {
        return this.engineSize;
    }
}
