package se.yrgo.domain;

import jakarta.persistence.*;

import java.util.*;

@Entity
public class Reader {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private String email;

    @ManyToMany
    @JoinTable(
            name = "reader_book",
            joinColumns = @JoinColumn(name = "reader_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id")
    )
    private Set<Book> books;

    public Reader() {
    }

    public Reader(String name, String email) {
        this.name = name;
        this.email = email;
        this.books = new HashSet<>();
    }

    public void allocateBook(Book book) {
        if (book == null) {
            return;
        }
        this.books.add(book);
        book.getReaders().add(this);
    }

    public void removeBook(Book book) {
        if (book == null) {
            return;
        }

        // List.remove returns true if the given object exists and was removed.
        if (this.books.remove(book)) {
            book.getReaders().remove(this);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Book> getBooks() {
        return books;
    }

    public Long getId() {
        return id;
    }


}
