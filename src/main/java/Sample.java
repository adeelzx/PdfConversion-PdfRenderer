import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;


public class Sample {

	public static void main(String[] args) {
		
		Document document = new Document(PageSize.A4, 60, 60, 60, 60);
		try
		{
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("/home/adeel/experiment.pdf"));
			TableHeader event = new TableHeader("Header");
			writer.setPageEvent(event);
			document.open();
		
			Resource resource = new ClassPathResource("/TimesRoman.ttf");
			String FONT = null;
			try {
				FONT =  resource.getFile().getPath();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			BaseFont bfTimes = null;

			try {
				bfTimes = BaseFont.createFont(FONT, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
			} catch (IOException e) {
				e.printStackTrace();
			}
			Font fontnormal = new Font(bfTimes, 12);
			
			String text = "Divide by example : 45 \u00F7; 3";
			// String text = "Divide by example : 45 ÷ 3";
			Paragraph para = new Paragraph(text, fontnormal);

			document.add(para);
			document.close();
			writer.close();

			System.out.println("Done!");
		} catch (DocumentException e)
		{
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
