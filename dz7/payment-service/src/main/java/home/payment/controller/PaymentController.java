package home.payment.controller;

import home.payment.client.dto.ProductDto;
import home.payment.dto.PaymentRequest;
import home.payment.dto.PaymentResponse;
import home.payment.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/clients/{userId}/products")
    public List<ProductDto> getClientProducts(@PathVariable Long userId) {
        return paymentService.getClientProducts(userId);
    }

    @PostMapping("/execute")
    public ResponseEntity<PaymentResponse> executePayment(@RequestBody PaymentRequest request) {
        return ResponseEntity.ok(paymentService.executePayment(request));
    }
}