package ro.buza.returo.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "total")
public class TotalReceipt {
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

    public TotalReceipt() {
    }

    public TotalReceipt(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTotalPlastic() {
        return totalPlastic;
    }

    public void setTotalPlastic(int totalPlastic) {
        this.totalPlastic = totalPlastic;
    }

    public int getTotalMetal() {
        return totalMetal;
    }

    public void setTotalMetal(int totalMetal) {
        this.totalMetal = totalMetal;
    }

    public int getTotalGlass() {
        return totalGlass;
    }

    public void setTotalGlass(int totalGlass) {
        this.totalGlass = totalGlass;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }
}
