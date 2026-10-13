package home.product.repository;

import home.product.entity.Product;
import home.product.entity.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAllByUserId(Long userId);

    List<Product> findAllByUserIdAndProductType(Long userId, ProductType productType);
}