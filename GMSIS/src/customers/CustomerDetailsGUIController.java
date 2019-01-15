/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customers;

/**
 *
 * @author bukharih
 */
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import common.Database;
import diagrep.DiagRepGUIController;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.stage.Stage;
import javafx.util.Callback;
import parts.PartsGUIController;
import vehicles.VehicleGUIController;

public class CustomerDetailsGUIController {

    @FXML
    private JFXTreeTableView<TableBooking> table_bookings;

    @FXML
    private JFXTreeTableView<TableVehicle> table_vehicles;

    @FXML
    private JFXTreeTableView<TablePart> table_parts;
    
    @FXML
    private JFXButton btn_settle;

    private static final Database DB = new Database();    

    @FXML
    void initialize() {
        initBookingsTable();
        initVehiclesTable();
        initPartsTable();
    }
    
    // Customer ID which is passed to us by the CustomerGUIController    
    private final int customerID;
    
    private final users.User loggedInUser;

    public CustomerDetailsGUIController(int customerID, users.User loggedInUser) {
        this.customerID = customerID;
        this.loggedInUser = loggedInUser;
    }
   
    // This initializes our table column with headers and setups a value factory for Bookings table
    public void initBookingsTable(){
        JFXTreeTableColumn<TableBooking,String> type = new JFXTreeTableColumn<>("Type");
        type.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TableBooking, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TableBooking, String> param) {
                return param.getValue().getValue().type;
            }
        });

        JFXTreeTableColumn<TableBooking,String> hoursSpent = new JFXTreeTableColumn<>("Hours Spent");
        hoursSpent.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TableBooking, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TableBooking, String> param) {
                return param.getValue().getValue().hoursSpent;
            }
        });
        
        JFXTreeTableColumn<TableBooking,String> vehicleMileage = new JFXTreeTableColumn<>("Vehicle Mileage");
        vehicleMileage.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TableBooking, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TableBooking, String> param) {
                return param.getValue().getValue().vehicleMileage;
            }
        });

        JFXTreeTableColumn<TableBooking,String> vehicleRegistration = new JFXTreeTableColumn<>("Vehicle Registration");
        vehicleRegistration.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TableBooking, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TableBooking, String> param) {
                return param.getValue().getValue().vehicleRegistration;
            }
        });

        JFXTreeTableColumn<TableBooking,String> datetimeOfBooking = new JFXTreeTableColumn<>("Booking Time");
        datetimeOfBooking.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TableBooking, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TableBooking, String> param) {
                return param.getValue().getValue().datetimeOfBooking;
            }
        });


        JFXTreeTableColumn<TableBooking,String> bill = new JFXTreeTableColumn<>("Bill");
        bill.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TableBooking, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TableBooking, String> param) {
                return param.getValue().getValue().bill;
            }
        });

        JFXTreeTableColumn<TableBooking,String> settled = new JFXTreeTableColumn<>("Settled");
        settled.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TableBooking, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TableBooking, String> param) {
                return param.getValue().getValue().settled;
            }
        });

        TreeItem<TableBooking> root = new TreeItem<TableBooking>(new TableBooking(-1,"test","test","test","test","test","test","test"));
        table_bookings.getColumns().setAll(vehicleRegistration, type, datetimeOfBooking, hoursSpent, bill, settled, vehicleMileage);
        
        
        loadBookingsTable();
    }



    // Loads the relavent bookings from the database then displays it in the bookings table
    public void loadBookingsTable(){
        TreeItem<TableBooking> root = new TreeItem<TableBooking>(new TableBooking(-1,"test","test","test","test","test","test","test"));
        table_bookings.setRoot(root);
        table_bookings.setShowRoot(false);
        try(Connection connection = DriverManager.getConnection("jdbc:sqlite:data.db");
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select * from Booking where customerID ="+customerID);
        ) {

          ArrayList<TableBooking> list = new ArrayList<>();
          while (rs.next()) {
              list.add(new TableBooking(Integer.parseInt(rs.getString("id")),rs.getString("type"), rs.getString("hoursSpent"), rs.getString("vehicleMileage"), rs.getString("vehicleRegistration"), rs.getString("timeOfBooking").substring(0,2) + ":" + rs.getString("timeOfBooking").substring(2,4) + " - " + rs.getString("dateOfBooking"), "£" + rs.getString("bill"), rs.getString("settled")));
          }
        for (int i = 0; i < list.size(); i++) {
          table_bookings.getRoot().getChildren().add(new TreeItem<TableBooking>(list.get(i)));
        }

        }catch(Exception e){
            System.err.println(e.getMessage());
        }
    }



    // This initializes our table column with headers and setups a value factory for Vehicles table
    public void initVehiclesTable(){
        JFXTreeTableColumn<TableVehicle,String> classification = new JFXTreeTableColumn<>("Classification");
        classification.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TableVehicle, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TableVehicle, String> param) {
                return param.getValue().getValue().classification;
            }
        });

        JFXTreeTableColumn<TableVehicle,String> registration = new JFXTreeTableColumn<>("Registration");
        registration.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TableVehicle, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TableVehicle, String> param) {
                return param.getValue().getValue().registration;
            }
        });
        
        JFXTreeTableColumn<TableVehicle,String> model = new JFXTreeTableColumn<>("Model");
        model.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TableVehicle, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TableVehicle, String> param) {
                return param.getValue().getValue().model;
            }
        });

        JFXTreeTableColumn<TableVehicle,String> make = new JFXTreeTableColumn<>("Make");
        make.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TableVehicle, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TableVehicle, String> param) {
                return param.getValue().getValue().make;
            }
        });

        JFXTreeTableColumn<TableVehicle,String> engineSize = new JFXTreeTableColumn<>("Engine Size");
        engineSize.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TableVehicle, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TableVehicle, String> param) {
                return param.getValue().getValue().engineSize;
            }
        });


        JFXTreeTableColumn<TableVehicle,String> fuelType = new JFXTreeTableColumn<>("Fuel Type");
        fuelType.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TableVehicle, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TableVehicle, String> param) {
                return param.getValue().getValue().fuelType;
            }
        });

        JFXTreeTableColumn<TableVehicle,String> colour = new JFXTreeTableColumn<>("Colour");
        colour.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TableVehicle, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TableVehicle, String> param) {
                return param.getValue().getValue().colour;
            }
        });

        JFXTreeTableColumn<TableVehicle,String> motRenewal = new JFXTreeTableColumn<>("MoT Renewal");
        motRenewal.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TableVehicle, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TableVehicle, String> param) {
                return param.getValue().getValue().motRenewal;
            }
        });

        JFXTreeTableColumn<TableVehicle,String> lastService = new JFXTreeTableColumn<>("Last Service");
        lastService.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TableVehicle, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TableVehicle, String> param) {
                return param.getValue().getValue().lastService;
            }
        });

        JFXTreeTableColumn<TableVehicle,String> currentMileage = new JFXTreeTableColumn<>("Current Mileage");
        currentMileage.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TableVehicle, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TableVehicle, String> param) {
                return param.getValue().getValue().currentMileage;
            }
        });

        TreeItem<TableVehicle> root = new TreeItem<TableVehicle>(new TableVehicle("test","test","test","test","test","test","test","test","test","test"));
        table_vehicles.getColumns().setAll(registration, classification, make, model, engineSize, fuelType, colour, motRenewal, lastService, currentMileage);
        
        
        loadVehiclesTable();
    }


    // Loads the relavent vehicles from the database then displays it in the vehicles table
    public void loadVehiclesTable(){
        TreeItem<TableVehicle> root = new TreeItem<TableVehicle>(new TableVehicle("test","test","test","test","test","test","test","test","test","test"));
        table_vehicles.setRoot(root);
        table_vehicles.setShowRoot(false);
        try(Connection connection = DriverManager.getConnection("jdbc:sqlite:data.db");
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select * from Vehicle where customerID ="+customerID);
        ) {

          ArrayList<TableVehicle> list = new ArrayList<>();
          while (rs.next()) {
              list.add(new TableVehicle(rs.getString("classification"), rs.getString("registration"), rs.getString("model"), rs.getString("make"), rs.getString("engineSize"), rs.getString("fuelType"), rs.getString("colour"), rs.getString("MoTRenewal"), rs.getString("lastService"), rs.getString("currentMileage")));
          }
        for (int i = 0; i < list.size(); i++) {
          table_vehicles.getRoot().getChildren().add(new TreeItem<TableVehicle>(list.get(i)));
        }

        }catch(Exception e){
            System.err.println(e.getMessage());
        }
    }

    // This initializes our table column with headers and setups a value factory for parts table
    public void initPartsTable(){
        JFXTreeTableColumn<TablePart,String> name = new JFXTreeTableColumn<>("Name");
        name.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TablePart, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TablePart, String> param) {
                return param.getValue().getValue().name;
            }
        });

        JFXTreeTableColumn<TablePart,String> description = new JFXTreeTableColumn<>("Description");
        description.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TablePart, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TablePart, String> param) {
                return param.getValue().getValue().description;
            }
        });
        
        JFXTreeTableColumn<TablePart,String> quantity = new JFXTreeTableColumn<>("Quantity");
        quantity.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TablePart, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TablePart, String> param) {
                return param.getValue().getValue().quantity;
            }
        });

        JFXTreeTableColumn<TablePart,String> warrantyStart = new JFXTreeTableColumn<>("Warranty Start");
        warrantyStart.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TablePart, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TablePart, String> param) {
                return param.getValue().getValue().warrantyStart;
            }
        });

        JFXTreeTableColumn<TablePart,String> warrantyEnd = new JFXTreeTableColumn<>("Warranty End");
        warrantyEnd.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TablePart, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TablePart, String> param) {
                return param.getValue().getValue().warrantyEnd;
            }
        });

        JFXTreeTableColumn<TablePart,String> registration = new JFXTreeTableColumn<>("Registration");
        registration.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TablePart, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TablePart, String> param) {
                return param.getValue().getValue().registration;
            }
        });

        TreeItem<TablePart> root = new TreeItem<TablePart>(new TablePart("test","test","test","test","test","test"));
        table_parts.getColumns().setAll(registration, name, description, quantity, warrantyStart, warrantyEnd);
        
        
        loadPartsTable();
    }


    // Loads the relavent parts from the database then displays it in the parts table
    public void loadPartsTable(){
        TreeItem<TablePart> root = new TreeItem<TablePart>(new TablePart("test","test","test","test","test","test"));
        table_parts.setRoot(root);
        table_parts.setShowRoot(false);
        try(Connection connection = DriverManager.getConnection("jdbc:sqlite:data.db");
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select * from Part join Vehicle on Part.vehicleID = Vehicle.id where Part.customerID ="+customerID);
        ) {

          ArrayList<TablePart> list = new ArrayList<>();
          while (rs.next()) {
              list.add(new TablePart(rs.getString("name"), rs.getString("description"), rs.getString("Quantity"), rs.getString("warrantyStart"), rs.getString("warrantyEnd"), rs.getString("registration")));
          }
        for (int i = 0; i < list.size(); i++) {
          table_parts.getRoot().getChildren().add(new TreeItem<TablePart>(list.get(i)));
        }

        }catch(Exception e){
            System.err.println(e.getMessage());
        }
    }
    

    // Marking a bill as settled   
    @FXML
    void settle_bill(ActionEvent event) {
        TableBooking selected = table_bookings.getSelectionModel().getSelectedItem().getValue();
        if (selected.getSettled().getValue().equals("False")) {
            System.out.println(selected.getId());
            try {
                DB.executeUpdate("UPDATE Booking SET settled = 1 WHERE id = " + selected.getId());
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Bill Settled");
                alert.setHeaderText("Bill has been Settled.");
                alert.showAndWait();
                loadBookingsTable();
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
            }
        } else {
            showAlert("Already Settled.");
        }
            
    }
    
    @FXML
    void new_booking(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/guis/DiagRepGUI.fxml"));
        DiagRepGUIController c = new diagrep.DiagRepGUIController(loggedInUser);
        loader.setController(c);
        Parent root = (Parent) loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) btn_settle.getScene().getWindow();
        stage.setTitle("GMSIS | Diagnosis & Repair");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
    
    @FXML
    void view_vehicle(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/guis/VehicleGUI.fxml"));
        VehicleGUIController c = new vehicles.VehicleGUIController(loggedInUser);
        loader.setController(c);
        Parent root = (Parent) loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) btn_settle.getScene().getWindow();
        stage.setTitle("GMSIS | Vehicle Records");
        stage.setScene(scene);
        stage.setResizable(true);
        stage.show();
    }

    @FXML
    void view_vehicle_parts(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/guis/PartsGUI.fxml"));
        PartsGUIController c = new parts.PartsGUIController(loggedInUser);
        loader.setController(c);
        Parent root = (Parent) loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) btn_settle.getScene().getWindow();
        stage.setTitle("GMSIS | Vehicle Parts");
        stage.setScene(scene);
        stage.setResizable(true);
        stage.show();
    }
    
    // Method for showing an alert box with specified message
    // Credit to Tanzil for this function!    
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(message);
        alert.showAndWait();
    }
    
}
