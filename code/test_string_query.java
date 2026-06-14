package microservice.search.service;
import org.springframework.data.elasticsearch.core.query.StringQuery;
public class test_string_query {
    public void test() {
        StringQuery query = new StringQuery("...");
    }
}
