package ind.automation.salesexpense.controller;

import java.io.IOException;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ind.automation.salesexpense.beans.SalesExpense;
import ind.automation.salesexpense.responsebeans.SalesExpenseResponse;
import ind.automation.salesexpense.service.SalesExpenseService;

@RestController
@RequestMapping("/salesexpense")
public class GenerateSalesExpenseController {
	
	@Autowired
	private SalesExpenseService service;
	
	@CrossOrigin
	@PostMapping(value = "/generatesalesexpense")
	public ResponseEntity<SalesExpenseResponse> generateSalesExpense(@RequestBody SalesExpense salesExpense)
			throws EncryptedDocumentException, InvalidFormatException, IOException {
		SalesExpenseResponse responseBean = service.generateSalesExpense(salesExpense);
		return new ResponseEntity<>(responseBean, HttpStatus.OK);
	}
}
