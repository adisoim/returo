package ro.buza.returo.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

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
}
