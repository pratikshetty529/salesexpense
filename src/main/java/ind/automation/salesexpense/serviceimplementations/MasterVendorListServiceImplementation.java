package ind.automation.salesexpense.serviceimplementations;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ind.automation.salesexpense.service.MasterVendorListService;
import ind.automation.salesexpense.entity.MasterVendorListEntity;
import ind.automation.salesexpense.repository.MasterVendorListRepository;
import ind.automation.salesexpense.responsebeans.MasterVendorListResponseBean;

@Component
public class MasterVendorListServiceImplementation implements MasterVendorListService {

	@Autowired
	private MasterVendorListService service;

	@Autowired
	private MasterVendorListRepository repository;

	@Override
	public ArrayList<MasterVendorListResponseBean> getMasterVendorList() {
		List<MasterVendorListEntity> getMasterVendorListData = service.getMasterVendorListData();
		ArrayList<MasterVendorListResponseBean> masterVendorList = new ArrayList<MasterVendorListResponseBean>();
		if (!getMasterVendorListData.isEmpty()) {
			for (MasterVendorListEntity masterVendor : getMasterVendorListData) {
				MasterVendorListResponseBean bean = new MasterVendorListResponseBean();
				bean.setName(masterVendor.getVendorName());
				bean.setAccount(masterVendor.getAccountName());
				masterVendorList.add(bean);
			}
		}
		return masterVendorList;
	}

	@Override
	public List<MasterVendorListEntity> getMasterVendorListData() {
		return repository.getMasterVendorListData();
	}
}
