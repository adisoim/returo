package ro.buza.returo.services;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.print.*;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.MediaSizeName;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;

@Service
public class PdfPrintService {
    @Value("${returo.printer.name}")
    String printerName;

    public void printPdf(String filePath) throws IOException, PrinterException {
        PDDocument document = PDDocument.load(new File(filePath));

        PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
        PrintService selectedPrintService = null;

        for (PrintService printService : printServices) {
            if (printService.getName().equalsIgnoreCase(printerName)) {
                selectedPrintService = printService;
                break;
            }
        }

        if (selectedPrintService != null) {
            PrinterJob printerJob = PrinterJob.getPrinterJob();
            printerJob.setPageable(new PDFPageable(document));

            PrintRequestAttributeSet attr = new HashPrintRequestAttributeSet();
            attr.add(MediaSizeName.ISO_A10);
            attr.add(new MediaPrintableArea(0, 0, 58, 297, MediaPrintableArea.MM)); // 58mm x 297mm

            printerJob.setPrintService(selectedPrintService);

            printerJob.print(attr);
        } else {
            System.err.println("Printer not found: " + printerName);
        }

        document.close();
    }
}
