package ind.automation.salesexpense.controller;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ind.automation.salesexpense.responsebeans.MasterVendorListResponseBean;
import ind.automation.salesexpense.service.MasterVendorListService;

@RestController
@RequestMapping("/salesexpense")
public class GetMasterVendorListController {
	
	@Autowired
	private MasterVendorListService service;

	@CrossOrigin
	@GetMapping(value = "/getmastervendorlist")
	public ResponseEntity<ArrayList<MasterVendorListResponseBean>> uploadDocuments() {
		ArrayList<MasterVendorListResponseBean> bean = service.getMasterVendorList();
		return new ResponseEntity<>(bean, HttpStatus.OK);
	}
}
