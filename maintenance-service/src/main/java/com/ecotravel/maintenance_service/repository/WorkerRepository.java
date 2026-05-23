package com.ecotravel.maintenance_service.repository;
import com.ecotravel.maintenance_service.model.WorkOrderStatus;
import com.ecotravel.maintenance_service.model.Worker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkerRepository extends JpaRepository<Worker, Long> {
}
