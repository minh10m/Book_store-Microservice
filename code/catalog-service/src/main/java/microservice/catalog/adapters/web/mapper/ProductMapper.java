package microservice.catalog.adapters.web.mapper;

import microservice.catalog.adapters.web.dto.Product;
import microservice.catalog.domain.ProductEntity;

public class ProductMapper {

    public static Product toProduct(ProductEntity productEntity) {
        return new Product(
                productEntity.getCode(),
                productEntity.getName(),
                productEntity.getDescription(),
                productEntity.getImageUrl(),
                productEntity.getPrice());
    }
}
