package ro.buza.returo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.buza.returo.entities.Receipt;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReceiptRepo extends JpaRepository<Receipt, Integer> {
    List<Receipt> findByLocalDateTimeBetween(LocalDateTime startOfDay, LocalDateTime endOfDay);

    Optional<Receipt> findByUuid(String uuid);
}
