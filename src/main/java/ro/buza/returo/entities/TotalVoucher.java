package ro.buza.returo.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "total_voucher")
public class TotalVoucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;
    @Column(name = "total_plastic")
    public int totalPlastic;
    @Column(name = "total_metal")
    public int totalMetal;
    @Column(name = "total_glass")
    public int totalGlass;
    @Column(name = "date_time")
    public LocalDateTime localDateTime;
    @Column(name = "total_returned_vouchers")
    public int totalReturnedVouchers;
    @Column(name = "total_price_of_returned_vouchers")
    public double totalPriceOfReturnedVouchers;
    @Column(name = "total_vouchers")
    public int totalVouchers;
    @Column(name = "total_price")
    public double totalPrice;

    public TotalVoucher() {
    }

    public TotalVoucher(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }
}
