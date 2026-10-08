package home.controller;

import home.dto.ProductDto;
import home.entity.Product;
import home.entity.ProductType;
import home.entity.User;
import home.mapper.ProductMapper;
import home.service.ProductService;
import home.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;
    private final UserService userService;
    private final ProductMapper productMapper;

    public ProductController(ProductService productService,
                             UserService userService,
                             ProductMapper productMapper) {
        this.productService = productService;
        this.userService = userService;
        this.productMapper = productMapper;
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long productId) {
        return productService.getById(productId)
                .map(productMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<ProductDto> getProductsByUserId(@RequestParam("userId") Long userId) {
        return productService.getByUserId(userId).stream()
                .map(productMapper::toDto)
                .toList();
    }

    @GetMapping("/filter")
    public List<ProductDto> getProductsByUserIdAndType(
            @RequestParam("userId") Long userId,
            @RequestParam("type") ProductType type) {
        return productService.getByUserIdAndType(userId, type).stream()
                .map(productMapper::toDto)
                .toList();
    }
}