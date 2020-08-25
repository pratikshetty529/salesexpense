package ind.automation.salesexpense.utilities;

import java.util.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.springframework.web.multipart.MultipartFile;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import technology.tabula.ObjectExtractor;
import technology.tabula.Page;
import technology.tabula.RectangularTextContainer;
import technology.tabula.Table;
import technology.tabula.extractors.SpreadsheetExtractionAlgorithm;
/*import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;*/

public class PdfToExcel {

	@SuppressWarnings("rawtypes")
	public String convertPdfToExcel(MultipartFile file) throws IOException {
		/*
		 * try (PDDocument pdfDocument = PDDocument.load(new
		 * File("D:\\SAP\\salesexpense\\" + pdfFilename))) {
		 * 
		 * pdfDocument.getClass();
		 * 
		 * if (!pdfDocument.isEncrypted()) {
		 * 
		 * PDFTextStripperByArea pdfTextStripperByArea = new PDFTextStripperByArea();
		 * pdfTextStripperByArea.setSortByPosition(Boolean.TRUE);
		 * 
		 * PDFTextStripper pdfTextStripper = new PDFTextStripper();
		 * 
		 * String pdfFileInText = pdfTextStripper.getText(pdfDocument);
		 * 
		 * String lines[] = pdfFileInText.split("\\r?\\n"); for (String line : lines) {
		 * System.out.println(line); }
		 * 
		 * }
		 * 
		 * }
		 */
		File pdfFile = convertMultiPartToFile(file);

		// Set Excel options
		PDDocument pd = PDDocument.load(pdfFile);
		int totalPages = pd.getNumberOfPages();
		ObjectExtractor oe = new ObjectExtractor(pd);
		SpreadsheetExtractionAlgorithm sea = new SpreadsheetExtractionAlgorithm();
		String fileName = "Geek.txt";
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
			out.close();
		} catch (IOException e) {
			System.out.println("Exception Occurred" + e);
		}

		for (int pageno = 0; pageno < totalPages; pageno++) {
			Page page = oe.extract(pageno + 1);

			// extract text from the table after detecting
			List<Table> table = sea.extract(page);
			for (Table tables : table) {

				List<List<RectangularTextContainer>> rows = tables.getRows();
				for (int i = 0; i < rows.size(); i++) {
					String str = "";
					List<RectangularTextContainer> cells = rows.get(i);

					for (int j = 0; j < cells.size(); j++) {
						if (pageno == 0 && i > 0 && j == 0) {
							str = "\n";
						} else if (pageno > 0 && i > 0 && j == 0) {
							str = "\n";
						} else if (pageno > 0 && i == 0 && j == 0) {
							str = "\n";
						}

						if (j == cells.size() - 1) {
							str = str + cells.get(j).getText().replaceAll("\\r\\n|\\r|\\n", "");
						} else {
							str = str + cells.get(j).getText().replaceAll("\\r\\n|\\r|\\n", "") + "|";
						}
					}

					appendStrToFile(fileName, str);

					// Let us print modified file
					try {
						@SuppressWarnings("resource")
						BufferedReader in = new BufferedReader(new FileReader("Geek.txt"));

						String mystring;
						while ((mystring = in.readLine()) != null) {
							System.out.println(mystring);
						}
					} catch (IOException e) {
						System.out.println("Exception Occurred" + e);
					}

					System.out.println();
				}
			}
		}

		// Let us append given str to above
		// created file.

		String txtFileName = System.getProperty("user.dir") + "\\Geek.txt";
		String excelFileName = System.getProperty("user.dir") + "\\Sample.xlsx";

		// Create a Workbook and a sheet in it
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Sheet1");

		// Read your input file and make cells into the workbook
		try (BufferedReader br = new BufferedReader(new FileReader(txtFileName))) {
			String line;
			Row row;
			Cell cell;
			int rowIndex = 0;
			while ((line = br.readLine()) != null) {
				row = sheet.createRow(rowIndex);
				String[] tokens = line.split("[|]");
				for (int iToken = 0; iToken < tokens.length; iToken++) {
					cell = row.createCell(iToken);
					cell.setCellValue(tokens[iToken]);
				}
				rowIndex++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Write your xlsx file
		try (FileOutputStream outputStream = new FileOutputStream(excelFileName)) {
			workbook.write(outputStream);
			workbook.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		File tempTxtFile = new File(fileName);
		tempTxtFile.delete();
		pd.close();
		Files.deleteIfExists(Paths.get(System.getProperty("user.dir") + "\\" + file.getOriginalFilename()));
		String outputFileName = "Sample.xlsx";
		return outputFileName;

		/*
		 * 
		 * 
		 * 
		 * 
		 * 
		 * final String apiKey = "1znymwarhvc0";
		 * 
		 * 
		 * 
		 * if (!formats.contains(format)) {
		 * System.out.println("Invalid output format: \"" + format + "\"");
		 * System.exit(1); }
		 * 
		 * // Avoid cookie warning with default cookie configuration RequestConfig
		 * globalConfig =
		 * RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build();
		 * 
		 * File inputFile = new File("D:\\Akshay Shetty\\salesexpense\\" + pdfFilename);
		 * 
		 * if (!inputFile.canRead()) {
		 * System.out.println("Can't read input PDF file: \"" + pdfFilename + "\"");
		 * System.exit(1); }
		 * 
		 * try (CloseableHttpClient httpclient =
		 * HttpClients.custom().setDefaultRequestConfig(globalConfig).build()) {
		 * HttpPost httppost = new HttpPost("https://pdftables.com/api?format=" + format
		 * + "&key=" + apiKey); FileBody fileBody = new FileBody(inputFile);
		 * 
		 * HttpEntity requestBody = MultipartEntityBuilder.create().addPart("f",
		 * fileBody).build(); httppost.setEntity(requestBody);
		 * 
		 * System.out.println("Sending request");
		 * 
		 * try (CloseableHttpResponse response = httpclient.execute(httppost)) { if
		 * (response.getStatusLine().getStatusCode() != 200) {
		 * System.out.println(response.getStatusLine()); System.exit(1); } HttpEntity
		 * resEntity = response.getEntity(); if (resEntity != null) { final String
		 * outputFilename = getOutputFilename(pdfFilename, format.replaceFirst("-.*$",
		 * "")); System.out.println("Writing output to " + outputFilename);
		 * 
		 * final File outputFile = new File(outputFilename);
		 * FileUtils.copyToFile(resEntity.getContent(), outputFile); return
		 * outputFilename; } else {
		 * System.out.println("Error: file missing from response"); System.exit(1);
		 * return ""; } } }
		 */
	}

	public static void appendStrToFile(String fileName, String str) {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(fileName, true));
			out.write(str);
			out.close();
		} catch (IOException e) {
			System.out.println("exception occoured" + e);
		}
	}

	public static File convertMultiPartToFile(MultipartFile file) {
		File convFile = new File(file.getOriginalFilename());
		try (FileOutputStream fos = new FileOutputStream(convFile)) {
			fos.write(file.getBytes());
		} catch (IOException ex) {
			ex.getMessage();
		}
		return convFile;
	}

	public static boolean deleteFile(File file) {
		Boolean isDeleted = false;
		if (file.delete()) {
			isDeleted = true;
		}
		return isDeleted;
	}
}
