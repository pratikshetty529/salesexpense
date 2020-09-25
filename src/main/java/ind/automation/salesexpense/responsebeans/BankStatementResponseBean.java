package ind.automation.salesexpense.responsebeans;

import java.util.ArrayList;
import ind.automation.salesexpense.beans.BankStatementBean;

public class BankStatementResponseBean {
	private Double openingBalance;

	private Double closingBalance;

	private ArrayList<BankStatementBean> cleanedBankStatement;

	public Double getOpeningBalance() {
		return openingBalance;
	}

	public void setOpeningBalance(Double openingBalance) {
		this.openingBalance = openingBalance;
	}

	public Double getClosingBalance() {
		return closingBalance;
	}

	public void setClosingBalance(Double closingBalance) {
		this.closingBalance = closingBalance;
	}

	public ArrayList<BankStatementBean> getCleanedBankStatement() {
		return cleanedBankStatement;
	}

	public void setCleanedBankStatement(ArrayList<BankStatementBean> cleanedBankStatement) {
		this.cleanedBankStatement = cleanedBankStatement;
	}
}
