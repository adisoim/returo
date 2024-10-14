package ro.buza.returo.controllers;

import com.itextpdf.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.buza.returo.entities.Receipt;
import ro.buza.returo.entities.TotalReceipt;
import ro.buza.returo.entities.TotalVoucher;
import ro.buza.returo.services.*;

import java.awt.print.PrinterException;
import java.io.IOException;
import java.time.LocalDate;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class RegisterRecycledProducts {
    @Autowired
    ReceiptService receiptService;
    @Autowired
    TotalReceiptService totalReceiptService;
    @Autowired
    TotalVoucherService totalVoucherService;

    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<Receipt> register(@RequestBody InputData inputData) throws DocumentException, IOException, PrinterException {
        try {
            Receipt newReceipt = receiptService.saveReceipt(inputData);
            return new ResponseEntity<>(newReceipt, HttpStatus.OK);
        } catch (PrinterException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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

    @PostMapping("/generateAndPrintDailyVoucherTotalPdf")
    @ResponseBody
    public ResponseEntity<TotalVoucher> generateAndPrintDailyVoucherTotalPdf(@RequestBody PrintRequest printRequest) {
        try {
            TotalVoucher newTotalVoucher = totalVoucherService.saveTotalVoucher(printRequest.getDate());

            return new ResponseEntity<>(newTotalVoucher, HttpStatus.OK);
        } catch (IOException | PrinterException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/partialTotalReceipt")
    @ResponseBody
    public ResponseEntity<TotalReceipt> printPartialTotalReceipt() {
        try {
            TotalReceipt newTotalReceipt = totalReceiptService.generatePartialTotalReceipt(LocalDate.now());

            return new ResponseEntity<>(newTotalReceipt, HttpStatus.OK);
        } catch (IOException | PrinterException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/partialTotalVoucher")
    @ResponseBody
    public ResponseEntity<TotalVoucher> printPartialTotalVoucher() {
        try {
            TotalVoucher newTotalVoucher = totalVoucherService.generatePartialTotalVoucher(LocalDate.now());

            return new ResponseEntity<>(newTotalVoucher, HttpStatus.OK);
        } catch (IOException | PrinterException e) {
            e.printStackTrace(
            );
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
