package sample.ui.adminmain;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sample.classes.User;
import sample.ui.membermain.MembermainController;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdminmainController implements Initializable {

    public Label username;

    public void goToCatalogue(ActionEvent actionEvent) {
        loadWindow("../catalogue/catalogue.fxml", "Library Information System - Catalogue");//TODO
    }

    public void goToAddBook(ActionEvent actionEvent) {
        loadWindow("../addbook/addbook.fxml", "Library Information System - Adding Book");//TODO
    }

    public void goToMembers(ActionEvent actionEvent) {
        loadWindow("../memberlist/memberlist.fxml", "Library Information System - Member List");//TODO
    }

    public void goToLogin(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../login/login.fxml")));
        Scene scene = new Scene(root);
        Stage appStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        appStage.setScene(scene);
        appStage.show();
        appStage.setResizable(false);
    }

    void loadWindow(String loc, String title){
        try{
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(loc)));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();
        }catch (IOException ex){
            Logger.getLogger(MembermainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String usernameL = User.getUsername();
        username.setText(usernameL);
    }
}
