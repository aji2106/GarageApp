/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

/**
 *
 * @author Tanzil
 */
public class ActiveUsersTable {
    private int ID;
    private String surname;
    private String firstname;
    private String admin;
    private int hourlyRate;

    public ActiveUsersTable(int userID,String lastName,String firstName,int adminstration,int rate) {
        if(adminstration == 1)
        {
            admin = "Yes";
        }
        else{
            admin = "No";
        }
        ID = userID;
        surname = lastName;
        firstname = firstName;
        hourlyRate = rate;
    }

    public String getAdmin() {
        return admin;
    }

    public String getFirstname() {
        return firstname;
    }

    public int getHourlyRate() {
        return hourlyRate;
    }

    public int getID() {
        return ID;
    }
    
    public String getSurname() {
        return surname;
    }
}
