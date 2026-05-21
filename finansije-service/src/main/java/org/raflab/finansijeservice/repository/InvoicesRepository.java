package org.raflab.finansijeservice.repository;

import org.raflab.finansijeservice.model.Invoices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface InvoicesRepository extends JpaRepository<Invoices, Long> {

    Invoices findInvoicesByInvoiceNumber(String invoiceNumber);
}
