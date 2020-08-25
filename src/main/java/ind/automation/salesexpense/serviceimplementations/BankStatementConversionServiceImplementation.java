package ind.automation.salesexpense.serviceimplementations;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ind.automation.salesexpense.responsebeans.BankStatementConversionResponseBean;
import ind.automation.salesexpense.responsebeans.BankStatementResponseListResponse;
import ind.automation.salesexpense.service.BankStatementConversionService;
import ind.automation.salesexpense.utilities.PdfToExcel;

@Component
public class BankStatementConversionServiceImplementation implements BankStatementConversionService {

	@Override
	public BankStatementResponseListResponse convertBankStatement(MultipartFile file) throws IOException {
		PdfToExcel obj = new PdfToExcel();
		String outputFileName = obj.convertPdfToExcel(file);
		FileInputStream fis = new FileInputStream(new File(System.getProperty("user.dir") + "\\" + outputFileName));
		XSSFWorkbook workbook = new XSSFWorkbook(fis);
		XSSFSheet worksheet = workbook.getSheetAt(0);
		DataFormatter formatter = new DataFormatter();
		String toFind = "Tran Id";
		ArrayList<Integer> getindex = new ArrayList<Integer>();
		for (Row row : worksheet) {
			for (Cell cell : row) {
				String text = formatter.formatCellValue(cell);
				if (toFind.equals(text)) {
					getindex.add(row.getRowNum());
				}
			}
		}
		int lastRow = worksheet.getLastRowNum();

		ArrayList<BankStatementConversionResponseBean> bankStatementList = new ArrayList<BankStatementConversionResponseBean>();
		for (int i : getindex) {
			int getIndexNo = 0;
			for (int j = i + 1; j < lastRow; j++) {
				if (j == getindex.get(getIndexNo + 1)) {
					break;
				}
				BankStatementConversionResponseBean bean = new BankStatementConversionResponseBean();
				XSSFRow row = worksheet.getRow(j);
				XSSFCell cellDate = row.getCell((short) 1);
				XSSFCell cellDescription = row.getCell((short) 2);
				XSSFCell cellAmount = row.getCell((short) 3);
				if (cellDate != null || cellDescription != null || cellAmount != null) {
					bean.setDate(cellDate.toString());
					bean.setDescription(cellDescription.toString());
					bean.setAmount(cellAmount.toString());
					bankStatementList.add(bean);
				} else {
					break;
				}
			}
		}
		workbook.close();
		BankStatementResponseListResponse listData = new BankStatementResponseListResponse();
		listData.setBankStatementList(bankStatementList);
		File tempTxtFile = new File(outputFileName);
		tempTxtFile.delete();
		return listData;
	}
}