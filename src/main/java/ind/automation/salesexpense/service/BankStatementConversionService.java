package ind.automation.salesexpense.service;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;
import ind.automation.salesexpense.responsebeans.BankStatementResponseListResponse;

public interface BankStatementConversionService {
	BankStatementResponseListResponse convertBankStatement(MultipartFile file) throws IOException;
}
