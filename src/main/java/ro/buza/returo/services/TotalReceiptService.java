package ro.buza.returo.services;

import org.springframework.stereotype.Service;
import ro.buza.returo.dao.ReceiptRepo;
import ro.buza.returo.dao.TotalReceiptRepo;
import ro.buza.returo.entities.Receipt;
import ro.buza.returo.entities.TotalReceipt;

import java.awt.print.PrinterException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class TotalReceiptService {
    private final TotalReceiptRepo totalReceiptRepo;
    private final ReceiptRepo receiptRepo;
    private final PdfService pdfService;
    private final PdfPrintService pdfPrintService;

    public TotalReceiptService(TotalReceiptRepo totalReceiptRepo, ReceiptRepo receiptRepo, PdfService pdfService, PdfPrintService pdfPrintService) {
        this.totalReceiptRepo = totalReceiptRepo;
        this.receiptRepo = receiptRepo;
        this.pdfService = pdfService;
        this.pdfPrintService = pdfPrintService;
    }

    public List<TotalReceipt> getAllReceipts() {
        return totalReceiptRepo.findAll();
    }

    public TotalReceipt getTotalReceiptById(Integer id) {
        Optional<TotalReceipt> optionalTotalReceipt = totalReceiptRepo.findById(id);
        if (optionalTotalReceipt.isPresent()) {
            return optionalTotalReceipt.get();
        }
        return null;
    }

    public TotalReceipt saveTotalReceipt(LocalDate localDate) throws IOException, PrinterException {
        LocalDateTime startOfDay = localDate.atStartOfDay();
        LocalDateTime endOfDay = localDate.atTime(LocalTime.MAX);

        List<Receipt> receipts = receiptRepo.findByLocalDateTimeBetween(startOfDay, endOfDay);

        Optional<TotalReceipt> existingTotalReceipt = totalReceiptRepo.findByLocalDateTimeBetween(startOfDay, endOfDay);

        TotalReceipt totalReceipt;
        if (existingTotalReceipt.isPresent()) {
            totalReceipt = existingTotalReceipt.get();

            totalReceipt.totalGlass = 0;
            totalReceipt.totalPlastic = 0;
            totalReceipt.totalMetal = 0;
            totalReceipt.totalPrice = 0;
            totalReceipt.localDateTime = LocalDateTime.now();

        } else {
            totalReceipt = new TotalReceipt(LocalDateTime.now());

        }
        return getTotalReceipt(receipts, totalReceipt);
    }

    private TotalReceipt getTotalReceipt(List<Receipt> receipts, TotalReceipt totalReceipt) throws IOException, PrinterException {
        buildTotalReceipt(receipts, totalReceipt);
        TotalReceipt savedTotalReceipt = totalReceiptRepo.save(totalReceipt);

        String filePath = pdfService.generateDailyTotalPdf(savedTotalReceipt);

        pdfPrintService.printPdf(filePath);

        return savedTotalReceipt;
    }

    private void buildTotalReceipt(List<Receipt> receipts, TotalReceipt totalReceipt) {
        for (Receipt receipt : receipts) {
            if (receipt.uuid == null) {
                totalReceipt.totalMetal += receipt.totalMetal;
                totalReceipt.totalPlastic += receipt.totalPlastic;
                totalReceipt.totalGlass += receipt.totalGlass;
                totalReceipt.totalPrice += receipt.totalPrice;
            }
        }
    }

    public TotalReceipt generatePartialTotalReceipt(LocalDate localDate) throws IOException, PrinterException {
        LocalDateTime startOfDay = localDate.atStartOfDay();
        LocalDateTime endOfDay = localDate.atTime(LocalTime.MAX);

        List<Receipt> receipts = receiptRepo.findByLocalDateTimeBetween(startOfDay, endOfDay);

        TotalReceipt totalReceipt = new TotalReceipt(LocalDateTime.now());

        buildTotalReceipt(receipts, totalReceipt);
        String filePath = pdfService.generateDailyPartialTotalPdf(totalReceipt);

        pdfPrintService.printPdf(filePath);

        return totalReceipt;
    }

    public TotalReceipt updateTotalReceipt(TotalReceipt totalReceipt) {
        Optional<TotalReceipt> existingReceipt = totalReceiptRepo.findById(totalReceipt.id);
        return existingReceipt.map(totalReceiptRepo::save).orElse(null);
    }

    public void deleteTotalReceiptById(Integer id) {
        totalReceiptRepo.deleteById(id);
    }

}
