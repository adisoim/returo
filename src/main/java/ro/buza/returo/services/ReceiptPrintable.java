package ro.buza.returo.services;

import ro.buza.returo.entities.Receipt;

import java.awt.*;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.time.format.DateTimeFormatter;

public class ReceiptPrintable implements Printable {
    private final Receipt receipt;

    public ReceiptPrintable(Receipt receipt) {
        this.receipt = receipt;
    }

    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        if (pageIndex > 0) {
            return NO_SUCH_PAGE;
        }

        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

        int y = 20;
        graphics2D.drawString("FALKE A&TS SRL", 10, y);
        y += 15;
        graphics2D.drawString("JUD. CLUJ LOC. BUZA", 10, y);
        y += 15;
        graphics2D.drawString("NR.62", 10, y);
        y += 15;
        graphics2D.drawString("COD IDENTIFICARE FISCALA", 10, y);
        y += 15;
        graphics2D.drawString("RO214748", 10, y);
        y += 15;

        graphics2D.drawString("EXTRAGERE DIN CASA", 10, y);
        y += 15;
        graphics2D.drawString("NUMERAR: PLATI", 10, y);
        y += 15;
        graphics2D.drawString("-" + receipt.getTotalPrice() + " LEI", 10, y);
        y += 15;
        graphics2D.drawString(receipt.getTotalMetal() + " x SGR Metal", 10, y);
        y += 15;
        graphics2D.drawString(receipt.getTotalPlastic() + " x SGR Plastic", 10, y);
        y += 15;
        graphics2D.drawString(receipt.getTotalGlass() + " x SGR Sticla", 10, y);
        y += 15;

        graphics2D.drawString("BNF NR " + receipt.getId(), 10, y);
        y += 15;
        graphics2D.drawString("DATA: " + receipt.getLocalDateTime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")), 10, y);
        return PAGE_EXISTS;
    }
}
