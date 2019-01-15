 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customers;

import common.Database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bukharih
 */
public class CustomerRegistry {
    
    private static final Database DB = new Database();

    public CustomerRegistry(){

    }


    // Gets all customers from the database and returns them as an Arraylist
   public ArrayList<TableCustomer> getAllCustomers() throws SQLException {
      try(Connection connection = DriverManager.getConnection("jdbc:sqlite:data.db");
          Statement statement = connection.createStatement();
          ResultSet rs = statement.executeQuery("select * from Customer");
      ) {

        ArrayList<TableCustomer> list = new ArrayList<>();
        while (rs.next()) {
            list.add(new TableCustomer(Integer.parseInt(rs.getString("id")), rs.getString("firstname") + " " + rs.getString("surname"), rs.getString("type"), rs.getString("address"), rs.getString("postcode"), rs.getString("phone"), rs.getString("email")));
        }
        return list;
      
      }catch(Exception e){
          System.err.println(e.getMessage());
          return null;
      }

    }

    // Creates a customer with given fields
    public void createCustomer(String firstname, String surname, String type, String address, String postcode, String phone, String email) {
        try {
            DB.executeUpdate("insert into Customer (firstname, surname, type, address, postcode, phone, email) values ('" + firstname + "','" + surname + "','" + type + "','" + address + "','" + postcode + "','" + phone + "','" + email +  "');");
        } catch (SQLException ex) {
            Logger.getLogger(CustomerRegistry.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
 
    // Updates a customer with given fields
    public void editCustomer(String firstname, String surname, String type, String address, String postcode, String phone, String email, int ID) {
        try {
            DB.executeUpdate("update Customer set type = '"+ type +"', firstname = '" + firstname +"', surname = '" + surname + "', address = '" + address + "', postcode = '" + postcode + "', phone ='" + phone + "', email = '" + email + "' where id = " +ID);
        } catch (SQLException ex) {
            Logger.getLogger(CustomerRegistry.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // Deletes all records associated with the customer
    public void deleteCustomer(TableCustomer customer) {
        try {         
            DB.executeUpdate("delete from Customer where id =" + customer.getId());  
            DB.executeUpdate("delete from Bill where customerID =" + customer.getId());
            DB.executeUpdate("delete from Booking where customerID =" + customer.getId());
            DB.executeUpdate("delete from Part where customerID =" + customer.getId());
            DB.executeUpdate("delete from Vehicle where customerID =" + customer.getId());
        }
        catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }
    
    // Search the database using either firstname or surname
    public ArrayList<TableCustomer> searchName(String name) throws SQLException {
      try(Connection connection = DriverManager.getConnection("jdbc:sqlite:data.db");
          Statement statement = connection.createStatement();
          ResultSet rs = statement.executeQuery("SELECT * FROM Customer WHERE  firstname LIKE '%"+ name +"%' or surname LIKE '%" + name + "%'");
      ) {
        ArrayList<TableCustomer> list = new ArrayList<>();
        while (rs.next()) {
            list.add(new TableCustomer(Integer.parseInt(rs.getString("id")), rs.getString("firstname") + " " + rs.getString("surname"), rs.getString("type"), rs.getString("address"), rs.getString("postcode"), rs.getString("phone"), rs.getString("email")));
        }
        return list;
      
      }catch(Exception e){
          System.err.println(e.getMessage());
          return null;
      }

    }
   
   // Searches customers by their associcated vehicles using a join
    public ArrayList<TableCustomer> searchNumberPlate(String numberPlate) throws SQLException {
      try(Connection connection = DriverManager.getConnection("jdbc:sqlite:data.db");
          Statement statement = connection.createStatement();
          ResultSet rs = statement.executeQuery("SELECT c.* FROM Customer c inner join Vehicle v on c.id = v.customerID WHERE registration LIKE '%" + numberPlate + "%' ;\n" +
"");
      ) {

        ArrayList<TableCustomer> list = new ArrayList<>();
        while (rs.next()) {
            list.add(new TableCustomer(Integer.parseInt(rs.getString("id")), rs.getString("firstname") + " " + rs.getString("surname"), rs.getString("type"), rs.getString("address"), rs.getString("postcode"), rs.getString("phone"), rs.getString("email")));
        }
        return list;
      
      }catch(Exception e){
          System.err.println(e.getMessage());
          return null;
      }

    }

    
}
