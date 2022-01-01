package sample.ui.register;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.database.DatabaseHandler;
import sample.ui.addbook.AddBookController;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

public class RegisterController implements Initializable {
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private PasswordField repassword;

    DatabaseHandler handler;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        handler = DatabaseHandler.getInstance();
    }

    public void goToLoginPage(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../login/login.fxml")));
        Scene scene = new Scene(root, 420, 460);
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        appStage.setScene(scene);
        appStage.setTitle("Library Information System - Sign in");
        appStage.show();
        appStage.setResizable(false);
    }

    public void tryRegister(ActionEvent event) throws IOException {

        if (password.getText().isEmpty() || repassword.getText().isEmpty() || username.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(null);
            alert.setTitle("Error");
            alert.setContentText("Something is empty!");
            alert.showAndWait();
            return;
        }

        if (!password.getText().equals(repassword.getText())){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(null);
            alert.setTitle("Error");
            alert.setContentText("Passwords don't match!");
            alert.showAndWait();
            return;
        }

        String qu = "SELECT username FROM users";
        ResultSet rs = handler.execQuery(qu);
        try{
            while (rs.next()){
                String DBusername = rs.getString("username");
                if (DBusername.equalsIgnoreCase(username.getText())){
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setHeaderText(null);
                    alert.setTitle("Error");
                    alert.setContentText("Login already exists!");
                    alert.showAndWait();
                    return;
                }
            }
        }catch (SQLException ex){
            Logger.getLogger(AddBookController.class.getName()).log(Level.SEVERE, null, ex);
        }

        String q = "INSERT INTO users(username, password) VALUES(" +
                "'" + username.getText() + "'," +
                "'" + password.getText() + "'" +
                ")";

        System.out.println(qu);

        if(handler.execAction(q)){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setTitle("Good job, boss!");
            alert.setContentText("You have been registered");
            alert.showAndWait();
            goToLoginPage(event);

        } else //Error
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("We got a problem!");
            alert.setContentText("Something went wrong, boss!");
            alert.showAndWait();
        }
    }

}
