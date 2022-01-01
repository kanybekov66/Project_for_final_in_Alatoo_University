package sample.ui.ownbook;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sample.classes.Book;
import sample.classes.User;
import sample.database.DatabaseHandler;
import sample.ui.addbook.AddBookController;
import sample.ui.catalogue.CatalogueController;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OwnbookController extends CatalogueController implements Initializable {

    ObservableList<Book> list = FXCollections.observableArrayList();

    DatabaseHandler handler;

    @FXML
    private TextField bookName;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private TableView<Book> TableView;
    @FXML
    private TableColumn<Book, String> ISBNCol;
    @FXML
    private TableColumn<Book, String> TitleCol;
    @FXML
    private TableColumn<Book, String> AuthorCol;
    @FXML
    private TableColumn<Book, String> PublisherCol;
    @FXML
    private TableColumn<Book, String> TypeCol;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        handler = DatabaseHandler.getInstance();
    }

    private void loadData() {
        String qu = "SELECT * FROM book WHERE title like '%" + bookName.getText() + "%'";
        ResultSet rs = handler.execQuery(qu);
        try{
            while (rs.next()){
                String isbn = rs.getString("ISBN");
                String title = rs.getString("title");
                String author = rs.getString("author");
                String publisher = rs.getString("publisher");
                String type = rs.getString("type");

                list.add(new Book(isbn, title, author, publisher, type));
            }
        } catch (SQLException ex){
            Logger.getLogger(AddBookController.class.getName()).log(Level.SEVERE, null, ex);
        }

        TableView.getItems().setAll(list);
    }

    private void initCol() {
        ISBNCol.setCellValueFactory(new PropertyValueFactory<>("ISBN"));
        TitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        AuthorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        PublisherCol.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        TypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
    }

    public void search(ActionEvent actionEvent) {
        TableView.getItems().removeAll(list);
        list = FXCollections.observableArrayList();
        initCol();
        loadData();
    }

    public void ownBook(ActionEvent actionEvent) {
        String name = User.getUsername();
        Book book = TableView.getSelectionModel().getSelectedItem();

        String q = "SELECT * FROM current";
        ResultSet r = handler.execQuery(q);
        try{
            while (r.next()){
                String DBISBN = r.getString("ISBN");
                String DBuser = r.getString("user");
                if (DBISBN.toLowerCase().equals(book.getISBN().toLowerCase()) && DBuser.toLowerCase().equals(name)){
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setHeaderText(null);
                    alert.setTitle("Error");
                    alert.setContentText("You already have this book!");
                    alert.showAndWait();
                    return;
                }
            }
        }catch (SQLException ex){
            Logger.getLogger(AddBookController.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }

        String qu = "INSERT INTO current VALUES(" +
                "'" + book.getISBN() + "'," +
                "'" + name + "'" +
                ")";
        if(handler.execAction(qu)){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setTitle("Good job, boss!");
            alert.setContentText("You took this book!");
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

    public void cancel(ActionEvent actionEvent) {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }
}
