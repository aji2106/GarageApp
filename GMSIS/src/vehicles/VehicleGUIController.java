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
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import common.Database;
import common.HomeGUIController;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.input.InputMethodEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.stage.Stage;
import javafx.util.Callback;
import users.User;

public class VehicleGUIController implements Initializable {

    @FXML
    private JFXTreeTableView<Vehicle> table_vehicle;

    @FXML
    private JFXButton button_vehicle_add;

    @FXML
    private JFXButton button_vehicle_delete;

    @FXML
    private JFXButton button_vehicle_edit;

    @FXML
    private JFXTextField input_vehicle_search;

    @FXML
    private JFXButton button_vehicle_view;

    @FXML
    private JFXButton button_home;
    
    @FXML
    private ToggleGroup searchgroup;
    
    @FXML
    private JFXRadioButton input_radio_search_registration;

    @FXML
    private JFXRadioButton input_radio_search_make;

    @FXML
    private JFXButton button_vehicle_search;

    private static final Database DB = new Database();
    private final User loggedInUser;

    private VehicleRegistry registry_vehicle;
    
    public VehicleGUIController(User loggedInUser) {
        this.loggedInUser = loggedInUser;
        this.registry_vehicle = new VehicleRegistry();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setup_table(this.table_vehicle, false);
        
    }
    
    @FXML
    void add_vehicle(ActionEvent event) throws IOException {
        Stage stage = (Stage) button_vehicle_add.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/guis/VehicleFormGUI.fxml"));
        VehicleFormGUIController c = new VehicleFormGUIController(loggedInUser, null);
        loader.setController(c);
        Parent root = (Parent) loader.load();
        loader.setController(c);
        Scene scene = new Scene(root, 721, 479);
        stage.setTitle("GMSIS | Vehicle Add");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

    }

    @FXML
    void delete_vehicle(ActionEvent event) {
        // Credit for original code structure for confirmation pop up goes to Hasnain
        Vehicle toDelete = this.table_vehicle.getSelectionModel().getSelectedItem().getValue();
        String prompt = "Are you sure you want to delete this vehicle record with " + toDelete.getRegistration().getValue();
        Alert alert = new Alert(Alert.AlertType.WARNING, prompt, ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.YES){
            this.registry_vehicle.deleteVehicle(toDelete);
            this.setup_table(this.table_vehicle, false);
        } 
    }

    @FXML
    void edit_vehicle(ActionEvent event) throws IOException {
        Vehicle toEdit = this.table_vehicle.getSelectionModel().getSelectedItem().getValue();
        
        Stage stage = (Stage) button_vehicle_edit.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/guis/VehicleFormGUI.fxml"));
        VehicleFormGUIController c = new VehicleFormGUIController(loggedInUser, toEdit);
        loader.setController(c);
        Parent root = (Parent) loader.load();
        loader.setController(c);
        Scene scene = new Scene(root, 721, 479);
        stage.setTitle("GMSIS | Vehicle Edit");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    void view_vehicle(ActionEvent event) throws IOException {
        Vehicle toView = this.table_vehicle.getSelectionModel().getSelectedItem().getValue();
        
        Stage stage = (Stage) button_vehicle_edit.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/guis/VehicleViewGUI.fxml"));
        VehicleViewGUIController c = new VehicleViewGUIController(loggedInUser, toView);
        loader.setController(c);
        Parent root = (Parent) loader.load();
        loader.setController(c);
        Scene scene = new Scene(root, 800, 500);
        stage.setTitle("GMSIS | " + toView.getRegistration().getValue() + " Information");
        stage.setScene(scene);
        stage.setResizable(true);
        stage.show();
    }

    @FXML
    void filter_table(ActionEvent event) {
        this.setup_table(this.table_vehicle, true);
    }

    @FXML
    void home(ActionEvent event) throws IOException {
        Stage stage = (Stage) button_home.getScene().getWindow();   
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/guis/HomeGUI.fxml"));
        HomeGUIController c = new common.HomeGUIController(loggedInUser);
        loader.setController(c);
        Parent root = (Parent) loader.load();
        loader.setController(c);
        Scene scene = new Scene(root, 721, 479);
        stage.setTitle("GMSIS | Home");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
    
    // PRIVATE HELPER FUNCTIONS
    
    private void setup_table(JFXTreeTableView<Vehicle> vehicle_table, boolean isFiltered) {
        // Define vehicle data columns within the table
        // Using the JFXTreeTableView, the columns of the table have to be added programatically instead of within the fxml file
        // The code structure originally comes from Muhammed Afsal Villan of www.genuinecoder.com and his tutorials
        
        // Create customer id column
        // TODO return instead, a name from the customer
        JFXTreeTableColumn<Vehicle, String> customerName = new JFXTreeTableColumn<>("Customer Name");
        customerName.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Vehicle, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Vehicle, String> param) {
                // Here we need to case the value of the int result we get from getCustomerID to a read only reference 
                // value so it works with the Integer reference that ObservableValue requires
                int customerID = param.getValue().getValue().getCustomerID();
                String nameResult = "";
                try {
                    Connection connection = DriverManager.getConnection("jdbc:sqlite:data.db");
                    Statement statement = connection.createStatement();
                    ResultSet rs = statement.executeQuery("select * from Customer where id=" + customerID);
                    while (rs.next()) {
                        nameResult += rs.getString("firstname") + " " + rs.getString("surname");
                    }
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
                //return new ReadOnlyObjectWrapper<>(param.getValue().getValue().getCustomerID());
                return new SimpleStringProperty(nameResult);
            }
        });
        
        // Create registration column
        JFXTreeTableColumn<Vehicle, String> registration = new JFXTreeTableColumn<>("Registration");
        registration.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Vehicle, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Vehicle, String> param) {
                return param.getValue().getValue().getRegistration();
            }
        });
        
        // Create classification column
        JFXTreeTableColumn<Vehicle, String> classification = new JFXTreeTableColumn<>("Classification");
        classification.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Vehicle, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Vehicle, String> param) {
                return param.getValue().getValue().getClassification();
            }
        });
        
        // Create make column
        JFXTreeTableColumn<Vehicle, String> make = new JFXTreeTableColumn<>("Make");
        make.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Vehicle, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Vehicle, String> param) {
                return param.getValue().getValue().getMake();
            }
        });
        
        // Create model column
        JFXTreeTableColumn<Vehicle, String> model = new JFXTreeTableColumn<>("Model");
        model.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Vehicle, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Vehicle, String> param) {
                return param.getValue().getValue().getModel();
            }
        });
        
        // Create engine size column
        JFXTreeTableColumn<Vehicle, String> engineSize = new JFXTreeTableColumn<>("Engine Size");
        engineSize.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Vehicle, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Vehicle, String> param) {
                // Here we need to case the value of the int result we get from getCustomerID to a read only reference 
                // value so it works with the Integer reference that ObservableValue requires
                return param.getValue().getValue().getEngineSize();
            }
        });
        
        // Create fuel type column
        JFXTreeTableColumn<Vehicle, String> fuelType = new JFXTreeTableColumn<>("Fuel Type");
        fuelType.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Vehicle, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Vehicle, String> param) {
                // Here we need to case the value of the int result we get from getCustomerID to a read only reference 
                // value so it works with the Integer reference that ObservableValue requires
                return param.getValue().getValue().getFuelType();
            }
        });
        
        // Create fuel type column
        JFXTreeTableColumn<Vehicle, String> colour = new JFXTreeTableColumn<>("Colour");
        colour.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Vehicle, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Vehicle, String> param) {
                // Here we need to case the value of the int result we get from getCustomerID to a read only reference 
                // value so it works with the Integer reference that ObservableValue requires
                return param.getValue().getValue().getColour();
            }
        });
        
        // Create MoT Renewal date tab
        JFXTreeTableColumn<Vehicle,String> motRenewal = new JFXTreeTableColumn<>("MoT Renewal");
        motRenewal.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Vehicle, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Vehicle, String> param) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String motRenewalDate = simpleDateFormat.format(param.getValue().getValue().getMotRenewalDate());
                return new SimpleStringProperty(motRenewalDate);
            }
        });

        // Create last service date tab
        JFXTreeTableColumn<Vehicle,String> lastService = new JFXTreeTableColumn<>("Last Service");
        lastService.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Vehicle, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Vehicle, String> param) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String lastServiceDate = simpleDateFormat.format(param.getValue().getValue().getLastService());
                return new SimpleStringProperty(lastServiceDate);
            }
        });

        // Create current mileage tab
        JFXTreeTableColumn<Vehicle, Integer> currentMileage = new JFXTreeTableColumn<>("Current Mileage");
        currentMileage.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Vehicle, Integer>, ObservableValue<Integer>>() {
            @Override
            public ObservableValue<Integer> call(TreeTableColumn.CellDataFeatures<Vehicle, Integer> param) {
                return new ReadOnlyObjectWrapper<>(param.getValue().getValue().getMileage());
            }
        });
        
        
        Date test = new Date(0);
        TreeItem<Vehicle> root = new TreeItem<Vehicle>(new Vehicle(-1, -1, -1, -1, "", "", "", "", "", "", "", test, test));
        vehicle_table.getColumns().setAll(customerName, registration, classification, colour, make, model, engineSize, fuelType, motRenewal, lastService, currentMileage);
        vehicle_table.setRoot(root);
        vehicle_table.setShowRoot(false);
        
        ArrayList<Vehicle> current_vehicles = new ArrayList<Vehicle>();
        if(isFiltered == true) {
            String filterTerm = this.input_vehicle_search.getText();
            boolean isRegistrationSearch = this.input_radio_search_registration.isSelected();
            boolean isMakeSearch = this.input_radio_search_make.isSelected();
            System.out.println("FilterTerm: " + filterTerm);
            System.out.println("Searching by registration: " + isRegistrationSearch);
            current_vehicles = this.registry_vehicle.getFilteredVehicles(filterTerm, isRegistrationSearch, isMakeSearch);
        } else {
            current_vehicles = this.registry_vehicle.getAllVehicles();
        }
        System.out.println("# of vehicles: " + current_vehicles.size());
        for (int i = 0; i < current_vehicles.size(); i++) {
            vehicle_table.getRoot().getChildren().add(new TreeItem<Vehicle>(current_vehicles.get(i)));
        }
    }

}
