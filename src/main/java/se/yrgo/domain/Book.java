package se.yrgo.domain;

import jakarta.persistence.*;

import java.util.*;

@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;
    private String genre;
    private int publicationYear;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;

    @ManyToMany(mappedBy = "books")
    private Set<Reader> readers;

    public Book() {
    }

    public Book(String title, String genre, int publicationYear) {
        this.title = title;
        this.genre = genre;
        this.publicationYear = publicationYear;
        this.readers = new HashSet<>();
    }

    public void allocateReader(Reader reader) {
        if (reader == null) {
            return;
        }
        this.readers.add(reader);
        reader.getBooks().add(this);
    }

    public void removeReader(Reader reader) {
        if (reader == null) {
            return;
        }

        // List.remove returns true if the given object exists and was removed.
        if (this.readers.remove(reader)) {
            reader.getBooks().remove(this);
        }
    }



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Long getId() {
        return id;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }

    public Set<Reader> getReaders() {
        return readers;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }
}
