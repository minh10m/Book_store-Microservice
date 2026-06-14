package microservice.search.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import microservice.search.domain.BookDocument;
import microservice.search.repository.BookSearchRepository;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.StringQuery;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class BookSearchService {

    private final BookSearchRepository searchRepository;
    private final ElasticsearchOperations elasticsearchOperations;

    public BookDocument save(BookDocument book) {
        return searchRepository
                .findByCode(book.getCode())
                .map(existing -> {
                    book.setId(existing.getId());
                    return searchRepository.save(book);
                })
                .orElseGet(() -> searchRepository.save(book));
    }

    public List<BookDocument> searchBooks(String keyword) {
        return searchRepository.findByTitleContainingOrAuthorContaining(keyword, keyword);
    }

    public List<BookDocument> findByCategory(String category) {
        return searchRepository.findByCategory(category);
    }

    public List<BookDocument> searchWithFilters(
            String keyword, String category, BigDecimal minPrice, BigDecimal maxPrice) {

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("{\"bool\": {\"must\": [");

        List<String> conditions = new ArrayList<>();

        if (StringUtils.hasText(keyword)) {
            conditions.add(
                    "{ \"multi_match\": { \"query\": \"" + keyword + "\", \"fields\": [\"title\", \"author\"] } }");
        }

        if (StringUtils.hasText(category)) {
            conditions.add("{ \"term\": { \"category\": \"" + category + "\" } }");
        }

        if (minPrice != null || maxPrice != null) {
            String range = "{ \"range\": { \"price\": { ";
            if (minPrice != null) range += "\"gte\": " + minPrice + (maxPrice != null ? ", " : "");
            if (maxPrice != null) range += "\"lte\": " + maxPrice;
            range += " } } }";
            conditions.add(range);
        }

        if (conditions.isEmpty()) {
            return ((List<BookDocument>) searchRepository.findAll());
        }

        queryBuilder.append(String.join(",", conditions));
        queryBuilder.append("]}}");

        StringQuery query = new StringQuery(queryBuilder.toString());
        query.setMaxResults(100);
        return elasticsearchOperations.search(query, BookDocument.class).getSearchHits().stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }

    public void delete(String code) {
        searchRepository.deleteByCode(code);
    }
}
