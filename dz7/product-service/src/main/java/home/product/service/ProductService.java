package home.product.service;

import home.product.entity.Product;
import home.product.entity.ProductType;
import home.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Optional<Product> getById(Long id) {
        if (id == null) return Optional.empty();
        return productRepository.findById(id);
    }

    public List<Product> getByUserId(Long userId) {
        if (userId == null) return List.of();
        return productRepository.findAllByUserId(userId);
    }

    public List<Product> getAll() {
        return productRepository.findAll();
    }

    @Transactional
    public Product withdraw(Long productId, BigDecimal amount) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + productId));

        if (product.getBalance().compareTo(amount) < 0) {
            throw new IllegalStateException("Insufficient funds on product " + productId);
        }

        product.setBalance(product.getBalance().subtract(amount));
        return productRepository.save(product);
    }

    @Transactional
    public Product deposit(Long productId, BigDecimal amount) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + productId));

        product.setBalance(product.getBalance().add(amount));
        return productRepository.save(product);
    }

    public void deleteAll() {
        productRepository.deleteAll();
    }

    public Product create(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product must not be null");
        }
        return productRepository.save(product);
    }

    public List<Product> getByUserIdAndType(Long userId, ProductType type) {
        if (userId == null || type == null) return List.of();
        return productRepository.findAllByUserIdAndProductType(userId, type);
    }

}