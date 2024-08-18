package ro.buza.returo.services;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class PdfRequest {
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
