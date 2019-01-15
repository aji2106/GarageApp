/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import java.sql.Date;

/**
 *
 * @author ariag
 */
public class Warranty {
    private int id;
    private String companyName;
    private String companyAddress;
    private Date expiryDate;
    
    public Warranty(int id, String warrantyCompanyName, String warrantyCompanyAddress, Date warrantyExpiryDate) {
        this.id = id;
        this.companyName = warrantyCompanyName;
        this.companyAddress = warrantyCompanyAddress;
        this.expiryDate = warrantyExpiryDate;
    }
    
    public int getID() {
        return this.id;
    }
    
    public String getCompanyName() {
        return this.companyName;
    }
    
    public String getCompanyAddress() {
        return this.companyAddress;
    }
    
    public Date getExpiryDate() {
        return this.expiryDate;
    }
}
