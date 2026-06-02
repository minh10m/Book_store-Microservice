package microservice.search.repository;

import java.util.List;
import microservice.search.domain.BookDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookSearchRepository extends ElasticsearchRepository<BookDocument, String> {
    List<BookDocument> findByTitleContainingOrAuthorContaining(String title, String author);

    List<BookDocument> findByCategory(String category);
}
