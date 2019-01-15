package common;

import users.User;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class LoginController {

    @FXML
    private JFXTextField txt_ID;

    @FXML
    private JFXPasswordField txt_password;

    @FXML
    private JFXButton btn_login;

    @FXML
    private Group msg_error;

    @FXML
    void loginUser(ActionEvent event) {
        try {
            User loggedInUser = new Authentication().loginUser(Integer.parseInt(txt_ID.getText()), txt_password.getText());
            if(loggedInUser != null) {
                System.out.println("Succesfully Logged in: " + loggedInUser.getFirstname());
                
                Stage stage = (Stage) btn_login.getScene().getWindow();
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

            } else {
                msg_error.setVisible(true);
                txt_ID.setText("");
                txt_password.setText("");
            }
        } catch (Exception e){
            msg_error.setVisible(true);
            txt_ID.setText("");
            txt_password.setText("");
            System.err.println(e.getMessage());
        }


    }

}
