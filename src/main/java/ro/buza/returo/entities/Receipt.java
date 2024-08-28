package ro.buza.returo.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "receipt")
public class Receipt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;
    @Column(name = "total_plastic")
    public int totalPlastic;
    @Column(name = "total_metal")
    public int totalMetal;
    @Column(name = "total_glass")
    public int totalGlass;
    @Column(name = "total_price")
    public double totalPrice;
    @Column(name = "date_time")
    public LocalDateTime localDateTime;
    @Column(name = "used", nullable = false)
    public boolean used = false;
    @Column(name = "uuid", unique = true)
    public String uuid;

    public Receipt(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    public Receipt() {
    }

    public void generateUUIDForVoucher() {
        this.uuid = UUID.randomUUID().toString();
    }
}
