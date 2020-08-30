package ind.automation.salesexpense.controller;

import java.io.IOException;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.databind.ObjectMapper;
import ind.automation.salesexpense.responsebeans.BankStatementResponseBean;
import ind.automation.salesexpense.responsebeans.FullVendorListResponseBean;
import ind.automation.salesexpense.service.BankStatementService;

@RestController
@RequestMapping("/salesexpense")
public class BankStatementController {

	private ObjectMapper objectMapper = new ObjectMapper();

	@Autowired
	private BankStatementService service;
	
	@CrossOrigin
	@PostMapping(value = "/processbankstatement")
	public ResponseEntity<BankStatementResponseBean> uploadDocuments(@RequestPart("file") MultipartFile file,
			@RequestPart("vendorlistdata") String vendorlist)
			throws EncryptedDocumentException, InvalidFormatException, IOException {
		FullVendorListResponseBean vendorListData = objectMapper.readValue(vendorlist,
				FullVendorListResponseBean.class);
		BankStatementResponseBean bean = service.processBankStatement(file, vendorListData);
		return new ResponseEntity<>(bean, HttpStatus.OK);
	}

}
