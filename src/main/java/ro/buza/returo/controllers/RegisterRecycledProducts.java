package ro.buza.returo.controllers;

import com.itextpdf.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.buza.returo.entities.Receipt;
import ro.buza.returo.entities.TotalReceipt;
import ro.buza.returo.services.*;

import java.awt.print.PrinterException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class RegisterRecycledProducts {
    @Autowired
    ReceiptService receiptService;
    @Autowired
    TotalReceiptService totalReceiptService;

    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<Receipt> register(@RequestBody InputData inputData) throws DocumentException, IOException, PrinterException {
        try {
            Receipt newReceipt = receiptService.saveReceipt(inputData);
            return new ResponseEntity<>(newReceipt, HttpStatus.OK);
        } catch (PrinterException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getTotalReceiptByDate")
    @ResponseBody
    public ResponseEntity<Receipt> getTotalReceiptByDate(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        Receipt receipt = receiptService.getTotalReceiptByDate(date);
        return new ResponseEntity<>(receipt, HttpStatus.OK);
    }

    @GetMapping("/getReceiptsByDate")
    @ResponseBody
    public ResponseEntity<List<Receipt>> getReceiptsByDate(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<Receipt> receipt = receiptService.getReceiptsByDate(date);
        return new ResponseEntity<>(receipt, HttpStatus.OK);
    }

    @PostMapping("/generateAndPrintDailyTotalPdf")
    @ResponseBody
    public ResponseEntity<TotalReceipt> generateAndPrintDailyTotalPdf(@RequestBody PrintRequest printRequest) {
        try {
            TotalReceipt newTotalReceipt = totalReceiptService.saveTotalReceipt(printRequest.getDate());

            return new ResponseEntity<>(newTotalReceipt, HttpStatus.OK);
        } catch (IOException | PrinterException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
