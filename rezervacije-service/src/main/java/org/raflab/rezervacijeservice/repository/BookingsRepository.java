package org.raflab.rezervacijeservice.repository;

import org.raflab.rezervacijeservice.model.Bookings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingsRepository extends JpaRepository<Bookings, Long> {

    // Pametna pretraga gostiju (Ignoriše velika/mala slova, traži deo reči, filtrira po statusu)
    @Query("SELECT b FROM Bookings b " +
            "JOIN b.guest g " +
            "WHERE (LOWER(g.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "   OR LOWER(g.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +
            "AND b.status = :status " +
            "ORDER BY b.totalPrice DESC")
    List<Bookings> searchGuestsBookings(
            @Param("searchTerm") String searchTerm,
            @Param("status") String status
    );
}
