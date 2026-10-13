package home.payment.client;

import home.payment.client.dto.ProductDto;
import home.payment.exception.ProductNotFoundException;
import home.payment.exception.ProductServiceUnavailableException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Component
public class ProductClient {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public ProductClient(RestTemplate restTemplate,
                         @Value("${product-service.base-url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    public List<ProductDto> getProductsByUserId(Long userId) {
        String url = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .path("/api/v1/products")
                .queryParam("userId", userId)
                .toUriString();

        try {
            ResponseEntity<List<ProductDto>> response = restTemplate.exchange(
                    url, HttpMethod.GET, null,
                    new ParameterizedTypeReference<>() {}
            );
            return response.getBody();
        } catch (ResourceAccessException e) {
            throw new ProductServiceUnavailableException(
                    "Product service is unavailable: " + e.getMessage(), e);
        }
    }

    public ProductDto getProductById(Long productId) {
        String url = baseUrl + "/api/v1/products/" + productId;

        try {
            return restTemplate.getForObject(url, ProductDto.class);
        } catch (HttpClientErrorException.NotFound e) {
            throw new ProductNotFoundException("Product not found: " + productId, e);
        } catch (ResourceAccessException e) {
            throw new ProductServiceUnavailableException(
                    "Product service is unavailable: " + e.getMessage(), e);
        }
    }

    public ProductDto withdraw(Long productId, BigDecimal amount) {
        String url = baseUrl + "/api/v1/products/" + productId + "/withdraw";

        try {
            return restTemplate.postForObject(url, Map.of("amount", amount), ProductDto.class);
        } catch (HttpClientErrorException.NotFound e) {
            throw new ProductNotFoundException("Product not found: " + productId, e);
        } catch (HttpClientErrorException.Conflict e) {
            throw new IllegalStateException("Insufficient funds on product " + productId, e);
        } catch (ResourceAccessException e) {
            throw new ProductServiceUnavailableException(
                    "Product service is unavailable: " + e.getMessage(), e);
        }
    }
}