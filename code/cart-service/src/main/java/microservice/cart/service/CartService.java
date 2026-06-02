package microservice.cart.service;

import lombok.RequiredArgsConstructor;
import microservice.cart.domain.Cart;
import microservice.cart.domain.CartItem;
import microservice.cart.repository.CartRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;

    public Cart getCart(String cartId) {
        return cartRepository.findById(cartId).orElse(new Cart(cartId, new java.util.ArrayList<>()));
    }

    public Cart addItemToCart(String cartId, CartItem item) {
        Cart cart = getCart(cartId);
        cart.addItem(item);
        return cartRepository.save(cart);
    }

    public Cart removeItemFromCart(String cartId, String bookId) {
        Cart cart = getCart(cartId);
        cart.removeItem(bookId);
        return cartRepository.save(cart);
    }

    public void clearCart(String cartId) {
        cartRepository.deleteById(cartId);
    }
}
