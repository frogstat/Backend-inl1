package se.yrgo.domain;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String authorName;
    private String authorNationality;

    @OneToMany(mappedBy = "author", cascade = CascadeType.PERSIST)
    private List<Book> authorBooks;

    public Author() {

    }

    public Author(String authorName, String authorNationality) {
        this.authorName = authorName;
        this.authorNationality = authorNationality;
        this.authorBooks = new ArrayList<>();
    }

    public void allocateBook(Book book) {
        if (book == null) {
            return;
        }
        authorBooks.add(book);
        book.setAuthor(this);
    }

    public void removeBook(Book book) {
        if (book == null) {
            return;
        }

        //Only remove from book if removing from authorBooks succeeds.
        if(authorBooks.remove(book)){
            book.setAuthor(null);
        }
    }

    public List<Book> getAuthorBooks() {
        return authorBooks;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getAuthorNationality() {
        return authorNationality;
    }

    public Long getId() {
        return id;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public void setAuthorNationality(String authorNationality) {
        this.authorNationality = authorNationality;
    }

}
