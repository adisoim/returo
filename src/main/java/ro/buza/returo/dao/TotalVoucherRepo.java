package ro.buza.returo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.buza.returo.entities.TotalVoucher;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface TotalVoucherRepo extends JpaRepository<TotalVoucher, Integer> {
    Optional<TotalVoucher> findByLocalDateTimeBetween(LocalDateTime localDateTimeAtStartOfDay, LocalDateTime localDateTimeAtEndOfDay);
}
