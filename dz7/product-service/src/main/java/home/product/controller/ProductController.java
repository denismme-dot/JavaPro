package home.product.controller;

import home.product.dto.ProductDto;
import home.product.entity.Product;
import home.product.mapper.ProductMapper;
import home.product.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;

    public ProductController(ProductService productService, ProductMapper productMapper) {
        this.productService = productService;
        this.productMapper = productMapper;
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> getById(@PathVariable Long productId) {
        return productService.getById(productId)
                .map(productMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<ProductDto> getByUserId(@RequestParam("userId") Long userId) {
        return productService.getByUserId(userId).stream()
                .map(productMapper::toDto)
                .toList();
    }

    @PostMapping("/{productId}/withdraw")
    public ResponseEntity<ProductDto> withdraw(@PathVariable Long productId,
                                               @RequestBody Map<String, BigDecimal> body) {
        BigDecimal amount = body.get("amount");
        Product updated = productService.withdraw(productId, amount);
        return ResponseEntity.ok(productMapper.toDto(updated));
    }

    @PostMapping("/{productId}/deposit")
    public ResponseEntity<ProductDto> deposit(@PathVariable Long productId,
                                              @RequestBody Map<String, BigDecimal> body) {
        BigDecimal amount = body.get("amount");
        Product updated = productService.deposit(productId, amount);
        return ResponseEntity.ok(productMapper.toDto(updated));
    }
}