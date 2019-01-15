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
public class User {
    
    private final int id;
    private final String passwordHash;
    private final String firstname;
    private final String surname;
    private final boolean admin;

    
    public User(int initId,  String initPasswordHash, String initFirstname, String initSurname, boolean initAdmin){
      id = initId;
      passwordHash = initPasswordHash;
      firstname = initFirstname;
      surname = initSurname;
      admin = initAdmin;
    }

    public int getId() {
        return id;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getSurname() {
        return surname;
    }
        
}
