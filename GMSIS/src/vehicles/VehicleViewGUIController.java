/*
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
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import common.Database;
import common.Warranty;
import customers.TableBooking;
import customers.TablePart;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
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
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import customers.TableBooking;
import customers.TableCustomer;
import customers.TablePart;
import java.sql.Date;
import java.util.ArrayList;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.util.Callback;
import users.User;

public class VehicleViewGUIController implements Initializable {

    @FXML
    private Label label_vehicle_view;

    @FXML
    private JFXTreeTableView<TablePart> table_parts;

    @FXML
    private TextArea textarea_vehicle_information;

    @FXML
    private JFXTreeTableView<TableBooking> table_bookings;

    @FXML
    private JFXButton btn_back;

    private static final Database DB = new Database();
    private final User loggedInUser;
    private final Vehicle vehicleToDisplay;
    private VehicleRegistry registry_vehicles;
    
    public VehicleViewGUIController(User loggedInUser, Vehicle toDisplay) {
        this.loggedInUser = loggedInUser;
        this.vehicleToDisplay = toDisplay;
        this.registry_vehicles = new VehicleRegistry();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setup_bookings_table();
        setup_parts_table();
        
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
         
        String vehicleInfoToDisplay = "";
        vehicleInfoToDisplay += "OWNER INFORMATION: \n";
        vehicleInfoToDisplay += "Customer id: " + this.vehicleToDisplay.getCustomerID() + ", \n";
        TableCustomer cus = null;
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:data.db");
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select * from Customer where id=" + this.vehicleToDisplay.getCustomerID());
            while (rs.next()) {
                cus = new TableCustomer(Integer.parseInt(rs.getString("id")), rs.getString("firstname") + " " + rs.getString("surname"), rs.getString("type"), rs.getString("address"), rs.getString("postcode"), rs.getString("phone"), rs.getString("email"));
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        vehicleInfoToDisplay += "Customer Name: " + cus.getName().getValue() + ", \n";
        vehicleInfoToDisplay += "Customer Address: " + cus.getAddress().getValue() + ", \n";
        vehicleInfoToDisplay += "Customer Postcode: " + cus.getPostcode().getValue() + ", \n";
        vehicleInfoToDisplay += "Customer Phone: " + cus.getPhone().getValue() + ", \n";
        vehicleInfoToDisplay += "Customer Email: " + cus.getEmail().getValue() + ". \n\n";

        vehicleInfoToDisplay += "WARRANTY INFORMATION: \n";
        if(this.vehicleToDisplay.getWarrantyID() != -1) {
            Warranty displayVehicleWarranty = this.registry_vehicles.getWarrantyById(this.vehicleToDisplay.getWarrantyID());
            vehicleInfoToDisplay += "Warranty ID: " + displayVehicleWarranty.getID() + ",\n";
            vehicleInfoToDisplay += "Company Name: " + displayVehicleWarranty.getCompanyName() + ",\n";
            vehicleInfoToDisplay += "Company Address: " + displayVehicleWarranty.getCompanyAddress() + ",\n";
            String expiryDate = simpleDateFormat.format(displayVehicleWarranty.getExpiryDate());
            vehicleInfoToDisplay += "Expiry Date: " + expiryDate + ". \n\n";  
        } else {
            vehicleInfoToDisplay += "No warranty is active on this vehicle.\n\n";
        }
        
        vehicleInfoToDisplay += "VEHICLE INFORMATION: \n";
        vehicleInfoToDisplay += "Registration: " + this.vehicleToDisplay.getRegistration().getValue() + ", \n";
        vehicleInfoToDisplay += "Classification: " + this.vehicleToDisplay.getClassification().getValue() + ",\n";
        vehicleInfoToDisplay += "Colour: " + this.vehicleToDisplay.getColour().getValue() + ", \n";
        vehicleInfoToDisplay += "Make: " + this.vehicleToDisplay.getMake().getValue() + ", \n";
        vehicleInfoToDisplay += "Model: " + this.vehicleToDisplay.getModel().getValue() + ", \n";
        vehicleInfoToDisplay += "Mileage: " + this.vehicleToDisplay.getMileage() + ", \n";
        vehicleInfoToDisplay += "Engine Size: " + this.vehicleToDisplay.getEngineSize().getValue() + ", \n";
        vehicleInfoToDisplay += "Fuel Type: " + this.vehicleToDisplay.getFuelType().getValue() + ", \n";
        
        String motRenewalDate = simpleDateFormat.format(this.vehicleToDisplay.getMotRenewalDate());
        String lastServiceDate = simpleDateFormat.format(this.vehicleToDisplay.getLastService());
        
        vehicleInfoToDisplay += "MoT Renewal Date: " + motRenewalDate + ", \n";
        vehicleInfoToDisplay += "Date of Last Service: " + lastServiceDate + ". \n";
        
        this.textarea_vehicle_information.setText(vehicleInfoToDisplay);
        this.textarea_vehicle_information.setEditable(false);
        
        this.label_vehicle_view.setText(this.vehicleToDisplay.getRegistration().getValue() + " Information");
    }
    
    @FXML
    void back(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/guis/VehicleGUI.fxml"));
        VehicleGUIController c = new vehicles.VehicleGUIController(loggedInUser);
        loader.setController(c);
        Parent root = (Parent) loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) btn_back.getScene().getWindow();
        stage.setTitle("GMSIS | Vehicle Records");
        stage.setScene(scene);
        stage.setResizable(true);
        stage.show();
    }
    
    
    // private helper functions
    
    
    void setup_bookings_table() {
        // Define vehicle data columns within the table
        // Using the JFXTreeTableView, the columns of the table have to be added programatically instead of within the fxml file
        // The code structure originally comes from Muhammed Afsal Villan of www.genuinecoder.com and his tutorials
        
        // Create date column
        JFXTreeTableColumn<TableBooking, String> date = new JFXTreeTableColumn<>("Date");
        date.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TableBooking, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TableBooking, String> param) {
                // Here we need to case the value of the int result we get from getCustomerID to a read only reference 
                // value so it works with the Integer reference that ObservableValue requires
                return param.getValue().getValue().getDatetimeOfBooking();
            }
        });
        
        // Create bill column
        JFXTreeTableColumn<TableBooking, String> bill = new JFXTreeTableColumn<>("Bill");
        bill.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TableBooking, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TableBooking, String> param) {
                // Here we need to case the value of the int result we get from getCustomerID to a read only reference 
                // value so it works with the Integer reference that ObservableValue requires
                return param.getValue().getValue().getBill();
            }
        });
        
        TreeItem<TableBooking> root = new TreeItem<TableBooking>(new TableBooking("","","","","",""));
        this.table_bookings.getColumns().setAll(date, bill);
        this.table_bookings.setRoot(root);
        this.table_bookings.setShowRoot(false);

        
        ArrayList<TableBooking> bookings = new ArrayList<TableBooking>();
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:data.db");
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select * from Booking where vehicleRegistration='" + this.vehicleToDisplay.getRegistration().getValue()+"'");
            while (rs.next()) {
              bookings.add(new TableBooking(rs.getString("type"), rs.getString("hoursSpent"), rs.getString("vehicleMileage"), rs.getString("vehicleRegistration"), rs.getString("dateOfBooking"), rs.getString("bill")));
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }


        System.out.println("# of bookings: " + bookings.size());
        for (int i = 0; i < bookings.size(); i++) {
            this.table_bookings.getRoot().getChildren().add(new TreeItem<TableBooking>(bookings.get(i)));
        }
    }
    
    void setup_parts_table() {
        // Define vehicle data columns within the table
        // Using the JFXTreeTableView, the columns of the table have to be added programatically instead of within the fxml file
        // The code structure originally comes from Muhammed Afsal Villan of www.genuinecoder.com and his tutorials
        
        // Create date column
        JFXTreeTableColumn<TablePart,String> name = new JFXTreeTableColumn<>("Name");
        name.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TablePart, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TablePart, String> param) {
                return param.getValue().getValue().getName();
            }
        });

        JFXTreeTableColumn<TablePart,String> description = new JFXTreeTableColumn<>("Description");
        description.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TablePart, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TablePart, String> param) {
                return param.getValue().getValue().getDescription();
            }
        });

        JFXTreeTableColumn<TablePart,String> warrantyStart = new JFXTreeTableColumn<>("Warranty Start");
        warrantyStart.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TablePart, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TablePart, String> param) {
                return param.getValue().getValue().getWarrantyStart();
            }
        });

        JFXTreeTableColumn<TablePart,String> warrantyEnd = new JFXTreeTableColumn<>("Warranty End");
        warrantyEnd.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TablePart, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TablePart, String> param) {
                return param.getValue().getValue().getWarrantyEnd();
            }
        });


        TreeItem<TablePart> root = new TreeItem<TablePart>(new TablePart("","","","","",""));
        this.table_parts.getColumns().setAll(name, description, warrantyStart, warrantyEnd);
        this.table_parts.setRoot(root);
        this.table_parts.setShowRoot(false);

        
        ArrayList<TablePart> parts = new ArrayList<TablePart>();
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:data.db");
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select * from Part where vehicleID=" + this.vehicleToDisplay.getID());
            while (rs.next()) {
              parts.add(new TablePart(rs.getString("name"), rs.getString("description"), rs.getString("Quantity"), rs.getString("warrantyStart"), rs.getString("warrantyEnd"), ""));
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }


        System.out.println("# of parts: " + parts.size());
        for (int i = 0; i < parts.size(); i++) {
            this.table_parts.getRoot().getChildren().add(new TreeItem<TablePart>(parts.get(i)));
        }
    }
}