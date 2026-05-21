package org.raflab.rezervacijeservice.repository;

import org.raflab.rezervacijeservice.model.Guests;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GuestsRepository extends JpaRepository<Guests, Long> {
    Optional<Guests> findByEmail(String email);
}
