package server.service.printing;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPrintable;

import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;


/*will be selected printer working by default*/
public class PdfPrinter {

    public static void print(File pdf) throws IOException, PrinterException {
        PDDocument document = PDDocument.load(pdf);
        PDFPrintable printable = new PDFPrintable(document);
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPrintable(printable);
        job.print();
    }

}
