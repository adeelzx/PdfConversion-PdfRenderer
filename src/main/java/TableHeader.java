
import java.awt.Color;
import java.io.IOException;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.ExceptionConverter;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;

public class TableHeader extends PdfPageEventHelper {
   
	/** The header text. */
    private String header;
    /** The template with the total number of pages. */
    private PdfTemplate total;

    /**
     * Allows us to change the content of the header.
     * @param header The new header String
     */
    public void setHeader(String header) {
        this.header = header;
    }
    
    public TableHeader(String header) {
    	this.header = header;
    }
    
    /**
     * Creates the PdfTemplate that will hold the total number of pages.
     * @see com.itextpdf.text.pdf.PdfPageEventHelper#onOpenDocument(
     *      com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document)
     */
    public void onOpenDocument(PdfWriter writer, Document document) {
        total = writer.getDirectContent().createTemplate(30, 16);
    }

    /**
     * Adds a header to every page
     * @see com.itextpdf.text.pdf.PdfPageEventHelper#onEndPage(
     *      com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document)
     */
    public void onEndPage(PdfWriter writer, Document document) {
        PdfPTable table = new PdfPTable(2);
        try {
        	
        	PdfPCell cell = new PdfPCell();
        	cell.addElement(new Paragraph(this.header));
        	cell.setBorderWidthLeft(0);
        	cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        	cell.setColspan(2);
        	Image image = null;
    		try {
    			image = Image.getInstance(Thread.currentThread().getContextClassLoader().getResource("LPLogo.png"));
    			
    		} catch (BadElementException | IOException e) {
    			e.printStackTrace();
    		}
    		PdfPCell imageCell = new PdfPCell();
    		imageCell.addElement(image);
    		imageCell.setBorderWidthRight(0f);
    		imageCell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
    		
            table.setWidths(new int[]{6,20});
            table.setTotalWidth(550);
            table.setLockedWidth(true);
            table.getDefaultCell().setFixedHeight(20);
            table.getDefaultCell().setBorder(Rectangle.BOX);
            table.addCell(imageCell);
            table.addCell(cell);
            table.getDefaultCell().setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
            table.writeSelectedRows(0, -1, 20, 823, writer.getDirectContent());
            
            final int currentPageNumber = writer.getCurrentPageNumber();
            
            try {
                final Rectangle pageSize = document.getPageSize();
                final PdfContentByte directContent = writer.getDirectContent();
     
                directContent.setColorFill(Color.GRAY);
                directContent.setFontAndSize(BaseFont.createFont(), 10);
     
                directContent.setTextMatrix(pageSize.getRight(40), pageSize.getBottom(30));
                directContent.showText(String.valueOf(currentPageNumber));
     
            } catch (DocumentException | IOException e) {
            	e.printStackTrace();
            }
           
        }
        catch(DocumentException de) {
            throw new ExceptionConverter(de);
        }
    }

    /**
     * Fills out the total number of pages before the document is closed.
     * @see com.itextpdf.text.pdf.PdfPageEventHelper#onCloseDocument(
     *      com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document)
     */
    public void onCloseDocument(PdfWriter writer, Document document) {
        ColumnText.showTextAligned(total, Element.ALIGN_LEFT,
                new Phrase(String.valueOf(writer.getPageNumber() - 1)),
                2, 2, 0);
    }
}