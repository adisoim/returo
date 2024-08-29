package ro.buza.returo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.buza.returo.entities.TotalVoucher;

@Repository
public interface TotalVoucherRepo extends JpaRepository<TotalVoucher, Integer> {
}
