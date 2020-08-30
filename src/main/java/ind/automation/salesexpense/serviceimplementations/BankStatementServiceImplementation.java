package ind.automation.salesexpense.serviceimplementations;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ind.automation.salesexpense.responsebeans.BankStatementResponseBean;
import ind.automation.salesexpense.responsebeans.FullVendorListResponseBean;
import ind.automation.salesexpense.responsebeans.VendorListResponseBean;
import ind.automation.salesexpense.service.BankStatementService;
import ind.automation.salesexpense.utilities.Parameters;
import ind.automation.salesexpense.utilities.PdfToExcel;
import ind.automation.salesexpense.beans.AccountVendorBean;
import ind.automation.salesexpense.beans.BankStatementBean;

@Component
public class BankStatementServiceImplementation implements BankStatementService {

	@Autowired
	private Parameters parameters;

	@Autowired
	private BankStatementService service;

	@SuppressWarnings("deprecation")
	@Override
	public BankStatementResponseBean processBankStatement(MultipartFile file, FullVendorListResponseBean vendorList)
			throws IOException {
		BankStatementResponseBean responseBean = new BankStatementResponseBean();
		XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
		XSSFSheet worksheet = workbook.getSheetAt(0);
		int lastRow = worksheet.getLastRowNum() + 1;
		ArrayList<BankStatementBean> bankStatementList = new ArrayList<BankStatementBean>();
		int matchcnt = 0;
		for (int i = 0; i < lastRow; i++) {
			XSSFRow row = worksheet.getRow(i);
			if (row != null) {
				XSSFCell cell = row.getCell((short) 0);
				if (cell.getCellTypeEnum() != CellType.STRING) {
					if (DateUtil.isCellDateFormatted(cell)) {
						BankStatementBean bean = new BankStatementBean();
						LocalDate paymentDateValue = PdfToExcel.convertToLocalDateViaInstant(cell.getDateCellValue());
						bean.setDate(java.sql.Date.valueOf(paymentDateValue));
						String description = row.getCell((short) 1).toString();
						bean.setDescription(description);
						bean.setAmount(Double.parseDouble(row.getCell((short) 2).toString()));
						AccountVendorBean accountVendorBean = getAccountVendorDetails(description, vendorList);
						if (accountVendorBean.getAccount() != null) {
							matchcnt += 1;
							bean.setVendor(accountVendorBean.getVendor());
							bean.setAccount(accountVendorBean.getAccount());
							bean.setFlag("V");
						}
						bankStatementList.add(bean);
					}
				}
			}
		}
		System.out.println(matchcnt);
		Collections.sort(bankStatementList, new Comparator<BankStatementBean>() {
			public int compare(BankStatementBean o1, BankStatementBean o2) {
				return o1.getDate().compareTo(o2.getDate());
			}
		});
		Double closingBalance = 0.0;
		for (BankStatementBean bankStatementBean : bankStatementList) {
			closingBalance += bankStatementBean.getAmount();
		}
		closingBalance = Math.round(closingBalance * 100.0) / 100.0;
		workbook.close();
		responseBean.setOpeningBalance(0.0);
		responseBean.setClosingBalance(closingBalance);
		responseBean.setCleanedBankStatement(bankStatementList);
		return responseBean;
	}

	@Override
	public AccountVendorBean getAccountVendorDetails(String description, FullVendorListResponseBean vendorList) {
		AccountVendorBean bean = new AccountVendorBean();
		ArrayList<VendorListResponseBean> vendorListData = vendorList.getVendorList();
		for (VendorListResponseBean vendorData : vendorListData) {
			if (description.toLowerCase().replaceAll("\\'", "").replace("`", "")
					.indexOf(vendorData.getName().toLowerCase().replaceAll("\\'", "").replace("`", "")) != -1) {
				bean.setVendor(vendorData.getName());
				bean.setAccount(vendorData.getAccount().get(0));
				break;
			} else {
				String vendorSplit[] = vendorData.getName().split(" ");
				for (int i = 0; i < vendorSplit.length; i++) {
					String vendorSplitName = vendorSplit[i].replaceAll("[*0-9]", "");
					if (vendorSplitName.length() > 2
							&& !parameters.getIgnoredWords().contains(vendorSplitName.toLowerCase())
							&& (description.toLowerCase().indexOf(parameters.getAtmWithdrawal().toLowerCase())) < 0) {
						if (service.matchVendorName(
								description.toLowerCase().replaceAll("\\'", "").replace("`", "").replace(".", " "),
								vendorSplitName.toLowerCase().replaceAll("\\'", "").replace("`", "").replace(".",
										" "))) {
							bean.setVendor(vendorData.getName());
							bean.setAccount(vendorData.getAccount().get(0));
							break;
						}
					}
				}
			}
		}
		return bean;
	}

	@Override
	public Boolean matchVendorName(String description, String vendorName) {
		Boolean flag = false;
		String descriptionSplit[] = description.split(" ");
		for (int i = 0; i < descriptionSplit.length; i++) {
			if (descriptionSplit[i].matches(vendorName)) {
				flag = true;
				break;
			}
		}
		return flag;
	}
}