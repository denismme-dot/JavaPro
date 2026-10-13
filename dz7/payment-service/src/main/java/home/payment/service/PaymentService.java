package home.payment.service;

import home.payment.client.ProductClient;
import home.payment.client.dto.ProductDto;
import home.payment.dto.PaymentRequest;
import home.payment.dto.PaymentResponse;
import home.payment.exception.InsufficientFundsException;
import home.payment.exception.ProductNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService {

    private final ProductClient productClient;

    public PaymentService(ProductClient productClient) {
        this.productClient = productClient;
    }

    public List<ProductDto> getClientProducts(Long userId) {
        return productClient.getProductsByUserId(userId);
    }

    public PaymentResponse executePayment(PaymentRequest request) {
        ProductDto product = productClient.getProductById(request.productId());

        if (product == null) {
            throw new ProductNotFoundException("Product not found: " + request.productId());
        }

        if (product.balance().compareTo(request.amount()) < 0) {
            throw new InsufficientFundsException(
                    "Insufficient funds on product " + product.id() +
                            ": balance=" + product.balance() + ", required=" + request.amount()
            );
        }

        ProductDto updated = productClient.withdraw(request.productId(), request.amount());

        return new PaymentResponse(
                updated.id(),
                updated.accountNumber(),
                request.amount(),
                updated.balance(),
                "SUCCESS"
        );
    }
}