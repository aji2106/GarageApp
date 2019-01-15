/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parts;

/**
 *
 * @author Ajiri
 */
public class PartMain {
    
    private int ID;
    private String PartName;
    private String Description;
    private int Quantity;
    private String warrentyStartDate;
    private String warrentyEndDate;
    private int vehicleID;
    private int customerID;
    private int bookingID;

    public int getBookingID() {
        return bookingID;
    }
    
    /*Initalising instance variables*/

    public PartMain(int ID, String PartName, String Description, int Quantity,String warrentyStartDate, String warrentyEndDate, int vehicleID, int customerID,int bookingID) {
        this.ID = ID;
        this.PartName = PartName;
        this.Description = Description;
        this.Quantity = Quantity;
        this.warrentyStartDate = warrentyStartDate;
        this.warrentyEndDate = warrentyEndDate;
        this.vehicleID = vehicleID;
        this.customerID = customerID;
        this.bookingID = bookingID;
    }

    public int getID() {
        return ID;
    }

    public String getPartName() {
        return PartName;
    }

    public String getDescription() {
        return Description;
    }

    public int getQuantity() {
        return Quantity;
    }

    

    public String getWarrentyStartDate() {
        return warrentyStartDate;
    }

    public String getWarrentyEndDate() {
        return warrentyEndDate;
    }

    public int getVehicleID() {
        return vehicleID;
    }

    public int getCustomerID() {
        return customerID;
    }
    
    
    
}
