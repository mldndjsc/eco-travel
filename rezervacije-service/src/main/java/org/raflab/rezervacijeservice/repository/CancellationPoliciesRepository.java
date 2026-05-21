package org.raflab.rezervacijeservice.repository;

import org.raflab.rezervacijeservice.model.CancellationPolicies;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CancellationPoliciesRepository extends JpaRepository<CancellationPolicies, Long> {
    Optional<CancellationPolicies> findByPolicyName(String policyName);
}
