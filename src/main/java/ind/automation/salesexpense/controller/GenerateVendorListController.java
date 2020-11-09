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
import ind.automation.salesexpense.beans.DocumentType;
import ind.automation.salesexpense.responsebeans.BankStatementResponseListResponse;
import ind.automation.salesexpense.responsebeans.FullVendorListResponseBean;
import ind.automation.salesexpense.service.BalanceSheetService;
import ind.automation.salesexpense.service.BankStatementConversionService;
import ind.automation.salesexpense.service.DesktopDocumentsService;
import ind.automation.salesexpense.utilities.Constant;

@RestController
@RequestMapping("/salesexpense")
public class GenerateVendorListController {

	private ObjectMapper objectMapper = new ObjectMapper();

	@Autowired
	private BalanceSheetService balanceSheetService;

	@Autowired
	private DesktopDocumentsService desktopDocumentsService;

	@Autowired
	private BankStatementConversionService bankStatementConversionService;

	@CrossOrigin
	@PostMapping(value = "/uploaddocuments")
	public ResponseEntity<FullVendorListResponseBean> uploadDocuments(
			@RequestPart("balanceSheetDetails") MultipartFile balanceSheetDetails,
			@RequestPart("acccountList") MultipartFile acccountList, @RequestPart("documentType") String documentType)
			throws EncryptedDocumentException, InvalidFormatException, IOException {
		DocumentType documentTypeBean = objectMapper.readValue(documentType, DocumentType.class);
		FullVendorListResponseBean bean = new FullVendorListResponseBean();
		if (Constant.DESKTOP_DOCUMENT_TYPE.equalsIgnoreCase(documentTypeBean.getDocumentType())) {
			bean = desktopDocumentsService.modifyDesktopBalanceSheet(balanceSheetDetails, acccountList);
		} else {
			bean = balanceSheetService.modifyBalanceSheet(balanceSheetDetails, acccountList);
		}
		return new ResponseEntity<>(bean, HttpStatus.OK);
	}

	@CrossOrigin
	@PostMapping(value = "/uploadbankstatement")
	public ResponseEntity<BankStatementResponseListResponse> uploadBankstatement(
			@RequestPart("file") MultipartFile file) throws Exception {
		BankStatementResponseListResponse bankStatementList = bankStatementConversionService.convertBankStatement(file);
		return new ResponseEntity<>(bankStatementList, HttpStatus.OK);
	}
}