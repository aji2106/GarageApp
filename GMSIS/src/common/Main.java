/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import javafx.application.Application;
import static javafx.application.Application.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application{
  public static void main(String[] args) {
            
//            Authentication auth = new Authentication();
//            auth.createUser("Ajiri", "bar", 1, "pass");

//            auth.editPassword(23, "tech");
//            System.out.println(auth.loginUser(35, "pass"));
//            System.out.println(auth.isAdmin(24));
//            auth.editAdmin(24, 1);
//            System.out.println(auth.isAdmin(24));

        launch(args);

  }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/guis/LoginGUI.fxml"));
        Parent root = (Parent) loader.load();
    
        Scene scene = new Scene(root, 721, 479);
    
        stage.setTitle("GMSIS | Login");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
  
  
}
