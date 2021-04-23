package com.lambdaschool.bookstore.services;

import com.lambdaschool.bookstore.BookstoreApplicationTest;
import com.lambdaschool.bookstore.exceptions.ResourceNotFoundException;
import com.lambdaschool.bookstore.models.Author;
import com.lambdaschool.bookstore.models.Book;
import com.lambdaschool.bookstore.models.Section;
import com.lambdaschool.bookstore.models.Wrote;
import com.lambdaschool.bookstore.repository.AuthorRepository;
import com.lambdaschool.bookstore.repository.BookRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static junit.framework.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static junit.framework.TestCase.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BookstoreApplicationTest.class,
    properties = {
        "command.line.runner.enabled=false"
    })
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BookServiceImplUnitTestNoDB
{

    @Autowired
    private BookService bookService;

    @MockBean
    private BookRepository bookrepos;

    @MockBean
    private AuthorRepository authorRepository;

    List<Book> myBookList = new ArrayList<>();

    @Before
    public void setUp() throws
            Exception
    {

        Author a1 = new Author("John", "Mitchell");
        a1.setAuthorid(1);
        Author a2 = new Author("Dan", "Brown");
        a1.setAuthorid(2);
        Author a3 = new Author("Jerry", "Poe");
        a1.setAuthorid(3);
        Author a4 = new Author("Wells", "Teague");
        a1.setAuthorid(4);
        Author a5 = new Author("George", "Gallinger");
        a1.setAuthorid(5);
        Author a6 = new Author("Ian", "Stewart");
        a1.setAuthorid(6);

        Section s1 = new Section("Fiction");
        s1.setSectionid(1);
        Section s2 = new Section("Technology");
        s2.setSectionid(2);
        Section s3 = new Section("Travel");
        s3.setSectionid(3);
        Section s4 = new Section("Business");
        s4.setSectionid(4);
        Section s5 = new Section("Religion");
        s5.setSectionid(5);

        String book1Name = "Test Flatterland";
        Book b1 = new Book(book1Name, "9780738206752", 2001, s1);
        b1.setBookid(1);
        b1.getWrotes()
            .add(new Wrote(a6, b1));
        myBookList.add(b1);

        String book2Name = "Test Digital Fortess";
        Book b2 = new Book(book2Name, "9788489367012", 2007, s1);
        b2.setBookid(2);
        b2.getWrotes()
            .add(new Wrote(a2, b2));
        myBookList.add(b2);

        String book3Name = "Test The Da Vinci Code";
        Book b3 = new Book(book3Name, "9780307474278", 2009, s1);
        b3.setBookid(3);
        b3.getWrotes()
            .add(new Wrote(a2, b3));
        myBookList.add(b3);

        String book4Name = "Test Essentials of Finance";
        Book b4 = new Book(book4Name, "1314241651234", 0, s4);
        b4.setBookid(4);
        b4.getWrotes()
            .add(new Wrote(a3, b4));
        b4.getWrotes()
            .add(new Wrote(a5, b4));
        myBookList.add(b4);

        String book5Name = "Test Calling Texas Home";
        Book b5 = new Book(book5Name, "1885171382134", 2000, s3);
        b5.setBookid(5);
        b5.getWrotes()
            .add(new Wrote(a4, b5));
        myBookList.add(b5);

        System.out.println("Size " + myBookList.size());
        for (Book b : myBookList)
        {
            System.out.println(b);
        }

        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() throws
            Exception
    {
    }

    @Test
    public void findAll()
    {
        Mockito.when(bookrepos.findAll())
                .thenReturn(myBookList);
        System.out.println(bookService.findAll());
        assertEquals(5,
                bookService.findAll()
        .size());
    }

    @Test
    public void findBookById()
    {
        Mockito.when(bookrepos.findById(29L))
                .thenReturn(Optional.of(myBookList.get(3)));
        assertEquals("Test Essentials of Finance",
                bookService.findBookById(29)
                    .getTitle());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void notFindBookById()
    {
        Mockito.when(bookrepos.findById(100L))
                .thenThrow(ResourceNotFoundException.class);
        assertEquals("Test Essentials of Finance",
                bookService.findBookById(100)
        .getTitle());
    }

    @Test
    public void delete()
    {
        Mockito.when(bookrepos.findById(4L))
        .thenReturn(Optional.of(myBookList.get(0)));

        Mockito.doNothing()
                .when(bookrepos)
                .deleteById(4L);

        bookService.delete(4);
        assertEquals(5,
                myBookList.size());
    }

    @Test
    public void save()
    {
        String bookname = "Test Book";
        Book newBook = new Book();
        newBook.setTitle(bookname);

        Mockito.when(bookrepos.save(any(Book.class)))
                .thenReturn(newBook);
        Mockito.when(bookrepos.findById(1L))
                .thenReturn(Optional.of(newBook));

        Book saveBook = bookService.save(newBook);
        assertNotNull(saveBook);
        assertEquals(bookname, saveBook.getTitle());
    }

    @Test
    public void update()
    {
    }

    @Test
    public void deleteAll()
    {
    }
}