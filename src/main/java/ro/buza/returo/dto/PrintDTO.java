package ro.buza.returo.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Setter
@Getter
public class PrintDTO {
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;
}
