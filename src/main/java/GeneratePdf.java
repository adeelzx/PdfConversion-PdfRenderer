import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PdfWriter;


public class GeneratePdf {


	static HashMap<String, String> symbols = new HashMap<String, String>() {
		{
			put("&ang;", "\u2220");
			put("&deg;", "\u00B0");
			put("&epsilon;", "\u03B5");
			put("&isin;", "\u2208");
			put("&notin;", "\u2209");
			put("&cup;", "\u222A");
			put("&cap;", "\u2229");
			put("&#x25B3;", "\u0394");
			put("&empty;", "\u2205");
			put("&sub;", "\u2282");
			put("&sup;","\u2283");
			put("&#8800;", "\u2260");
			put("&radic;", "\u221A");
			put("&equiv;","\u2261");
			put("&divide;", "\u00F7");
			put("&frac12;","\u00BD");
			put("&frac14;","\u00BC");
			put("&frac34;","\u00BE");
			put("&int;", "\u222B");
			put("&le;","\u2264");
			put("&ge;","\u2265");
			put("&sum;", "\u2211");
			put("&perp;","\u22A5");
			put("&Iota;","\u0399");
			/*put("&#x221a;", "\u221A");*/

		}
	};


	public static void main(String[] args)
	{


		


		Document document = new Document(PageSize.A4, 60, 60, 60, 60);
		try
		{
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("/home/adeel/experiment.pdf"));
			TableHeader event = new TableHeader("Header");
			writer.setPageEvent(event);
			document.open();
			//document.add(new Paragraph("Long question will be here Long question will be here Long question will be here estion will be here Long question ?"));

			String str = "2Na + 2H(sub)3(/sub)PO(sub)4(/sub) => 2NaH(sub)2(/sub)PO(sub)4(/sub) + H(sub)2(/sub) +  &radic;(span style=text-decoration: overline;) ABC(/span) ";


			Resource resource = new ClassPathResource("/Verdana.ttf");
//			Resource resource = new ClassPathResource("/TimesRoman.ttf");
			String s = null;
			try {
				s =  resource.getFile().getPath();
				System.out.println("File name :"+s);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			BaseFont bfTimes = null;

			try {
				bfTimes = BaseFont.createFont(s, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
			} catch (IOException e) {
				e.printStackTrace();
			}
			Font fontnormal = new Font(bfTimes, 12);
			
			Paragraph para = new Paragraph(showFormula(str, fontnormal));

			document.add(para);

		

			//Set attributes here
			document.addAuthor("Adeel Ahmad");
			document.addCreationDate();
			document.addCreator("LetsPractice.com");
			document.addTitle("PdfGen");
			document.addSubject("Experiment");

			document.close();
			writer.close();

			System.out.println("Done!");
		} catch (DocumentException e)
		{
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		/*try {
			createPdf("/home/adeel/exp.pdf");
		} catch (IOException | DocumentException e) {
			e.printStackTrace();
		}*/


	}

	public static String stamp(String filename, String text)
	{
		String outputFilename = filename.replaceAll(".pdf","") + "_stamped.pdf";

		try
		{
			System.out.print(String.format("Reading file %s (%.1f KB)...", filename, new File(filename).length()/1024.0));
			PdfReader reader = new PdfReader(filename);
			PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(outputFilename));
			stamper.setRotateContents(false);
			stamper.setFullCompression();
			PdfContentByte canvas = stamper.getOverContent(1);
			Font defaultFont = new Font(Font.TIMES_ROMAN, 30, Font.ZAPFDINGBATS, Color.BLACK);
			ColumnText.showTextAligned(canvas, Element.ALIGN_CENTER, new Phrase(16f, text, defaultFont), 500, 775, 0);
			stamper.close();
			reader.close();
			System.out.println(String.format("wrote to %s (%.1f KB)", outputFilename, new File(outputFilename).length()/1024.0));
		}
		catch (Exception e)
		{
			System.out.println();
			e.printStackTrace();
			return "";
		}
		try {
			createPdf("/home/adeel/exp.pdf");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return outputFilename;
	}

	@SuppressWarnings("unchecked")
	public static  Phrase showFormula(String repltext, Font myfont)
	{
		

		for (String key : symbols.keySet()) {
			repltext = repltext.replace(key,symbols.get(key));
		}
		
		Phrase htmlpara = new Phrase();
		String repltemp = repltext;
		if (repltemp.indexOf("(sub)") >= 0 || repltemp.indexOf("(sup)") >= 0 || repltemp.indexOf("(span style=text-decoration: overline;)") >= 0)
		{
			repltemp = repltemp.replace("(sub)", "(sub)::");
			repltemp = repltemp.replace("(/sub)", "(sub)");
			repltemp = repltemp.replace("(sup)", "(sup)$$");
			repltemp = repltemp.replace("(/sup)", "(sup)");
			repltemp = repltemp.replace("(span style=text-decoration: overline;)", "(span)##");
			repltemp = repltemp.replace("(/span)","(span)");
			String[] splitsub = repltemp.split("\\(sub\\)");

			// For subscript
			for(String mych : splitsub)
			{
				Chunk subc = null;
				if (mych.indexOf("::") >= 0)
				{
					String myc = mych.replace("::", "");
					subc = new Chunk(myc, myfont).setTextRise(-3f);
				}
				else
				{
					String myc = mych.replace("::", "");
					subc = new Chunk(myc, myfont);
				}
				htmlpara.add(subc);
			}

			List<Chunk> ch = htmlpara.getChunks();
			htmlpara.clear();

			// For superscript
			for(Chunk c: ch) {

				String content = c.getContent();
				if(content.contains("(sup)")) {
					String[] splitsup = content.split("\\(sup\\)");
					for(String mych : splitsup) {

						Chunk supc = null;
						if (mych.indexOf("$$") >= 0) {
							String myc = mych.replace("$$", "");
							supc = new Chunk(myc, myfont).setTextRise(3f);
						}
						else {
							String myc = mych.replace("$$", "");
							supc = new Chunk(myc, myfont);
						}

						htmlpara.add(supc);
					}

				} else {
					htmlpara.add(c);
				}

			}
			List<Chunk> chuncks = htmlpara.getChunks();
			htmlpara.clear();

			// For overline
			for(Chunk c: chuncks) {

				String content = c.getContent();
				if(content.contains("(span)")) {
					String[] splitsup = content.split("\\(span\\)");
					for(String mych : splitsup) {

						Chunk supc = null;
						if (mych.indexOf("##") >= 0) {
							String myc = mych.replace("##", "");
							supc = new Chunk(myc, myfont).setUnderline(1, 11);
						}
						else {
							String myc = mych.replace("##", "");
							supc = new Chunk(myc, myfont);
						}

						htmlpara.add(supc);
					}

				} else {
					htmlpara.add(c);
				}
			}

		}
		else
		{ 
			Chunk subc = new Chunk(repltemp, myfont); 
			htmlpara.add(subc); 
		}


		return htmlpara;
	}


	public static void addImage(Document doc, String imagePath) {

		try {

			/** Get image for question*/
			Image image = null;
			image = Image.getInstance(imagePath);
			image.scaleToFit(150f, 150f); 

			PdfPTable imageTable = new PdfPTable(2); 
			imageTable.setWidthPercentage(new float[] { 20, 150 }, doc.getPageSize());
			PdfPCell p1cell = new PdfPCell(); 
			p1cell.setBorder(0);
			PdfPCell p2cell = new PdfPCell();
			p2cell.setBorder(0);
			p1cell.addElement(new Paragraph(new Phrase(""))); 
			imageTable.addCell(p1cell);
			p2cell.addElement(image);
			imageTable.addCell(p2cell);
			doc.add(imageTable); 

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final String[][] MOVIES = {
		{
			"Cp1252",
			"A Very long Engagement (France)",
			"directed by Jean-Pierre Jeunet",
			"Un long dimanche de fian\u00e7ailles \u2220"
		},
		{
			"Cp1250",
			"No Man's Land (Bosnia-Herzegovina)",
			"Directed by Danis Tanovic",
			"Nikogar\u0161nja zemlja \u2220"
		},
		{
			"Cp1251",
			"You I Love (Russia)",
			"directed by Olga Stolpovskaja and Dmitry Troitsky",
			"\u042f \u043b\u044e\u0431\u043b\u044e \u0442\u0435\u0431\u044f \u2220"
		},
		{
			"Cp1253",
			"Brides (Greece)",
			"directed by Pantelis Voulgaris",
			"\u039d\u03cd\u03c6\u03b5\u03c2 \u2220"
		}
	};

	/**
	 * Creates a PDF document.
	 * @param filename the path to the new PDF document
	 * @throws DocumentException 
	 * @throws IOException 
	 * @throws    DocumentException 
	 * @throws    IOException
	 */
	public static void createPdf(String filename) throws IOException, DocumentException {
		// step 1
		Document document = new Document();
		// step 2
		PdfWriter.getInstance(document, new FileOutputStream(filename));
		// step 3
		document.open();
		// step 4
		BaseFont bf;
		for (int i = 0; i < 4; i++) {
			bf = BaseFont.createFont(BaseFont.TIMES_ROMAN, MOVIES[i][0], BaseFont.EMBEDDED);
			document.add(new Paragraph("Font: " + bf.getPostscriptFontName()
					+ " with encoding: " + bf.getEncoding()));
			document.add(new Paragraph(MOVIES[i][1]));
			document.add(new Paragraph(MOVIES[i][2]));
			document.add(new Paragraph(MOVIES[i][3], new Font(bf, 12)));
			document.add(Chunk.NEWLINE);
		}
		// step 5
		document.close();
	}
	
	/*FontSelector selector = new FontSelector();
	selector.addFont(FontFactory.getFont("MSung-Light", "UniCNS-UCS2-H",BaseFont.NOT_EMBEDDED));

	Phrase ph = selector.process("\u2220");
	document.add(new Paragraph(ph));
	document.add(para);*/
}
