package ind.automation.salesexpense.serviceimplementations;

import org.springframework.web.multipart.MultipartFile;
import ind.automation.salesexpense.service.CsvToExcelService;

public class CsvToExcelServiceImplementation implements CsvToExcelService {

	public static final char FILE_DELIMITER = ',';
	public static final String FILE_EXTN = ".xlsx";
	public static final String FILE_NAME = "EXCEL_DATA";

	@Override
	public void convertCsvToXls(MultipartFile file) {
		// TODO Auto-generated method stub

	}

}
