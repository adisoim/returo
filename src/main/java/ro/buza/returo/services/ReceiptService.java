package ro.buza.returo.services;

import org.springframework.stereotype.Service;
import ro.buza.returo.dao.ReceiptRepo;
import ro.buza.returo.entities.Receipt;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReceiptService {
    private final ReceiptRepo receiptRepo;

    public ReceiptService(ReceiptRepo receiptRepo) {
        this.receiptRepo = receiptRepo;
    }

    public List<Receipt> getAllReceipts() {
        return receiptRepo.findAll();
    }

    public Receipt getReceiptById(Integer id) {
        Optional<Receipt> optionalReceipt = receiptRepo.findById(id);
        if (optionalReceipt.isPresent()) {
            return optionalReceipt.get();
        }

        return null;
    }

    public Receipt saveReceipt(InputData inputData) {
        Receipt newReceipt = new Receipt();
        newReceipt.totalPlastic = inputData.plastic;
        newReceipt.totalGlass = inputData.glass;
        newReceipt.totalMetal = inputData.metal;
        newReceipt.totalPrice = 0.5 * (newReceipt.totalGlass + newReceipt.totalMetal + newReceipt.totalPlastic);
        newReceipt.localDateTime = LocalDateTime.now();
        Receipt savedReceipt = receiptRepo.save(newReceipt);
        return savedReceipt;
    }

    public Receipt updateReceipt(Receipt receipt) {
        Optional<Receipt> existingReceipt = receiptRepo.findById(receipt.id);
        Receipt updatedReceipt = receiptRepo.save(receipt);
        return updatedReceipt;
    }

    public void deleteReceiptById(Integer id) {
        receiptRepo.deleteById(id);
    }
}