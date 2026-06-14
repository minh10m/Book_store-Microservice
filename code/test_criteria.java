import org.springframework.data.elasticsearch.core.query.Criteria;
public class test_criteria {
    public static void main(String[] args) {
        Criteria c1 = new Criteria("title").matches("abc");
        Criteria c2 = new Criteria("category").is("def");
        Criteria c3 = c1.and(c2);
    }
}
