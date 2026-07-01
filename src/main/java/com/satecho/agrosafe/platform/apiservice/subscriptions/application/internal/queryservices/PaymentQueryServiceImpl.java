package com.satecho.agrosafe.platform.apiservice.subscriptions.application.internal.queryservices;

import com.satecho.agrosafe.platform.apiservice.subscriptions.application.queryservices.PaymentQueryService;
import com.satecho.agrosafe.platform.apiservice.subscriptions.domain.model.aggregates.Payment;
import com.satecho.agrosafe.platform.apiservice.subscriptions.domain.repositories.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PaymentQueryServiceImpl implements PaymentQueryService {

    private final PaymentRepository paymentRepository;

    public PaymentQueryServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public List<Payment> findByUserId(Long userId) {
        return paymentRepository.findByUserId(userId);
    }

    @Override
    public Optional<Payment> findByTransactionId(String transactionId) {
        return paymentRepository.findByTransactionId(transactionId);
    }
}
