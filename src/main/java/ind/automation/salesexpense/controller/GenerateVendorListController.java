package ind.automation.salesexpense.controller;

import java.io.IOException;
import java.util.ArrayList;
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
import ind.automation.salesexpense.responsebeans.BankStatementResponseListResponse;
import ind.automation.salesexpense.responsebeans.VendorListResponseBean;
import ind.automation.salesexpense.service.BalanceSheetService;
import ind.automation.salesexpense.service.BankStatementConversionService;

@RestController
@RequestMapping("/salesexpense")
public class GenerateVendorListController {

	@Autowired
	private BalanceSheetService balanceSheetService;

	@Autowired
	private BankStatementConversionService bankStatementConversionService;

	@CrossOrigin
	@PostMapping(value = "/uploaddocuments")
	public ResponseEntity<ArrayList<VendorListResponseBean>> uploadDocuments(@RequestPart("file") MultipartFile file)
			throws EncryptedDocumentException, InvalidFormatException, IOException {
		ArrayList<VendorListResponseBean> bean = balanceSheetService.modifyBalanceSheet(file);
		return new ResponseEntity<>(bean, HttpStatus.OK);
	}

	@CrossOrigin
	@PostMapping(value = "/uploadbankstatement")
	public ResponseEntity<BankStatementResponseListResponse> uploadBankstatement(
			@RequestPart("file") MultipartFile file)
			throws Exception {
		BankStatementResponseListResponse bankStatementList = bankStatementConversionService.convertBankStatement(file);
		return new ResponseEntity<>(bankStatementList, HttpStatus.OK);
	}
}
