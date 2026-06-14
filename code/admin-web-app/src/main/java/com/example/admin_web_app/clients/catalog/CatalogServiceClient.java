package com.example.admin_web_app.clients.catalog;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;
import org.springframework.web.service.annotation.PutExchange;
import org.springframework.web.service.annotation.DeleteExchange;
public interface CatalogServiceClient {

    @GetExchange("/catalog/api/products")
    PagedResult<Product> getProducts(@RequestParam int page);

    @GetExchange("/catalog/api/products/{code}")
    ResponseEntity<Product> getProductByCode(@PathVariable String code);

    @PostExchange("/catalog/api/products")
    ResponseEntity<Product> createProduct(@RequestBody Product product);

    @PutExchange("/catalog/api/products/{code}")
    ResponseEntity<Product> updateProduct(@PathVariable String code, @RequestBody Product product);

    @DeleteExchange("/catalog/api/products/{code}")
    ResponseEntity<Void> deleteProduct(@PathVariable String code);
}
