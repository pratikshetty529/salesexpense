package ind.automation.salesexpense.repository;

import java.util.List;
import ind.automation.salesexpense.entity.MasterVendorListEntity;

public interface MasterVendorListRepository {
	List<MasterVendorListEntity> getMasterVendorListData();
	
	void saveNewAccountDetails(MasterVendorListEntity entity);
	
	void updateNewAccountDetails(MasterVendorListEntity entity);
	
	List<MasterVendorListEntity> getMasterDetailByVendor(String vendor);
}
