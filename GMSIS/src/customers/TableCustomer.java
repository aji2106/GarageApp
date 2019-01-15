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
public class TableCustomer extends RecursiveTreeObject<TableCustomer>{
    
    private int id;
    StringProperty name;
    StringProperty type;
    StringProperty address;
    StringProperty postcode;
    StringProperty phone;
    StringProperty email;

    public TableCustomer(int id, String name, String type, String address, String postcode, String phone, String email) {
        this.id = id;
        this.name = new SimpleStringProperty(name);
        this.type = new SimpleStringProperty(type);
        this.address = new SimpleStringProperty(address);
        this.postcode = new SimpleStringProperty(postcode);
        this.phone = new SimpleStringProperty(phone);
        this.email = new SimpleStringProperty(email);
    }

    public int getId() {
        return id;
    }

    public StringProperty getName() {
        return name;
    }

    public StringProperty getType() {
        return type;
    }

    public StringProperty getAddress() {
        return address;
    }

    public StringProperty getPostcode() {
        return postcode;
    }

    public StringProperty getPhone() {
        return phone;
    }

    public StringProperty getEmail() {
        return email;
    }
    
    
    
}
