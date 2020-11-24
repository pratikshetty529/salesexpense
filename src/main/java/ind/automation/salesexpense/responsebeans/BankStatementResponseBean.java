package ind.automation.salesexpense.responsebeans;

import java.util.ArrayList;

public class BankStatementResponseBean {
	private Double openingBalance;

	private Double closingBalance;

	private ArrayList<BankStatementFinalBean> cleanedBankStatement;

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

	public ArrayList<BankStatementFinalBean> getCleanedBankStatement() {
		return cleanedBankStatement;
	}

	public void setCleanedBankStatement(ArrayList<BankStatementFinalBean> cleanedBankStatement) {
		this.cleanedBankStatement = cleanedBankStatement;
	}
}
