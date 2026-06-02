package microservice.search.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import microservice.search.domain.BookDocument;
import microservice.search.repository.BookSearchRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookSearchService {

    private final BookSearchRepository searchRepository;

    public BookDocument save(BookDocument book) {
        return searchRepository.save(book);
    }

    public List<BookDocument> searchBooks(String keyword) {
        return searchRepository.findByTitleContainingOrAuthorContaining(keyword, keyword);
    }

    public List<BookDocument> findByCategory(String category) {
        return searchRepository.findByCategory(category);
    }

    public void delete(String id) {
        searchRepository.deleteById(id);
    }
}
