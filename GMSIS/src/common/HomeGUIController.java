package common;

import com.jfoenix.controls.JFXButton;
import customers.CustomerGUIController;
import diagrep.DiagRepGUIController;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import parts.PartsGUIController;
import users.Admin;
import users.User;
import vehicles.VehicleGUIController;

public class HomeGUIController {

    @FXML
    private JFXButton btn_customerAcc;

    @FXML
    private JFXButton btn_vehRec;

    @FXML
    private JFXButton btn_vehParts;
 

    @FXML
    private JFXButton btn_diagRep;
    
     @FXML
    private JFXButton btn_logoutUser;
  

    @FXML
    private JFXButton btn_activeUsers;

    @FXML
    private Text txt_IDStamp;
    
    @FXML
    private Text txt_timeStamp;
    
    private final User loggedInUser;
    
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
   

    public HomeGUIController(User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    @FXML
    public void initialize() {
        txt_IDStamp.setText("Logged in as " + loggedInUser.getFirstname() + " " + loggedInUser.getSurname());
        LocalDateTime now = LocalDateTime.now();
        txt_timeStamp.setText("Logged in at " + dtf.format(now));
        if(loggedInUser instanceof Admin){
//            Admin loggedInAdmin = (Admin) loggedInUser;
            btn_activeUsers.setVisible(true);
            
        }
    }
    
    
    @FXML
    void btn_customer_account(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/guis/CustomerGUI.fxml"));
        CustomerGUIController c = new customers.CustomerGUIController(loggedInUser);
        loader.setController(c);
        Parent root = (Parent) loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) btn_customerAcc.getScene().getWindow();
        stage.setTitle("GMSIS | Customer Accounts");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    void btn_diagrep(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/guis/DiagRepGUI.fxml"));
        DiagRepGUIController c = new diagrep.DiagRepGUIController(loggedInUser);
        loader.setController(c);
        Parent root = (Parent) loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) btn_diagRep.getScene().getWindow();
        stage.setTitle("GMSIS | Diagnosis & Repair");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    void btn_vehicle_parts(ActionEvent event) throws IOException  {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/guis/PartsGUI.fxml"));
        PartsGUIController c = new parts.PartsGUIController(loggedInUser);
        loader.setController(c);
        Parent root = (Parent) loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) btn_vehParts.getScene().getWindow();
        stage.setTitle("GMSIS | Vehicle Parts");
        stage.setScene(scene);
        stage.setResizable(true);
        stage.show();
   }

    
    @FXML
    void btn_logout_user(ActionEvent event) throws IOException {
        
        Stage stage;
        stage = (Stage) btn_logoutUser.getScene().getWindow();
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/guis/LoginGUI.fxml"));
        Parent root = (Parent) loader.load();
    
        Scene scene = new Scene(root, 721, 479);
    
        stage.setTitle("GMSIS | Login");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        showAlert("Logged out successfully");  
    }
    
   
    
    @FXML
    void btn_vehicle_record(ActionEvent event) throws IOException  {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/guis/VehicleGUI.fxml"));
        VehicleGUIController c = new vehicles.VehicleGUIController(loggedInUser);
        loader.setController(c);
        Parent root = (Parent) loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) btn_vehRec.getScene().getWindow();
        stage.setTitle("GMSIS | Vehicle Records");
        stage.setScene(scene);
        stage.setResizable(true);
        stage.show();
    }  
    
    @FXML
    void manageUsers(ActionEvent event) throws IOException
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/guis/ActiveUsersGUI.fxml"));
        ActiveUsersGUIController c = new common.ActiveUsersGUIController(loggedInUser);
        loader.setController(c);
        Parent root = (Parent) loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) btn_vehParts.getScene().getWindow();
        stage.setTitle("GMSIS | Manage Users");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();        
    }

      public void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(message);
        alert.showAndWait();
    }

     
   

    
    
    
    
    
    
    
    
    
   }

    
  
    




