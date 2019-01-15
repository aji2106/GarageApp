package parts; // Name of module and package

// Imports the following libraries to enable functionality.

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import common.Database;
import common.HomeGUIController;
import diagrep.DiagRepGUIController;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
/*import sun.net.www.MimeTable;*/
import users.User;

public class PartsGUIController implements Initializable {

    private static final Database DB = new Database(); // Establishes connection to SQlite Database

    /*
	Private buttons declared only to be used in PartsPackage
	Following buttons iniliatiesd create a link between GUI and database.

    */
    @FXML
    private JFXTextField partSearchQuery; // Function allowing multiple parts to be searched 

    @FXML
    private JFXRadioButton radio_name; // Radio button used as search criteria.

    @FXML
    private JFXRadioButton radio_number_plate; // Search parts used by number plate

    private boolean searchName = true;

    @FXML
    private Label partName; // Part name meets requirment 3 , prompts the user to enter name for part  

    @FXML
    private Label partDescription; // Part label meets requirment 3 , prompts the user to enter description for part

    @FXML
    private Label partID; // Part lavel meets requirment 3 , prompts the user to enter ID for part

    @FXML
    private Label partCost; // Part label meets requirment 3 , prompts the user to enter cost for part in £

    @FXML
    private Label partInstallDate; // Part label meets requirment 3 , prompts the user to enter name for part

    @FXML
    private Label partWarrantyEndDate;  // Part label meets requirment 6 , authentication link between GUI and database.

    @FXML
    private JFXDatePicker partInstallDateButton; 

    @FXML
    private JFXDatePicker partWarrantyEndDateButton;  // Part button meets requirment 6 , authentication link between GUI and database.

    @FXML
    private JFXButton addPart; // Part button meets requirment 3 , authentication link between GUI and database allowing  the user to enter part.

    @FXML
    private JFXButton editPart;  // Part button meets requirment 3 , authentication link between GUI and database allowing  the user to edit part.

    @FXML
    private JFXTextField enterPartName;  // Part button meets requirment 3 , authentication link between GUI and database allowing  the user to enter part name.

    @FXML
    private JFXTextField enterPartCost;  // Part button meets requirment 3 , authentication link between GUI and database allowing  the user to enter part cost.

    @FXML
    private JFXTextField enterPartID;  // Part button meets requirment 3 , authentication link between GUI and database allowing  the user to enter part ID.

    @FXML
    private JFXTextArea enterPartDesc;  // Part button meets requirment 3 , authentication link between GUI and database allowing  the user to enter description.

    @FXML
    private JFXTextField enterPartQuantity, part_customerId, part_vehicleId, part_quantity, part_quantityAvi; // Parts text field used for installing part in vehicle  

    @FXML
    private Label partsMainButton; // Parts main tab

    @FXML
    private JFXButton btn_home; // Home Button enabling user to go back to home GUI to access other modules

    @FXML
    private JFXComboBox<String> part_bookingId, part_partId; 
    
    @FXML
    private JFXButton universalbookings;
    
    @FXML
    void btn_view_ubookings(ActionEvent event) throws IOException {  // Method linking to Diagnosis and Repairs
     
            
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/guis/DiagRepGUI.fxml"));
        DiagRepGUIController c = new diagrep.DiagRepGUIController(loggedInUser);
        loader.setController(c);
        Parent root = (Parent) loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) universalbookings.getScene().getWindow();
        stage.setTitle("GMSIS | Diagnosis & Repair");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
            
            
          
    }

    @FXML
    private TableView<PartInventory> table_partInv; // Main table displaying part inventory

    @FXML
    private TableView<PartMain> table_partMain; // Main table displaying installed and non installed parts

    @FXML
    private TableView<InstalledPart> table_installedPart; // Table which views installed parts

    private final User loggedInUser; // Displays user who's logged in , it's like a cookie 

    public PartsGUIController(User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    //Redirect to home tab upon user clicking btn_home
    /* Method original source from HomeGUIController - sceneswitcher - credit to Hasnain*/
    @FXML
    void home(ActionEvent event) throws IOException {
        Stage stage = (Stage) btn_home.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/guis/HomeGUI.fxml"));
        HomeGUIController c = new common.HomeGUIController(loggedInUser);
        loader.setController(c);
        Parent root = (Parent) loader.load();
        loader.setController(c);

        Scene scene = new Scene(root, 721, 479);  // sets size of screen

        stage.setTitle("GMSIS | Home"); // name of screen
        stage.setScene(scene);
        stage.setResizable(false);  // limits user control of screen size
        stage.show(); //shows screen
    }

    //Add new part into PartInventory table
    //Meets requirment 3: Each part will have a name, a description and relevant id number (E.g. spark plugs, prop shaft, handbrake cable etc.)
    // Test case: 6.	Add new stock item. Show stock of part increased. also met.

    @FXML
    void btn_add_parts(ActionEvent event) throws IOException {
        try {
            DB.executeUpdate("insert into PartInventory (PartName, Description,Count,Cost,DeliveryDate) values ('" + enterPartName.getText() + "' , '" + enterPartDesc.getText() + "'," + enterPartQuantity.getText() + " ," + enterPartCost.getText() + ",strftime('%d-%m-%Y', date('now')))");
            showAlert("Part Successfully Added");

        } catch (SQLException ex) {
            ex.printStackTrace();
            Logger.getLogger(PartsGUIController.class.getName()).log(Level.SEVERE, null, ex);
        }
        loadPartInventoyTable();
        loadPartMainTable();
        loadInstalledPartTable();
        resetTextBox();

    }

    //Show message in alert box
    public void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(message);
        alert.showAndWait();
    }

    //Intialize all tab
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        loadPartInventoyTable();
        loadPartMainTable();
        loadInstalledPartTable();
        setPartBookingDropdown();
    }

    //Load table of Part Inventory tab
    @FXML
    void loadPartInventoyTable() {

        handlePartInventoryTable(table_partInv);
        try {
            getPartInventoryData(table_partInv);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PartsGUIController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(PartsGUIController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    //Method to intialize table view
    static void handlePartInventoryTable(TableView<PartInventory> tableView) {
        ObservableList<TableColumn<PartInventory, ?>> list = tableView.getColumns();
        for (TableColumn column : list) {
            if (column.getId().equals("ID")) {
                column.setCellValueFactory(new PropertyValueFactory<PartInventory, String>("ID"));
            }
            if (column.getId().equals("PartName")) {
                column.setCellValueFactory(new PropertyValueFactory<PartInventory, String>("PartName"));
            }
            if (column.getId().equals("Description")) {
                column.setCellValueFactory(new PropertyValueFactory<PartInventory, String>("Description"));
            }
            if (column.getId().equals("Cost")) {
                column.setCellValueFactory(new PropertyValueFactory<PartInventory, String>("Cost"));
            }
            if (column.getId().equals("Count")) {
                column.setCellValueFactory(new PropertyValueFactory<PartInventory, String>("Count"));
            }
            if (column.getId().equals("DeliveryDate")) {
                column.setCellValueFactory(new PropertyValueFactory<PartInventory, String>("DeliveryDate"));
            }

        }

    }
     //Method to populate the table of PartInventory tab

    public ObservableList<PartInventory> getPartInventoryData(TableView<PartInventory> tableView) throws ClassNotFoundException, SQLException {
        Connection connection = DriverManager.getConnection("jdbc:sqlite:data.db");
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("select * from PartInventory");
        ObservableList<PartInventory> diagrepList = FXCollections.observableArrayList();
        while (rs.next()) {
            diagrepList.add(new PartInventory(rs.getInt(1), rs.getString(2), rs.getString(6), rs.getInt(3), rs.getInt(4), rs.getString(5)));
        }
        tableView.setItems(diagrepList);
        return diagrepList;
    }

    //Load table of PartMain tab
    @FXML
    void loadPartMainTable() {

        handlePartMainTable(table_partMain);
        try {
            getPartMainData(table_partMain);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PartsGUIController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(PartsGUIController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    //Method to intialize table view  <  > <---- Name of table stored between brackets, If Statements used to view multiple colums.
    static void handlePartMainTable(TableView<PartMain> tableView) {
        ObservableList<TableColumn<PartMain, ?>> list = tableView.getColumns();
        for (TableColumn column : list) {
            if (column.getId().equals("partIDColumn")) {
                column.setCellValueFactory(new PropertyValueFactory<PartInventory, String>("ID"));
            }
            if (column.getId().equals("partNameColumn")) {
                column.setCellValueFactory(new PropertyValueFactory<PartInventory, String>("PartName"));
            }
            if (column.getId().equals("partDescriptionColumn")) {
                column.setCellValueFactory(new PropertyValueFactory<PartInventory, String>("Description"));
            }
            if (column.getId().equals("partStockColumn")) {
                column.setCellValueFactory(new PropertyValueFactory<PartInventory, String>("Quantity"));
            }
            if (column.getId().equals("partInstallDateColumn")) {
                column.setCellValueFactory(new PropertyValueFactory<PartInventory, String>("warrentyStartDate"));
            }
            if (column.getId().equals("partWarrantyEndDateColumn")) {
                column.setCellValueFactory(new PropertyValueFactory<PartInventory, String>("warrentyEndDate"));
            }

            if (column.getId().equals("partVehicleIDColumn")) {
                column.setCellValueFactory(new PropertyValueFactory<PartInventory, String>("vehicleID"));
            }

            if (column.getId().equals("partCustomerIDColumn")) {
                column.setCellValueFactory(new PropertyValueFactory<PartInventory, String>("customerID"));
            }

            if (column.getId().equals("partbookingIDColumn")) {
                column.setCellValueFactory(new PropertyValueFactory<PartInventory, String>("bookingID"));
        }

    }
    }
    
    //Method to populate the table of PartMain tab

    public ObservableList<PartMain> getPartMainData(TableView<PartMain> tableView) throws ClassNotFoundException, SQLException {
        Connection connection = DriverManager.getConnection("jdbc:sqlite:data.db"); // Establishes database connection
        Statement statement = connection.createStatement();
        ResultSet rs;

        if (searchName) { // search query used to search parts used by custoomer ID/name or vehicle ID
            rs = statement.executeQuery("select * from Part where customerid in (select id from customer where firstname  like '%" + partSearchQuery.getText() + "%')");
        } else {
            rs = statement.executeQuery("select * from Part where vehicleid in (select id from vehicle where registration like '%" + partSearchQuery.getText() + "%')");
        }
        ObservableList<PartMain> partMainList = FXCollections.observableArrayList();
        while (rs.next()) {
            partMainList.add(new PartMain(rs.getInt(10), rs.getString(5), rs.getString(6), rs.getInt(7), rs.getString(8), rs.getString(9), rs.getInt(2), rs.getInt(4),rs.getInt(3)));
        }
        tableView.setItems(partMainList);
        return partMainList;
    }

    // Method to set all created booking id into Manage Part Booking tab
    private void setPartBookingDropdown() {
        part_bookingId.getItems().removeAll(part_bookingId.getItems());

        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:data.db");
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery("select id from Booking")) {
            while (rs.next()) {
                part_bookingId.getItems().add(rs.getString("id"));
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

    }

    //Set customer id and vehicle id of selected booking id in Manage Part Booking tab
    @FXML
    private void setCustIdandVehicleId(ActionEvent event) {

        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:data.db");
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery("select customerID,vehicleRegistration from Booking where id =" + part_bookingId.getSelectionModel().getSelectedItem());) {
            while (rs.next()) {

                part_customerId.setText(rs.getString("customerID"));
                part_vehicleId.setText(rs.getString("vehicleRegistration"));
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        part_partId.getItems().removeAll(part_partId.getItems());
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:data.db");
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery("select PartName from PartInventory");) {

            while (rs.next()) {
                part_partId.getItems().add(rs.getString("PartName"));

            }

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

    }

    //Set Available qunatity of part ,after select part into Manage Part Booking tab
    @FXML
    private void setPartQuantity(ActionEvent event) {

        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:data.db");
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery("select PartName,Count from PartInventory where PartName='" + part_partId.getSelectionModel().getSelectedItem() + "'");) {

            while (rs.next()) {
                part_partId.getItems().add(rs.getString("PartName"));
                part_quantityAvi.setText(String.valueOf(rs.getInt("Count")));

            }

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

    }

    //Add part into booking from Manage Part Booking tab
    @FXML
    private void addPart(ActionEvent event) throws SQLException {
        int partId = 0; // relvant part ID number
        String partDesc = null; // partDescription set to null as part is being selected from PartInventory table where description has already been set. Meets requirment 11: At least 20 stock items must be created and integrated with data from other modules. Test case 4 also met.
        int avilQuan = 0;
        int totalQuantity;
        int vehicleId = 0;
        int cost = 0;
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:data.db");
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery("select id,description,Count,Cost from PartInventory where PartName = '" + part_partId.getSelectionModel().getSelectedItem() + "'");) {

            while (rs.next()) {

                partId = rs.getInt("id");
                partDesc = rs.getString("description");
                avilQuan = rs.getInt("Count");
                cost = rs.getInt("Cost");
            }

            rs.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:data.db");
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery("select id from Vehicle where customerID = '" + part_customerId.getText() + "'");) {

            while (rs.next()) {

                vehicleId = rs.getInt("id");

            }

            rs.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        String bookingDate=null;
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:data.db");
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery("select dateOfBooking from Booking where id = " + part_bookingId.getSelectionModel().getSelectedItem() + "");) {

            while (rs.next()) {

               bookingDate= rs.getString("dateOfBooking");
            }

            rs.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        
      System.out.println("Booking date : " +bookingDate);
      String warrentyStartDate = bookingDate.trim().substring(bookingDate.length()-4,bookingDate.length() );
      
      int year = Integer.parseInt(warrentyStartDate) + 1;
      String warrentyEnddate = bookingDate.replace(warrentyStartDate, ""+year);
                
        int enterQuan = Integer.parseInt(part_quantity.getText());
        
        if (enterQuan <= avilQuan) {
            int totalCostPart = enterQuan * cost;
            totalQuantity = avilQuan - Integer.parseInt(part_quantity.getText());
            String s = "insert into Part (PartID, vehicleID,bookingID,customerID,name,description,Quantity,warrantyStart,warrantyEnd) values (" + partId + " , " + vehicleId + "," + part_bookingId.getSelectionModel().getSelectedItem() + " ," + part_customerId.getText() + ",'" + part_partId.getSelectionModel().getSelectedItem() + "','" + partDesc + "' ," + part_quantity.getText() + ",'"+bookingDate+"','"+warrentyEnddate+"')";
            DB.executeUpdate(s);
            
            DB.executeUpdate("update PartInventory set Count = " + totalQuantity + " where id=" + partId);

            DB.executeUpdate("update bill set totalCost = totalCost+"+totalCostPart+" where bookingID="+part_bookingId.getSelectionModel().getSelectedItem());
            
            DB.executeUpdate("update Booking set bill = bill+"+totalCostPart+" where ID="+part_bookingId.getSelectionModel().getSelectedItem());
            
            showAlert("Part Successfully Added");
        } else {
            showAlert("Entered quantity is greater than available quantity");
            showAlert("Please increase stock of part required to continue");
        }
                resetManagePartBooking();
        loadPartInventoyTable();
        loadPartMainTable();
        loadInstalledPartTable();

    }

    /*Delete new part from partInventory table
    Gets the selected part from table_partInv combo box then updates table after delete button is clicked
    Test case 8 met: Delete a stock item. */

    @FXML
    private void delete_part(ActionEvent event) {
        PartInventory partInv = table_partInv.getSelectionModel().getSelectedItem();

        try {
            DB.executeUpdate("delete from PartInventory where id =" + partInv.getID());
            showAlert("Part Successfully Deleted");
            loadPartInventoyTable();
            loadPartMainTable();
            loadInstalledPartTable();
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

    //Add selected part information for edit from PartsInventory Table.
    @FXML
    private void view_part(ActionEvent event) {
        PartInventory partInv = table_partInv.getSelectionModel().getSelectedItem();

        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:data.db");
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery("select * from PartInventory where id = " + partInv.getID());) {

            while (rs.next()) {

                enterPartID.setText(rs.getString("ID"));
                enterPartName.setText(rs.getString("PartName"));
                enterPartDesc.setText(rs.getString("Description"));
                enterPartCost.setText(rs.getString("Cost"));
                enterPartQuantity.setText(rs.getString("Count"));
            }

            addPart.setDisable(true);

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    /*Edit part details
	Test case 7 - Edit the record for a stock item.
	Requirment 8:
	The system user should be able to query the system to get a list of parts used to repair a vehicle along with the vehicle and customer details. 
	They should be able to add, edit and delete parts directly from this list.
	*/
    @FXML
    private void edit_part(ActionEvent event) {
        try {

            DB.executeUpdate("update PartInventory set PartName ='" + enterPartName.getText() + "' , Description ='" + enterPartDesc.getText() + "' ,Count = " + enterPartQuantity.getText() + ",Cost=" + enterPartCost.getText() + " where id = " + enterPartID.getText());
            showAlert("Part Successfully Updated");
            addPart.setDisable(false);
            resetTextBox();
        } catch (SQLException ex) {
            ex.printStackTrace();
            Logger.getLogger(PartsGUIController.class.getName()).log(Level.SEVERE, null, ex);
        }
       loadPartInventoyTable();
       loadPartMainTable();
       loadInstalledPartTable();
    }

    /*search by customer name in PartMain tab
	Requirment 12- The user should be able to search for a part used by partial or full vehicle registration number or customer surname and firstname.
	*/

    @FXML
    void selectSearchByName(ActionEvent event) {
        if (this.searchName) {
            radio_name.setSelected(true);
        } else {
            this.searchName = true;
            radio_number_plate.setSelected(false);
        }
    }

    /*search by vehicle registration number in PartMain tab
     Requirment 12 - The user should be able to search for a part used by partial or full vehicle registration number or customer surname and firstname.
 	*/
    @FXML
    void selectSearchByNumberPlate(ActionEvent event) {
        if (this.searchName) {
            this.searchName = false;
            radio_name.setSelected(false);
        } else {
            radio_number_plate.setSelected(true);
        }
    }

    //Search all parts (installed and non -installed parts)
    @FXML
    void searchParts(ActionEvent event) {
        loadPartMainTable();
    }

    //Load InstalledPart tab table from Parts Main Table
    @FXML
    void loadInstalledPartTable() {

        handleInstalledPartTable(table_installedPart);
        try {
            getInstalledPartData(table_installedPart);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PartsGUIController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(PartsGUIController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    //Method to intialize table view
    static void handleInstalledPartTable(TableView<InstalledPart> tableView) {
        ObservableList<TableColumn<InstalledPart, ?>> list = tableView.getColumns();
        for (TableColumn column : list) {
            if (column.getId().equals("install_part_id")) {
                column.setCellValueFactory(new PropertyValueFactory<PartInventory, String>("ID"));
            }
            if (column.getId().equals("install_part_name")) {
                column.setCellValueFactory(new PropertyValueFactory<PartInventory, String>("PartName"));
            }
            if (column.getId().equals("install_part_desc")) {
                column.setCellValueFactory(new PropertyValueFactory<PartInventory, String>("Description"));
            }
            if (column.getId().equals("install_quantity")) {
                column.setCellValueFactory(new PropertyValueFactory<PartInventory, String>("InstalledQuantity"));
            }
            if (column.getId().equals("avilable_quantity")) {
                column.setCellValueFactory(new PropertyValueFactory<PartInventory, String>("AvilableQuantity"));
            }
            if (column.getId().equals("install_cost")) {
                column.setCellValueFactory(new PropertyValueFactory<PartInventory, String>("Cost"));
            }

        }

    }
    //Method to populate the table OF InstalledPart tabs

    public ObservableList<InstalledPart> getInstalledPartData(TableView<InstalledPart> tableView) throws ClassNotFoundException, SQLException {
        Connection connection = DriverManager.getConnection("jdbc:sqlite:data.db");
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("select distinct PartID , count (Quantity) from Part group by PartId");

        ObservableList<InstalledPart> installedPartList = FXCollections.observableArrayList();
        List<Integer> partIdList = new ArrayList<Integer>();
        List<Integer> installedQuaList = new ArrayList<Integer>();

        while (rs.next()) {
            partIdList.add(rs.getInt("PartID"));
            installedQuaList.add(rs.getInt(2));
        }

        StringBuilder sb = new StringBuilder("select * from PartInventory where ID IN (");
        for (int i = 0, size = partIdList.size(); i < size; i++) {
            sb.append(partIdList.get(i));
            if (i < size - 1) {
                sb.append(",");
            }
        }
        sb.append(")");
        System.out.println("Installed Part Query : "+sb.toString());
        rs = statement.executeQuery(sb.toString());
        int i = 0;
        while (rs.next()) {
            installedPartList.add(new InstalledPart(partIdList.get(i), rs.getString(2), rs.getString(6), installedQuaList.get(i), rs.getInt(4), rs.getInt(3)));
            i++;
        }
        tableView.setItems(installedPartList);
        return installedPartList;
    }

    //Reset text box after add and edit part, allowing multiple parts to be added/edited.
    private void resetTextBox() {
        enterPartName.setText("");
        enterPartDesc.setText("");
        enterPartQuantity.setText("");
        enterPartCost.setText("");

    }

    // Delete new part from partInventory table
    @FXML
    private void delete_part_main(ActionEvent event) {
        PartMain part = table_partMain.getSelectionModel().getSelectedItem();

        int cost = 0;
        try {
            
            try (Connection connection = DriverManager.getConnection("jdbc:sqlite:data.db");
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery("select Cost from PartInventory where PartName = '" + part.getPartName()+"'");) {

            while (rs.next()) {

                
                cost = rs.getInt("Cost");
                
}

            rs.close();

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
            cost = cost*part.getQuantity();
             System.out.println(" Cost " +cost);
            DB.executeUpdate("update PartInventory set count = count+" +part.getQuantity() +" where PartName='"+part.getPartName()+"'");
            
            DB.executeUpdate("delete from part where PartId =" + part.getID() +" and bookingid = "+part.getBookingID());
            
            //DB.executeUpdate("update bill set totalCost = totalCost-"+cost+" where bookingID="+part.getBookingID());
            
            DB.executeUpdate("update Booking set bill = bill-"+cost+" where ID="+part.getBookingID());
            showAlert("Part Successfully Deleted");
            loadPartInventoyTable();
            loadPartMainTable();
            loadInstalledPartTable();
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

    public void resetManagePartBooking(){
        
        part_customerId.setText("");
        part_quantity.setText("");
        part_vehicleId.setText("");
        part_quantityAvi.setText("");
    }
}
