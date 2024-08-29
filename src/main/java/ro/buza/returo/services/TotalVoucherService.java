package ro.buza.returo.services;

import org.springframework.stereotype.Service;
import ro.buza.returo.dao.ReceiptRepo;
import ro.buza.returo.dao.TotalVoucherRepo;
import ro.buza.returo.entities.Receipt;
import ro.buza.returo.entities.TotalVoucher;

import java.awt.print.PrinterException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class TotalVoucherService {
    public TotalVoucherService(ReceiptRepo receiptRepo, TotalVoucherRepo totalVoucherRepo, PdfService pdfService, PdfPrintService pdfPrintService) {
        this.receiptRepo = receiptRepo;
        this.totalVoucherRepo = totalVoucherRepo;
        this.pdfService = pdfService;
        this.pdfPrintService = pdfPrintService;
    }

    private final ReceiptRepo receiptRepo;
    private final TotalVoucherRepo totalVoucherRepo;
    private final PdfService pdfService;
    private final PdfPrintService pdfPrintService;

    public List<TotalVoucher> getAllVouchers() {
        return totalVoucherRepo.findAll();
    }

    public TotalVoucher getTotalVoucherById(Integer id) {
        Optional<TotalVoucher> optionalTotalVoucher = totalVoucherRepo.findById(id);
        return optionalTotalVoucher.orElse(null);
    }

    public TotalVoucher saveTotalVoucher(LocalDate localDate) throws IOException, PrinterException {
        LocalDateTime startOfDay = localDate.atStartOfDay();
        LocalDateTime endOfDay = localDate.atTime(LocalTime.MAX);

        List<Receipt> receipts = receiptRepo.findByLocalDateTimeBetween(startOfDay, endOfDay);

        TotalVoucher totalVoucher = new TotalVoucher(LocalDateTime.now());

        for (Receipt receipt : receipts) {
            if (receipt.uuid != null) {
                totalVoucher.totalMetal += receipt.totalMetal;
                totalVoucher.totalPlastic += receipt.totalPlastic;
                totalVoucher.totalGlass += receipt.totalGlass;
                totalVoucher.totalPrice += receipt.totalPrice;
                if (receipt.isUsed()) {
                    totalVoucher.totalReturnedVouchers++;
                    totalVoucher.totalPriceOfReturnedVouchers += receipt.totalPrice;
                }
                totalVoucher.totalVouchers++;
            }
        }

        TotalVoucher savedTotalVoucher = totalVoucherRepo.save(totalVoucher);

        String filePath = pdfService.generateDailyTotalVoucherPdf(savedTotalVoucher);

        pdfPrintService.printPdf(filePath);

        return savedTotalVoucher;
    }

    public TotalVoucher updateTotalVoucher(TotalVoucher totalVoucher) {
        Optional<TotalVoucher> existingTotalVoucher = totalVoucherRepo.findById(totalVoucher.getId());
        return existingTotalVoucher.map(totalVoucherRepo::save).orElse(null);
    }

    public void deleteTotalVoucherById(Integer id) {
        totalVoucherRepo.deleteById(id);
    }
}
