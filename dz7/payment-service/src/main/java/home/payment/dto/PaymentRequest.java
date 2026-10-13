package home.payment.dto;

import java.math.BigDecimal;

public record PaymentRequest(
        Long productId,
        BigDecimal amount
) {
}