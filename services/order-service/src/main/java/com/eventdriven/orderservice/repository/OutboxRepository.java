package com.eventdriven.orderservice.repository;

import com.eventdriven.orderservice.domain.OutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.time.Instant;
import java.util.List;
import java.util.UUID;


@Repository
public interface OutboxRepository extends JpaRepository<OutboxEvent, UUID> {

    @Query("""
    select e
    from OutboxEvent e
    where e.status = :status
      and (e.nextRetryAt is null or e.nextRetryAt <= :now)
    order by e.createdAt asc
""")


    public List<OutboxEvent> findEligibleEvents(String status, Instant now, Pageable pageable);

}
