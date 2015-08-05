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

import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;


public class PdfToImage {

	public static void previewPDFDocumentInImage() throws IOException {

		ByteBuffer buf = null;
		Path pdfPath = Paths.get("/home/adeel/abc.pdf");
		byte[] pdf = Files.readAllBytes(pdfPath);
		buf = ByteBuffer.wrap(pdf);

		PDFFile pdffile = new PDFFile(buf);
		pdffile.getNumPages();
		for (int i = 0; i <  pdffile.getNumPages(); i++) {
			PDFPage page = pdffile.getPage(i+1);
			Rectangle rect = new Rectangle(0, 0, (int)page.getBBox().getWidth(), (int)page.getBBox().getHeight());

			Image img = page.getImage(rect.width, rect.height, rect, null, true, true) ;
			save(toBufferedImage(img), String.valueOf(i));
		}
	}


	private static void save(BufferedImage image, String fileName) {

		File file = new File("/home/adeel/pdfImg/"+fileName + ".png");
		try {
			ImageIO.write(image, "png", file);  
		} catch(IOException e) {

		}
	}

	private static BufferedImage toBufferedImage(Image src) {
		
		int w = src.getWidth(null);
		int h = src.getHeight(null);
		int type = BufferedImage.TYPE_INT_RGB; 
		BufferedImage img = new BufferedImage(w, h, type);
		Graphics2D g = img.createGraphics();
		g.drawImage(src, 0, 0, null);
		g.dispose();
		return img;
		
	}

	public static void main(final String[] args) {

		try {
			PdfToImage.previewPDFDocumentInImage();
		} catch (IOException e) { }



	}
}
