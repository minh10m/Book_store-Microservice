package microservice.search.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import microservice.search.domain.BookDocument;
import microservice.search.service.BookSearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
@Tag(name = "Search API", description = "Book Search APIs")
public class SearchController {

    private final BookSearchService searchService;

    @GetMapping
    @Operation(summary = "Search books by keyword")
    public ResponseEntity<List<BookDocument>> searchBooks(@RequestParam String keyword) {
        return ResponseEntity.ok(searchService.searchBooks(keyword));
    }

    @GetMapping("/category")
    @Operation(summary = "Search books by category")
    public ResponseEntity<List<BookDocument>> searchByCategory(@RequestParam String category) {
        return ResponseEntity.ok(searchService.findByCategory(category));
    }

    @PostMapping
    @Operation(summary = "Index a new book")
    public ResponseEntity<BookDocument> indexBook(@RequestBody BookDocument book) {
        return ResponseEntity.ok(searchService.save(book));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove a book from index")
    public ResponseEntity<Void> deleteBook(@PathVariable String id) {
        searchService.delete(id);
        return ResponseEntity.ok().build();
    }
}
