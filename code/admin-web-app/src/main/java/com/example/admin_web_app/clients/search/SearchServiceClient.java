package com.example.admin_web_app.clients.search;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;
import org.springframework.web.service.annotation.DeleteExchange;

public interface SearchServiceClient {

    @GetExchange("/search/api/search/filter")
    List<BookDocumentResponse> searchWithFilters(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice);

    @PostExchange("/search/api/search")
    BookDocumentResponse indexBook(@RequestBody BookDocumentResponse book);

    @DeleteExchange("/search/api/search/{id}")
    void deleteBook(@PathVariable String id);

    record BookDocumentResponse(
            String id,
            String code,
            String title,
            String author,
            String isbn,
            BigDecimal price,
            String category,
            String imageUrl) {}
}
