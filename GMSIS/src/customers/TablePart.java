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
public class TablePart extends RecursiveTreeObject<TablePart>{
    
    StringProperty name;
    StringProperty description;
    StringProperty quantity;
    StringProperty warrantyStart;
    StringProperty warrantyEnd;
    StringProperty registration;


    public TablePart(String name, String description, String quantity, String warrantyStart, String warrantyEnd, String registration) {
        this.name = new SimpleStringProperty(name);
        this.description = new SimpleStringProperty(description);
        this.quantity = new SimpleStringProperty(quantity);
        this.warrantyStart = new SimpleStringProperty(warrantyStart);
        this.warrantyEnd = new SimpleStringProperty(warrantyEnd);
        this.registration = new SimpleStringProperty(registration);
    }

    public StringProperty getName() {
        return name;
    }

    public StringProperty getDescription() {
        return description;
    }

    public StringProperty getQuantity() {
        return quantity;
    }

    public StringProperty getWarrantyStart() {
        return warrantyStart;
    }

    public StringProperty getWarrantyEnd() {
        return warrantyEnd;
    }

    public StringProperty getRegistration() {
        return registration;
    }
    
    
    
}
