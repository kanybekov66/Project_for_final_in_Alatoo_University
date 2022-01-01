package sample.ui.login;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import sample.classes.User;
import sample.database.DatabaseHandler;


import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;


public class LoginController implements Initializable {
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;

    DatabaseHandler handler;

    public void goToRegisterPage(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../register/register.fxml"));
        Scene scene = new Scene(root);
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        appStage.setTitle("Library Information System - Sign Up");
        appStage.setScene(scene);
        appStage.show();
        appStage.setResizable(false);
    }
    public void tryLogin(ActionEvent event) throws IOException{
        if (password.getText().isEmpty() || username.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(null);
            alert.setTitle("Error");
            alert.setContentText("Something is empty!");
            alert.showAndWait();
            return;
        }

        String qu = "SELECT * FROM users";
        ResultSet rs = handler.execQuery(qu);
        try{
            while (rs.next()){
                String DBusername = rs.getString("username");
                String DBpassword = rs.getString("password");
                String DBtype = rs.getString("type");
                if (DBusername.equalsIgnoreCase(username.getText()) && DBpassword.equals(password.getText())){
                    if(DBtype.equals("member")){
                        User.setUsername(DBusername);
                        Parent root = FXMLLoader.load(getClass().getResource("../membermain/membermain.fxml"));
                        Scene scene = new Scene(root);
                        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        appStage.setScene(scene);
                        appStage.setTitle("Library Information System - Member");
                        appStage.show();
                        appStage.setResizable(false);
                        return;
                    }
                    else if (DBtype.equals("admin")){
                        User.setUsername(DBusername);
                        Parent root = FXMLLoader.load(getClass().getResource("../adminmain/adminmain.fxml"));//TODO
                        Scene scene = new Scene(root);
                        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        appStage.setTitle("Library Information System - Admin");
                        appStage.setScene(scene);
                        appStage.show();
                        appStage.setResizable(false);
                        return;
                    }
                }
            }
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(null);
            alert.setTitle("Error");
            alert.setContentText("Username or Password is incorrect");
            alert.showAndWait();
        }catch (SQLException ex){
            //TODO
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        handler = DatabaseHandler.getInstance();
    }

}
