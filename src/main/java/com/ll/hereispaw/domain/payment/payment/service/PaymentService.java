package com.ll.hereispaw.domain.payment.payment.service;

import com.ll.hereispaw.domain.payment.payment.entity.Payment;
import com.ll.hereispaw.domain.payment.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;

    // 결제 후 response data를 DB에 저장
    @Transactional
    public Payment savePaymentData(JSONObject responseData) {
        JSONObject card = (JSONObject) responseData.get("card");
        Integer amount = 0;
        if (card != null) {
            amount = ((Long) card.get("amount")).intValue();
        }

        Payment payment = Payment.builder()
                .amount(amount)
                .build();

        return paymentRepository.save(payment);
    }
}
