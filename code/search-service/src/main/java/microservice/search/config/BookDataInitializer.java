package microservice.search.config;

import jakarta.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import microservice.search.domain.BookDocument;
import microservice.search.repository.BookSearchRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class BookDataInitializer {

    private final BookSearchRepository repository;

    @PostConstruct
    public void initData() {
        log.info("Clearing and initializing search data in Elasticsearch...");
        repository.deleteAll();
        List<BookDocument> books = List.of(
                new BookDocument(
                        null,
                        "P100",
                        "The Hunger Games",
                        "Suzanne Collins",
                        "9780439023481",
                        new BigDecimal("14.99"),
                        "Fiction",
                        "https://images.gr-assets.com/books/1447303603l/2767052.jpg"),
                new BookDocument(
                        null,
                        "P101",
                        "To Kill a Mockingbird",
                        "Harper Lee",
                        "9780060935467",
                        new BigDecimal("10.99"),
                        "Fiction",
                        "https://images.gr-assets.com/books/1361975680l/2657.jpg"),
                new BookDocument(
                        null,
                        "P102",
                        "The Chronicles of Narnia",
                        "C.S. Lewis",
                        "9780060256654",
                        new BigDecimal("25.99"),
                        "Fiction",
                        "https://images.gr-assets.com/books/1449868701l/11127.jpg"),
                new BookDocument(
                        null,
                        "P103",
                        "Gone with the Wind",
                        "Margaret Mitchell",
                        "9781451635621",
                        new BigDecimal("19.99"),
                        "Fiction",
                        "https://images.gr-assets.com/books/1328025229l/18405.jpg"),
                new BookDocument(
                        null,
                        "P104",
                        "The Fault in Our Stars",
                        "John Green",
                        "9780525478812",
                        new BigDecimal("12.99"),
                        "Fiction",
                        "https://images.gr-assets.com/books/1360206420l/11870085.jpg"),
                new BookDocument(
                        null,
                        "P105",
                        "The Giving Tree",
                        "Shel Silverstein",
                        "9780060256654",
                        new BigDecimal("15.50"),
                        "Fiction",
                        "https://images.gr-assets.com/books/1174210942l/370493.jpg"),
                new BookDocument(
                        null,
                        "P106",
                        "The Da Vinci Code",
                        "Dan Brown",
                        "9780307474278",
                        new BigDecimal("16.99"),
                        "Fiction",
                        "https://images.gr-assets.com/books/1303252999l/968.jpg"),
                new BookDocument(
                        null,
                        "P107",
                        "The Alchemist",
                        "Paulo Coelho",
                        "9780061122415",
                        new BigDecimal("14.50"),
                        "Fiction",
                        "https://images.gr-assets.com/books/1483412266l/865.jpg"),
                new BookDocument(
                        null,
                        "P108",
                        "Charlotte's Web",
                        "E.B. White",
                        "9780064400558",
                        new BigDecimal("8.99"),
                        "Fiction",
                        "https://images.gr-assets.com/books/1439632243l/24178.jpg"),
                new BookDocument(
                        null,
                        "P109",
                        "The Little Prince",
                        "Antoine de Saint-Exupéry",
                        "9780156012195",
                        new BigDecimal("9.99"),
                        "Fiction",
                        "https://images.gr-assets.com/books/1367545443l/157993.jpg"),
                new BookDocument(
                        null,
                        "P110",
                        "A Thousand Splendid Suns",
                        "Khaled Hosseini",
                        "9781594489501",
                        new BigDecimal("13.99"),
                        "Fiction",
                        "https://images.gr-assets.com/books/1345958969l/128029.jpg"),
                new BookDocument(
                        null,
                        "P111",
                        "A Game of Thrones",
                        "George R.R. Martin",
                        "9780553103540",
                        new BigDecimal("29.99"),
                        "Fiction",
                        "https://images.gr-assets.com/books/1436732693l/13496.jpg"),
                new BookDocument(
                        null,
                        "P112",
                        "The Book Thief",
                        "Markus Zusak",
                        "9780375842207",
                        new BigDecimal("14.99"),
                        "Fiction",
                        "https://images.gr-assets.com/books/1522157426l/19063.jpg"),
                new BookDocument(
                        null,
                        "P113",
                        "One Flew Over the Cuckoo's Nest",
                        "Ken Kesey",
                        "9780451163967",
                        new BigDecimal("11.99"),
                        "Fiction",
                        "https://images.gr-assets.com/books/1516211014l/332613.jpg"),
                new BookDocument(
                        null,
                        "P114",
                        "Fifty Shades of Grey",
                        "E.L. James",
                        "9780345803481",
                        new BigDecimal("15.99"),
                        "Fiction",
                        "https://images.gr-assets.com/books/1385207843l/10818853.jpg"),
                new BookDocument(
                        null,
                        "P201",
                        "Clean Code",
                        "Robert C. Martin",
                        "9780132350884",
                        new BigDecimal("45.00"),
                        "IT",
                        "https://images.gr-assets.com/books/1436202607l/3735293.jpg"),
                new BookDocument(
                        null,
                        "P202",
                        "Design Patterns",
                        "Erich Gamma",
                        "9780201633610",
                        new BigDecimal("54.00"),
                        "IT",
                        "https://images.gr-assets.com/books/1348027904l/85009.jpg"),
                new BookDocument(
                        null,
                        "P203",
                        "The Pragmatic Programmer",
                        "Andrew Hunt",
                        "9780201616224",
                        new BigDecimal("42.00"),
                        "IT",
                        "https://images.gr-assets.com/books/1401432508l/4099.jpg"),
                new BookDocument(
                        null,
                        "P301",
                        "Zero to One",
                        "Peter Thiel",
                        "9780804139298",
                        new BigDecimal("22.50"),
                        "Business",
                        "https://images.gr-assets.com/books/1414347376l/18050143.jpg"),
                new BookDocument(
                        null,
                        "P302",
                        "The Lean Startup",
                        "Eric Ries",
                        "9780307887894",
                        new BigDecimal("18.00"),
                        "Business",
                        "https://images.gr-assets.com/books/1333576876l/10127019.jpg"),
                new BookDocument(
                        null,
                        "P303",
                        "Thinking, Fast and Slow",
                        "Daniel Kahneman",
                        "9780374275631",
                        new BigDecimal("20.00"),
                        "Business",
                        "https://images.gr-assets.com/books/1317793965l/11468377.jpg"),
                new BookDocument(
                        null,
                        "P401",
                        "A Brief History of Time",
                        "Stephen Hawking",
                        "9780553380163",
                        new BigDecimal("15.50"),
                        "Science",
                        "https://covers.openlibrary.org/b/isbn/9780553380163-L.jpg"),
                new BookDocument(
                        null,
                        "P402",
                        "Cosmos",
                        "Carl Sagan",
                        "9780345539434",
                        new BigDecimal("19.99"),
                        "Science",
                        "https://covers.openlibrary.org/b/isbn/9780345331359-L.jpg"),
                new BookDocument(
                        null,
                        "P403",
                        "Sapiens",
                        "Yuval Noah Harari",
                        "9780062316097",
                        new BigDecimal("24.99"),
                        "Science",
                        "https://images.gr-assets.com/books/1420585954l/23692271.jpg"));
        repository.saveAll(books);
        log.info("Initialized {} books in Elasticsearch.", books.size());
    }
}
