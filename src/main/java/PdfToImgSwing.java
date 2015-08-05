
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;


public class PdfToImgSwing {
    
    public static void previewPDFDocumentInImage() throws IOException {
        
    	JFrame frame = new JFrame("My incredible PDF document");
    	JPanel panel = new JPanel();
    	ByteBuffer buf = null;
    	Path pdfPath = Paths.get("/home/adeel/abc.pdf");
    	byte[] pdf = Files.readAllBytes(pdfPath);
        buf = ByteBuffer.wrap(pdf);
        
        // use the PDF&nbsp;Renderer library on the buf which contains the in memory PDF document
        PDFFile pdffile = new PDFFile(buf);
        pdffile.getNumPages();
        for (int i = 0; i <  pdffile.getNumPages(); i++) {
        	 PDFPage page = pdffile.getPage(i+1);
        	  Rectangle rect =
        	            new Rectangle(0, 0, (int)page.getBBox().getWidth(), (int)page.getBBox().getHeight());
        	  
        	  
        	  Image img = page.getImage(rect.width, rect.height, //width &amp; height
                      rect, // clip rect
                      null, // null for the ImageObserver
                      true, // fill background with white
                      true) // block until drawing is done
              ;
        	  
        	  save(toBufferedImage(img), "png" , String.valueOf(i));
        	  toBufferedImage(img);
        	  panel.add(new JLabel(new ImageIcon(img)));
		}
        
        JScrollPane jScrollPane = new JScrollPane(panel);
        jScrollPane.setAutoscrolls(true);
        frame.add(jScrollPane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
    
    
    private static void save(BufferedImage image, String ext, String fileName) {
        
    	File file = new File("/home/adeel/pdfImg/"+fileName + "." + ext);
        try {
            ImageIO.write(image, ext, file);  // ignore returned boolean
        } catch(IOException e) {
            
        }
    }
    
    private static BufferedImage toBufferedImage(Image src) {
        int w = src.getWidth(null);
        int h = src.getHeight(null);
        int type = BufferedImage.TYPE_INT_RGB;  // other options
        BufferedImage dest = new BufferedImage(w, h, type);
        Graphics2D g2 = dest.createGraphics();
        g2.drawImage(src, 0, 0, null);
        g2.dispose();
        return dest;
    }
    
    public static void main(final String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    try {
                        PdfToImgSwing.previewPDFDocumentInImage();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            });
    }
}
