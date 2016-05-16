package server.service.printing;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.LineSeparator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import transferFiles.service.rmiService.IAdminService;
import transferFiles.model.denomination.Denomination;
import transferFiles.model.dish.ingridient.Ingridient;

import java.io.*;
import java.util.List;

@Component("reportsGenerator")
@Scope("singleton")
public class ReportsPDFGenerator implements Serializable {

    @Autowired(required = true)
    @Qualifier("adminService")
    private IAdminService service;

    private List<Denomination> denominations;
    private List<Ingridient> ingridients;

    private String REPORTDIRECTOTY = "D:/temp/";
    private String headString = "";

    private static final Logger LOGGER = Logger.getLogger(ReportsPDFGenerator.class);


    public File generatePdf(List<Denomination> denominations, List<Ingridient> ingridients, String header) throws FileNotFoundException {
        this.denominations = denominations;
        this.ingridients = ingridients;
        this.headString = header;
        Document document = new Document(PageSize.A5, 15, 15, 40, 40);
        PdfWriter writer = null;
        PdfPTable head = new PdfPTable(1);
        head.setHorizontalAlignment(Element.ALIGN_CENTER);
        initHead(head, header);
        PdfPTable denominationsTable = new PdfPTable(5);
        denominationsTable.setHorizontalAlignment(Element.ALIGN_CENTER);
        setDenominationTableColNames(denominationsTable);
        setDenominationTableRows(denominations, denominationsTable);

        PdfPTable mid = new PdfPTable(1);
        mid.setHorizontalAlignment(Element.ALIGN_CENTER);
        initMid(mid);
        PdfPTable ingridientsTable = new PdfPTable(3);
        denominationsTable.setHorizontalAlignment(Element.ALIGN_CENTER);
        setIngridientsTableColNames(ingridientsTable);
        setIngridientTableRows(ingridients, ingridientsTable);

        try {
            writer = PdfWriter.getInstance(document, new FileOutputStream(getFile(headString)));
            writer.setPageEvent(new BackgroundAdder());
            document.open();
            document.addAuthor("Generator created by Brytskyi");
            document.addSubject("Report");
            document.addCreationDate();
        } catch (DocumentException e) {
            LOGGER.error(e);
        }

        Paragraph paragraph = new Paragraph(30);
        paragraph.add(new Phrase("  "));
        try {
            document.add(new LineSeparator());
            document.add(head);
            document.add(denominationsTable);
            document.add(new LineSeparator());
            document.add(paragraph);
            document.add(mid);
            document.add(ingridientsTable);

        } catch (DocumentException e) {
            LOGGER.error(e);
        }
        document.close();
        return getFile(headString);
    }

    private void setIngridientTableRows(List<Ingridient> ingridients, PdfPTable ingridientsTable) {
        Font font = (FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.ITALIC));
        for (Ingridient ingridient : ingridients) {
            PdfPRow row = new PdfPRow(new PdfPCell[]{
                    new PdfPCell(new Phrase(ingridient.getProduct().getName(), font)),
                    new PdfPCell(new Phrase(ingridient.getProduct().getMesuarment().toString(), font)),
                    new PdfPCell(new Phrase(String.valueOf(ingridient.getAmount()), font)),
            });
            for (PdfPCell pdfPCell : row.getCells()) {
                ingridientsTable.addCell(pdfPCell);
            }
        }
        int size = ingridientsTable.getRows().size();
        for (int i = 1; i < size; i++) {
            for (PdfPCell pdfPCell : ingridientsTable.getRow(i).getCells()) {
                if (pdfPCell != null && pdfPCell.getPhrase() != null) {
                    pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    pdfPCell.setVerticalAlignment(Element.ALIGN_CENTER);
                    pdfPCell.setBorderWidth(0.4f);
                }
            }
        }
    }

    private void setIngridientsTableColNames(PdfPTable ingridientsTable) {
        try {
            ingridientsTable.setWidths(new float[]{3f, 3f, 2f});
        } catch (DocumentException e) {
            LOGGER.error(e);
        }
        ingridientsTable.addCell(new PdfPCell(new Phrase("Product")));
        ingridientsTable.addCell(new PdfPCell(new Phrase("Mesurement")));
        ingridientsTable.addCell(new PdfPCell(new Phrase("Amount")));
        for (PdfPCell pdfPCell : ingridientsTable.getRow(0).getCells()) {
            pdfPCell.getPhrase().setFont(FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD));
            pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            pdfPCell.setVerticalAlignment(Element.ALIGN_CENTER);
            pdfPCell.setBorderWidth(0.9f);
        }
    }

    private void initMid(PdfPTable mid) {
        Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN, 15, Font.BOLD);

        PdfPCell dishCell = new PdfPCell(new Phrase("Products", font));
        setBorderNull(dishCell);

        mid.addCell(dishCell);
    }

    private void initHead(PdfPTable head, String header) {

        Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN, 15, Font.BOLD);
        PdfPCell firstCell = new PdfPCell(new Phrase(header, font));
        setBorderNull(firstCell);

        PdfPCell dishCell = new PdfPCell(new Phrase("Dishes", font));
        setBorderNull(dishCell);

        head.addCell(firstCell);
        head.addCell(dishCell);
    }

    private void setBorderNull(PdfPCell cell) {
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(Rectangle.NO_BORDER);
    }

    private File getFile(String headString) {
        File file = new File(REPORTDIRECTOTY + headString + ".pdf");
        if (!file.exists()) try {
            file.createNewFile();
        } catch (IOException e) {
            LOGGER.error(e);
        }
        return file;
    }

    private void setDenominationTableRows(List<Denomination> denominations, PdfPTable denomTable) {
        Font font = (FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.ITALIC));
        double sum = 0;
        for (Denomination denomination : denominations) {
            PdfPRow row = new PdfPRow(new PdfPCell[]{
                    new PdfPCell(new Phrase(denomination.getDish().getName(), font)),
                    new PdfPCell(new Phrase(denomination.getDish().getType().toString(), font)),
                    new PdfPCell(new Phrase(String.valueOf(denomination.getPortion()), font)),
                    new PdfPCell(new Phrase(String.valueOf(denomination.getDish().getPriceForPortion()), font)),
                    new PdfPCell(new Phrase(String.valueOf(denomination.getPrice()), font)),
            });
            for (PdfPCell pdfPCell : row.getCells()) {
                denomTable.addCell(pdfPCell);
            }
            sum += denomination.getPrice();
        }
        PdfPCell cell = new PdfPCell();
        cell.setColspan(3);
        denomTable.addCell(cell);
        font = (FontFactory.getFont(FontFactory.TIMES_ROMAN, 13, Font.ITALIC));
        denomTable.addCell(new PdfPCell(new Phrase("Sum ", font)));
        denomTable.addCell(new PdfPCell(new Phrase(String.valueOf(sum), font)));
        int size = denomTable.getRows().size();
        for (int i = 1; i < size; i++) {
            for (PdfPCell pdfPCell : denomTable.getRow(i).getCells()) {
                if (pdfPCell != null && pdfPCell.getPhrase() != null) {
                    pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    pdfPCell.setVerticalAlignment(Element.ALIGN_CENTER);
                    pdfPCell.setBorderWidth(0.4f);
                }
            }
        }
        denomTable.getRow(denomTable.getRows().size() - 1).getCells()[denomTable.getRow(denomTable.getRows().size() - 1).getCells().length - 1].setBorderWidth(1f);
    }

    private void setDenominationTableColNames(PdfPTable denominations) {
        try {
            denominations.setWidths(new float[]{3f, 3f, 2f, 3f, 3f});
        } catch (DocumentException e) {
            LOGGER.error(e);
        }
        denominations.addCell(new PdfPCell(new Phrase("Dish")));
        denominations.addCell(new PdfPCell(new Phrase("Type")));
        denominations.addCell(new PdfPCell(new Phrase("Portions")));
        denominations.addCell(new PdfPCell(new Phrase("Price for portion")));
        denominations.addCell(new PdfPCell(new Phrase("Price")));
        for (PdfPCell pdfPCell : denominations.getRow(0).getCells()) {
            pdfPCell.getPhrase().setFont(FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD));
            pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            pdfPCell.setVerticalAlignment(Element.ALIGN_CENTER);
            pdfPCell.setBorderWidth(0.9f);
        }
    }

    private class BackgroundAdder extends PdfPageEventHelper {
        private int page = 1;

        @Override
        public void onEndPage(PdfWriter writer, Document document) {

            Font ffont = new Font(Font.FontFamily.UNDEFINED, 10, Font.ITALIC);
            PdfContentByte cb = writer.getDirectContent();
            Phrase header = new Phrase(headString + ", page" + page, ffont);
            ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
                    header,
                    (document.right() - document.left()) / 2 + document.leftMargin(),
                    document.top() + 25, 0);
            page++;
        }
    }

    public String getREPORTDIRECTOTY() {
        return REPORTDIRECTOTY;
    }

    public void setREPORTDIRECTOTY(String REPORTDIRECTOTY) {
        this.REPORTDIRECTOTY = REPORTDIRECTOTY;
    }
}
