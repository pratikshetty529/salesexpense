package ind.automation.salesexpense.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ind.automation.salesexpense.beans.NewMasterAccountDetails;
import ind.automation.salesexpense.responsebeans.NewVendorResponse;
import ind.automation.salesexpense.service.NewAccountService;

@RestController
@RequestMapping("/salesexpense")
public class AddMasterVendorListController {

	@Autowired
	private NewAccountService service;

	@CrossOrigin
	@PostMapping(value = "/addnewvendordetails")
	public ResponseEntity<NewVendorResponse> addNewVendorDetails(@RequestBody NewMasterAccountDetails bean) {
		NewVendorResponse response = service.addNewMasterAccountDetails(bean);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
