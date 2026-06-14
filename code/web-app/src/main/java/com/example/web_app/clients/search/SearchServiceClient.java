package com.example.web_app.clients.search;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

public interface SearchServiceClient {

    @GetExchange("/search/api/search/filter")
    List<BookDocumentResponse> searchWithFilters(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice);

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
