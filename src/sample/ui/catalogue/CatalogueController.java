package sample.ui.catalogue;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import sample.classes.Book;
import sample.database.DatabaseHandler;
import sample.ui.addbook.AddBookController;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CatalogueController implements Initializable {

    ObservableList<Book> list = FXCollections.observableArrayList();

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
    @FXML
    private ChoiceBox<String> bookType;

    DatabaseHandler handler;
    String type;

    public void showCatalogue(ActionEvent actionEvent) {
        TableView.getItems().removeAll(list);
        list = FXCollections.observableArrayList();
        type = bookType.getValue();
        initCol();
        loadData();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        handler = DatabaseHandler.getInstance();
        bookType.getItems().addAll("Literature", "Science", "Journal");
        bookType.setValue("Literature");
    }

    private void loadData() {
        String qu = "SELECT * FROM book WHERE type='" + type + "'";
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
}
