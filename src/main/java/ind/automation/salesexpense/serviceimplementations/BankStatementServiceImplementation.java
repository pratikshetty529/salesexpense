package ind.automation.salesexpense.serviceimplementations;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ind.automation.salesexpense.responsebeans.BankStatementFinalBean;
import ind.automation.salesexpense.responsebeans.BankStatementResponseBean;
import ind.automation.salesexpense.responsebeans.FullVendorListResponseBean;
import ind.automation.salesexpense.responsebeans.VendorListResponseBean;
import ind.automation.salesexpense.service.BankStatementService;
import ind.automation.salesexpense.service.MasterVendorListService;
import ind.automation.salesexpense.utilities.Parameters;
import ind.automation.salesexpense.utilities.PdfToExcel;
import ind.automation.salesexpense.beans.AccountVendorBean;
import ind.automation.salesexpense.beans.BankStatementBean;
import ind.automation.salesexpense.entity.MasterVendorListEntity;

@Component
public class BankStatementServiceImplementation implements BankStatementService {

	@Autowired
	private Parameters parameters;

	@Autowired
	private BankStatementService service;

	@Autowired
	private MasterVendorListService masterVendorService;

	@SuppressWarnings("deprecation")
	@Override
	public BankStatementResponseBean processBankStatement(MultipartFile file, FullVendorListResponseBean vendorList)
			throws IOException {
		BankStatementResponseBean responseBean = new BankStatementResponseBean();
		XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
		XSSFSheet worksheet = workbook.getSheetAt(0);
		int lastRow = worksheet.getLastRowNum() + 1;
		ArrayList<BankStatementBean> bankStatementList = new ArrayList<BankStatementBean>();
		List<MasterVendorListEntity> getMasterVendorListData = masterVendorService.getMasterVendorListData();
		int matchcnt = 0;
		for (int i = 0; i < lastRow; i++) {
			String description = "";
			XSSFRow row = worksheet.getRow(i);
			if (row != null) {
				XSSFCell cell = row.getCell((short) 0);
				if (cell != null && cell.getCellTypeEnum() != CellType.STRING) {
					if (DateUtil.isCellDateFormatted(cell)) {
						BankStatementBean bean = new BankStatementBean();
						LocalDate paymentDateValue = PdfToExcel.convertToLocalDateViaInstant(cell.getDateCellValue());
						bean.setDate(java.sql.Date.valueOf(paymentDateValue));
						XSSFRow despRow = worksheet.getRow(i + 1);
						if (despRow != null) {
							XSSFCell dateCell = despRow.getCell((short) 0);
							XSSFCell amountCell = despRow.getCell((short) 2);
							XSSFCell continuedDescriptionCell = despRow.getCell((short) 1);
							if ((amountCell == null || amountCell.toString().equals("")
									&& (dateCell == null || dateCell.toString().equals(""))
									&& continuedDescriptionCell != null)) {
								description = row.getCell((short) 1).toString() + " "
										+ continuedDescriptionCell.toString();
							} else {
								description = row.getCell((short) 1).toString();
							}
						} else {
							description = row.getCell((short) 1).toString();
						}
						bean.setDescription(description);
						bean.setAmount(Double.parseDouble(row.getCell((short) 2).toString()));
						AccountVendorBean accountVendorBean = getAccountVendorDetails(description, vendorList);
						if (accountVendorBean.getAccount() != null) {
							matchcnt += 1;
							bean.setVendor(accountVendorBean.getVendor());
							bean.setAccount(accountVendorBean.getAccount());
							bean.setFlag("V");
						} else {
							AccountVendorBean masterBean = getMasterVendorDetails(description, getMasterVendorListData);
							if (masterBean.getAccount() != null) {
								bean.setVendor(masterBean.getVendor());
								bean.setAccount(masterBean.getAccount());
								bean.setFlag("M");
							} else {
								bean.setVendor("");
								bean.setAccount("");
								bean.setFlag("");
							}
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
		Collections.sort(bankStatementList, new Comparator<BankStatementBean>() {
			public int compare(BankStatementBean c1, BankStatementBean c2) {
				return c1.getVendor().compareTo(c2.getVendor());
			}
		});
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		ArrayList<BankStatementFinalBean> finalBeanList = new ArrayList<BankStatementFinalBean>();
		for (BankStatementBean b : bankStatementList) {
			BankStatementFinalBean finalBean = new BankStatementFinalBean();
			finalBean.setDate(formatter.format(b.getDate()));
			finalBean.setDescription(b.getDescription());
			finalBean.setAmount(b.getAmount());
			finalBean.setVendor(b.getVendor());
			finalBean.setAccount(b.getAccount());
			finalBean.setFlag(b.getFlag());
			finalBeanList.add(finalBean);
		}
		responseBean.setOpeningBalance(0.0);
		responseBean.setClosingBalance(closingBalance);
		responseBean.setCleanedBankStatement(finalBeanList);
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

	@Override
	public AccountVendorBean getMasterVendorDetails(String description, List<MasterVendorListEntity> entityList) {
		AccountVendorBean bean = new AccountVendorBean();
		for (MasterVendorListEntity entity : entityList) {
			if (description.toLowerCase().replaceAll("\\'", "").replace("`", "")
					.indexOf(entity.getVendorName().toLowerCase().replaceAll("\\'", "").replace("`", "")) != -1) {
				bean.setVendor(entity.getVendorName());
				bean.setAccount(entity.getAccountName());
				break;
			} else {
				String vendorSplit[] = entity.getVendorName().split(" ");
				for (int i = 0; i < vendorSplit.length; i++) {
					String vendorSplitName = vendorSplit[i].replaceAll("[*0-9]", "").replace("(", "").replace(")", "");
					if (vendorSplitName.length() > 2
							&& !parameters.getIgnoredWords().contains(vendorSplitName.toLowerCase())
							&& (description.toLowerCase().indexOf(parameters.getAtmWithdrawal().toLowerCase())) < 0) {
						if (service.matchVendorName(
								description.toLowerCase().replaceAll("\\'", "").replace("`", "").replace(".", " "),
								vendorSplitName.toLowerCase().replaceAll("\\'", "").replace("`", "").replace(".",
										" "))) {
							bean.setVendor(entity.getVendorName());
							bean.setAccount(entity.getAccountName());
							break;
						}
					}
				}
			}
		}
		return bean;
	}
}