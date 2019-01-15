/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parts;

import java.sql.Date;

/**
 *
 * @author Ajiri
 */
public class PartInventory {
    
    private int ID;
    private String PartName;
    private String Description;
    private int Cost;
    private int Count;
    private String DeliveryDate;

    public PartInventory(int ID, String PartName, String Description, int Cost, int Count, String DeliveryDate) {
        this.ID = ID;
        this.PartName = PartName;
        this.Description = Description;
        this.Cost = Cost;
        this.Count = Count;
        this.DeliveryDate = DeliveryDate;
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

    public int getCost() {
        return Cost;
    }

    public int getCount() {
        return Count;
    }

    public String getDeliveryDate() {
        return DeliveryDate;
    }
    
    
}
