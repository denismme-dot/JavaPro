package home.service;

import home.entity.Product;
import home.entity.ProductType;
import home.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product create(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product must not be null");
        }
        return productRepository.save(product);
    }

    public Optional<Product> getById(Long id) {
        if (id == null) return Optional.empty();
        return productRepository.findById(id);
    }

    public List<Product> getByUserId(Long userId) {
        if (userId == null) return List.of();
        return productRepository.findAllByUserId(userId);
    }

    public List<Product> getByUserIdAndType(Long userId, ProductType type) {
        if (userId == null || type == null) return List.of();
        return productRepository.findAllByUserIdAndProductType(userId, type);
    }

    public List<Product> getAll() {
        return productRepository.findAll();
    }

    public void deleteAll() {
        productRepository.deleteAll();
    }
}