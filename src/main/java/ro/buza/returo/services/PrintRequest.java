package ro.buza.returo.services;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class PrintRequest {
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;

    // Getters and setters
    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
