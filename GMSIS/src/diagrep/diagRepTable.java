/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diagrep;

/**
 *
 * @author Tanzil
 */
public class diagRepTable {
    private int customerID;
    private String vehReg;
    private String futureBooking;
    private int bookingID;
    
    
    public diagRepTable(int ID, String reg, String booking,int bookingidentifier) {
        customerID = ID;
        vehReg = reg;
        futureBooking = booking;
        bookingID = bookingidentifier;
    }

    public int getCustomerID() {
        return customerID;
    }

    public String getVehReg() {
        return vehReg;
    }

    public String getFutureBooking() {
       
        return futureBooking;
    }

    public int getBooking() {
        return bookingID;
    }
    
            
}