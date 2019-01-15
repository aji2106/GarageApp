package customers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXRadioButton;
import common.HomeGUIController;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.stage.Stage;
import javafx.util.Callback;
import users.User;

public class CustomerGUIController implements Initializable{

    @FXML
    private JFXButton btn_search;

    @FXML
    private JFXButton btn_new_customer;

    @FXML
    private JFXButton btn_view_customer;

    @FXML
    private JFXButton btn_delete_customer;

    @FXML
    private JFXRadioButton radio_name;

    @FXML
    private JFXRadioButton radio_number_plate;

    @FXML
    private JFXTextField txt_field_name;

    @FXML
    private JFXTextField txt_field_address;

    @FXML
    private JFXTextField txt_field_postcode;

    @FXML
    private JFXTextField txt_field_phone;

    @FXML
    private JFXTextField txt_field_email;

    @FXML
    private JFXButton btn_confirm;

    @FXML
    private JFXButton btn_home;
    
    @FXML
    private JFXTextField txt_search_box;
    
    @FXML
    private JFXComboBox<String> combo_type;

    @FXML
    private JFXTreeTableView<TableCustomer> table_customers;
    
    
    private final User loggedInUser;
    
    private boolean confirmBtnCreate = false;
    
    private CustomerRegistry customerRegistry = new CustomerRegistry();
    
    private boolean searchName = true;
    
    private int selectedUser;

    public CustomerGUIController(User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initializing the table with column names
        // Credit to Genuine Coder for the inital code
        // Source: https://www.youtube.com/watch?v=nbl0kOum-ps
        JFXTreeTableColumn<TableCustomer,String> name = new JFXTreeTableColumn<>("Name");
        name.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TableCustomer, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TableCustomer, String> param) {
                return param.getValue().getValue().name;
            }
        });

        JFXTreeTableColumn<TableCustomer,String> type = new JFXTreeTableColumn<>("Type");
        type.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TableCustomer, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TableCustomer, String> param) {
                return param.getValue().getValue().type;
            }
        });
        
        JFXTreeTableColumn<TableCustomer,String> address = new JFXTreeTableColumn<>("Address");
        address.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TableCustomer, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TableCustomer, String> param) {
                return param.getValue().getValue().address;
            }
        });

        JFXTreeTableColumn<TableCustomer,String> postcode = new JFXTreeTableColumn<>("Postcode");
        postcode.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TableCustomer, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TableCustomer, String> param) {
                return param.getValue().getValue().postcode;
            }
        });

        JFXTreeTableColumn<TableCustomer,String> phone = new JFXTreeTableColumn<>("Phone");
        phone.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TableCustomer, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TableCustomer, String> param) {
                return param.getValue().getValue().phone;
            }
        });


        JFXTreeTableColumn<TableCustomer,String> email = new JFXTreeTableColumn<>("Email");
        email.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TableCustomer, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TableCustomer, String> param) {
                return param.getValue().getValue().email;
            }
        });

        TreeItem<TableCustomer> root = new TreeItem<TableCustomer>(new TableCustomer(-1,"test", "test", "test", "test", "test", "test"));
        table_customers.getColumns().setAll(name, type, email, phone, address, postcode);
        table_customers.setRoot(root);
        table_customers.setShowRoot(false);
        // Getting customer from Database then setting it as rows
        try {
            ArrayList<TableCustomer> cus = customerRegistry.getAllCustomers();
            for (int i = 0; i < cus.size(); i++) {
              table_customers.getRoot().getChildren().add(new TreeItem<TableCustomer>(cus.get(i)));
            }
            
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        
        // Add options to combo box
        combo_type.getItems().addAll("Individual", "Business");
        
        // Credit for base template to Stackoverflow user invariant
        // http://stackoverflow.com/a/13860457/4805276
        // Creates an listener for when a row is selected from the customer table
        // We then fill all the user's details ready to edit       
        table_customers.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                btn_delete_customer.setDisable(false);
                btn_view_customer.setDisable(false);
                edit_customer();
            }
        });
        
        // Listener to for the search textfield
        // We want to show user's updated records as they type
        txt_search_box.textProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if (!newValue.equals("")) {
                    search(null);
                } else {
                    refreshTable();
                }
                
            }
        });

    }
    
    @FXML
    void btn_home(ActionEvent event) throws IOException {
                // Changing current window to home 
                // But first we must pass the loggedIn user that was passed to us in the constructor
                Stage stage = (Stage) btn_home.getScene().getWindow();
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

    @FXML
    void view_customer(ActionEvent event) throws IOException {
        // Get selected customer from table
        TableCustomer cus = table_customers.getSelectionModel().getSelectedItem().getValue();
        
        // Load up a new window for customer details
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/guis/CustomerDetailsGUI.fxml"));
        // Passing the customer ID to the new controller before the controller is set
        CustomerDetailsGUIController c = new customers.CustomerDetailsGUIController(cus.getId(), loggedInUser);
        loader.setController(c);
        Parent root = (Parent) loader.load();
        loader.setController(c);

        Scene scene = new Scene(root, 839, 484);

        stage.setTitle("GMSIS | Customer Details");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
    
    @FXML
    void confirm(ActionEvent event) {
        // We first run some validations to ensure that fields are not empty and that email & phone number are in correct formats
        if (!(txt_field_name.getText().isEmpty() || txt_field_address.getText().isEmpty() || txt_field_postcode.getText().isEmpty() || txt_field_phone.getText().isEmpty() || txt_field_email.getText().isEmpty() || combo_type.getSelectionModel().isEmpty())) {
            if (isValidEmailAddress(txt_field_email.getText())) {
                if (isNumber(txt_field_phone.getText())) {
                    btn_confirm.setDisable(true);
                    txt_field_name.setDisable(true);
                    txt_field_address.setDisable(true);
                    txt_field_postcode.setDisable(true);
                    txt_field_phone.setDisable(true);
                    txt_field_email.setDisable(true);
                    combo_type.setDisable(true);

                      // The name is stored as two fields in the database but shown as one
                      String[] name = new String[2];
                      String[] tmpname = txt_field_name.getText().split(" ", 2);
                       if (tmpname.length < 2) {
                          name[0] = tmpname[0];
                          name[1] = "";
                       } else {
                          name[0] = tmpname[0];
                          name[1] = tmpname[1];
                       }

                       // This statements checks if we need to create or update customer
                        if(this.confirmBtnCreate){
                           customerRegistry.createCustomer(name[0], name[1] , combo_type.getSelectionModel().getSelectedItem().toString(), txt_field_address.getText(), txt_field_postcode.getText(), txt_field_phone.getText(), txt_field_email.getText());
                        } else {
                            customerRegistry.editCustomer(name[0], name[1] , combo_type.getSelectionModel().getSelectedItem().toString(), txt_field_address.getText(), txt_field_postcode.getText(), txt_field_phone.getText(), txt_field_email.getText(), selectedUser);
                        }

                        // Just resetting everything & refreshing stale data in the table
                        txt_field_name.setText("");
                        txt_field_address.setText("");
                        txt_field_postcode.setText("");
                        txt_field_phone.setText("");
                        txt_field_email.setText("");
                        txt_search_box.setText("");
                        combo_type.getSelectionModel().clearSelection();
                        btn_confirm.setVisible(false);
                        this.refreshTable();
                } else {
                   showAlert("Invalid Phone Number Format.");
                }
            } else {
                showAlert("Invalid Email Format.");
            }
        } else {
              showAlert("No fields can be empty.");
        }
         
    }

    
    private void edit_customer() {
      TableCustomer cus = table_customers.getSelectionModel().getSelectedItem().getValue();
      selectedUser = cus.getId();

       btn_confirm.setText("Update Customer");
       btn_confirm.setVisible(true);
       btn_confirm.setDisable(false);
       txt_field_name.setDisable(false);
       txt_field_address.setDisable(false);
       txt_field_postcode.setDisable(false);
       txt_field_phone.setDisable(false);
       txt_field_email.setDisable(false);
       combo_type.setDisable(false);
       this.confirmBtnCreate = false;

       txt_field_name.setText(cus.getName().getValue());
       txt_field_address.setText(cus.getAddress().getValue());
       txt_field_postcode.setText(cus.getPostcode().getValue());
       txt_field_phone.setText(cus.getPhone().getValue());
       txt_field_email.setText(cus.getEmail().getValue());
       combo_type.getSelectionModel().select(cus.getType().getValue());
    }

    @FXML
    void delete_customer(ActionEvent event) {
        TableCustomer cus = table_customers.getSelectionModel().getSelectedItem().getValue();
        // Credit to Tanzil for original AlertBox box      
        Alert alert = new Alert(AlertType.WARNING, 
                                "Are you sure you want to delete the customer with the name " + cus.name.getValue(), 
                                ButtonType.YES, ButtonType.NO);

        // Making sure user confirms before deleting
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.YES){
            customerRegistry.deleteCustomer(cus);
            this.refreshTable();
        } 
    }

    @FXML
    void new_customer(ActionEvent event) {
       btn_confirm.setText("Create Customer");
       btn_confirm.setVisible(true);
       btn_confirm.setDisable(false);
       txt_field_name.setDisable(false);
       txt_field_address.setDisable(false);
       txt_field_postcode.setDisable(false);
       txt_field_phone.setDisable(false);
       txt_field_email.setDisable(false);
       combo_type.setDisable(false);
       this.confirmBtnCreate = true;
    }

    @FXML
    void selectSearchByName(ActionEvent event) {
        if (this.searchName) {
            radio_name.setSelected(true);
        } else {
            this.searchName = true;
            radio_number_plate.setSelected(false);  
        }

    }

    @FXML
    void selectSearchByNumberPlate(ActionEvent event) {
        if (this.searchName) {
            this.searchName = false;
            radio_name.setSelected(false);   
        } else {
            radio_number_plate.setSelected(true);  
        }

    }
    
    
    @FXML
    void search(ActionEvent event) {
          TreeItem<TableCustomer> root = new TreeItem<TableCustomer>(new TableCustomer(-1,"test", "test", "test", "test", "test", "test"));
          table_customers.setRoot(root);
          table_customers.setShowRoot(false);
          try {
              if (this.searchName) {
                  ArrayList<TableCustomer> cus = customerRegistry.searchName(txt_search_box.getText());
                for (int i = 0; i < cus.size(); i++) {
                    table_customers.getRoot().getChildren().add(new TreeItem<TableCustomer>(cus.get(i)));
                }
              } else {
                  ArrayList<TableCustomer> cus = customerRegistry.searchNumberPlate(txt_search_box.getText());
                for (int i = 0; i < cus.size(); i++) {
                    table_customers.getRoot().getChildren().add(new TreeItem<TableCustomer>(cus.get(i)));
                }
              }
             
              
          } catch (SQLException e) {
              System.err.println(e.getMessage());
          }        

    }
    
    // Method for showing an alert box with specified message
    // Credit to Tanzil for this function!    
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(message);
        alert.showAndWait();
    }
    
    // Credit to Pujan Srivastava for this function to vierfy if the email is correct 
    // http://stackoverflow.com/a/16058059/4805276   
    private boolean isValidEmailAddress(String email) {
           String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
           java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
           java.util.regex.Matcher m = p.matcher(email);
           return m.matches();
    }
    
    // Check if string is a number
    private boolean isNumber(String number) {
        try {
            Long.parseLong(number);
        } catch (NumberFormatException e) {
            return false;
        }
        
        return true;
    }
    
    // Refreshes the table when data is stale
    private void refreshTable(){
        TreeItem<TableCustomer> root = new TreeItem<TableCustomer>(new TableCustomer(-1,"test", "test", "test", "test", "test", "test"));
        table_customers.setRoot(root);
        table_customers.setShowRoot(false);
        try {
            ArrayList<TableCustomer> cus = customerRegistry.getAllCustomers();
            for (int i = 0; i < cus.size(); i++) {
              table_customers.getRoot().getChildren().add(new TreeItem<TableCustomer>(cus.get(i)));
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } 
    }
    
}
