package nigel.com.werfleider.pdf;

import android.content.Context;
import android.os.Environment;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import nigel.com.werfleider.model.DocumentLocation;
import nigel.com.werfleider.model.ParseDocumentImage;
import nigel.com.werfleider.model.Yard;
import nigel.com.werfleider.util.MeasuringUnit;
import org.joda.time.DateTime;

import static java.lang.String.format;
import static org.joda.time.DateTime.now;

/**
 * Created by nigel on 11/06/15.
 */
public class MeasurementsFileOperations {

  final static Font SMALL_FONT = new Font(Font.FontFamily.HELVETICA, 7);

  private final Context context;

  public MeasurementsFileOperations(final Context context) {

    this.context = context;
  }

  public boolean writeDocument(final Yard yard, DocumentLocation location,
      final Multimap<String, ParseDocumentImage> documentMap) {

    Document document = new Document(PageSize.A4, 0, 10, 50, 0);
    try {

      final File path = Environment.getExternalStoragePublicDirectory(
          "werfleider/" + yard.getNaam() + "/" + location.getDocumentType().name().toLowerCase());

      path.mkdirs();

      final File file = new File(Environment.getExternalStorageDirectory(), "werfleider/"
          + yard.getNaam()
          + "/"
          + location.getDocumentType().name().toLowerCase()
          + "/"
          + now().toString("d-MM-yyyy hh:mm")
          + ".pdf");

      // If file does not exists, then create it

      if (!file.exists()) {
        file.createNewFile();
      }

      // creation of the different writers
      final PdfWriter writer =
          PdfWriter.getInstance(document, new FileOutputStream(file.getAbsoluteFile()));

      writer.setBoxSize("art", new Rectangle(36, 54, 559, 788));

      // various fonts

      document.open();

      document.add(createTable(yard, location, documentMap));

      document.close();
      return true;
    } catch (FileNotFoundException | DocumentException e) {
      e.printStackTrace();
      return false;
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
  }

  public static PdfPTable createTable(final Yard yard, final DocumentLocation location,
      final Multimap<String, ParseDocumentImage> documentMap) {

    // a table with three columns
    PdfPTable table = new PdfPTable(12);
    // the cell object
    addHeaders(table, yard);

    for (String floor : documentMap.keySet()) {

      addLocationHeader(table, location, documentMap.get(floor));

      addFloorRow(table, location, floor, documentMap.get(floor));

      ImmutableListMultimap<String, ParseDocumentImage> locationPartition =
          Multimaps.index(documentMap.get(floor), new Function<ParseDocumentImage, String>() {
            @Override public String apply(ParseDocumentImage input) {

              return input.getLocation();
            }
          });

      for (String imageLocation : locationPartition.keySet()) {

        addLocationRow(table, location, imageLocation, locationPartition.get(imageLocation));

        int index = 0;

        for (ParseDocumentImage aImage : locationPartition.get(imageLocation)) {

          addImageRow(table, index, aImage, location.getMeasuringUnit());
          index++;
        }
      }

      addEmptyRow(table);
      addEmptyRow(table);
    }

    return table;
  }

  private static void addImageRow(final PdfPTable table, int index, final ParseDocumentImage aImage,
      final MeasuringUnit measuringUnit) {

    table.addCell(getSmallFontPdfCell("", 1));
    table.addCell(getSmallFontPdfCell("", 3));
    table.addCell(getSmallFontPdfCell(Integer.toString(index), 1));
    table.addCell(getSmallFontPdfCell(Integer.toString(aImage.getQuantity()), 1));
    table.addCell(getSmallFontPdfCell(format("%.2f", aImage.getLength()), 1));
    table.addCell(getSmallFontPdfCell(format("%.2f", aImage.getWidth()), 1));
    table.addCell(getSmallFontPdfCell(format("%.2f", aImage.getHeight()), 1));
    table.addCell(getSmallFontPdfCell(format("%.2f", aImage.getTotal(measuringUnit)), 1));
    table.addCell(getSmallFontPdfCell("", 1));
    table.addCell(getSmallFontPdfCell("", 1));
  }

  private static void addLocationRow(PdfPTable table, DocumentLocation location,
      String imageLocation, final List<ParseDocumentImage> images) {

    table.addCell(getSmallFontPdfCell("", 1));
    table.addCell(getSmallFontPdfCell(imageLocation, 3));
    table.addCell(getSmallFontPdfCell("", 1));
    table.addCell(getSmallFontPdfCell("", 1));
    table.addCell(getSmallFontPdfCell("", 1));
    table.addCell(getSmallFontPdfCell("", 1));
    table.addCell(getSmallFontPdfCell("", 1));
    table.addCell(getSmallFontPdfCell("", 1));
    table.addCell(
        getSmallFontPdfCell(format("%.2f", getImageTotal(images, location.getMeasuringUnit())), 1));
    table.addCell(getSmallFontPdfCell(images.get(0).getMS(), 1));
  }

  private static void addFloorRow(PdfPTable table, DocumentLocation location, String floor,
      Collection<ParseDocumentImage> imageCollection) {

    table.addCell(getSmallFontPdfCell("", 1));
    table.addCell(getSmallFontPdfCell(floor, 3));
    table.addCell(getSmallFontPdfCell("", 1));
    table.addCell(getSmallFontPdfCell("", 1));
    table.addCell(getSmallFontPdfCell("", 1));
    table.addCell(getSmallFontPdfCell("", 1));
    table.addCell(getSmallFontPdfCell("", 1));
    table.addCell(getSmallFontPdfCell("", 1));
    table.addCell(getSmallFontPdfCell(
        format("%.2f", getImageTotal(imageCollection, location.getMeasuringUnit())), 1));
    table.addCell(getSmallFontPdfCell("", 1));
  }

  private static double getImageTotal(final Collection<ParseDocumentImage> images,
      final MeasuringUnit measuringUnit) {

    double total = 0;

    for (ParseDocumentImage image : images) {

      total += image.getTotal(measuringUnit);
    }
    return total;
  }

  private static void addEmptyRow(final PdfPTable table) {

    table.addCell(getSmallFontPdfCell("-", 1));
    table.addCell(getSmallFontPdfCell("", 3));
    table.addCell(getSmallFontPdfCell("", 1));
    table.addCell(getSmallFontPdfCell("", 1));
    table.addCell(getSmallFontPdfCell("", 1));
    table.addCell(getSmallFontPdfCell("", 1));
    table.addCell(getSmallFontPdfCell("", 1));
    table.addCell(getSmallFontPdfCell("", 1));
    table.addCell(getSmallFontPdfCell("", 1));
    table.addCell(getSmallFontPdfCell("", 1));
  }

  private static void addLocationHeader(PdfPTable table, DocumentLocation location,
      Collection<ParseDocumentImage> images) {

    table.addCell(getSmallFontPdfCell(location.getArtNr(), 1));
    table.addCell(getSmallFontPdfCell("", 3));
    table.addCell(getSmallFontPdfCell("", 1));
    table.addCell(getSmallFontPdfCell("", 1));
    table.addCell(getSmallFontPdfCell("", 1));
    table.addCell(getSmallFontPdfCell("", 1));
    table.addCell(getSmallFontPdfCell("", 1));
    table.addCell(getSmallFontPdfCell(location.getMeasuringUnit().name(), 1));
    table.addCell(
        getSmallFontPdfCell(format("%.2f", getImageTotal(images, location.getMeasuringUnit())), 1));
    table.addCell(getSmallFontPdfCell("", 1));
  }

  private static void addHeaders(PdfPTable table, final Yard yard) {

    PdfPCell cell;
    // we add a cell with colspan 3
    cell = new PdfPCell(new Phrase(yard.getArchitectNaam()));
    cell.setColspan(4);
    cell.setRowspan(2);
    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
    table.addCell(cell);
    // now we add a cell with rowspan 2
    cell = new PdfPCell(new Phrase("UITGEBREIDE OPMETING"));
    cell.setColspan(6);
    cell.setRowspan(2);
    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
    table.addCell(cell);

    cell = new PdfPCell(new Phrase("Blad:"));
    cell.setRowspan(2);
    cell.setColspan(2);
    table.addCell(cell);

    cell = new PdfPCell(new Phrase("Group Monument"));
    cell.setColspan(4);
    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
    table.addCell(cell);

    cell = new PdfPCell(new Phrase("Opdrachtgever: " + yard.getBouwheerNaam()));
    cell.setColspan(5);
    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
    table.addCell(cell);
    table.addCell("");

    cell = new PdfPCell(new Phrase("besteknr:"));
    cell.setColspan(2);
    table.addCell(cell);

    cell = new PdfPCell(new Phrase(yard.getArchitectNaam()));
    cell.setColspan(4);
    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
    table.addCell(cell);

    cell = new PdfPCell(new Phrase("Afmetingen"));
    cell.setColspan(5);
    table.addCell(cell);
    table.addCell("");

    cell = new PdfPCell(new Phrase("datum:" + DateTime.now().toString("dd-MM-yy")));
    cell.setColspan(2);
    table.addCell(cell);

    table.addCell(getSmallFontPdfCell(yard.getYardAddress(), 11));
    table.addCell(getSmallFontPdfCell("", 1));
    table.addCell(getSmallFontPdfCell("art.", 1));
    table.addCell(getSmallFontPdfCell("beschrijving", 3));
    table.addCell(getSmallFontPdfCell("foto", 1));
    table.addCell(getSmallFontPdfCell("#", 1));
    table.addCell(getSmallFontPdfCell("L", 1));
    table.addCell(getSmallFontPdfCell("B", 1));
    table.addCell(getSmallFontPdfCell("D/H", 1));
    table.addCell(getSmallFontPdfCell("=", 1));
    table.addCell(getSmallFontPdfCell("TOTAAL", 1));
    table.addCell(getSmallFontPdfCell("MS", 1));
  }

  private static PdfPCell getSmallFontPdfCell(final String text, int colspan) {

    return getSmallFontPdfCell(text, colspan, 1);
  }

  private static PdfPCell getSmallFontPdfCell(final String text, int colspan, int rowspan) {

    final Phrase phrase = new Phrase(text, SMALL_FONT);

    final PdfPCell cell = new PdfPCell(phrase);
    cell.setColspan(colspan);
    cell.setRowspan(rowspan);

    return cell;
  }
}
