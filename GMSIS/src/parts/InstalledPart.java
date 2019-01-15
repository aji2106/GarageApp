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
public class InstalledPart {
    
    private int ID;
    private String PartName;
    private String Description;
    private int InstalledQuantity;
    private int AvilableQuantity;
    private int Cost;

    public InstalledPart(int ID, String PartName, String Description, int InstalledQuantity, int AvilableQuantity, int Cost) {
        this.ID = ID;
        this.PartName = PartName;
        this.Description = Description;
        this.InstalledQuantity = InstalledQuantity;
        this.AvilableQuantity = AvilableQuantity;
        this.Cost = Cost;
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

    public int getInstalledQuantity() {
        return InstalledQuantity;
    }

    public int getAvilableQuantity() {
        return AvilableQuantity;
    }

    public int getCost() {
        return Cost;
    }
   
    
}
