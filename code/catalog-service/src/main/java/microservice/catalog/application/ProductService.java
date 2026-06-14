package microservice.catalog.application;

import java.util.Optional;
import microservice.catalog.adapters.config.ApplicationProperties;
import microservice.catalog.adapters.persistent.ProductRepository;
import microservice.catalog.adapters.web.dto.PagedResult;
import microservice.catalog.adapters.web.dto.Product;
import microservice.catalog.adapters.web.mapper.ProductMapper;
import microservice.catalog.domain.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductService {
    private final ProductRepository productRepository;
    private final ApplicationProperties properties;

    ProductService(ProductRepository productRepository, ApplicationProperties properties) {
        this.productRepository = productRepository;
        this.properties = properties;
    }

    public PagedResult<Product> getProducts(int pageNo) {
        Sort sort = Sort.by("name").ascending();
        pageNo = pageNo <= 1 ? 0 : pageNo - 1;
        Pageable pageable = PageRequest.of(pageNo, properties.pageSize(), sort);
        Page<Product> productsPage = productRepository.findAll(pageable).map(ProductMapper::toProduct);

        return new PagedResult<>(
                productsPage.getContent(),
                productsPage.getTotalElements(),
                productsPage.getNumber() + 1,
                productsPage.getTotalPages(),
                productsPage.isFirst(),
                productsPage.isLast(),
                productsPage.hasNext(),
                productsPage.hasPrevious());
    }

    public Optional<Product> getProductByCode(String code) {
        return productRepository.findByCode(code).map(ProductMapper::toProduct);
    }

    public Product createProduct(Product product) {
        ProductEntity entity = new ProductEntity(
                null,
                product.code(),
                product.name(),
                product.description(),
                product.previewText(),
                product.imageUrl(),
                product.price());
        return ProductMapper.toProduct(productRepository.save(entity));
    }

    public Optional<Product> updateProduct(String code, Product product) {
        return productRepository.findByCode(code).map(existingEntity -> {
            existingEntity.setName(product.name());
            existingEntity.setDescription(product.description());
            existingEntity.setPreviewText(product.previewText());
            existingEntity.setImageUrl(product.imageUrl());
            existingEntity.setPrice(product.price());
            return ProductMapper.toProduct(productRepository.save(existingEntity));
        });
    }

    public void deleteProduct(String code) {
        productRepository.findByCode(code).ifPresent(productRepository::delete);
    }
}
