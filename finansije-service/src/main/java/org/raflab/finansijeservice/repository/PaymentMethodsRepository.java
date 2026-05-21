package org.raflab.finansijeservice.repository;

import org.raflab.finansijeservice.model.PaymentMethods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentMethodsRepository extends JpaRepository<PaymentMethods, Long> {
    PaymentMethods findPaymentMethodsByMethodName(String methodName);
}
