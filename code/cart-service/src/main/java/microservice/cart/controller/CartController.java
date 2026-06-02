package microservice.cart.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import microservice.cart.domain.Cart;
import microservice.cart.domain.CartItem;
import microservice.cart.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
@Tag(name = "Cart API", description = "Shopping Cart management APIs")
public class CartController {

    private final CartService cartService;

    @GetMapping("/{cartId}")
    @Operation(summary = "Get a cart by its ID")
    public ResponseEntity<Cart> getCart(@PathVariable String cartId) {
        return ResponseEntity.ok(cartService.getCart(cartId));
    }

    @PostMapping("/{cartId}/items")
    @Operation(summary = "Add an item to the cart")
    public ResponseEntity<Cart> addItem(@PathVariable String cartId, @RequestBody CartItem item) {
        return ResponseEntity.ok(cartService.addItemToCart(cartId, item));
    }

    @DeleteMapping("/{cartId}/items/{bookId}")
    @Operation(summary = "Remove an item from the cart")
    public ResponseEntity<Cart> removeItem(@PathVariable String cartId, @PathVariable String bookId) {
        return ResponseEntity.ok(cartService.removeItemFromCart(cartId, bookId));
    }

    @DeleteMapping("/{cartId}")
    @Operation(summary = "Clear the cart")
    public ResponseEntity<Void> clearCart(@PathVariable String cartId) {
        cartService.clearCart(cartId);
        return ResponseEntity.ok().build();
    }
}
