package nigel.com.werfleider.pdf;

/**
 * Created by nigel on 26/11/14.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;

import org.joda.time.DateTime;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import javax.inject.Inject;

import nigel.com.werfleider.model.Document;
import nigel.com.werfleider.model.DocumentImage;
import nigel.com.werfleider.model.DocumentLocation;
import nigel.com.werfleider.model.WerfInt;

import static com.itextpdf.text.Element.ALIGN_CENTER;
import static com.itextpdf.text.Element.ALIGN_LEFT;
import static java.lang.String.format;
import static nigel.com.werfleider.model.DocumentType.OPMETINGEN;
import static nigel.com.werfleider.model.DocumentType.AS_BUILT;
import static org.joda.time.DateTime.now;

public class FileOperations {

    public static final String DATE_FORMAT = "dd-MM-yyyy HH:mm";

    final static Font titleFont = new Font(
            Font.FontFamily.HELVETICA,
            24);

    final static Font paragraphTitleFont = new Font(
            Font.FontFamily.HELVETICA,
            18,
            Font.BOLD);

    final static Font paragraphSubTitleFont = new Font(
            Font.FontFamily.HELVETICA,
            16,
            Font.BOLD);

    final static Font titleFontBoldUnderline = new Font(
            Font.FontFamily.HELVETICA,
            24,
            Font.BOLD | Font.UNDERLINE);

    final static Font paragraphFont = new Font(
            Font.FontFamily.HELVETICA,
            10);

    final static Font footerFont = new Font(
            Font.FontFamily.HELVETICA,
            8);

    final static int top = 820;

    final static int right = 600;

    final static int margin = 60;

    final Context context;

    @Inject
    public FileOperations(final Context context) {

        this.context = context;
    }

    private static void createThirdPage(com.itextpdf.text.Document document) throws DocumentException {

        document.add(new Paragraph("\n\n"));
        Paragraph paragraph = new Paragraph(
                "INLEIDENDE NOTA’S",
                paragraphTitleFont);
        paragraph.setSpacingAfter(30);
        paragraph.setAlignment(ALIGN_CENTER);
        document.add(paragraph);

        List unorderedList = new List(List.UNORDERED);
        unorderedList.add(
                new ListItem(
                        "Tijdens de rondgang zijn een aantal beschreven vaststellingen fotografisch vast gelegd. Deze foto’s " +
                                "zijn digitaal genomen en kunnen bij discussies of onduidelijkheden worden uitvergroot naar details. In " +
                                "het verslag wordt selectie van de foto’s  opgenomen, de omstandige reeks wordt digitaal " +
                                "gearchiveerd."));
        unorderedList.add(
                new ListItem(
                        "Verwering, slijtage en mankementen, eigen aan het bouwwerk of de materialen worden als normaal " +
                                "beschouwd en niet beschreven."));

        document.add(unorderedList);
    }

    private static void createSecondPage(com.itextpdf.text.Document document, final WerfInt werf) throws DocumentException {

        document.add(new Paragraph("\n\n"));
        Paragraph paragraph = new Paragraph(
                "PROCES-VERBAAL VAN PLAATSBESCHRIJVING\n" +
                        "  VOOR WERKEN",
                paragraphTitleFont);
        paragraph.setSpacingAfter(30);
        paragraph.setAlignment(ALIGN_CENTER);
        document.add(paragraph);

        paragraph = new Paragraph(
                "Staat van Bevinding",
                paragraphSubTitleFont);
        paragraph.setAlignment(ALIGN_CENTER);
        paragraph.setSpacingAfter(30);
        document.add(paragraph);

        document.add(paragraphNoIndention(now().toString("dd-MM-yyyy")));
        document.add(paragraphNoIndention("Ondergetekende Gust Dickens, hoofdprojectleider voor de firma FR. Goedleven gevestigd te Brasschaat, Pauwelslei 168."));
        document.add(
                paragraphNoIndention(
                        format(
                                "Handelend in opdracht van:         %s",
                                werf.getOpdrachtgever())));
        document.add(paragraphNoIndention("Met het doel een P.V. van bevinding gelijktijdig en tegensprekelijk op te stellen van het onroerend goed:"));
        document.add(
                paragraphNoIndention(
                        format(
                                "%s %s",
                                werf.getOpdrachtgever(),
                                werf.getOpdrachtgeverStad())));
        document.add(paragraphNoIndention(werf.getOpdrachtgeverAdres()));
        document.add(
                paragraphNoIndention(
                        format(
                                "Verklaart zich op %s ter plaatse te hebben begeven om onderstaande vaststellingen te doen en op te tekenen voor het pand.",
                                now())));
        document.add(paragraphNoIndention("Onderhavige plaatsbeschrijving wordt opgemaakt om mogelijke schade aan beschreven goederen vast te stellen door het demonteren van  driehoek, pinakel, loszittende vazen en steunen."));
    }

    private static void createFirstPagePlaatsbeschrijf(PdfContentByte cb, final WerfInt werf) throws IOException, DocumentException {

        BaseFont bf_helv_bold = BaseFont.createFont(
                BaseFont.HELVETICA_BOLD,
                "Cp1252",
                false);
        BaseFont bf_helv = BaseFont.createFont(
                BaseFont.HELVETICA,
                "Cp1252",
                false);

        cb.beginText();
        cb.setFontAndSize(
                bf_helv_bold,
                16);

        cb.showTextAligned(
                PdfContentByte.ALIGN_CENTER,
                format(
                        "%s -  %s",
                        werf.getOpdrachtgever().toUpperCase(),
                        werf.getOmschrijving().toUpperCase()),
                300,
                returnLinePosition(0.75),
                0);
        cb.showTextAligned(
                PdfContentByte.ALIGN_CENTER,
                format(
                        "%s - %s",
                        werf.getOpdrachtStad(),
                        werf.getOpdrachtAdres()),
                300,
                returnLinePosition(0.72),
                0);

        cb.setFontAndSize(
                bf_helv_bold,
                17);
        cb.showTextAligned(
                PdfContentByte.ALIGN_CENTER,
                "PLAATSBESCHRIJVING VOOR AANVANG DER WERKEN",
                300,
                returnLinePosition(0.55),
                0);
        cb.showTextAligned(
                PdfContentByte.ALIGN_CENTER,
                format(
                        "UITGEVOERD OP %s",
                        werf.getDatumAanvang().toString("dd/MM/yyyy")),
                300,
                returnLinePosition(0.52),
                0);

        cb.setFontAndSize(
                bf_helv,
                12);
        cb.showTextAligned(
                PdfContentByte.ALIGN_LEFT,
                "Opdrachtgever:",
                margin,
                returnLinePosition(0.3),
                0);
        cb.showTextAligned(
                PdfContentByte.ALIGN_LEFT,
                werf.getOpdrachtgever(),
                margin,
                returnLinePosition(0.28),
                0);
        cb.showTextAligned(
                PdfContentByte.ALIGN_LEFT,
                "Ontwerper:",
                margin,
                returnLinePosition(0.25),
                0);
        cb.showTextAligned(
                PdfContentByte.ALIGN_LEFT,
                werf.getOntwerper(),
                margin,
                returnLinePosition(0.23),
                0);
        cb.showTextAligned(
                PdfContentByte.ALIGN_LEFT,
                werf.getOntwerperAdres(),
                margin,
                returnLinePosition(0.21),
                0);
        cb.showTextAligned(
                PdfContentByte.ALIGN_LEFT,
                werf.getOntwerperStad(),
                margin,
                returnLinePosition(0.19),
                0);
        cb.endText();
    }

    private static Paragraph paragraph(final String text) {

        return paragraph(
                text,
                ALIGN_LEFT,
                150,
                20,
                paragraphFont);
    }

    private static Paragraph paragraphNoIndention(final String text) {

        return paragraph(
                text,
                ALIGN_LEFT,
                0,
                20,
                paragraphFont);
    }

    private static Paragraph paragraph(final String text, final int alignment, final float indention, final int spacingAfter, final Font font) {

        final Paragraph paragraph = new Paragraph(
                text,
                font);
        paragraph.setAlignment(alignment);
        paragraph.setIndentationLeft(indention);
        paragraph.setSpacingAfter(spacingAfter);
        return paragraph;
    }

    private static Paragraph spacingParagraph() {

        return new Paragraph("\n\n");
    }

    public static int returnLinePosition(double position) {

        return (int) (top * position);
    }

    private void createFirstPageDocuments(final WerfInt werf, final Document document, final com.itextpdf.text.Document iTextDocument) throws IOException, DocumentException {

        final float INDENTION = 150;

        iTextDocument.add(spacingParagraph());
        iTextDocument.add(
                paragraph(
                        format(
                                "%s:",
                                document.getDocumentType().name().toLowerCase()),
                        ALIGN_CENTER,
                        0,
                        10,
                        titleFontBoldUnderline));

        iTextDocument.add(paragraph("Locaties: "));

        final List unorderedList = new List(List.UNORDERED);
        for (DocumentLocation s : document.getFotoReeksList()) {
            unorderedList.add(
                    new ListItem(
                            s.getLocation(),
                            paragraphFont));
        }

        unorderedList.setIndentationLeft(INDENTION + 40);

        iTextDocument.add(unorderedList);

        iTextDocument.add(spacingParagraph());

        iTextDocument.add(
                paragraph(
                        format(
                                "Opgesteld door: %s",
                                "Peter Cuppens")));
        iTextDocument.add(
                paragraph(
                        format(
                                "Werf: %s",
                                werf.getNaam())));
        iTextDocument.add(
                paragraph(
                        format(
                                "Adres: %s",
                                werf.getOpdrachtAdres())));
        iTextDocument.add(
                paragraph(
                        format(
                                "Datum opmaak: %s",
                                now().toString(DATE_FORMAT))));

    }

    public Boolean write(final Document writeDocument, final WerfInt werf) {

        try {
            File path = Environment.getExternalStoragePublicDirectory(
                    "werfleider/" + now().toString("d-MM-yyyy"));

            path.mkdirs();

            File file = new File(
                    Environment.getExternalStorageDirectory(),
                    "werfleider/" + now().toString("d-MM-yyyy") + "/" + writeDocument.getDocumentType().name().toLowerCase() + ".pdf");
            // If file does not exists, then create it

            if (!file.exists()) {
                file.createNewFile();
            }

            // step 1
            com.itextpdf.text.Document document = new com.itextpdf.text.Document();
            // step 2
            PdfWriter writer =
                    PdfWriter.getInstance(
                            document,
                            new FileOutputStream(file.getAbsoluteFile()));

            // step 3
            writer.setBoxSize(
                    "art",
                    new Rectangle(
                            36,
                            54,
                            559,
                            788));

            PdfPageEventHelper event =
                    writeDocument.getDocumentType() == AS_BUILT ?
                            new PlaatsbeschrijfHeaderFooter() :
                            new PageNumberHeader();
            writer.setPageEvent(event);

            document.open();

            PdfContentByte cb = writer.getDirectContent();
            if (writeDocument.getDocumentType() == AS_BUILT) {
                createFirstPagePlaatsbeschrijf(
                        cb,
                        werf);

                document.newPage();

                createSecondPage(
                        document,
                        werf);

                document.newPage();

                createThirdPage(document);

                document.newPage();
            } else {
                createFirstPageDocuments(
                        werf,
                        writeDocument,
                        document);

                document.newPage();
            }

            int chapterIndex = 1;
            double totalPageHeight;

            // step 4
            for (DocumentLocation collection : writeDocument.getFotoReeksList()) {
                Chapter chapter = new Chapter(
                        new Paragraph(
                                collection.getLocation(),
                                titleFont),
                        chapterIndex);
                chapterIndex++;
                document.add(chapter);
                totalPageHeight = 0;
                int imagesInChapter = 1;
                for (DocumentImage documentImage : collection.getImageList()) {

                    // demonstrate some table features
                    PdfPTable table = new PdfPTable(2);
                    // table spacing before starts from top
                    table.setSpacingBefore(20);
                    // ook van top
                    table.getDefaultCell().setPadding(5);
                    table.setWidthPercentage(100);

                    final File imageFile = new File(documentImage.getImageURL());

                    final DateTime lastModDate = new DateTime(imageFile.lastModified());

                    final Bitmap bitmap = decodeFile(imageFile);

                    final Image image = Image.getInstance(getBytesFromBitmap(bitmap));

                    table.addCell(image);

                    final PdfPCell descriptionCell = new PdfPCell();
                    descriptionCell.addElement(new Phrase(documentImage.getTitle()));
                    descriptionCell.addElement(
                            new Phrase(
                                    buildMeasuringString(
                                            writeDocument,
                                            collection,
                                            documentImage)));
                    descriptionCell.addElement(new Phrase(documentImage.hasDescription() ? documentImage.getDescription() : ""));
                    descriptionCell.addElement(
                            new Phrase(
                                    format(
                                            "\n\nGenomen op %s",
                                            lastModDate.toString(DATE_FORMAT))));

                    table.addCell(descriptionCell);

                    document.add(table);
                    totalPageHeight += table.getTotalHeight();

                    imagesInChapter++;

                    if (totalPageHeight > 500) {
                        document.newPage();
                        document.add(Chunk.NEWLINE);
                        totalPageHeight = 0;
                    }
                }
            }
            // step 5
            document.close();

            Log.d(
                    "Success",
                    "Success");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }

    // convert from bitmap to byte array
    public byte[] getBytesFromBitmap(final Bitmap bitmap) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(
                Bitmap.CompressFormat.JPEG,
                100,
                stream);

        return stream.toByteArray();
    }

    //decodes image and scales it to reduce memory consumption
    private Bitmap decodeFile(File f) {

        try {
            //Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(
                    new FileInputStream(f),
                    null,
                    o);

            //The new size we want to scale to
            final int REQUIRED_SIZE = 110;

            //Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE && o.outHeight / scale / 2 >= REQUIRED_SIZE)
                scale *= 2;

            //Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(
                    new FileInputStream(f),
                    null,
                    o2);
        } catch (FileNotFoundException e) {
        }
        return null;
    }

    private String buildMeasuringString(final Document document, final DocumentLocation collection, final DocumentImage documentImage) {

        if (document.getDocumentType() != OPMETINGEN) {
            return "";
        }

        switch (collection.getMeasuringUnit().getWeight()) {
            case 0:
                return format(
                        "L %.2f %s",
                        documentImage.getLength(),
                        collection.getMeasuringUnit().name().toLowerCase());
            case 1:
                return format(
                        "L %.2f %s x \nB %.2f %s \n = %.2f %s",
                        documentImage.getLength(),
                        collection.getMeasuringUnit().name().toLowerCase(),
                        documentImage.getWidth(),
                        collection.getMeasuringUnit().name().toLowerCase(),
                        (documentImage.getLength() * documentImage.getWidth()),
                        collection.getMeasuringUnit().name().toLowerCase());
            case 2:
                return format(
                        "L %.2f %s x \nB %.2f %s x \nH %.2f %s \n = %.2f %s",
                        documentImage.getLength(),
                        collection.getMeasuringUnit().name().toLowerCase(),
                        documentImage.getWidth(),
                        collection.getMeasuringUnit().name().toLowerCase(),
                        documentImage.getHeight(),
                        collection.getMeasuringUnit().name().toLowerCase(),
                        (documentImage.getLength() * documentImage.getWidth() * documentImage.getHeight()),
                        collection.getMeasuringUnit().name().toLowerCase());
            default:
                return "";
        }
    }

    public String read(String fname) {

        BufferedReader br = null;
        String response = null;
        try {
            StringBuffer output = new StringBuffer();
            String fpath = "/sdcard/" + fname + ".pdf";

            PdfReader reader = new PdfReader(new FileInputStream(fpath));
            PdfReaderContentParser parser = new PdfReaderContentParser(reader);

            StringWriter strW = new StringWriter();

            TextExtractionStrategy strategy;
            for (int i = 1; i <= reader.getNumberOfPages(); i++) {
                strategy = parser.processContent(
                        i,
                        new SimpleTextExtractionStrategy());

                strW.write(strategy.getResultantText());

            }

            response = strW.toString();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return response;
    }

    class PageNumberHeader extends PdfPageEventHelper {

        @Override public void onEndPage(final PdfWriter writer, final com.itextpdf.text.Document document) {

            Rectangle rect = writer.getBoxSize("art");
            Phrase phrase = new Phrase(
                    null,
                    footerFont);
            phrase.add(Integer.toString(writer.getPageNumber()));
            ColumnText.showTextAligned(
                    writer.getDirectContent(),
                    Element.ALIGN_RIGHT,
                    phrase,
                    rect.getRight(),
                    rect.getTop(),
                    0);

        }
    }

    /**
     * Inner class to add a header and a footer.
     */
    class PlaatsbeschrijfHeaderFooter extends PdfPageEventHelper {

        public void onEndPage(PdfWriter writer, com.itextpdf.text.Document document) {

            Rectangle rect = writer.getBoxSize("art");
            Phrase phrase = new Phrase(
                    null,
                    footerFont);


            switch (writer.getPageNumber()) {
                case 1:
                    break;
                default:

                    rect.setBorderColorTop(BaseColor.BLACK);

                    phrase.add("Fr Goedleven nv");
                    ColumnText.showTextAligned(
                            writer.getDirectContent(),
                            ALIGN_LEFT,
                            phrase,
                            rect.getLeft(),
                            rect.getTop(),
                            0);

                    phrase.remove(0);
                    phrase.add("Plaatsbeschrijving: Mehta-  wilrijk – 27/10/14");
                    ColumnText.showTextAligned(
                            writer.getDirectContent(),
                            ALIGN_CENTER,
                            phrase,
                            (rect.getLeft() + rect.getRight()) / 2,
                            rect.getTop(),
                            0);

                    phrase.remove(0);
                    phrase.add(Integer.toString(writer.getPageNumber()));
                    ColumnText.showTextAligned(
                            writer.getDirectContent(),
                            Element.ALIGN_RIGHT,
                            phrase,
                            rect.getRight(),
                            rect.getTop(),
                            0);

                    break;
            }

            try {
                InputStream inputStream = context.getAssets().open("image/company_logo.jpeg");
                Bitmap bmp = BitmapFactory.decodeStream(inputStream);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(
                        Bitmap.CompressFormat.PNG,
                        100,
                        stream);
                Image image = Image.getInstance(stream.toByteArray());
                image.setAbsolutePosition(
                        rect.getLeft() + 15,
                        rect.getBottom() - 18);
                image.scaleToFit(
                        120,
                        80);
                document.add(image);

                float nextColumnX = image.getScaledWidth() + image.getAbsoluteX() + 40;

                phrase = new Phrase(
                        null,
                        footerFont);
                phrase.add("R.P.R. Antwerpen");

                ColumnText.showTextAligned(
                        writer.getDirectContent(),
                        ALIGN_LEFT,
                        phrase,
                        nextColumnX,
                        rect.getBottom(),
                        0);

                phrase.remove(0);
                phrase.add("B.T.W. BE 0403.829.707");
                ColumnText.showTextAligned(
                        writer.getDirectContent(),
                        ALIGN_LEFT,
                        phrase,
                        nextColumnX,
                        rect.getBottom() - 10,
                        0);


                phrase.remove(0);
                phrase.add("Registratie: 02.00.9.3");
                ColumnText.showTextAligned(
                        writer.getDirectContent(),
                        ALIGN_LEFT,
                        phrase,
                        nextColumnX,
                        rect.getBottom() - 20,
                        0);

                nextColumnX = nextColumnX + 160;

                phrase.remove(0);
                phrase.add("info@goedleven.net");

                ColumnText.showTextAligned(
                        writer.getDirectContent(),
                        ALIGN_LEFT,
                        phrase,
                        nextColumnX,
                        rect.getBottom(),
                        0);

                phrase.remove(0);
                phrase.add("www.monument.be");
                ColumnText.showTextAligned(
                        writer.getDirectContent(),
                        ALIGN_LEFT,
                        phrase,
                        nextColumnX,
                        rect.getBottom() - 10,
                        0);

                phrase.remove(0);
                phrase.add("T: +32 3 651 33 51");
                ColumnText.showTextAligned(
                        writer.getDirectContent(),
                        Element.ALIGN_RIGHT,
                        phrase,
                        rect.getRight(),
                        rect.getBottom(),
                        0);

                phrase.remove(0);
                phrase.add("F: +32 3 652 13 92");
                ColumnText.showTextAligned(
                        writer.getDirectContent(),
                        Element.ALIGN_RIGHT,
                        phrase,
                        rect.getRight(),
                        rect.getBottom() - 10,
                        0);
            } catch (BadElementException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (DocumentException e) {
                e.printStackTrace();
            }

        }
    }
}
