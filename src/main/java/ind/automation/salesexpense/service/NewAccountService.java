package ind.automation.salesexpense.service;

import java.util.List;
import ind.automation.salesexpense.beans.NewMasterAccountDetails;
import ind.automation.salesexpense.entity.MasterVendorListEntity;
import ind.automation.salesexpense.responsebeans.NewVendorResponse;

public interface NewAccountService {
	NewVendorResponse addNewMasterAccountDetails(NewMasterAccountDetails newMasterAccountDetails);

	void saveNewAccountDetails(MasterVendorListEntity entity);

	void updateNewAccountDetails(MasterVendorListEntity entity);

	List<MasterVendorListEntity> getMasterDetailByVendor(String vendor);
}
