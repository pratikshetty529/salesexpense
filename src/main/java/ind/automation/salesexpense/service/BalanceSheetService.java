package ind.automation.salesexpense.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.web.multipart.MultipartFile;
import ind.automation.salesexpense.responsebeans.VendorListResponseBean;

public interface BalanceSheetService {
	ArrayList<VendorListResponseBean> modifyBalanceSheet(MultipartFile file) throws EncryptedDocumentException, InvalidFormatException, IOException;
	
	ArrayList<VendorListResponseBean> createVendorList(String balanceSheetFileName) throws FileNotFoundException, IOException;
}
