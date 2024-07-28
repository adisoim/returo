package ro.buza.returo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.buza.returo.entities.Receipt;

@Repository
public interface ReceiptRepo extends JpaRepository<Receipt, Integer> {
}
