/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vehicles;

// Import
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import java.sql.Date;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
/**
 *
 * @author ariag
 */
public class Vehicle extends RecursiveTreeObject<Vehicle> {
    private int id;
    private int customerID;
    private int warrantyID;
    
    private int mileage;
    private StringProperty classification;
    private StringProperty registration;
    private StringProperty model;
    private StringProperty make;
    private StringProperty fuelType;
    private StringProperty colour;
    private StringProperty engineSize;
    private Date motRenewalDate;
    private Date lastService;
    
    Vehicle(int id, int customerID, int warrantyID, int mileage, VehicleTemplate template, String registration, String colour, Date motRenewalDate, Date lastService) {
        this.id = id;
        this.customerID = customerID;
        this.warrantyID = warrantyID;
        this.classification = new SimpleStringProperty(template.getClassification());
        this.model =  new SimpleStringProperty(template.getModel());
        this.make =  new SimpleStringProperty(template.getMake());
        this.engineSize =  new SimpleStringProperty(template.getEngineSize());
        this.fuelType =  new SimpleStringProperty(template.getFuelType());
        this.registration = new SimpleStringProperty(registration);
        this.colour = new SimpleStringProperty(colour);
        this.mileage = mileage;
        this.motRenewalDate = motRenewalDate;
        this.lastService = lastService;
    }
    
    Vehicle(int id, int customerID, int warrantyID, int mileage, String classification, String registration, String model, String make, String fuelType, 
            String colour, String engineSize, Date motRenewalDate, Date lastService) {
        this.id = id;
        this.customerID = customerID;
        this.warrantyID = warrantyID;
        this.classification = new SimpleStringProperty(classification);
        this.model = new SimpleStringProperty(model);
        this.make = new SimpleStringProperty(make);
        this.engineSize = new SimpleStringProperty(engineSize);
        this.fuelType = new SimpleStringProperty(fuelType);
        this.registration = new SimpleStringProperty(registration);
        this.colour = new SimpleStringProperty(colour);
        this.mileage = mileage;
        this.motRenewalDate = motRenewalDate;
        this.lastService = lastService;
    }
    
    public int getID() {
        return this.id;
    }
    
    public int getCustomerID() {
        return this.customerID;
    }
    
    public int getWarrantyID() {
        return this.warrantyID;
    }
    
    public StringProperty getClassification() {
        return this.classification;
    }
    
    public StringProperty getModel() {
        return this.model;
    }
    
    public StringProperty getMake() {
        return this.make;
    }
    
    public StringProperty getEngineSize() {
        return this.engineSize;
    }
    
    public StringProperty getFuelType() {
        return this.fuelType;
    }
    
    public StringProperty getRegistration() {
        return this.registration;
    }
    
    public StringProperty getColour() {
        return this.colour;
    }
    
    public int getMileage() {
        return this.mileage;
    }
    
    public Date getMotRenewalDate() {
        return this.motRenewalDate;
    }
    
    public Date getLastService() {
        return this.lastService;
    }
    
}
