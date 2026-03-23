package se.yrgo.main;

import jakarta.persistence.*;
import org.hibernate.service.spi.ServiceException;
import se.yrgo.domain.Author;
import se.yrgo.domain.Book;
import se.yrgo.domain.Reader;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        System.out.println("Connecting to Derby server...");

        try (EntityManagerFactory emf = Persistence.createEntityManagerFactory("databaseConfig");
             EntityManager em = emf.createEntityManager()) {
            EntityTransaction tx = em.getTransaction();

            System.out.println("Success!");
            System.out.println("******************************");

            // This loops through all assignments.
            for (int i = 1; i <= 6; i++) {
                System.out.println("Executing uppgift " + i + "...");

                switch (i) {
                    case 1 -> uppgift1(tx, em);
                    case 2 -> uppgift2(tx, em);
                    case 3 -> uppgift3(tx, em);
                    case 4 -> uppgift4(tx, em);
                    case 5 -> uppgift5(tx, em);
                    case 6 -> uppgift6(tx, em);
                }
                System.out.println("Done");
                System.out.println("******************************");
            }

        } catch (ServiceException e) {
            System.err.println("Could not connect to jdbc:derby://localhost:50000/hibernate\nIs the server active?");
        } catch (Exception e) {
            System.err.println("Something went wrong!");
            System.err.println(e.getMessage());
        }
    }

    // Uppgift 1
    // Skapa och lagra data
    private static void uppgift1(EntityTransaction tx, EntityManager em) {
        try {
            System.out.println("Creating example data for database...");

            List<Author> authorList = new ArrayList<>();
            List<Reader> readerList = new ArrayList<>();
            generateExampleData(authorList, readerList);

            tx.begin();

            for (Author author : authorList) {
                em.persist(author);
            }
            for (Reader reader : readerList) {
                em.persist(reader);
            }

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            System.err.println("Something went wrong!");
            System.err.println(e.getMessage());
        }
    }

    // Uppgift 2
    // Hämta alla böcker av en specifik författare
    private static void uppgift2(EntityTransaction tx, EntityManager em) {
        String authorName = "Terry Pratchett";
        System.out.println("Searching for " + authorName + "...");

        try {
            tx.begin();

            TypedQuery<Author> q = em.createQuery("FROM Author a WHERE a.authorName=:name", Author.class);
            q.setParameter("name", authorName);

            // Will throw exception if nothing is found, which I catch lower down in the method.
            Author author = q.getSingleResult();

            //I don't believe authorBooks will ever be null, but I added the check just to be safe.
            if (author.getAuthorBooks() == null || author.getAuthorBooks().isEmpty()) {
                System.out.println("Could not find any books for " + author.getAuthorName());
            } else {
                System.out.println("Found the following books for " + author.getAuthorName() + ":");
                for (Book book : author.getAuthorBooks()) {
                    System.out.println("\t" + book.getPublicationYear() + " | " + book.getTitle());
                }
            }

            tx.commit();
        } catch (NoResultException e) {
            System.out.println("Found no authors by the name " + authorName);
            tx.rollback();
        } catch (Exception e) {
            tx.rollback();
            System.err.println("Something went wrong!");
            System.err.println(e.getMessage());
        }
    }

    // Uppgift 3
    // Hämta alla läsare som har läst en viss bok
    private static void uppgift3(EntityTransaction tx, EntityManager em) {
        String bookToFind = "Harry Potter and the Chamber of Secrets";

        try {
            tx.begin();

            // First I need the book object to search after.
            TypedQuery<Book> qBook = em.createQuery("FROM Book b WHERE b.title = :title", Book.class);
            qBook.setParameter("title", bookToFind);
            Book book = qBook.getSingleResult();

            // Then I can see if that object is in each reader's list.
            TypedQuery<Reader> qReader = em.createQuery("FROM Reader r WHERE :book MEMBER OF r.books", Reader.class);
            qReader.setParameter("book", book);
            List<Reader> readers = qReader.getResultList();

            if (readers.isEmpty()) {
                System.out.println(bookToFind + " is in the database, but has no readers.");
            } else {
                System.out.println("The following people are reading " + book.getTitle() + ":");
                for (Reader reader : readers) {
                    System.out.println("\t" + reader.getName());
                }
            }

            tx.commit();
        } catch (NoResultException e) {
            tx.rollback();
            System.out.println("Found no books by the title: " + bookToFind);
        } catch (Exception e) {
            tx.rollback();
            System.err.println("Something went wrong!");
            System.err.println(e.getMessage());
        }
    }

    // Uppgift 4
    // Hämta författare vars böcker har lästs av minst en läsare
    private static void uppgift4(EntityTransaction tx, EntityManager em) {
        try {
            tx.begin();

            TypedQuery<Author> q = em.createQuery("FROM Author a JOIN a.authorBooks ab JOIN ab.readers r", Author.class);
            List<Author> authors = q.getResultList();

            if (authors.isEmpty()) {
                System.out.println("Could not find any authors with readers.");
            } else {
                System.out.println("The following authors have at least one reader:");
                for (Author author : authors) {
                    System.out.println("\t" + author.getAuthorName());
                }
            }

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            System.err.println("Something went wrong!");
            System.err.println(e.getMessage());
        }
    }

    // Uppgift 5
    // Räkna antalet böcker per författare
    private static void uppgift5(EntityTransaction tx, EntityManager em) {

        try {
            tx.begin();

            // Left join also gives me authors with no books
            TypedQuery<Object[]> q = em.createQuery("SELECT a, COUNT(ab) FROM Author a LEFT JOIN a.authorBooks ab GROUP BY a", Object[].class);
            List<Object[]> authorAndBooks = q.getResultList();

            if (authorAndBooks.isEmpty()) {
                System.out.println("Found no authors");
            } else {
                for (Object[] object : authorAndBooks) {
                    Author author = (Author) object[0];
                    Long numberOfBooks = (Long) object[1];

                    String number = numberOfBooks == 0 ? "no" : numberOfBooks.toString();
                    String bookString = numberOfBooks == 1 ? "book" : "books";
                    System.out.println("\t" + author.getAuthorName() + " has " + number + " " + bookString);
                }
            }

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            System.err.println("Something went wrong!");
            System.err.println(e.getMessage());
        }
    }


    // Uppgift 6
    // Named Query - Hämta böcker efter genre
    private static void uppgift6(EntityTransaction tx, EntityManager em) {
        String genreToSearchFor = "Fantasy";

        try {
            tx.begin();

            TypedQuery<Book> q = em.createNamedQuery("searchByGenre", Book.class);
            q.setParameter("genre", genreToSearchFor);
            List<Book> books = q.getResultList();

            if (books.isEmpty()) {
                System.out.println("Found no " + genreToSearchFor + " books");
            } else {
                System.out.println("Here are all " + genreToSearchFor + " books:");
                for (Book book : books) {
                    System.out.println("\t" + book.getPublicationYear() + " | " + book.getTitle());
                }
            }

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            System.err.println("Something went wrong!");
            System.err.println(e.getMessage());
        }
    }

    private static void generateExampleData(List<Author> authorList, List<Reader> readerList) {
        Author author1 = new Author("J.K Rowling", "British");
        Book book1 = new Book("Harry Potter and the Chamber of Secrets", "Fantasy", 1998);
        Book book2 = new Book("Harry Potter and the Prisoner of Azkaban", "Fantasy", 1999);
        author1.allocateBook(book1);
        author1.allocateBook(book2);

        Author author2 = new Author("Terry Pratchett", "British");
        Book book3 = new Book("The Truth", "Fantasy", 2000);
        Book book4 = new Book("Thief of Time", "Fantasy", 2001);
        author2.allocateBook(book3);
        author2.allocateBook(book4);

        Author author3 = new Author("Astrid Lindgren", "Swedish");
        Book book5 = new Book("Pippi Longstocking", "Children", 1945);
        author3.allocateBook(book5);

        Reader reader1 = new Reader("Sonny Bengtson", "the_son@hotmail.com");
        reader1.allocateBook(book1);
        reader1.allocateBook(book4);

        Reader reader2 = new Reader("Hubert Trough", "hubhub@hotmail.com");
        reader2.allocateBook(book1);
        reader2.allocateBook(book5);

        Reader reader3 = new Reader("Kris Kristofferson", "shipwrecked1933@hotmail.com");
        reader3.allocateBook(book2);

        // This author will have no readers to make sure Uppgift 4 works.
        Author author4 = new Author("Frank Herbert", "American");
        Book book6 = new Book("Dune", "Fantasy", 1965);
        author4.allocateBook(book6);

        // This author has no books to make sure Uppgift 5 works.
        Author author5 = new Author("Ken Follett", "British");

        authorList.addAll(List.of(author1, author2, author3, author4, author5));
        readerList.addAll(List.of(reader1, reader2, reader3));

    }
}
