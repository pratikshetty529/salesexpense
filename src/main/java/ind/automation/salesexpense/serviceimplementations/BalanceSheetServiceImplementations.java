package ind.automation.salesexpense.serviceimplementations;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ind.automation.salesexpense.responsebeans.VendorListResponseBean;
import ind.automation.salesexpense.service.BalanceSheetService;

@Component
public class BalanceSheetServiceImplementations implements BalanceSheetService {

	@Override
	public ArrayList<VendorListResponseBean> modifyBalanceSheet(MultipartFile file)
			throws EncryptedDocumentException, InvalidFormatException, IOException {
		XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
		XSSFSheet worksheet = workbook.getSheetAt(0);
		int lastRow = worksheet.getLastRowNum();
		while (lastRow >= 0 && worksheet.getRow(lastRow).getCell(0) == null) {
			lastRow--;
		}
		int columnSize = lastRow - 2;
		String tempValue = "";
		for (int i = 5; i < columnSize; i++) {
			XSSFRow row = worksheet.getRow(i);
			XSSFCell cell = row.getCell((short) 0);
			if (cell == null) {
				cell = row.createCell((short) 0);
				cell.setCellValue(tempValue);
			} else {
				tempValue = cell.toString();
			}
		}
		Path root = Paths.get(".").normalize().toAbsolutePath();
		String rootPath = root.toString();
		System.out.print(rootPath);
		String outputFileName = "BalanceSheetNew.xlsx";
		FileOutputStream outputStream = new FileOutputStream(outputFileName);
		workbook.write(outputStream);
		workbook.close();
		outputStream.close();
		ArrayList<VendorListResponseBean> bean = createVendorList(outputFileName);
		return bean;
	}

	@Override
	public ArrayList<VendorListResponseBean> createVendorList(String balanceSheetFileName) throws IOException {
		FileInputStream fis = new FileInputStream(new File(System.getProperty("user.dir") + "\\" + balanceSheetFileName));
		XSSFWorkbook workbook = new XSSFWorkbook(fis);
		XSSFSheet worksheet = workbook.getSheetAt(0);
		int lastRow = worksheet.getLastRowNum();
		while (lastRow >= 0 && worksheet.getRow(lastRow).getCell(0) == null) {
			lastRow--;
		}
		int columnSize = lastRow - 2;
		ArrayList<String> vendorNameList = new ArrayList<String>();
		for (int i = 8; i < columnSize; i++) {
			XSSFRow row = worksheet.getRow(i);
			XSSFCell cell = row.getCell((short) 4);
			if (cell != null && cell.toString() != "") {
				if (!vendorNameList.contains(cell.toString())) {
					vendorNameList.add(cell.toString());
				}
			}
		}
		Collections.sort(vendorNameList);
		ArrayList<VendorListResponseBean> vendorListResponseBeanList = new ArrayList<VendorListResponseBean>();
		for (int j = 0; j < vendorNameList.size(); j++) {
			VendorListResponseBean bean = new VendorListResponseBean();
			ArrayList<Integer> getindex = new ArrayList<Integer>();
			for (int i = 8; i < columnSize; i++) {
				XSSFRow row = worksheet.getRow(i);
				XSSFCell cell = row.getCell((short) 4);
				if (cell != null && cell.toString() != "") {
					if (vendorNameList.get(j).contains(cell.toString())) {
						getindex.add(row.getRowNum());
					}
				}
			}

			bean.setName(vendorNameList.get(j));
			ArrayList<String> uniqueAccountlist = new ArrayList<String>();
			for (int i : getindex) {
				XSSFRow row = worksheet.getRow(i);
				XSSFCell cell = row.getCell((short) 6);
				if (cell != null && cell.toString() != "") {
					if (!uniqueAccountlist.contains(cell.toString())) {
						uniqueAccountlist.add(cell.toString());
					}
				}
			}
			bean.setAccount(uniqueAccountlist);
			ArrayList<Integer> accountCount = new ArrayList<Integer>();
			int count = 0;
			for (int k = 0; k < bean.getAccount().size(); k++) {
				count = 0;
				for (int z : getindex) {
					XSSFRow accountRow = worksheet.getRow(z);
					XSSFCell accountCell = accountRow.getCell((short) 6);
					if (accountCell != null && accountCell.toString() != "") {
						if (bean.getAccount().get(k).contains(accountCell.toString())) {
							count = count + 1;
						}
					}
				}
				accountCount.add(count);
			}
			bean.setCount(accountCount);
			vendorListResponseBeanList.add(bean);
		}
		workbook.close();
		File tempTxtFile = new File(balanceSheetFileName);
		tempTxtFile.delete();
		return vendorListResponseBeanList;
	}
}