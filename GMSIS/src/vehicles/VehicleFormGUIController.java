/*;
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vehicles;
/**
 *
 * @author ariag
 */
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import common.Database;
import common.Warranty;
import customers.TableCustomer;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import users.User;


public class VehicleFormGUIController implements Initializable {

    @FXML
    private JFXDatePicker input_warranty_expiry;

    @FXML
    private JFXComboBox<String> input_vehicle_template;
    
    @FXML
    private JFXComboBox<String> input_vehicle_owner;

    @FXML
    private JFXTextField input_vehicle_classification;

    @FXML
    private JFXTextField input_vehicle_registration;

    @FXML
    private JFXTextField input_vehicle_model;

    @FXML
    private JFXTextField input_vehicle_make;

    @FXML
    private JFXTextField input_vehicle_fuel;

    @FXML
    private JFXTextField input_vehicle_colour;

    @FXML
    private JFXTextField input_vehicle_mileage;

    @FXML
    private JFXTextField input_vehicle_engine;

    @FXML
    private JFXDatePicker input_vehicle_mot;

    @FXML
    private JFXDatePicker input_vehicle_service;

    @FXML
    private JFXButton button_save;

    @FXML
    private JFXButton button_cancel;

    @FXML
    private JFXTextField input_warranty_name;

    @FXML
    private JFXTextField input_warranty_address;
    
    @FXML
    private Label form_title;

    private static final Database DB = new Database();
    private final User loggedInUser;
    private boolean isEdit = false;
    private Vehicle toEdit = null;
    private VehicleRegistry registry_vehicle;

    public VehicleFormGUIController(User loggedInUser, Vehicle toEdit) {
        this.loggedInUser = loggedInUser;
        this.isEdit = (toEdit != null);
        this.toEdit = toEdit;
        this.registry_vehicle = new VehicleRegistry();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {     
            Connection connection = DriverManager.getConnection("jdbc:sqlite:data.db");
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select * from VehicleTemplate");
            while(rs.next()) {
                this.input_vehicle_template.getItems().add(rs.getString("id"));
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        
        try {        
            Connection connection = DriverManager.getConnection("jdbc:sqlite:data.db");
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select * from Customer");
            while(rs.next()) {
                this.input_vehicle_owner.getItems().add(rs.getString("id"));
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        
        if(this.isEdit) {
            // We are going to be editing a vehicle record, so we setup the information
            // that is already existant in the database for that vehicle
            this.form_title.setText("Edit Vehicle Record");
            
            this.input_vehicle_classification.setText(this.toEdit.getClassification().getValue());
            this.input_vehicle_colour.setText(this.toEdit.getColour().getValue());
            this.input_vehicle_engine.setText(this.toEdit.getEngineSize().getValue());
            this.input_vehicle_fuel.setText(this.toEdit.getFuelType().getValue());
            this.input_vehicle_make.setText(this.toEdit.getMake().getValue());
            this.input_vehicle_mileage.setText(Integer.toString(this.toEdit.getMileage()));
            this.input_vehicle_model.setText(this.toEdit.getModel().getValue());
            this.input_vehicle_registration.setText(this.toEdit.getRegistration().getValue());
            
            this.input_vehicle_mot.setValue(this.toEdit.getMotRenewalDate().toLocalDate());
            this.input_vehicle_service.setValue(this.toEdit.getLastService().toLocalDate());
            
            this.input_vehicle_owner.getSelectionModel().select(String.valueOf(this.toEdit.getCustomerID()));
            
            int warrantyID = this.toEdit.getWarrantyID();
            if(warrantyID != -1) {
                Warranty currentVehicleWarranty = this.registry_vehicle.getWarrantyById(this.toEdit.getWarrantyID());
                this.input_warranty_name.setText(currentVehicleWarranty.getCompanyName());
                this.input_warranty_address.setText(currentVehicleWarranty.getCompanyAddress());
                this.input_warranty_expiry.setValue(currentVehicleWarranty.getExpiryDate().toLocalDate());
            }
            
        } else {
            // We are not editing a previous record of a vehicle, and as such we
            // leave the inputs blank
            this.form_title.setText("Add Vehicle Record");
        }
        
    }
    
    @FXML
    void add_template_data(ActionEvent event) throws IOException {
        try {
            String templateID = this.input_vehicle_template.getSelectionModel().getSelectedItem();
            Connection connection = DriverManager.getConnection("jdbc:sqlite:data.db");
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select * from VehicleTemplate where id=" + templateID);
            while(rs.next()) {
                this.input_vehicle_model.setText(rs.getString("model"));
                this.input_vehicle_make.setText(rs.getString("make"));
                this.input_vehicle_classification.setText(rs.getString("classification"));
                this.input_vehicle_fuel.setText(rs.getString("fuelType"));
                this.input_vehicle_engine.setText(rs.getString("engineSize"));
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    @FXML
    void cancel(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/guis/VehicleGUI.fxml"));
        VehicleGUIController c = new vehicles.VehicleGUIController(loggedInUser);
        loader.setController(c);
        Parent root = (Parent) loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) button_cancel.getScene().getWindow();
        stage.setTitle("GMSIS | Vehicle Records");
        stage.setScene(scene);
        stage.setResizable(true);
        stage.show();
    }

    @FXML
    void save(ActionEvent event) throws IOException {
        if(!isEdit) {
            // It does not matter what we set the id as here, as it is automatically and appropriately modified when added to the database.
            
            // Check if a warranty is being added
            int warrantyID;
            String warrantyName = this.input_warranty_name.getText();
            if(warrantyName.equals("")) {
                // Then we have no warranty being added
                warrantyID = -1;
            } else {
                // Format warranty expiry date correctly
                Date warrantyExpiryDate = Date.valueOf(this.input_warranty_expiry.getValue());
                Warranty warrantyToAdd = new Warranty(-1, this.input_warranty_name.getText(), this.input_warranty_address.getText(), warrantyExpiryDate);
                warrantyID = this.registry_vehicle.addWarranty(warrantyToAdd);
            }

            // Format vehicle dates correctly
            Date motRenewalDate = Date.valueOf(this.input_vehicle_mot.getValue());
            Date lastServiceDate = Date.valueOf(this.input_vehicle_service.getValue());
            
            Vehicle toAdd = new Vehicle(-1, Integer.parseInt(this.input_vehicle_owner.getSelectionModel().getSelectedItem()), warrantyID, Integer.parseInt(this.input_vehicle_mileage.getText()), this.input_vehicle_classification.getText(), this.input_vehicle_registration.getText(),
                this.input_vehicle_model.getText(), this.input_vehicle_make.getText(), this.input_vehicle_fuel.getText(), this.input_vehicle_colour.getText(), this.input_vehicle_engine.getText(), 
                motRenewalDate, lastServiceDate);
            
            this.registry_vehicle.addVehicle(toAdd);
        } else {
            int toEditID = toEdit.getID();
            int warrantyID = toEdit.getWarrantyID();
            // for if, we are editing a vehicle that doesnt have a warranty but one is added, then we need to make a new id for warranty
            String warrantyName = this.input_warranty_name.getText();
            if(warrantyName.equals("")) {
                // Then we have no warranty being added
                warrantyID = -1;
            } else {
                // Format warranty expiry date correctly
                Date warrantyExpiryDate = Date.valueOf(this.input_warranty_expiry.getValue());
                Warranty newData = new Warranty(-1, this.input_warranty_name.getText(), this.input_warranty_address.getText(), warrantyExpiryDate);
                if(warrantyID == -1) {
                    warrantyID = this.registry_vehicle.addWarranty(newData);
                } else {
                    this.registry_vehicle.editWarranty(warrantyID, newData);
                }
            }
            // Format vehicle dates correctly
            Date motRenewalDate = Date.valueOf(this.input_vehicle_mot.getValue());
            Date lastServiceDate = Date.valueOf(this.input_vehicle_service.getValue());
            
            Vehicle editedData = new Vehicle(-1, Integer.parseInt(this.input_vehicle_owner.getSelectionModel().getSelectedItem()), warrantyID, Integer.parseInt(this.input_vehicle_mileage.getText()), this.input_vehicle_classification.getText(), this.input_vehicle_registration.getText(),
                this.input_vehicle_model.getText(), this.input_vehicle_make.getText(), this.input_vehicle_fuel.getText(), this.input_vehicle_colour.getText(), this.input_vehicle_engine.getText(), 
                motRenewalDate, lastServiceDate);
            
            this.registry_vehicle.editVehicle(toEditID, editedData);
        }
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/guis/VehicleGUI.fxml"));
        VehicleGUIController c = new vehicles.VehicleGUIController(loggedInUser);
        loader.setController(c);
        Parent root = (Parent) loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) button_cancel.getScene().getWindow();
        stage.setTitle("GMSIS | Vehicle Records");
        stage.setScene(scene);
        stage.setResizable(true);
        stage.show();
    }

}