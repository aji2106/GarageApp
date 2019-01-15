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
public class TableBooking extends RecursiveTreeObject<TableBooking>{
    
    private int id;
    StringProperty type;
    StringProperty hoursSpent;
    StringProperty vehicleMileage;
    StringProperty vehicleRegistration;
    StringProperty datetimeOfBooking;
    StringProperty bill;
    StringProperty settled;

    public TableBooking(String type, String hoursSpent, String vehicleMileage, String vehicleRegistration, String datetimeOfBooking, String bill) {
        this.type = new SimpleStringProperty(type);
        this.hoursSpent = new SimpleStringProperty(hoursSpent);
        this.vehicleMileage = new SimpleStringProperty(vehicleMileage);
        this.vehicleRegistration = new SimpleStringProperty(vehicleRegistration);
        this.datetimeOfBooking = new SimpleStringProperty(datetimeOfBooking);
        this.bill = new SimpleStringProperty(bill);
        this.settled = new SimpleStringProperty("0");
    }

    public TableBooking(int id, String type, String hoursSpent, String vehicleMileage, String vehicleRegistration, String datetimeOfBooking, String bill, String settled) {
        this.type = new SimpleStringProperty(type);
        this.hoursSpent = new SimpleStringProperty(hoursSpent);
        this.vehicleMileage = new SimpleStringProperty(vehicleMileage);
        this.vehicleRegistration = new SimpleStringProperty(vehicleRegistration);
        this.datetimeOfBooking = new SimpleStringProperty(datetimeOfBooking);
        this.bill = new SimpleStringProperty(bill);
        if (settled.equals("1")) {
            this.settled = new SimpleStringProperty("True");
        } else {
            this.settled = new SimpleStringProperty("False");
        }
        
        this.id = id;
    }

    public StringProperty getType() {
        return type;
    }

    public StringProperty getHoursSpent() {
        return hoursSpent;
    }

    public StringProperty getVehicleMileage() {
        return vehicleMileage;
    }

    public StringProperty getVehicleRegistration() {
        return vehicleRegistration;
    }

    public StringProperty getDatetimeOfBooking() {
        return datetimeOfBooking;
    }

    public StringProperty getBill() {
        return bill;
    }

    public StringProperty getSettled() {
        return settled;
    }

    public int getId() {
        return id;
    }
}
