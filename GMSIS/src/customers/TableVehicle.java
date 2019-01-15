/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customers;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author bukharih
 */
public class TableVehicle extends RecursiveTreeObject<TableVehicle>{
    
    StringProperty classification;
    StringProperty registration;
    StringProperty model;
    StringProperty make;
    StringProperty engineSize;
    StringProperty fuelType;
    StringProperty colour;
    StringProperty motRenewal;
    StringProperty lastService;
    StringProperty currentMileage;

    public TableVehicle(String classification, String registration, String model, String make, String engineSize, String fuelType, String colour, String motRenewal, String lastService, String currentMileage) {
        this.classification = new SimpleStringProperty(classification);
        this.registration = new SimpleStringProperty(registration);
        this.model = new SimpleStringProperty(model);
        this.make = new SimpleStringProperty(make);
        this.engineSize = new SimpleStringProperty(engineSize);
        this.fuelType = new SimpleStringProperty(fuelType);
        this.colour = new SimpleStringProperty(colour);
        this.motRenewal = new SimpleStringProperty(motRenewal);
        this.lastService = new SimpleStringProperty(lastService);
        this.currentMileage = new SimpleStringProperty(currentMileage);
    }
    
    
    
}
