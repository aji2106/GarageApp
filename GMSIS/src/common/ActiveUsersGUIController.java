package common;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.sqlite.core.DB;
import users.User;

public class ActiveUsersGUIController implements Initializable {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXButton btn_delete, btn_confirm, btn_home, btn_clear, btn_add;

    @FXML
    private JFXTextField txt_fn, txt_ln, txt_id, txt_pass, txt_rate;

    @FXML
    private TableView<ActiveUsersTable> table_activeUsersTable;

    private ObservableList<ActiveUsersTable> createList;

    @FXML
    private TableColumn column_firstName, column_userID, column_lastName, column_ID, column_hourlyRate, column_admin;

    @FXML
    private JFXComboBox<String> combo_admin;

    private User loggedInUser;
    String temp = "";
    private static final Database DB = new Database();

    public ActiveUsersGUIController(User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            activeUsersTableHandle.handleTable(table_activeUsersTable);
            createList = activeUsersTableHandle.getData(table_activeUsersTable);
            btn_confirm.setVisible(false);
            btn_delete.setVisible(false);
            btn_add.setVisible(true);
            btn_clear.setVisible(true);
            combo_admin.getItems().addAll(
                    "True",
                    "False"
                );

        } catch (ClassNotFoundException | SQLException e) {
        }
    }

    @FXML
    void Confirm(ActionEvent event) throws ClassNotFoundException, SQLException {
        try {
            int id = Integer.parseInt(txt_id.getText());
            int hourly = Integer.parseInt(txt_rate.getText());
            if (!(combo_admin.getSelectionModel().getSelectedItem().isEmpty() || txt_fn.getText().isEmpty() || txt_id.getText().isEmpty() || txt_ln.getText().isEmpty() || txt_pass.getText().isEmpty() || txt_rate.getText().isEmpty())) {
                if (txt_id.getLength() == 5) {
                    if (combo_admin.getSelectionModel().getSelectedItem().equalsIgnoreCase("TRUE")) {
                        DB.executeUpdate("UPDATE User SET id = " + txt_id.getText() + ", surname = '" + txt_ln.getText() + "', firstname ='" + txt_fn.getText() + "' , admin ='1' , password = '" + generatePasswordHash(txt_pass.getText()) + "' , hourlyRate = " + txt_rate.getText() + " WHERE id = '" + temp + "'");
                        activeUsersTableHandle.getData(table_activeUsersTable);
                        table_activeUsersTable.getSelectionModel().select(null);
                        show(true);
                        clear(event);

                    } else {
                        DB.executeUpdate("UPDATE User SET id = " + txt_id.getText() + ", surname = '" + txt_ln.getText() + "', firstname ='" + txt_fn.getText() + "' , admin ='0' , password = '" + generatePasswordHash(txt_pass.getText()) + "' , hourlyRate = " + txt_rate.getText() + " WHERE id = '" + temp + "'");
                        activeUsersTableHandle.getData(table_activeUsersTable);
                        table_activeUsersTable.getSelectionModel().select(null);
                        show(true);
                        clear(event);
                    }
                } else {
                    showAlert("ID must be 5 digits long");
                }
            } else {
                showAlert("Please enter details in all the boxes");
            }
        } catch (NumberFormatException e) {
            showAlert("User ID and hourly rate must be integers");
        }
    }

    @FXML
    void delete(ActionEvent event) throws SQLException, ClassNotFoundException {
        if (deleteConfirmation("Would you like to delete this user?")) {
            DB.executeUpdate("DELETE from User WHERE id= '" + temp + "'");
            activeUsersTableHandle.getData(table_activeUsersTable);
            table_activeUsersTable.getSelectionModel().select(null);
            clear(event);

        }
    }

    @FXML
    void home(ActionEvent event) throws IOException {
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
    void rowClick(MouseEvent event) {
        try {
            show(true);
            btn_add.setVisible(false);
            btn_clear.setVisible(false);
            ActiveUsersTable extractRecords = table_activeUsersTable.getSelectionModel().getSelectedItem();
            temp = Integer.toString(extractRecords.getID());
            if (extractRecords.getAdmin().equals("Yes")) {
                combo_admin.setValue("True");   
            } else {
                combo_admin.setValue("False");  
            }
            txt_fn.setText(extractRecords.getFirstname());
            txt_ln.setText(extractRecords.getSurname());
            txt_id.setText(Integer.toString(extractRecords.getID()));
            txt_rate.setText(Integer.toString(extractRecords.getHourlyRate()));
        } catch (NullPointerException e) {
            showAlert("Please select a row");
            initialize(location, resources);
        }

    }

    @FXML
    void add(ActionEvent event) throws SQLException, ClassNotFoundException {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:data.db");
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery("select * from User where id ='" + txt_id.getText() + "'");) {
            
            try {
                if (rs.getString(1).equals(txt_id.getText())) {
                    showAlert("ID already exists");
                    clear(event);
                    return;
                }
            } catch (SQLException sQLException) {
            }
            int id = Integer.parseInt(txt_id.getText());
            int hourly = Integer.parseInt(txt_rate.getText());
            String fn = txt_fn.getText();
            String ln = txt_ln.getText();
            if (!(combo_admin.getSelectionModel().isEmpty() || txt_fn.getText().isEmpty() || txt_id.getText().isEmpty() || txt_ln.getText().isEmpty() || txt_pass.getText().isEmpty() || txt_rate.getText().isEmpty())) {
                if (txt_id.getLength() == 5) {
                    if (combo_admin.getSelectionModel().getSelectedItem().equalsIgnoreCase("TRUE")) {
                        DB.executeUpdate("INSERT into USER (id,surname,firstname,admin,password,hourlyRate)values(" + txt_id.getText() + ",'" + txt_ln.getText() + "','" + txt_fn.getText() + "','1','" + generatePasswordHash(txt_pass.getText()) + "'," + txt_rate.getText() + ")");
                        activeUsersTableHandle.getData(table_activeUsersTable);
                        table_activeUsersTable.getSelectionModel().select(null);
                        clear(event);
                    } else {
                        DB.executeUpdate("INSERT into USER (id,surname,firstname,admin,password,hourlyRate)values(" + txt_id.getText() + ",'" + txt_ln.getText() + "','" + txt_fn.getText() + "','0','" + generatePasswordHash(txt_pass.getText()) + "'," + txt_rate.getText() + ")");
                        activeUsersTableHandle.getData(table_activeUsersTable);
                        table_activeUsersTable.getSelectionModel().select(null);
                        clear(event);
                    }
                } else {
                    showAlert("ID must be 5 digits long");
                }
            } else {
                showAlert("Please enter details in all the boxes");
            }
        } catch (NumberFormatException e) {
            showAlert("User ID and hourly rate must be integers");
        }
    }

    @FXML
    void clear(ActionEvent event
    ) {
        combo_admin.getSelectionModel().clearSelection();
        txt_fn.clear();
        txt_id.clear();
        txt_ln.clear();
        txt_pass.clear();
        txt_rate.clear();
        table_activeUsersTable.getSelectionModel().select(null);
        initialize(location, resources);
    }

    public boolean deleteConfirmation(String message) {
        Alert dAlert = new Alert(Alert.AlertType.CONFIRMATION);
        dAlert.setTitle("Deletion confirmation");
        dAlert.setHeaderText(message);
        dAlert.showAndWait();
        return dAlert.getResult() == ButtonType.OK;
    }

    public void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Information");
        alert.setHeaderText(message);
        alert.showAndWait();
    }

    void show(boolean condition) {
        combo_admin.setVisible(condition);
        txt_fn.setVisible(condition);
        txt_ln.setVisible(condition);
        txt_id.setVisible(condition);
        txt_pass.setVisible(condition);
        txt_rate.setVisible(condition);
        btn_clear.setVisible(condition);
        btn_confirm.setVisible(condition);
        btn_delete.setVisible(condition);
        btn_add.setVisible(condition);
    }
    
    private static String generatePasswordHash(String password){
        StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
        String encryptedPassword = passwordEncryptor.encryptPassword(password);
        return encryptedPassword;
    }

}
