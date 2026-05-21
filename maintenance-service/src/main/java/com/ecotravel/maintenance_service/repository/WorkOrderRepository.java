package com.ecotravel.maintenance_service.repository;
import com.ecotravel.maintenance_service.model.WorkOrder;
import com.ecotravel.maintenance_service.model.WorkOrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
@Repository
public interface WorkOrderRepository extends JpaRepository<WorkOrder, Long> {
    List<WorkOrder> findByStatus(WorkOrderStatus status);
    List<WorkOrder> findByWorkerIdAndStatusIn(Long workerId, List<WorkOrderStatus> statuses);


    @Query("SELECT CASE WHEN COUNT(w) > 0 THEN true ELSE false END FROM WorkOrder w " +
            "WHERE w.unitId = :unitId " +
            "AND w.worker.id = :workerId "  +
            "AND DATE(w.scheduledFor) = DATE(:date)")
    boolean existsDuplicate(@Param("unitId") Long unitId,
                            @Param("workerId") Long workerId,
                            @Param("date") LocalDateTime date);
}
