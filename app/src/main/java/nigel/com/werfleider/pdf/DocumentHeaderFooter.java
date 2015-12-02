package nigel.com.werfleider.pdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.IOException;
import org.joda.time.DateTime;

/**
 * Created by nigel on 04/06/15.
 */
public class DocumentHeaderFooter extends PdfPageEventHelper {

    private PdfTemplate totalPages;

    protected BaseFont baseFont;

    final float footerTextSize = 8f;

    final static Font footerFont = new Font(
            Font.FontFamily.COURIER,
            8);

    public DocumentHeaderFooter() {

        super();
        try {
            baseFont = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.EMBEDDED);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override public void onOpenDocument(final PdfWriter writer, final Document document) {

        totalPages = writer.getDirectContent().createTemplate(
                100,
                100);
        totalPages.setBoundingBox(
                new Rectangle(
                        -20,
                        -20,
                        100,
                        100));
    }

    public void onEndPage(PdfWriter writer, com.itextpdf.text.Document document) {


        switch (writer.getPageNumber()) {
            case 1:

                Rectangle rect = writer.getBoxSize("art");
                Phrase phrase = new Phrase(
                        null,
                        footerFont);

                phrase.add(DateTime.now().toString("dd-MM-yyyy"));
                ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT, phrase,
                    rect.getLeft(), rect.getBottom(), 0);
                break;
            default:

                final PdfContentByte cb = writer.getDirectContent();
                cb.saveState();

                float textBase = document.bottom() - 20;

                cb.setFontAndSize(
                        baseFont,
                        footerTextSize);

                final String text = (writer.getPageNumber() - 1 + "/");
                float textSize = baseFont.getWidthPoint(
                        text,
                        footerTextSize);

                cb.beginText();

                float adjust = baseFont.getWidthPoint(
                        "0",
                        footerTextSize);
                cb.setTextMatrix(
                        document.right() - textSize - adjust,
                        textBase);
                cb.showText(text);
                cb.endText();
                cb.addTemplate(
                        totalPages,
                        document.right() - adjust,
                        textBase);
                cb.restoreState();
                break;
        }
    }

    @Override
    public void onCloseDocument(PdfWriter writer, Document document) {

        totalPages.beginText();
        totalPages.setFontAndSize(
                baseFont,
                footerTextSize);
        totalPages.setTextMatrix(
                0,
                0);

        totalPages.showText(String.valueOf(writer.getPageNumber() - 2));
        totalPages.endText();
    }


}
