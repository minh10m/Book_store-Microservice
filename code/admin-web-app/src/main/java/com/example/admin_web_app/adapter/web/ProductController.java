package com.example.admin_web_app.adapter.web;

import com.example.admin_web_app.clients.catalog.CatalogServiceClient;
import com.example.admin_web_app.clients.catalog.PagedResult;
import com.example.admin_web_app.clients.catalog.Product;
import com.example.admin_web_app.clients.search.SearchServiceClient;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
class ProductController {
    private static final Logger log = LoggerFactory.getLogger(ProductController.class);
    private final CatalogServiceClient catalogService;
    private final SearchServiceClient searchService;

    ProductController(CatalogServiceClient catalogService, SearchServiceClient searchService) {
        this.catalogService = catalogService;
        this.searchService = searchService;
    }

    @GetMapping
    String index() {
        return "redirect:/products";
    }

    @GetMapping("/products")
    String showProductsPage(@RequestParam(name = "page", defaultValue = "1") int page, Model model) {
        model.addAttribute("pageNo", page);
        return "products";
    }

    @GetMapping("/api/products")
    @ResponseBody
    PagedResult<Product> products(@RequestParam(name = "page", defaultValue = "1") int page, Model model) {
        log.info("Fetching products for page: {}", page);
        return catalogService.getProducts(page);
    }

    @GetMapping("/api/products/search")
    @ResponseBody
    PagedResult<Product> searchProducts(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) java.math.BigDecimal minPrice,
            @RequestParam(required = false) java.math.BigDecimal maxPrice) {
        log.info("Searching products with filters");
        var searchResults = searchService.searchWithFilters(keyword, category, minPrice, maxPrice);

        List<Product> products = searchResults.stream()
                .map(doc -> new Product(
                        null,
                        doc.code() != null ? doc.code() : doc.isbn(),
                        doc.title(),
                        "By " + doc.author(),
                        null,
                        doc.imageUrl(),
                        doc.price()))
                .toList();

        return new PagedResult<>(products, products.size(), 1, 1, true, true, false, false);
    }

    @GetMapping("/products/{code}")
    String showProductDetails(@PathVariable String code, Model model) {
        model.addAttribute("productCode", code);
        return "product_details";
    }

    @GetMapping("/products/{code}/preview")
    String showProductPreview(@PathVariable String code, Model model) {
        model.addAttribute("productCode", code);
        return "product_preview";
    }

    @GetMapping("/api/products/{code}")
    @ResponseBody
    Product getProductByCode(@PathVariable String code) {
        log.info("Fetching product details for code: {}", code);
        return catalogService.getProductByCode(code).getBody();
    }

    @PostMapping("/api/products")
    @ResponseBody
    Product createProduct(@RequestBody Product product) {
        log.info("Creating product: {}", product.code());
        Product created = catalogService.createProduct(product).getBody();
        if (created != null) {
            searchService.indexBook(new SearchServiceClient.BookDocumentResponse(
                    null, created.code(), created.name(), created.description(), null, created.price(), "Uncategorized", created.imageUrl()));
        }
        return created;
    }

    @PutMapping("/api/products/{code}")
    @ResponseBody
    Product updateProduct(@PathVariable String code, @RequestBody Product product) {
        log.info("Updating product: {}", code);
        Product updated = catalogService.updateProduct(code, product).getBody();
        if (updated != null) {
            searchService.indexBook(new SearchServiceClient.BookDocumentResponse(
                    null, updated.code(), updated.name(), updated.description(), null, updated.price(), "Uncategorized", updated.imageUrl()));
        }
        return updated;
    }

    @DeleteMapping("/api/products/{code}")
    @ResponseBody
    void deleteProduct(@PathVariable String code) {
        log.info("Deleting product: {}", code);
        catalogService.deleteProduct(code);
        searchService.deleteBook(code);
    }
}
