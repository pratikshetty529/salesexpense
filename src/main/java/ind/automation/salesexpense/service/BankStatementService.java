package ind.automation.salesexpense.service;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;
import ind.automation.salesexpense.beans.AccountVendorBean;
import ind.automation.salesexpense.responsebeans.BankStatementResponseBean;
import ind.automation.salesexpense.responsebeans.FullVendorListResponseBean;

public interface BankStatementService {
	BankStatementResponseBean processBankStatement(MultipartFile file, FullVendorListResponseBean vendorList)
			throws IOException;

	AccountVendorBean getAccountVendorDetails(String description, FullVendorListResponseBean vendorList);
	
	Boolean matchVendorName(String description, String vendorName);
}
