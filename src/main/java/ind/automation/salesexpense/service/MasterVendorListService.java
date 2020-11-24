package ind.automation.salesexpense.service;

import java.util.ArrayList;
import java.util.List;
import ind.automation.salesexpense.entity.MasterVendorListEntity;
import ind.automation.salesexpense.responsebeans.MasterVendorListResponseBean;

public interface MasterVendorListService {
	ArrayList<MasterVendorListResponseBean> getMasterVendorList();
	
	List<MasterVendorListEntity> getMasterVendorListData();
}
