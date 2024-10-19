package ro.buza.returo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.buza.returo.entities.TotalReceipt;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface TotalReceiptRepo extends JpaRepository<TotalReceipt, Integer> {
    Optional<TotalReceipt> findByLocalDateTimeBetween(LocalDateTime localDateTimeAtStartOfDay, LocalDateTime localDateTimeAtEndOfDay);
}
