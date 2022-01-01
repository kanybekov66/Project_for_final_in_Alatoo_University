package sample.classes;

public class Book {

    private final String isbn;
    private final String title;
    private final String author;
    private final String publisher;
    private final String type;


    public Book(String isbn, String title, String author, String publisher, String type) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.type = type;
    }

    public String getISBN() { return isbn; }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getType() {
        return type;
    }
}