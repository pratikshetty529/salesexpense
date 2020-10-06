package ind.automation.salesexpense.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.web.multipart.MultipartFile;

import ind.automation.salesexpense.beans.AccountList;
import ind.automation.salesexpense.responsebeans.FullVendorListResponseBean;

public interface BalanceSheetService {
	FullVendorListResponseBean modifyBalanceSheet(MultipartFile balanceSheetFile, MultipartFile accountListFile)
			throws EncryptedDocumentException, InvalidFormatException, IOException;

	FullVendorListResponseBean createVendorList(String balanceSheetFileName, AccountList accountListBean)
			throws FileNotFoundException, IOException;

	AccountList getAccountList(MultipartFile accountListFile) throws IOException;
}
