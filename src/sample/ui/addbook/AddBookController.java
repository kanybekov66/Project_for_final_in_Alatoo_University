package sample.ui.addbook;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sample.database.DatabaseHandler;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AddBookController implements Initializable {
    @FXML
    private TextField bookISBN;
    @FXML
    private TextField bookName;
    @FXML
    private TextField bookAuthor;
    @FXML
    private TextField bookPublisher;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private ChoiceBox<String> bookType;

    DatabaseHandler handler;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        handler = DatabaseHandler.getInstance();
        bookType.getItems().addAll("Literature", "Science", "Journal");
        bookType.setValue("Literature");
    }


    public void addBook(ActionEvent actionEvent) throws SQLException {
        String ISBN = bookISBN.getText();
        String name = bookName.getText();
        String author = bookAuthor.getText();
        String publisher = bookPublisher.getText();
        String type = bookType.getValue();

        if(ISBN.isEmpty() || name.isEmpty() || author.isEmpty() || publisher.isEmpty() || type.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Why are you doing this to me?");
            alert.setContentText("Please fill all fields");
            alert.showAndWait();
            return;
        }

        String q = "SELECT ISBN FROM book";
        ResultSet r = handler.execQuery(q);
        try{
            while (r.next()){
                String DBISBN = r.getString("ISBN");
                if (DBISBN.toLowerCase().equals(ISBN.toLowerCase())){
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setHeaderText(null);
                    alert.setTitle("Error");
                    alert.setContentText("Such a book already exists!");
                    alert.showAndWait();
                    return;
                }
            }
        }catch (SQLException ex){
            Logger.getLogger(AddBookController.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }

        String qu = "INSERT INTO book VALUES(" +
                "'" + ISBN + "'," +
                "'" + name + "'," +
                "'" + author + "'," +
                "'" + publisher + "'," +
                "'" + type + "'" +
                ")";

        if(handler.execAction(qu)){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setTitle("Good job, boss!");
            alert.setContentText("Book has been added!");
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

    public void cancel(ActionEvent event){
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }
}
