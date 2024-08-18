package ro.buza.returo.services;

import com.itextpdf.text.DocumentException;
import org.springframework.stereotype.Service;
import ro.buza.returo.dao.ReceiptRepo;
import ro.buza.returo.entities.Receipt;

import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ReceiptService {
    private final ReceiptRepo receiptRepo;
    private final PdfService pdfService;
    private final PdfPrintService pdfPrintService;

    public ReceiptService(ReceiptRepo receiptRepo, PdfService pdfService, PdfPrintService pdfPrintService) {
        this.receiptRepo = receiptRepo;
        this.pdfService = pdfService;
        this.pdfPrintService = pdfPrintService;
    }

    public List<Receipt> getAllReceipts() {
        return receiptRepo.findAll();
    }

    public Receipt getTotalReceiptByDate(LocalDate localDate) {
        LocalDateTime startOfDay = localDate.atStartOfDay();
        LocalDateTime endOfDay = localDate.atTime(LocalTime.MAX);
        List<Receipt> receipts = receiptRepo.findByLocalDateTimeBetween(startOfDay, endOfDay);
        Receipt megaReceipt = new Receipt(LocalDateTime.now());
        for (Receipt receipt : receipts) {
            megaReceipt.totalMetal += receipt.totalMetal;
            megaReceipt.totalPlastic += receipt.totalPlastic;
            megaReceipt.totalGlass += receipt.totalGlass;
            megaReceipt.totalPrice += receipt.totalPrice;
        }
        return megaReceipt;
    }

    public List<Receipt> getReceiptsByDate(LocalDate localDate) {
        LocalDateTime startOfDay = localDate.atStartOfDay();
        LocalDateTime endOfDay = localDate.atTime(LocalTime.MAX);
        return receiptRepo.findByLocalDateTimeBetween(startOfDay, endOfDay);
    }

    public Receipt getReceiptById(Integer id) {
        Optional<Receipt> optionalReceipt = receiptRepo.findById(id);
        if (optionalReceipt.isPresent()) {
            return optionalReceipt.get();
        }

        return null;
    }

    public Receipt saveReceipt(InputData inputData) throws DocumentException, IOException, PrinterException {
        Receipt newReceipt = new Receipt();
        newReceipt.totalPlastic = inputData.plastic;
        newReceipt.totalGlass = inputData.glass;
        newReceipt.totalMetal = inputData.metal;
        newReceipt.totalPrice = 0.5 * (newReceipt.totalGlass + newReceipt.totalMetal + newReceipt.totalPlastic);
        newReceipt.localDateTime = LocalDateTime.now();
        Receipt savedReceipt = receiptRepo.save(newReceipt);

        String filePath = pdfService.generateReceiptPdf(savedReceipt);

        pdfPrintService.printReceiptPdf(filePath);

//      printReceipt(savedReceipt);

        return savedReceipt;
    }

    public Receipt saveReceiptAsVoucher(InputData inputData) throws Exception {
        Receipt newReceipt = new Receipt();
        newReceipt.totalPlastic = inputData.plastic;
        newReceipt.totalGlass = inputData.glass;
        newReceipt.totalMetal = inputData.metal;
        newReceipt.totalPrice = 0.5 * (newReceipt.totalGlass + newReceipt.totalMetal + newReceipt.totalPlastic);
        newReceipt.localDateTime = LocalDateTime.now();
        Receipt savedReceipt = receiptRepo.save(newReceipt);

        String filePath = pdfService.generateReceiptVoucherPdf(savedReceipt);

        pdfPrintService.printReceiptPdf(filePath);

        return savedReceipt;
    }

    private void printReceipt(Receipt receipt) throws PrinterException {
        PrinterJob printerJob = PrinterJob.getPrinterJob();
        printerJob.setPrintable(new ReceiptPrintable(receipt));

        if (printerJob.printDialog()) {
            printerJob.print();
        }
    }

    public Receipt updateReceipt(Receipt receipt) {
        Optional<Receipt> existingReceipt = receiptRepo.findById(receipt.id);
        return receiptRepo.save(receipt);
    }

    public void deleteReceiptById(Integer id) {
        receiptRepo.deleteById(id);
    }
}