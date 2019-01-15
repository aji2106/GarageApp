/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import java.sql.Connection;
import java.sql.DriverManager;
import users.User;
import users.Admin;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.jasypt.util.password.StrongPasswordEncryptor;

public class Authentication {
    
    private static final Database DB = new Database();

    public Authentication(){
        
    }
    
    public User loginUser(int id, String password) {
            try(Connection connection = DriverManager.getConnection("jdbc:sqlite:data.db");
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery("select * from User where id =" + id);
            ) {
                StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
                if (passwordEncryptor.checkPassword(password, rs.getString("password"))) {
                    if (rs.getInt("Admin") == 1) {
                        return new Admin(rs.getInt("id"), rs.getString("password"), rs.getString("firstname"), rs.getString("surname"), true);
                    } else {
                        return new User(rs.getInt("id"), rs.getString("password"), rs.getString("firstname"), rs.getString("surname"), false);    
                    }  
                } else {
                    return null;
                }
            }catch(Exception e){
                System.err.println(e.getMessage());
                return null; 
            }
        
        // try {
        //     ResultSet rs = DB.executeQuery("select * from User where id =" + id);
        //     StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
        //     if (passwordEncryptor.checkPassword(password, rs.getString("password"))) {
        //         if (rs.getInt("Admin") == 1) {
        //             return new Admin(rs.getInt("id"), rs.getString("password"), rs.getString("firstname"), rs.getString("surname"), true);
        //         } else {
        //             return new User(rs.getInt("id"), rs.getString("password"), rs.getString("firstname"), rs.getString("surname"), false);    
        //         }  
        //     } else {
        //         return null;
        //     }
            
        // }
        // catch (SQLException ex) {
        //     System.err.println(ex.getMessage());
        //     return null; 
        // }
    }
  
}
