package org.raflab.rezervacijeservice.repository;

import org.raflab.rezervacijeservice.model.BookingStatusLogs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingStatusLogsRepository extends JpaRepository<BookingStatusLogs, Long> {
}
