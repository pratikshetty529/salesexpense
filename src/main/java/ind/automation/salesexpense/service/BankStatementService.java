package ind.automation.salesexpense.service;

import java.io.IOException;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import ind.automation.salesexpense.beans.AccountVendorBean;
import ind.automation.salesexpense.entity.MasterVendorListEntity;
import ind.automation.salesexpense.responsebeans.BankStatementResponseBean;
import ind.automation.salesexpense.responsebeans.FullVendorListResponseBean;

public interface BankStatementService {
	BankStatementResponseBean processBankStatement(MultipartFile file, FullVendorListResponseBean vendorList)
			throws IOException;

	AccountVendorBean getAccountVendorDetails(String description, FullVendorListResponseBean vendorList);
	
	AccountVendorBean getMasterVendorDetails(String description, List<MasterVendorListEntity> entity);
	
	Boolean matchVendorName(String description, String vendorName);
}
