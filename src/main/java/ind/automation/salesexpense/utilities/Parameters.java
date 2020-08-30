package ind.automation.salesexpense.utilities;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

@Component
public class Parameters {
	static HashMap<String, Object> param = new HashMap<String, Object>();
	static InputStream inputStream;

	public Parameters() {
		try {
			inputStream = this.getClass().getClassLoader().getResourceAsStream("parameters.yaml");
			param = new Yaml().load(inputStream);
		} catch (Exception e1) {
			e1.getMessage();
		}
	}
	
	public String getAtmWithdrawal() {
		String atmWord;
		atmWord = ((String) param.get("atm.withdrawal"));
		return atmWord;
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getIgnoredWords() {
		List<String> ignoredWords;
		ignoredWords = ((List<String>) param.get("ignored.words"));
		return ignoredWords;
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getAccountList() {
		List<String> accountList;
		accountList = ((List<String>) param.get("account.list"));
		return accountList;
	}
}
