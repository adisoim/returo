package ro.buza.returo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.buza.returo.entities.TotalReceipt;

@Repository
public interface TotalReceiptRepo extends JpaRepository<TotalReceipt, Integer> {
}