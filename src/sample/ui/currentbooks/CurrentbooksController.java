package sample.ui.currentbooks;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sample.classes.Book;
import sample.classes.User;
import sample.database.DatabaseHandler;
import sample.ui.addbook.AddBookController;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CurrentbooksController implements Initializable {

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
    private AnchorPane rootPane;

    DatabaseHandler handler;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        handler = DatabaseHandler.getInstance();
        initCol();
        loadData();
    }

    private void loadData() {
        ArrayList<String> listbook = new ArrayList<>();
        String qu = "SELECT * FROM current WHERE user='" + User.getUsername() + "'";
        ResultSet rs = handler.execQuery(qu);
        try {
            while (rs.next()){
                listbook.add(rs.getString("ISBN"));
            }
        } catch (SQLException ex) {
            System.out.println("BOOOM");
            Logger.getLogger(AddBookController.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (String s : listbook) {
            String q = "SELECT * FROM book WHERE ISBN='" + s + "'";
            ResultSet r = handler.execQuery(q);
            try {
                while (r.next()) {
                    String isbn = r.getString("ISBN");
                    String title = r.getString("title");
                    String author = r.getString("author");
                    String publisher = r.getString("publisher");
                    String type = r.getString("type");
                    list.add(new Book(isbn, title, author, publisher, type));
                }
            } catch (SQLException ex) {
                Logger.getLogger(AddBookController.class.getName()).log(Level.SEVERE, null, ex);
            }
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

    public void remove(ActionEvent actionEvent) {
        Book book = TableView.getSelectionModel().getSelectedItem();
        String qu = "DELETE FROM current WHERE user='" + User.getUsername() + "' AND ISBN='" + book.getISBN() + "'";
        if(handler.execAction(qu)){
            TableView.getItems().removeAll(list);
            list = FXCollections.observableArrayList();
            loadData();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setTitle("Good job, boss!");
            alert.setContentText("Book has been removed!");
            alert.showAndWait();

        } else
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
