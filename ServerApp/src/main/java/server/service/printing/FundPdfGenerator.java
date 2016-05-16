package server.service.printing;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.LineSeparator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import transferFiles.service.rmiService.IBarmenService;
import transferFiles.model.denomination.Denomination;
import transferFiles.model.fund.Fund;
import transferFiles.model.order.Ordering;

import java.io.*;
import java.net.MalformedURLException;

@Component("fundGenerator")
@Scope("singleton")
public class FundPdfGenerator implements Serializable {

    @Autowired(required = true)
    @Qualifier("barmenService")
    private IBarmenService service;

    private Ordering ordering = null;

    private String FUNDSDIRECTOTY = "D:/temp/";

    private static final Logger LOGGER = Logger.getLogger(FundPdfGenerator.class);


    public File generatePdf(Ordering orderingSource) throws FileNotFoundException {
        this.ordering = orderingSource;
        Document document = new Document(PageSize.A5, 15, 15, 40, 40);
        PdfWriter writer = null;
        PdfPTable head = new PdfPTable(3);
        head.setHorizontalAlignment(Element.ALIGN_CENTER);
        initHead(head, orderingSource);
        PdfPTable denominations = new PdfPTable(5);
        denominations.setHorizontalAlignment(Element.ALIGN_CENTER);
        setColNames(denominations);
        setRows(orderingSource.getFund(), denominations, orderingSource);
        try {
            writer = PdfWriter.getInstance(document, new FileOutputStream(getFile(orderingSource)));
            writer.setPageEvent(new BackgroundAdder());
            document.open();
            document.addAuthor("Generator created by Brytskyi");
            document.addCreator(orderingSource.getWhoServesOrder().getName());
            document.addSubject("Fund");
            document.addCreationDate();
        } catch (DocumentException e) {
            LOGGER.error(e);
        }

        Paragraph paragraph = new Paragraph(30);
        paragraph.add(new Phrase("  "));
        try {
            PdfContentByte canvas = writer.getDirectContentUnder();
            Image image = Image.getInstance("ServerApp/src/main/java/server/service/printing/backgroung.gif");
            image.scaleAbsolute(PageSize.A5);
            image.setAbsolutePosition(0, 0);
            canvas.addImage(image);
            document.add(new LineSeparator());
            document.add(head);
            document.add(paragraph);
            document.add(denominations);
        } catch (DocumentException e) {
            LOGGER.error(e);
        } catch (MalformedURLException e) {
            LOGGER.error(e);
        } catch (IOException e) {
            LOGGER.error(e);
        }
        document.close();
        return getFile(orderingSource);
    }

    private void initHead(PdfPTable head, Ordering orderingSource) {
        try {
            head.setWidths(new float[]{5f, 5f, 8f});
        } catch (DocumentException e) {
            LOGGER.error(e);
        }
        Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD);
        PdfPCell numCell = new PdfPCell(new Phrase("# " + orderingSource.getId(), font));
        numCell.setColspan(3);
        setBorderNull(numCell);

        PdfPCell createdCell = new PdfPCell(new Phrase("Created on " + orderingSource.getDateOrderCreated().toString(), font));
        setBorderNull(createdCell);

        PdfPCell taken = new PdfPCell(new Phrase("Taken by " + orderingSource.getWhoTakenOrder().getName(), font));
        setBorderNull(taken);

        PdfPCell desc = new PdfPCell(new Phrase(orderingSource.getDescription(), font));
        desc.setRowspan(3);
        setBorderNull(desc);
        desc.setHorizontalAlignment(Element.ALIGN_LEFT);
        desc.setVerticalAlignment(Element.ALIGN_CENTER);

        PdfPCell comeTimeCell = new PdfPCell(new Phrase("Starts on " + orderingSource.getDateClientsCome().toString(), font));
        setBorderNull(comeTimeCell);

        PdfPCell serves = new PdfPCell(new Phrase("Served by " + orderingSource.getWhoServesOrder().getName(), font));
        setBorderNull(serves);

        PdfPCell amount = new PdfPCell(new Phrase(orderingSource.getAmountOfPeople() + " persons", font));
        setBorderNull(amount);

        PdfPCell advance = new PdfPCell(new Phrase("Advance Pay = " + orderingSource.getAdvancePayment(), font));
        setBorderNull(advance);

        head.addCell(numCell);
        head.addCell(createdCell);
        head.addCell(taken);
        head.addCell(desc);
        head.addCell(comeTimeCell);
        head.addCell(serves);
        head.addCell(amount);
        head.addCell(advance);

    }

    private void setBorderNull(PdfPCell cell) {
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(Rectangle.NO_BORDER);
    }

    private File getFile(Ordering orderingSource) {
        File file = new File(FUNDSDIRECTOTY + orderingSource.getId() + ".pdf");
        if (!file.exists()) try {
            file.createNewFile();
        } catch (IOException e) {
            LOGGER.error(e);
        }
        return file;
    }

    private void setRows(Fund fundd, PdfPTable denominations, Ordering ordering) {
        int denomNum = 1;
        Font font = (FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.ITALIC));
        for (Denomination denomination : service.getDenominationsByOrderForFund(ordering)) {
            PdfPRow row = new PdfPRow(new PdfPCell[]{
                    new PdfPCell(new Phrase(String.valueOf(denomNum), font)),
                    new PdfPCell(new Phrase(denomination.getDish().getName(), font)),
                    new PdfPCell(new Phrase(String.valueOf(denomination.getPortion()), font)),
                    new PdfPCell(new Phrase(String.valueOf(denomination.getDish().getPriceForPortion()), font)),
                    new PdfPCell(new Phrase(String.valueOf(denomination.getPrice()), font)),
            });
            denomNum++;
            for (PdfPCell pdfPCell : row.getCells()) {
                denominations.addCell(pdfPCell);
            }
        }
        PdfPCell cell = new PdfPCell();
        cell.setColspan(3);
        cell.setRowspan(4);
        cell.setBorderWidthLeft(0);
        cell.setBorderWidthBottom(0);
        denominations.addCell(cell);
        font = (FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.ITALIC));
        denominations.addCell(new PdfPCell(new Phrase("Price ", font)));
        denominations.addCell(new PdfPCell(new Phrase(String.valueOf(fundd.getPrice()), font)));
        denominations.addCell(new PdfPCell(new Phrase("KO " + fundd.getKo() * 100 + " %", font)));
        denominations.addCell(new PdfPCell(new Phrase(String.format("%.2f", fundd.getPrice() * fundd.getKo(), font))));
        denominations.addCell(new PdfPCell(new Phrase("Avance", font)));
        denominations.addCell(new PdfPCell(new Phrase(String.valueOf(ordering.getAdvancePayment()), font)));
        denominations.addCell(new PdfPCell(new Phrase("Total", font)));
        denominations.addCell(new PdfPCell(new Phrase(String.valueOf(fundd.getFinalPrice()), font)));
        int size = denominations.getRows().size();
        for (int i = 1; i < size; i++) {
            for (PdfPCell pdfPCell : denominations.getRow(i).getCells()) {
                if (pdfPCell != null && pdfPCell.getPhrase() != null) {
                    pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    pdfPCell.setVerticalAlignment(Element.ALIGN_CENTER);
                    pdfPCell.setBorderWidth(0.5f);
                }
            }
        }
        denominations.getRow(denominations.getRows().size() - 1).getCells()[denominations.getRow(denominations.getRows().size() - 1).getCells().length - 1].setBorderWidth(1f);
    }

    private void setColNames(PdfPTable denominations) {
        try {
            denominations.setWidths(new float[]{2f, 8f, 4f, 4f, 5f});
        } catch (DocumentException e) {
            LOGGER.error(e);
        }
        denominations.addCell(new PdfPCell(new Phrase("#")));
        denominations.addCell(new PdfPCell(new Phrase("Denomination")));
        denominations.addCell(new PdfPCell(new Phrase("Portions")));
        denominations.addCell(new PdfPCell(new Phrase("Price for portion")));
        denominations.addCell(new PdfPCell(new Phrase("Price")));
        for (PdfPCell pdfPCell : denominations.getRow(0).getCells()) {
            pdfPCell.getPhrase().setFont(FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD));
            pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            pdfPCell.setVerticalAlignment(Element.ALIGN_CENTER);
            pdfPCell.setBorderWidth(1f);
        }
    }

    private class BackgroundAdder extends PdfPageEventHelper {
        private int page = 1;

        @Override
        public void onEndPage(PdfWriter writer, Document document) {

            Image background = null;
            try {
                background = Image.getInstance("ServerApp/src/main/java/server/service/printing/backgroung.gif");
            } catch (BadElementException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            float width = document.getPageSize().getWidth();
            float height = document.getPageSize().getHeight();
            try {
                writer.getDirectContentUnder()
                        .addImage(background, width, 0, 0, height, 0, 0);
            } catch (DocumentException e) {
                e.printStackTrace();
            }
            Font ffont = new Font(Font.FontFamily.UNDEFINED, 10, Font.ITALIC);
            PdfContentByte cb = writer.getDirectContent();
            Phrase header = new Phrase("Ordering #" + ordering.getId() + ", page" + page, ffont);
            ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
                    header,
                    (document.right() - document.left()) / 2 + document.leftMargin(),
                    document.top() + 25, 0);
            page++;
        }
    }

    public String getFUNDSDIRECTOTY() {
        return FUNDSDIRECTOTY;
    }

    public void setFUNDSDIRECTOTY(String FUNDSDIRECTOTY) {
        this.FUNDSDIRECTOTY = FUNDSDIRECTOTY;
    }

}
