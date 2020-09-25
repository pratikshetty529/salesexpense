package ind.automation.salesexpense.responsebeans;

import java.util.ArrayList;

public class BankStatementResponseListResponse {
	private ArrayList<BankStatementConversionResponseBean> bankStatementList;

	public ArrayList<BankStatementConversionResponseBean> getBankStatementList() {
		return bankStatementList;
	}

	public void setBankStatementList(ArrayList<BankStatementConversionResponseBean> bankStatementList) {
		this.bankStatementList = bankStatementList;
	}
	
}
