package ind.automation.salesexpense.serviceimplementations;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ind.automation.salesexpense.beans.NewAccountDetails;
import ind.automation.salesexpense.beans.NewMasterAccountDetails;
import ind.automation.salesexpense.entity.MasterVendorListEntity;
import ind.automation.salesexpense.repository.MasterVendorListRepository;
import ind.automation.salesexpense.responsebeans.NewVendorResponse;
import ind.automation.salesexpense.service.NewAccountService;

@Component
public class NewAccountServiceImplementation implements NewAccountService {

	@Autowired
	private MasterVendorListRepository repository;

	@Autowired
	private NewAccountService service;

	@Override
	public NewVendorResponse addNewMasterAccountDetails(NewMasterAccountDetails newMasterAccountDetails) {
		ArrayList<NewAccountDetails> vendorAccountList = newMasterAccountDetails.getNewVendorDetails();
		NewVendorResponse response = new NewVendorResponse();
		try {
			for (NewAccountDetails bean : vendorAccountList) {
				List<MasterVendorListEntity> vendorExist = service.getMasterDetailByVendor(bean.getName());
				MasterVendorListEntity entity = new MasterVendorListEntity();
				entity.setVendorName(bean.getName().replaceAll("\\'", "").replace("`", ""));
				entity.setAccountName(bean.getAccount().replaceAll("\\'", "").replace("`", ""));
				if (vendorExist.isEmpty()) {
					service.saveNewAccountDetails(entity);
				} else {
					service.updateNewAccountDetails(entity);
				}
			}
			response.setMessage("New Vendor Records added successfully");
			return response;
		} catch (Exception ex) {
			response.setMessage("Error adding the new Vendor Details");
			return response;
		}
	}

	@Override
	public void saveNewAccountDetails(MasterVendorListEntity entity) {
		repository.saveNewAccountDetails(entity);
	}

	@Override
	public void updateNewAccountDetails(MasterVendorListEntity entity) {
		repository.updateNewAccountDetails(entity);
	}

	@Override
	public List<MasterVendorListEntity> getMasterDetailByVendor(String vendor) {
		return repository.getMasterDetailByVendor(vendor);
	}
}