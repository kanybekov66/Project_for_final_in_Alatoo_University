package sample.ui.memberlist;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sample.classes.Member;
import sample.database.DatabaseHandler;
import sample.ui.addbook.AddBookController;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MemberlistController implements Initializable {

    ObservableList<Member> list = FXCollections.observableArrayList();

    @FXML
    private TableView<Member> TableView;
    @FXML
    private TableColumn<Member, String> usernameCol;
    @FXML
    private TableColumn<Member, String> passwordCol;
    @FXML
    private TableColumn<Member, String> typeCol;
    @FXML
    private AnchorPane rootPane;

    DatabaseHandler handler;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        handler = DatabaseHandler.getInstance();
        initCol();
        loadData();
    }

    private void loadData(){
        String qu = "SELECT * FROM users";
        ResultSet rs = handler.execQuery(qu);
        try {
            while (rs.next()){
                String username = rs.getString("username");
                String password = rs.getString("password");
                String type = rs.getString("type");
                list.add(new Member(username, password, type));
            }
        }catch (SQLException ex){
            Logger.getLogger(AddBookController.class.getName()).log(Level.SEVERE, null, ex);
        }
        TableView.getItems().setAll(list);
    }

    private void initCol() {
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        passwordCol.setCellValueFactory(new PropertyValueFactory<>("password"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
    }

    public void convert(ActionEvent actionEvent) {

        Member member = TableView.getSelectionModel().getSelectedItem();

        if (member.getType().toLowerCase().equals("member")){
            String qu = "UPDATE users SET type='admin' WHERE username='" + member.getUsername() + "'";
            if(handler.execAction(qu)){
                TableView.getItems().removeAll(list);
                list = FXCollections.observableArrayList();
                loadData();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setTitle("Good job, boss!");
                alert.setContentText("User has been converted!");
                alert.showAndWait();

            } else //Error
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setTitle("We got a problem!");
                alert.setContentText("Something went wrong, boss!");
                alert.showAndWait();
            }
        } else if (member.getType().toLowerCase().equals("admin")){
            String qu = "UPDATE users SET type='member' WHERE username='" + member.getUsername() + "'";
            if(handler.execAction(qu)){
                TableView.getItems().removeAll(list);
                list = FXCollections.observableArrayList();
                loadData();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setTitle("Good job, boss!");
                alert.setContentText("User has been converted!");
                alert.showAndWait();

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

    public void cancel(ActionEvent actionEvent) {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }
}
