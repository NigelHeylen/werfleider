package nigel.com.werfleider.pdf;

/**
 * Created by nigel on 26/11/14.
 */

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import com.google.common.collect.Multimap;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import com.parse.ParseException;
import com.parse.ParseUser;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import javax.inject.Inject;
import nigel.com.werfleider.model.DocumentLocation;
import nigel.com.werfleider.model.ParseDocumentImage;
import nigel.com.werfleider.model.Yard;
import org.joda.time.DateTime;

import static com.google.common.base.Strings.isNullOrEmpty;
import static com.google.common.base.Strings.nullToEmpty;
import static com.google.common.collect.Iterables.getLast;
import static com.itextpdf.text.Element.ALIGN_CENTER;
import static com.itextpdf.text.Element.ALIGN_LEFT;
import static java.lang.String.format;
import static nigel.com.werfleider.model.ParseDocumentImage.COMPARE_BY_FLOOR;
import static nigel.com.werfleider.util.ParseStringUtils.NAME;
import static org.joda.time.DateTime.now;

public class ParseFileOperations {

  public static final String DATE_FORMAT = "dd-MM-yyyy";

  final static Font titleFont = new Font(Font.FontFamily.HELVETICA, 24);

  final static Font paragraphTitleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);

  final static Font paragraphSubTitleFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);

  final static Font titleFontBoldUnderline =
      new Font(Font.FontFamily.HELVETICA, 24, Font.BOLD | Font.UNDERLINE);

  final static Font paragraphFont = new Font(Font.FontFamily.HELVETICA, 10);

  final static int top = 820;

  final static int right = 600;

  final static int margin = 60;

  final Context context;

  @Inject public ParseFileOperations(final Context context) {

    this.context = context;
  }

  private static Paragraph paragraph(final String text) {

    return paragraph(text, ALIGN_LEFT, 0, 0, paragraphFont);
  }

  private static Paragraph paragraph(final String text, final int alignment, final float indention,
      final int spacingAfter, final Font font) {

    final Paragraph paragraph = new Paragraph(text, font);
    paragraph.setAlignment(alignment);
    paragraph.setIndentationLeft(indention);
    paragraph.setSpacingAfter(spacingAfter);
    return paragraph;
  }

  private static Paragraph spacingParagraph() {

    return new Paragraph("\n\n");
  }

  private void createFirstPageDocuments(final Yard werf, final DocumentLocation location,
      final Document iTextDocument, final Multimap<String, ParseDocumentImage> locationsMap)
      throws IOException, DocumentException {

    iTextDocument.add(spacingParagraph());
    iTextDocument.add(paragraph(werf.getNaam(), ALIGN_CENTER, 0, 10, titleFontBoldUnderline));
    if (!isNullOrEmpty(werf.getNummer())) {
      iTextDocument.add(paragraph(format("Nr: %s", werf.getNummer())));
    }

    if (!isNullOrEmpty(werf.getYardAddress())) {
      iTextDocument.add(paragraph(
          format("Adres: %s %s", werf.getYardAddress(), nullToEmpty(werf.getYardAddressNumber()))));
    }

    if (!isNullOrEmpty(werf.getYardCity())) {

      iTextDocument.add(paragraph(
          format("Stad: %s %s", werf.getYardCity(), nullToEmpty(werf.getYardAreaCode()))));
    }

    if (!isNullOrEmpty(werf.getOmschrijving())) {

      iTextDocument.add(paragraph(format("Omschrijving der werken: %s", werf.getOmschrijving())));
    }

    iTextDocument.add(paragraph(
        format("Datum aanvang der werken: %s", werf.getDatumAanvang().toString(DATE_FORMAT))));

    if (!isNullOrEmpty(werf.getTermijn())) {
      iTextDocument.add(paragraph(format("Termijn: %s", werf.getTermijn())));
    }

    iTextDocument.add(
        paragraph(format("Opgesteld door: %s", ParseUser.getCurrentUser().get(NAME))));

    if (werf.getImageByteArray() != null) {
      final Image image = Image.getInstance(werf.getImageByteArray());
      image.scaleToFit(600, 300);
      image.setAlignment(ALIGN_CENTER);
      iTextDocument.add(image);
    }

    iTextDocument.add(
        paragraph(location.getDocumentType().toString(), ALIGN_CENTER, 0, 5, paragraphTitleFont));

    if (!isNullOrEmpty(werf.getArchitectNaam())) {

      iTextDocument.add(paragraph("Architect", ALIGN_LEFT, 0, 5, paragraphSubTitleFont));
      iTextDocument.add(paragraph(format("Naam: %s", werf.getArchitectNaam())));
      if (!isNullOrEmpty(werf.getArchitectEmail())) {
        iTextDocument.add(paragraph(format("Email: %s", werf.getArchitectEmail())));
      }
      if (!isNullOrEmpty(werf.getArchitectTelefoon())) {
        iTextDocument.add(paragraph(format("Telefoon: %s", werf.getArchitectTelefoon())));
      }
    }

    if (!isNullOrEmpty(werf.getBouwheerNaam())) {

      iTextDocument.add(paragraph("Bouwheer", ALIGN_LEFT, 0, 5, paragraphSubTitleFont));
      iTextDocument.add(paragraph(format("Naam: %s", werf.getBouwheerNaam())));
      if (!isNullOrEmpty(werf.getBouwheerEmail())) {
        iTextDocument.add(paragraph(format("Email: %s", werf.getBouwheerEmail())));
      }
      if (!isNullOrEmpty(werf.getBouwheerTelefoon())) {
        iTextDocument.add(paragraph(format("Telefoon: %s", werf.getBouwheerTelefoon())));
      }
    }

    if (!isNullOrEmpty(werf.getIngenieurNaam())) {

      iTextDocument.add(paragraph("Ingenieur", ALIGN_LEFT, 0, 5, paragraphSubTitleFont));
      iTextDocument.add(paragraph(format("Naam: %s", werf.getIngenieurNaam())));
      if (!isNullOrEmpty(werf.getIngenieurEmail())) {
        iTextDocument.add(paragraph(format("Email: %s", werf.getIngenieurEmail())));
      }
      if (!isNullOrEmpty(werf.getIngenieurTelefoon())) {
        iTextDocument.add(paragraph(format("Telefoon: %s", werf.getIngenieurTelefoon())));
      }
    }
  }

  public boolean write(final Yard yard, DocumentLocation location,
      final Multimap<String, ParseDocumentImage> locationsMap) {

    try {
      final File path = new File(Environment.getExternalStorageDirectory(),
          "werfleider/" + yard.getNaam() + "/" + location.getDocumentType().name().toLowerCase());

      if (!path.exists()) path.mkdirs();

      final File file = new File(Environment.getExternalStorageDirectory(), "werfleider/"
          + yard.getNaam()
          + "/"
          + location.getDocumentType().name().toLowerCase()
          + "/"
          + now().toString("d-MM-yyyy")
          + ".pdf");
      // If file does not exists, then create it

      if (!file.exists()) {
        file.createNewFile();
      }

      // step 1
      final Document document = new Document();
      // step 2
      final PdfWriter writer =
          PdfWriter.getInstance(document, new FileOutputStream(file.getAbsoluteFile()));

      // step 3
      writer.setBoxSize("art", new Rectangle(36, 54, 559, 788));

      final PdfPageEventHelper event = new DocumentHeaderFooter();
      writer.setPageEvent(event);

      document.open();

      createFirstPageDocuments(yard, location, document, locationsMap);

      document.newPage();

      int chapterIndex = 1;
      double totalPageHeight;

      // step 4
      for (String floor : locationsMap.keySet()) {

        Chapter chapter = new Chapter(new Paragraph(format("%s", floor), titleFont), chapterIndex);
        chapterIndex++;
        document.add(chapter);
        totalPageHeight = 0;

        final List<ParseDocumentImage> imageList = new ArrayList<>(locationsMap.get(floor));
        Collections.sort(imageList, COMPARE_BY_FLOOR);

        int index = 0;

        for (ParseDocumentImage documentImage : imageList) {

          // demonstrate some table features
          PdfPTable table = new PdfPTable(2);
          // table spacing before starts from top
          table.setSpacingBefore(20);
          // ook van top
          table.getDefaultCell().setPadding(5);
          table.setWidthPercentage(100);

          final Image image;
          if (documentImage.hasImage()) {
            image = Image.getInstance(documentImage.getImage().getData());
          } else if (documentImage.hasImageBytes()) {

            image = Image.getInstance(documentImage.getImageBytes());
          } else {

            image = Image.getInstance(documentImage.getImageURL());
          }

          table.addCell(image);

          final PdfPCell descriptionCell = new PdfPCell();

          descriptionCell.addElement(new Phrase(
              format(Locale.ENGLISH, "Afb. %d\nGenomen op %s", index,
                  new DateTime(documentImage.getImageTakenDate()).toString(DATE_FORMAT)),
              paragraphFont));

          if (!isNullOrEmpty(documentImage.getLocation())) {
            descriptionCell.addElement(new Phrase(format("\n%s", documentImage.getLocation())));
          }

          descriptionCell.addElement(new Phrase(
              isNullOrEmpty(documentImage.getTitle()) ? documentImage.getTitle()
                  : format("\n\n%s\n", documentImage.getTitle())));
          descriptionCell.addElement(new Phrase(documentImage.getDescription()));

          table.addCell(descriptionCell);

          document.add(table);
          totalPageHeight += table.getTotalHeight();

          if (totalPageHeight > 450 && !getLast(locationsMap.get(floor)).equals(documentImage)) {
            document.newPage();
            document.add(Chunk.NEWLINE);
            totalPageHeight = 0;
          }

          index++;
        }
      }
      // step 5
      document.close();

      Log.d("Success", "Success");
      return true;
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    } catch (DocumentException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      return false;
    } catch (ParseException e) {
      e.printStackTrace();
      return false;
    }
  }
}
