package home.mapper;

import home.dto.ProductDto;
import home.entity.Product;
import home.entity.User;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductDto toDto(Product product) {
        if (product == null) return null;
        return new ProductDto(
                product.getId(),
                product.getAccountNumber(),
                product.getBalance(),
                product.getProductType(),
                product.getUser() != null ? product.getUser().getId() : null
        );
    }

    public Product toEntity(ProductDto dto, User user) {
        if (dto == null) return null;
        Product product = new Product();
        product.setId(dto.id());
        product.setAccountNumber(dto.accountNumber());
        product.setBalance(dto.balance());
        product.setProductType(dto.productType());
        product.setUser(user);
        return product;
    }
}