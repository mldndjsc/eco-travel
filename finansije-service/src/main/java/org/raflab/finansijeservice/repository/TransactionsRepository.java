package org.raflab.finansijeservice.repository;

import org.raflab.finansijeservice.model.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionsRepository extends JpaRepository<Transactions, Long> {

    @Query("SELECT SUM(t.amount) FROM Transactions t WHERE t.invoice.id = :invoiceId")
    Double sumPaidAmountByInvoiceId(@Param("invoiceId") Long invoiceId);

    // Složen upit: Filtriranje po opsegu datuma i nazivu metode plaćanja (npr. 'Credit Card', 'PayPal')
    @Query("SELECT t FROM Transactions t " +
            "JOIN t.paymentMethod pm " +
            "WHERE t.date >= :startDate " +
            "AND t.date <= :endDate " +
            "AND pm.methodName = :methodName")
    List<Transactions> findTransactionsByDateAndMethod(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("methodName") String methodName
    );
}
