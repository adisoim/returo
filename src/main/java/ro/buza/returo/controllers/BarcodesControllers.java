package ro.buza.returo.controllers;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.BufferedImageHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.*;
import ro.buza.returo.dto.FormDTO;
import ro.buza.returo.entities.Receipt;
import ro.buza.returo.services.ReceiptService;

import java.awt.image.BufferedImage;
import java.awt.print.PrinterException;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/barcodes")
@CrossOrigin
public class BarcodesControllers {
    private final ReceiptService receiptService;

    public BarcodesControllers(ReceiptService receiptService) {
        this.receiptService = receiptService;
    }

    @PostMapping("/qr")
    @ResponseBody
    public ResponseEntity<Receipt> qrCodeGen(@RequestBody FormDTO inputData) throws Exception {
        try {
            Receipt newReceipt = receiptService.saveReceiptAsVoucher(inputData);

            return new ResponseEntity<>(newReceipt, HttpStatus.OK);
        } catch (PrinterException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/redeem")
    @ResponseBody
    public ResponseEntity<String> redeemVoucher(@RequestParam String uuid) {
        try {
            receiptService.redeemVoucher(uuid);
            return ResponseEntity.ok("Voucher folosit cu succes!");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @Bean
    public HttpMessageConverter<BufferedImage> createImageHttpMessageConverter() {
        return new BufferedImageHttpMessageConverter();
    }
}
