package ro.buza.returo.services;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ro.buza.returo.dao.ReceiptRepo;
import ro.buza.returo.entities.Receipt;
import ro.buza.returo.entities.TotalReceipt;
import ro.buza.returo.entities.TotalVoucher;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

import static com.itextpdf.text.FontFactory.HELVETICA_BOLD;

@Service
public class PdfService {
    private final ReceiptRepo receiptRepo;
    @Value("${returo.total.path}")
    String totalPath;
    @Value("${returo.receipt.path}")
    String receiptPath;

    @Autowired
    public PdfService(ReceiptRepo receiptRepo) {
        this.receiptRepo = receiptRepo;
    }

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    public String generateDailyTotalPdf(TotalReceipt totalReceipt) throws IOException {
        Rectangle pageSize = new Rectangle(58 * 2.43465f, 190f); // 58mm x 297mm
        Document document = new Document(pageSize, 5f, 5f, 5f, 5f); // Adjusted margins
        String filePath = totalPath + "\\total_zilnic_plata_numerar" + totalReceipt.getId() + ".pdf";

        try {
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            Font titleFont = FontFactory.getFont(HELVETICA_BOLD, 10, BaseColor.BLACK);
            Font headerFont = FontFactory.getFont(HELVETICA_BOLD, 8, BaseColor.BLACK);
            Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 8, BaseColor.BLACK);
            Font boldBodyFont = FontFactory.getFont(HELVETICA_BOLD, 8, BaseColor.BLACK);

            addCenteredParagraph(document, "FALKE A&TS SRL", titleFont);
            addCenteredParagraph(document, "JUD. CLUJ LOC. BUZA", headerFont);
            addCenteredParagraph(document, "NR.62", bodyFont);
            addCenteredParagraph(document, "COD IDENTIFICARE FISCALA", bodyFont);
            addCenteredParagraph(document, "RO214748", bodyFont);
            addCenteredParagraph(document, "EXTRAGERE DIN CASA", headerFont);
            addCenteredParagraph(document, "NUMERAR: TOTAL", boldBodyFont);
            addCenteredParagraph(document, "-" + totalReceipt.totalPrice + " LEI", boldBodyFont);

            addLeftAlignedParagraph(document, totalReceipt.totalMetal + " x SGR Metal", bodyFont);
            addLeftAlignedParagraph(document, totalReceipt.totalPlastic + " x SGR Plastic", bodyFont);
            addLeftAlignedParagraph(document, totalReceipt.totalGlass + " x SGR Sticla", bodyFont);
            addLeftAlignedParagraph(document, "DATA: " + totalReceipt.getLocalDateTime().format(formatter), bodyFont);
            addLeftAlignedParagraph(document, "BNF NR: " + totalReceipt.getId(), bodyFont);

            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return filePath;
    }

    public String generateDailyTotalVoucherPdf(TotalVoucher totalVoucher) throws IOException {
        Rectangle pageSize = new Rectangle(58 * 2.43465f, 220f); // 58mm x 297mm
        Document document = new Document(pageSize, 5f, 5f, 5f, 5f); // Adjusted margins
        String filePath = totalPath + "\\total_zilnic_vouchere_" + totalVoucher.getId() + ".pdf";

        try {
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            Font titleFont = FontFactory.getFont(HELVETICA_BOLD, 10, BaseColor.BLACK);
            Font headerFont = FontFactory.getFont(HELVETICA_BOLD, 8, BaseColor.BLACK);
            Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 8, BaseColor.BLACK);
            Font boldBodyFont = FontFactory.getFont(HELVETICA_BOLD, 8, BaseColor.BLACK);

            addCenteredParagraph(document, "FALKE A&TS SRL", titleFont);
            addCenteredParagraph(document, "JUD. CLUJ LOC. BUZA", headerFont);
            addCenteredParagraph(document, "NR.62", bodyFont);
            addCenteredParagraph(document, "COD IDENTIFICARE FISCALA", bodyFont);
            addCenteredParagraph(document, "RO214748", bodyFont);
            addCenteredParagraph(document, "VOUCHER", headerFont);
            addCenteredParagraph(document, "TOTAL VOUCHERE EMISE", boldBodyFont);
            addCenteredParagraph(document, totalVoucher.totalPrice + " LEI", boldBodyFont);
            addCenteredParagraph(document, "TOTAL VOUCHERE RETURNATE", boldBodyFont);
            addCenteredParagraph(document, totalVoucher.totalPriceOfReturnedVouchers + " LEI", boldBodyFont);

            addLeftAlignedParagraph(document, totalVoucher.totalMetal + " x SGR Metal", bodyFont);
            addLeftAlignedParagraph(document, totalVoucher.totalPlastic + " x SGR Plastic", bodyFont);
            addLeftAlignedParagraph(document, totalVoucher.totalGlass + " x SGR Sticla", bodyFont);
            addLeftAlignedParagraph(document, totalVoucher.totalReturnedVouchers + " x Vouchere returnate", bodyFont);
            addLeftAlignedParagraph(document, totalVoucher.totalVouchers + " x Vouchere emise", bodyFont);
            addLeftAlignedParagraph(document, "DATA: " + totalVoucher.getLocalDateTime().format(formatter), bodyFont);
            addLeftAlignedParagraph(document, "BNF NR: " + totalVoucher.getId(), bodyFont);

            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return filePath;
    }

    public String generateReceiptPdf(Receipt receipt) throws IOException, DocumentException {
        Rectangle pageSize = new Rectangle(58 * 2.43465f, 200f); // 58mm x 200mm
        Document document = new Document(pageSize, 5f, 5f, 5f, 5f); // Adjusted margins
        String filePath = receiptPath + "\\receipt_" + receipt.getId() + ".pdf";
        PdfWriter.getInstance(document, new FileOutputStream(filePath));

        document.open();
        Font titleFont = FontFactory.getFont(HELVETICA_BOLD, 10, BaseColor.BLACK);
        Font headerFont = FontFactory.getFont(HELVETICA_BOLD, 8, BaseColor.BLACK);
        Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 8, BaseColor.BLACK);

        addCenteredParagraph(document, "FALKE A&TS SRL", titleFont);
        addCenteredParagraph(document, "JUD. CLUJ LOC. BUZA", headerFont);
        addCenteredParagraph(document, "NR.62", bodyFont);
        addCenteredParagraph(document, "COD IDENTIFICARE FISCALA", bodyFont);
        addCenteredParagraph(document, "RO214748", bodyFont);
        addCenteredParagraph(document, "EXTRAGERE DIN CASA", headerFont);
        addCenteredParagraph(document, "NUMERAR: PLATI", headerFont);
        addCenteredParagraph(document, "-" + receipt.getTotalPrice() + " LEI", bodyFont);

        addLeftAlignedParagraph(document, receipt.getTotalMetal() + " x SGR Metal", bodyFont);
        addLeftAlignedParagraph(document, receipt.getTotalPlastic() + " x SGR Plastic", bodyFont);
        addLeftAlignedParagraph(document, receipt.getTotalGlass() + " x SGR Sticla", bodyFont);
        addLeftAlignedParagraph(document, "", bodyFont);

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);

        PdfPCell cell = new PdfPCell(new Phrase("BNF NR " + receipt.getId(), bodyFont));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("DATA: " + receipt.getLocalDateTime().format(formatter), bodyFont));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);

        document.add(table);

        document.close();

        return filePath;
    }

    public String generateReceiptVoucherPdf(Receipt receipt) throws Exception {
        Rectangle pageSize = new Rectangle(58 * 2.43465f, 297f); // 58mm x 297mm
        Document document = new Document(pageSize, 5f, 5f, 5f, 5f); // Adjusted margins
        String filePath = receiptPath + "\\receipt_" + receipt.getId() + ".pdf";
        PdfWriter.getInstance(document, new FileOutputStream(filePath));

        document.open();
        Font titleFont = FontFactory.getFont(HELVETICA_BOLD, 10, BaseColor.BLACK);
        Font headerFont = FontFactory.getFont(HELVETICA_BOLD, 8, BaseColor.BLACK);
        Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 8, BaseColor.BLACK);

        addCenteredParagraph(document, "FALKE A&TS SRL", titleFont);
        addCenteredParagraph(document, "JUD. CLUJ LOC. BUZA", headerFont);
        addCenteredParagraph(document, "NR.62", bodyFont);
        addCenteredParagraph(document, "COD IDENTIFICARE FISCALA", bodyFont);
        addCenteredParagraph(document, "RO214748", bodyFont);
        addCenteredParagraph(document, "VOUCHER", headerFont);
        addCenteredParagraph(document, receipt.getTotalPrice() + " LEI", bodyFont);

        addLeftAlignedParagraph(document, receipt.getTotalMetal() + " x SGR Metal", bodyFont);
        addLeftAlignedParagraph(document, receipt.getTotalPlastic() + " x SGR Plastic", bodyFont);
        addLeftAlignedParagraph(document, receipt.getTotalGlass() + " x SGR Sticla", bodyFont);
        addLeftAlignedParagraph(document, "", bodyFont);

        BufferedImage bi = QrCodeService.generateQRCodeImage(receipt.getUuid());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bi, "png", baos);
        Image qrCodeImage = Image.getInstance(baos.toByteArray());

        qrCodeImage.scaleToFit(100, 100);
        qrCodeImage.setAlignment(Element.ALIGN_CENTER);

        document.add(qrCodeImage);

        addCenteredParagraph(document, "Voucher valabil 30 de zile de la data emiterii!!!", bodyFont);

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);

        PdfPCell cell = new PdfPCell(new Phrase("BNF NR " + receipt.getId(), bodyFont));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("DATA: " + receipt.getLocalDateTime().format(formatter), bodyFont));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);

        document.add(table);

        document.close();

        return filePath;
    }

    private void addCenteredParagraph(Document document, String text, Font font) throws DocumentException {
        Paragraph paragraph = new Paragraph(text, font);
        paragraph.setAlignment(Element.ALIGN_CENTER);
        document.add(paragraph);
    }

    private void addLeftAlignedParagraph(Document document, String text, Font font) throws DocumentException {
        Paragraph paragraph = new Paragraph(text, font);
        paragraph.setAlignment(Element.ALIGN_LEFT);
        document.add(paragraph);
    }
}
