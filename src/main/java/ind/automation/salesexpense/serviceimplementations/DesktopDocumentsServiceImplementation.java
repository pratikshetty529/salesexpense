package ind.automation.salesexpense.serviceimplementations;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;
import java.util.stream.Collectors;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ind.automation.salesexpense.beans.AccountList;
import ind.automation.salesexpense.beans.IndexedEntry;
import ind.automation.salesexpense.responsebeans.FullVendorListResponseBean;
import ind.automation.salesexpense.responsebeans.VendorListResponseBean;
import ind.automation.salesexpense.service.DesktopDocumentsService;
import ind.automation.salesexpense.utilities.Parameters;

@Component
public class DesktopDocumentsServiceImplementation implements DesktopDocumentsService {

	@Autowired
	private Parameters parameters;

	@Autowired
	private DesktopDocumentsService service;

	@Override
	public FullVendorListResponseBean modifyDesktopBalanceSheet(MultipartFile balanceSheetFile,
			MultipartFile accountListFile) throws EncryptedDocumentException, InvalidFormatException, IOException {
		AccountList accountListBean = service.getDesktopAccountList(accountListFile);
		XSSFWorkbook workbook = new XSSFWorkbook(balanceSheetFile.getInputStream());
		XSSFSheet worksheet = workbook.getSheetAt(0);
		int lastRow = worksheet.getLastRowNum();
		while (lastRow >= 0 && worksheet.getRow(lastRow).getCell(0) == null) {
			lastRow--;
		}
		int columnSize = lastRow - 2;
		String tempValue = "";
		String creditTempValue = "";
		for (int i = 4; i < columnSize; i++) {
			XSSFRow row = worksheet.getRow(i);
			XSSFCell cell = row.getCell((short) 3);
			XSSFCell credit_card_cell = row.getCell((short) 4);
			if (cell == null || cell.toString() == "") {
				cell = row.createCell((short) 3);
				cell.setCellValue(tempValue);
			} else {
				tempValue = cell.toString();
			}
			if (credit_card_cell == null || credit_card_cell.toString() == "") {
				credit_card_cell = row.createCell((short) 4);
				credit_card_cell.setCellValue(creditTempValue);
			} else {
				creditTempValue = credit_card_cell.toString();
			}
		}
		String outputFileName = UUID.randomUUID().toString().replace("-", "") + ".xlsx";
		FileOutputStream outputStream = new FileOutputStream(outputFileName);
		workbook.write(outputStream);
		workbook.close();
		outputStream.close();
		FullVendorListResponseBean bean = createVendorList(outputFileName, accountListBean);
		return bean;
	}

	@SuppressWarnings("deprecation")
	@Override
	public FullVendorListResponseBean createVendorList(String balanceSheetFileName, AccountList accountListBean)
			throws FileNotFoundException, IOException {
		ArrayList<String> outputList = new ArrayList<>();
		ArrayList<String> accountList = accountListBean.getAccountList();
		accountList.stream().parallel().map((item) -> item.toLowerCase().replace(" ", ""))
				.collect(Collectors.toCollection(() -> outputList));
		FullVendorListResponseBean responseBean = new FullVendorListResponseBean();
		FileInputStream fis = new FileInputStream(
				new File(System.getProperty("user.dir") + "/" + balanceSheetFileName));
		XSSFWorkbook workbook = new XSSFWorkbook(fis);
		XSSFSheet worksheet = workbook.getSheetAt(0);
		int lastRow = worksheet.getLastRowNum();
		while (lastRow >= 0 && worksheet.getRow(lastRow).getCell(0) == null) {
			lastRow--;
		}
		int columnSize = lastRow - 2;
		int nameColumnIndex = 0;
		int splitColumnIndex = 0;
		Sheet s = workbook.getSheetAt(0);
		Row r = s.getRow(0);
		for (int cn = 0; cn < r.getLastCellNum(); cn++) {
			Cell c = r.getCell(cn);
			if (c == null || c.getCellType() == Cell.CELL_TYPE_BLANK) {
				continue;
			}
			if (c.getCellType() == Cell.CELL_TYPE_STRING) {
				String text = c.getStringCellValue();
				if ("Name".equals(text)) {
					nameColumnIndex = cn;
				}
				if ("Split".equals(text)) {
					splitColumnIndex = cn;
				}
			}
		}
		ArrayList<String> vendorNameList = new ArrayList<String>();
		for (int i = 5; i < columnSize; i++) {
			XSSFRow row = worksheet.getRow(i);
			XSSFCell assetsCell = row.getCell((short) 3);
			XSSFCell cell = row.getCell((short) nameColumnIndex);
			XSSFCell creditCardCell = row.getCell((short) 4);
			if (cell != null && cell.toString() != "") {
				if (i > 242) {
					System.out.print("242");
				}
				if (creditCardCell != null && creditCardCell.toString() != ""
						&& outputList.contains(creditCardCell.toString().toLowerCase().replace(" ", ""))
						&& !outputList.contains(creditCardCell.toString().toLowerCase().replace(" ", ""))) {
					if (!vendorNameList.contains(cell.toString())) {
						vendorNameList.add(cell.toString());
					}
				}
				if (assetsCell != null && assetsCell.toString() != ""
						&& outputList.contains(assetsCell.toString().toLowerCase().replace(" ", ""))
						&& !outputList.contains(cell.toString().toLowerCase().replace(" ", ""))) {
					if (!vendorNameList.contains(cell.toString())) {
						vendorNameList.add(cell.toString());
					}
				}
			}
		}
		Collections.sort(vendorNameList);
		System.out.println(vendorNameList.size());
		ArrayList<VendorListResponseBean> vendorListResponseBeanList = new ArrayList<VendorListResponseBean>();
		for (int j = 0; j < vendorNameList.size(); j++) {
			VendorListResponseBean bean = new VendorListResponseBean();
			ArrayList<Integer> getindex = new ArrayList<Integer>();
			for (int i = 5; i < columnSize; i++) {
				XSSFRow row = worksheet.getRow(i);
				XSSFCell cell = row.getCell((short) nameColumnIndex);
				if (cell != null && cell.toString() != "") {
					if (vendorNameList.get(j).contains(cell.toString())) {
						getindex.add(row.getRowNum());
					}
				}
			}

			ArrayList<String> uniqueAccountlist = new ArrayList<String>();
			for (int i : getindex) {
				XSSFRow row = worksheet.getRow(i);
				XSSFCell cell = row.getCell((short) splitColumnIndex);
				XSSFCell assetsCell = row.getCell((short) 3);
				XSSFCell creditCardCell = row.getCell((short) 4);
				if (cell != null && cell.toString() != "") {
					if (creditCardCell != null && creditCardCell.toString() != ""
							&& outputList.contains(creditCardCell.toString().toLowerCase().replace(" ", ""))
							&& !outputList.contains(creditCardCell.toString().toLowerCase().replace(" ", ""))) {
						if (!uniqueAccountlist.contains(cell.toString())) {
							uniqueAccountlist.add(cell.toString());
						}
					}
					if (assetsCell != null && assetsCell.toString() != ""
							&& outputList.contains(assetsCell.toString().toLowerCase().replace(" ", ""))
							&& !outputList.contains(cell.toString().toLowerCase().replace(" ", ""))) {
						if (!uniqueAccountlist.contains(cell.toString())) {
							uniqueAccountlist.add(cell.toString());
						}
					}
				}
			}
			if (uniqueAccountlist.isEmpty()) {
				vendorNameList.remove(j);
				continue;
			}
			bean.setName(vendorNameList.get(j));
			bean.setAccount(uniqueAccountlist);
			ArrayList<Integer> accountCount = new ArrayList<Integer>();
			int count = 0;
			for (int k = 0; k < bean.getAccount().size(); k++) {
				count = 0;
				for (int z : getindex) {
					XSSFRow accountRow = worksheet.getRow(z);
					XSSFCell accountCell = accountRow.getCell((short) splitColumnIndex);
					if (accountCell != null && accountCell.toString() != "") {
						if (bean.getAccount().get(k).contains(accountCell.toString())) {
							count = count + 1;
						}
					}
				}
				accountCount.add(count);
			}
			bean.setCount(accountCount);
			ArrayList<IndexedEntry<Integer>> ordered = new ArrayList<>();
			for (int i = 0; i < accountCount.size(); i++) {
				IndexedEntry<Integer> entry = new IndexedEntry<>(i, accountCount.get(i));
				ordered.add(entry);
			}
			Collections.sort(ordered, Collections.reverseOrder());
			ArrayList<Integer> sortedCountList = new ArrayList<Integer>();
			for (int g = 0; g < ordered.size(); g++) {
				sortedCountList.add(Integer.valueOf(ordered.get(g).toString()));
			}
			ArrayList<String> sortedUniqueAccountlist = new ArrayList<String>();
			ArrayList<Integer> sortedCountlist = new ArrayList<Integer>();
			for (int i = 0; i < sortedCountList.size(); i++) {
				String sortedAccountName = uniqueAccountlist.get(sortedCountList.get(i));
				Integer sortedCount = accountCount.get(sortedCountList.get(i));
				sortedUniqueAccountlist.add(sortedAccountName);
				sortedCountlist.add(sortedCount);
			}
			bean.setAccount(null);
			bean.setCount(null);
			bean.setAccount(sortedUniqueAccountlist);
			bean.setCount(sortedCountlist);
			vendorListResponseBeanList.add(bean);
		}
		workbook.close();
		File tempTxtFile = new File(balanceSheetFileName);
		tempTxtFile.delete();
		responseBean.setVendorList(vendorListResponseBeanList);
		responseBean.setAccountList(accountListBean.getAccountList());
		return responseBean;
	}

	@SuppressWarnings("deprecation")
	@Override
	public AccountList getDesktopAccountList(MultipartFile accountListFile) throws IOException {
		XSSFWorkbook workbook = new XSSFWorkbook(accountListFile.getInputStream());
		XSSFSheet worksheet = workbook.getSheetAt(0);
		int lastRow = worksheet.getLastRowNum() + 1;
		AccountList bean = new AccountList();
		ArrayList<String> accountList = new ArrayList<String>();
		Sheet s = workbook.getSheetAt(0);
		Row r = s.getRow(0);
		int acccountColumnIndex = 0;
		int typeColumnIndex = 0;
		for (int cn = 0; cn < r.getLastCellNum(); cn++) {
			Cell c = r.getCell(cn);
			if (c == null || c.getCellType() == Cell.CELL_TYPE_BLANK) {
				continue;
			}
			if (c.getCellType() == Cell.CELL_TYPE_STRING) {
				String text = c.getStringCellValue();
				if ("Account".equals(text)) {
					acccountColumnIndex = cn;
				}
				if ("Type".equals(text)) {
					typeColumnIndex = cn;
				}
			}
		}
		for (int i = 0; i < lastRow; i++) {
			XSSFRow row = worksheet.getRow(i);
			if (row != null) {
				XSSFCell typeCell = row.getCell((short) typeColumnIndex);
				XSSFCell accountCell = row.getCell((short) acccountColumnIndex);
				if (typeCell != null
						&& parameters.getAccountList().contains(typeCell.toString().toLowerCase().replace(" ", ""))
						&& accountCell != null) {
					if (!accountList.contains(accountCell.toString())) {
						if (!accountCell.toString().equals("`")) {
							accountList.add(accountCell.toString());
						}
					}
				}
			}
		}
		workbook.close();
		bean.setAccountList(accountList);
		return bean;
	}
}