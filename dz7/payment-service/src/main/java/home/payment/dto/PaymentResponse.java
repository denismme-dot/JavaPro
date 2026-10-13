package home.payment.dto;

import java.math.BigDecimal;

public record PaymentResponse(
        Long productId,
        String accountNumber,
        BigDecimal amount,
        BigDecimal balanceAfter,
        String status
) {
}