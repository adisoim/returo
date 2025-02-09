package ro.buza.returo.services;

import com.itextpdf.text.DocumentException;
import org.springframework.stereotype.Service;
import ro.buza.returo.dao.ReceiptRepo;
import ro.buza.returo.dto.FormDTO;
import ro.buza.returo.entities.Receipt;

import java.awt.print.PrinterException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

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

    public void redeemVoucher(String uuid) throws NoSuchElementException {
        Optional<Receipt> optionalReceipt = receiptRepo.findByUuid(uuid);

        if (optionalReceipt.isEmpty()) {
            throw new NoSuchElementException("Voucher inexistent!");
        }

        Receipt receipt = optionalReceipt.get();
        if (receipt.isUsed()) {
            throw new IllegalStateException("Voucher deja folosit!");
        }

        if (receipt.getLocalDateTime().isBefore(LocalDateTime.now().minusDays(30))) {
            throw new IllegalStateException("Voucher mai vechi de 30 de zile!");
        }

        receipt.setUsed(true);
        receipt.setLocalDateTimeOfReturn(LocalDateTime.now());
        receiptRepo.save(receipt);
    }

    public Receipt getReceiptById(Integer id) {
        Optional<Receipt> optionalReceipt = receiptRepo.findById(id);
        if (optionalReceipt.isPresent()) {
            return optionalReceipt.get();
        }

        return null;
    }

    public Receipt saveReceipt(FormDTO inputData) throws DocumentException, IOException, PrinterException {
        Receipt newReceipt = new Receipt();
        buildReceipt(newReceipt, inputData);
        Receipt savedReceipt = receiptRepo.save(newReceipt);

        String filePath = pdfService.generateReceiptPdf(savedReceipt);

        pdfPrintService.printPdf(filePath);

        return savedReceipt;
    }

    public Receipt saveReceiptAsVoucher(FormDTO inputData) throws Exception {
        Receipt newReceipt = new Receipt();
        newReceipt.generateUUIDForVoucher();
        buildReceipt(newReceipt, inputData);
        Receipt savedReceipt = receiptRepo.save(newReceipt);

        String filePath = pdfService.generateReceiptVoucherPdf(savedReceipt);

        pdfPrintService.printPdf(filePath);

        return savedReceipt;
    }

    private void buildReceipt(Receipt newReceipt, FormDTO inputData) {
        newReceipt.totalPlastic = inputData.getPlastic();
        newReceipt.totalGlass = inputData.getGlass();
        newReceipt.totalMetal = inputData.getMetal();
        newReceipt.totalPrice = 0.5 * (newReceipt.totalGlass + newReceipt.totalMetal + newReceipt.totalPlastic);
        newReceipt.localDateTime = LocalDateTime.now();
    }

    public Receipt updateReceipt(Receipt receipt) {
        Optional<Receipt> existingReceipt = receiptRepo.findById(receipt.id);
        return receiptRepo.save(receipt);
    }

    public void deleteReceiptById(Integer id) {
        receiptRepo.deleteById(id);
    }
}