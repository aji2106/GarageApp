/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package users;

/**
 *
 * @author bukharih
 */

import common.Database;

// Just an extension of the User class that lets us do typeof check to see if a user is an admin
public class Admin extends User {

    
    public Admin(int initId, String initPasswordHash, String initFirstname, String initSurname, boolean initAdmin) {
        super(initId, initPasswordHash, initFirstname, initSurname, initAdmin);
    }
    
}
