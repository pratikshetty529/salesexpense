package ind.automation.salesexpense.service;

import org.springframework.web.multipart.MultipartFile;

public interface CsvToExcelService {
	void convertCsvToXls(MultipartFile file);
}
