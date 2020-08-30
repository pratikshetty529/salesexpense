package ind.automation.salesexpense.serviceimplementations;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ind.automation.salesexpense.responsebeans.FullVendorListResponseBean;
import ind.automation.salesexpense.responsebeans.VendorListResponseBean;
import ind.automation.salesexpense.service.BalanceSheetService;
import ind.automation.salesexpense.utilities.Parameters;
import ind.automation.salesexpense.beans.AccountList;
import ind.automation.salesexpense.beans.IndexedEntry;

@Component
public class BalanceSheetServiceImplementations implements BalanceSheetService {

	@Autowired
	private Parameters parameters;

	@Autowired
	private BalanceSheetService service;

	@Override
	public FullVendorListResponseBean modifyBalanceSheet(MultipartFile balanceSheetFile, MultipartFile accountListFile)
			throws EncryptedDocumentException, InvalidFormatException, IOException {
		AccountList accountListBean = service.getAccountList(accountListFile);
		XSSFWorkbook workbook = new XSSFWorkbook(balanceSheetFile.getInputStream());
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
		String outputFileName = "BalanceSheetNew.xlsx";
		FileOutputStream outputStream = new FileOutputStream(outputFileName);
		workbook.write(outputStream);
		workbook.close();
		outputStream.close();
		FullVendorListResponseBean bean = createVendorList(outputFileName, accountListBean);
		return bean;
	}

	@Override
	public FullVendorListResponseBean createVendorList(String balanceSheetFileName, AccountList accountListBean)
			throws IOException {
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
		ArrayList<String> vendorNameList = new ArrayList<String>();
		for (int i = 8; i < columnSize; i++) {
			XSSFRow row = worksheet.getRow(i);
			XSSFCell assetsCell = row.getCell((short) 0);
			XSSFCell cell = row.getCell((short) 4);
			if (assetsCell != null && outputList.contains(assetsCell.toString().toLowerCase().replace(" ", ""))) {
				if (cell != null && cell.toString() != "") {
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
			for (int i = 8; i < columnSize; i++) {
				XSSFRow row = worksheet.getRow(i);
				XSSFCell assetsCell = row.getCell((short) 0);
				XSSFCell cell = row.getCell((short) 4);
				if (assetsCell != null && outputList.contains(assetsCell.toString().toLowerCase().replace(" ", ""))) {
					if (cell != null && cell.toString() != "") {
						if (vendorNameList.get(j).contains(cell.toString())) {
							getindex.add(row.getRowNum());
						}
					}
				}
			}

			bean.setName(vendorNameList.get(j));
			ArrayList<String> uniqueAccountlist = new ArrayList<String>();
			for (int i : getindex) {
				XSSFRow row = worksheet.getRow(i);
				XSSFCell assetsCell = row.getCell((short) 0);
				XSSFCell cell = row.getCell((short) 6);
				if (assetsCell != null && outputList.contains(assetsCell.toString().toLowerCase().replace(" ", ""))) {
					if (cell != null && cell.toString() != "") {
						if (!uniqueAccountlist.contains(cell.toString())) {
							uniqueAccountlist.add(cell.toString());
						}
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
					XSSFCell assetsCell = accountRow.getCell((short) 0);
					XSSFCell accountCell = accountRow.getCell((short) 6);
					if (assetsCell != null
							&& outputList.contains(assetsCell.toString().toLowerCase().replace(" ", ""))) {
						if (accountCell != null && accountCell.toString() != "") {
							if (bean.getAccount().get(k).contains(accountCell.toString())) {
								count = count + 1;
							}
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

	@Override
	public AccountList getAccountList(MultipartFile accountListFile) throws IOException {
		XSSFWorkbook workbook = new XSSFWorkbook(accountListFile.getInputStream());
		XSSFSheet worksheet = workbook.getSheetAt(0);
		int lastRow = worksheet.getLastRowNum() + 1;
		AccountList bean = new AccountList();
		ArrayList<String> accountList = new ArrayList<String>();
		for (int i = 5; i < lastRow; i++) {
			XSSFRow row = worksheet.getRow(i);
			if (row != null) {
				XSSFCell accountCell = row.getCell((short) 2);
				XSSFCell cell = row.getCell((short) 1);
				if (accountCell != null && parameters.getAccountList()
						.contains(accountCell.toString().toLowerCase().replace(" ", ""))) {
					if (cell != null && cell.toString() != "") {
						if (!accountList.contains(cell.toString())) {
							accountList.add(cell.toString());
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