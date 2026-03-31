package com.eventdriven.orderservice.outbox;

import com.eventdriven.orderservice.domain.OutboxEvent;
import com.eventdriven.orderservice.repository.OutboxRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


import java.time.Instant;
import java.util.List;

@Component
public class OutboxPublisher {

    private static final Logger log = LoggerFactory.getLogger(OutboxPublisher.class);
    private static final int BATCH_SIZE = 20;
    private static final int RETRY_DELAY_SECONDS = 30;

    private final OutboxRepository outboxRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public OutboxPublisher(
            OutboxRepository outboxRepository,
            KafkaTemplate<String, String> kafkaTemplate
    ) {
        this.outboxRepository = outboxRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Scheduled(fixedDelay = 5000)
    @Transactional
    public void publishPendingEvents() {


        List<OutboxEvent> events = outboxRepository.findEligibleEvents("PENDING",
                Instant.now(), PageRequest.of(0, BATCH_SIZE));

        if (events.isEmpty()) {
            log.info("No pending outbox events");
            return;
        }


        // 2. Process ONLY one event
//        OutboxEvent event = events.get(0);
//        log.info("Publishing outbox event {}", event.getId());

        for(OutboxEvent event: events) {
            try {
                // 3. Publish to Kafka and wait for ACK
                kafkaTemplate
                        .send(
                                "order-events",
                                event.getAggregateId().toString(),
                                event.getPayload()
                        )
                        .get();

                // 4. Mark event as SENT after successful publish
                event.setStatus("SENT");
                event.setPublishedAt(Instant.now());
                event.setLastError(null);
                event.setNextRetryAt(null);
                outboxRepository.save(event);

                log.info("Outbox event {} marked as SENT", event.getId());

            } catch (Exception ex){
                int nextAttempt = (event.getAttemptCount() == null ? 0 : event.getAttemptCount())+1;
                event.setAttemptCount(nextAttempt);
                event.setLastError(ex.getMessage());
                event.setNextRetryAt(Instant.now().plusSeconds(RETRY_DELAY_SECONDS));
                outboxRepository.save(event);

                log.error("Failed to publish outbox event {} on attempt {}", event.getId(), nextAttempt,ex);

            }
        }
    }
}