package microservice.cart.domain;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RedisHash(value = "Cart", timeToLive = 86400) // TTL 1 day
public class Cart {

    @Id
    private String id; // This can be the userId or a session id

    private List<CartItem> items = new ArrayList<>();

    public void addItem(CartItem item) {
        items.removeIf(i -> i.getBookId().equals(item.getBookId()));
        items.add(item);
    }

    public void removeItem(String bookId) {
        items.removeIf(i -> i.getBookId().equals(bookId));
    }
}
