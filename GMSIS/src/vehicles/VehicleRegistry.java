/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vehicles;

import common.Database;
import common.Warranty;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 *
 * @author ariag
 */
public class VehicleRegistry {
    public static Database DB;
    
    public VehicleRegistry() {
        this.DB = new Database();
    }
    
    public ArrayList<Vehicle> getAllVehicles() {
        ArrayList<Vehicle> vehicles = new ArrayList<Vehicle>();
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:data.db");
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select * from Vehicle");
            
            while (rs.next()) {      
                // convert the dates
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                java.util.Date tempMoTRenewalDate = simpleDateFormat.parse(rs.getString("MoTRenewal"));
                java.util.Date tempLastServiceDate = simpleDateFormat.parse(rs.getString("lastService"));
                Date motRenewal = new Date(tempMoTRenewalDate.getTime());
                Date lastService = new Date(tempLastServiceDate.getTime());
                
                int warrantyID;
                String warrantyIdStr = rs.getString("warrantyID");
                if(rs.wasNull()) {
                    warrantyID = -1;
                } else {
                    warrantyID = Integer.parseInt(warrantyIdStr);
                }
                
                // After the dates have been converted and warranty has been checked for null status, we can add the
                // current entry into the vehicle format and add it to the current list of vehicles
                vehicles.add(new Vehicle(Integer.parseInt(rs.getString("id")), Integer.parseInt(rs.getString("customerID")), warrantyID, Integer.parseInt(rs.getString("currentMileage")), 
                        rs.getString("classification"), rs.getString("registration"), rs.getString("model"),  rs.getString("make"), 
                        rs.getString("fuelType"), rs.getString("colour"), rs.getString("engineSize"), motRenewal, lastService));
                System.out.println("Load was called: here is size of vehicles after addition: " + vehicles.size());
            }
            
            return vehicles;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }
    
    // Filters the table, or in other terms searches the vehicles, in regards to the filter Term passed through as a paramter
    // The isRegistration refers to whether or not this is being filtered by registration, isMake for manufacturer, if neither then it is classification
    public ArrayList<Vehicle> getFilteredVehicles(String filterTerm, boolean isRegistration, boolean isMake) {
        ArrayList<Vehicle> results = new ArrayList<Vehicle>();
        
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:data.db");
            Statement statement = connection.createStatement();
            
            String sqlstatement = "select * from Vehicle ";
            if(isRegistration) {
                sqlstatement += "where registration like '%" + filterTerm + "%'";
            } else {
                if(isMake) {
                    sqlstatement += "where make like '%" + filterTerm + "%'";
                } else {
                    sqlstatement += "where classification like '%" + filterTerm + "%'";
                }
            }
            ResultSet rs = statement.executeQuery(sqlstatement);
            
            while (rs.next()) {      
                // convert the dates
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                java.util.Date tempMoTRenewalDate = simpleDateFormat.parse(rs.getString("MoTRenewal"));
                java.util.Date tempLastServiceDate = simpleDateFormat.parse(rs.getString("lastService"));
                Date motRenewal = new Date(tempMoTRenewalDate.getTime());
                Date lastService = new Date(tempLastServiceDate.getTime());
                
                int warrantyID;
                String warrantyIdStr = rs.getString("warrantyID");
                if(rs.wasNull()) {
                    warrantyID = -1;
                } else {
                    warrantyID = Integer.parseInt(warrantyIdStr);
                }
                
                // After the dates have been converted and warranty has been checked for null status, we can add the
                // current entry into the vehicle format and add it to the current list of vehicles
                results.add(new Vehicle(Integer.parseInt(rs.getString("id")), Integer.parseInt(rs.getString("customerID")), warrantyID, Integer.parseInt(rs.getString("currentMileage")), 
                        rs.getString("classification"), rs.getString("registration"), rs.getString("model"),  rs.getString("make"), 
                        rs.getString("fuelType"), rs.getString("colour"), rs.getString("engineSize"), motRenewal, lastService));
                System.out.println("Load was called: here is size of vehicles after addition: " + results.size());
            }
            return results;
        } catch (Exception e) {
            return null;
        }
    }
    
    public void addVehicle(Vehicle toAdd) {
        try {
            String sqlstatement = "insert into Vehicle (customerID, warrantyID, classification, registration, model, make, engineSize, fuelType, colour, MoTRenewal, lastService, currentMileage) values (";
            sqlstatement += "'" + toAdd.getCustomerID() + "',";
            
            if(toAdd.getWarrantyID() == -1) {
                sqlstatement += "null,";
            } else {
                sqlstatement += "'" + toAdd.getWarrantyID() + "',";
            }
            
            sqlstatement += "'" + toAdd.getClassification().getValue() + "',";
            sqlstatement += "'" + toAdd.getRegistration().getValue() + "',";
            sqlstatement += "'" + toAdd.getModel().getValue() + "',";
            sqlstatement += "'" + toAdd.getMake().getValue() + "',";
            sqlstatement += "'" + toAdd.getEngineSize().getValue() + "',";
            sqlstatement += "'" + toAdd.getFuelType().getValue() + "',";
            sqlstatement += "'" + toAdd.getColour().getValue() + "',";
            
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String motRenewalDate = simpleDateFormat.format(toAdd.getMotRenewalDate());
            String lastServiceDate = simpleDateFormat.format(toAdd.getLastService());
            
            sqlstatement += "'" + motRenewalDate + "',";
            sqlstatement += "'" + lastServiceDate + "',";
            sqlstatement += "'" + toAdd.getMileage() + "');";
            System.out.println("SQL Statement attempted: " + sqlstatement);
            DB.executeUpdate(sqlstatement);
            
        } catch (Exception e) {
            System.out.println("Error occured in the vehicle registry add vehicle function:");
            System.err.println(e.getMessage());
        }
    }
    
    public void editVehicle(int vehicleID, Vehicle newData) {
        try {
            String sqlstatement = "UPDATE Vehicle SET ";

            sqlstatement += "customerID='" + newData.getCustomerID() + "', ";
            //sqlstatement += "warrantyID=" + newData.getWarrantyID() + ", ";
            sqlstatement += "warrantyID=";
            if(newData.getWarrantyID() == -1) {
                sqlstatement += "null, ";
            } else {
                sqlstatement += "'" + newData.getWarrantyID() + "', ";
            }
            
            sqlstatement += "currentMileage='" + newData.getMileage() + "', ";
            sqlstatement += "classification='" + newData.getClassification().getValue() + "', ";
            sqlstatement += "registration='" + newData.getRegistration().getValue() + "', ";
            sqlstatement += "model='" + newData.getModel().getValue() + "', ";
            sqlstatement += "make='" + newData.getMake().getValue() + "', ";
            sqlstatement += "engineSize='" + newData.getEngineSize().getValue() + "', ";
            sqlstatement += "fuelType='" + newData.getModel().getValue() + "', ";
            sqlstatement += "colour='" + newData.getColour().getValue() + "', ";

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String motRenewalDate = simpleDateFormat.format(newData.getMotRenewalDate());
            String lastServiceDate = simpleDateFormat.format(newData.getLastService());

            sqlstatement += "MoTRenewal='" + motRenewalDate + "', ";
            sqlstatement += "lastService='" + lastServiceDate + "' ";

            sqlstatement += "WHERE id='" + vehicleID+"'";

            System.out.println("SQL Statement attempted: " + sqlstatement);
            DB.executeUpdate(sqlstatement);
        } catch (Exception e) {
            System.out.println("Error occured in the vehicle registry edit vehicle function:");
            System.err.println(e.getMessage());
        }
    }
    
    public int addWarranty(Warranty toAdd) {
        Connection connection;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:data.db");
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException("Database connection failed!", ex);
        }
        
        String warrantyID;
        int warrantyIDResult = -1; // Set as -1 in case the sql functions cannot return with id that was inserted
        
        Statement insertStatement;
        Statement returnStatement;
        
        String sqlstatement = "insert into Warranty (companyName, companyAddress, expiry) values (";
        sqlstatement += "'" + toAdd.getCompanyName() + "',";
        sqlstatement += "'" + toAdd.getCompanyAddress() + "',"; 
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String expiryDate = simpleDateFormat.format(toAdd.getExpiryDate());
        sqlstatement += "'" + expiryDate + "');";
        
        System.out.println("SQL Statement attempted: " + sqlstatement);
        try {
            insertStatement = connection.createStatement();
            insertStatement.setQueryTimeout(10);
            insertStatement.executeUpdate(sqlstatement);
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        } finally {
            if (connection != null) {
                try {
                    returnStatement = connection.createStatement();
                    returnStatement.setQueryTimeout(10);
                    ResultSet rs = returnStatement.executeQuery("select last_insert_rowid() as LASTID");
                    warrantyID = rs.getString("LASTID");
                    warrantyIDResult = Integer.parseInt(warrantyID);
                    System.out.println(warrantyIDResult);
                    connection.close();
                } catch (SQLException ex) {
                    System.err.println(ex.getMessage());
                }
            }
        }
        return warrantyIDResult;
    }
    
    public void editWarranty(int id, Warranty newData) {
        try {
            String sqlstatement = "UPDATE Warranty SET ";
            sqlstatement += "companyName='" + newData.getCompanyName() + "', ";        
            sqlstatement += "companyAddress='" + newData.getCompanyAddress() + "', ";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String expiryDate = simpleDateFormat.format(newData.getExpiryDate());
            sqlstatement += "expiry='" + expiryDate + "' ";
            sqlstatement += "WHERE id='" + id +"'";
            System.out.println("SQL Statement attempted: " + sqlstatement);
            DB.executeUpdate(sqlstatement);
        } catch (Exception e) {
            System.out.println("Error occured in the vehicle registry edit warranty function:");
            System.err.println(e.getMessage());
        }
    }
    
    public Warranty getWarrantyById(int id) {
        Warranty result = null;
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:data.db");
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select * from Warranty where id="+id);
            
            while (rs.next()) {
                
                // After the dates have been converted and warranty has been checked for null status, we can add the
                // current entry into the vehicle format and add it to the current list of vehicles
                /*new Vehicle(Integer.parseInt(rs.getString("id")), Integer.parseInt(rs.getString("customerID")), warrantyID, Integer.parseInt(rs.getString("currentMileage")), 
                        rs.getString("classification"), rs.getString("registration"), rs.getString("model"),  rs.getString("make"), 
                        rs.getString("fuelType"), rs.getString("colour"), rs.getString("engineSize"), motRenewal, lastService);*/
                
                
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                java.util.Date tempExpiryDate = simpleDateFormat.parse(rs.getString("expiry"));
                Date expiry = new Date(tempExpiryDate.getTime());
                
                result = new Warranty(id, rs.getString("companyName"), rs.getString("companyAddress"), expiry);
                        
                         //public Warranty(int id, String warrantyCompanyName, String warrantyCompanyAddress, Date warrantyExpiryDate) {
            }
            
            return result;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }
    
    public void deleteVehicle(Vehicle toDelete) {
        try {
            DB.executeUpdate("delete from Vehicle where id =" + toDelete.getID());
            // deleting a vehicle should also delete the warranty associated with it
            DB.executeUpdate("delete from Warranty where id =" + toDelete.getWarrantyID());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
