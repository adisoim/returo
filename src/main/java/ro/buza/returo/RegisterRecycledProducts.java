package ro.buza.returo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.buza.returo.entities.Receipt;
import ro.buza.returo.services.InputData;
import ro.buza.returo.services.ReceiptService;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class RegisterRecycledProducts {
    @Autowired
    ReceiptService receiptService;

    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<Receipt> register(@RequestBody InputData inputData) {

        receiptService.saveReceipt(inputData);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
