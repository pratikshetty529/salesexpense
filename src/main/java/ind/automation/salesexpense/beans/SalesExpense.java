package ind.automation.salesexpense.beans;

import ind.automation.salesexpense.responsebeans.BankStatementResponseBean;

public class SalesExpense {
	private String bankName;
	
	private Integer salesReffNumber;
	
	private Integer expenseReffNumber;
	
	private BankStatementResponseBean bankStatement;

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public Integer getSalesReffNumber() {
		return salesReffNumber;
	}

	public void setSalesReffNumber(Integer salesReffNumber) {
		this.salesReffNumber = salesReffNumber;
	}

	public Integer getExpenseReffNumber() {
		return expenseReffNumber;
	}

	public void setExpenseReffNumber(Integer expenseReffNumber) {
		this.expenseReffNumber = expenseReffNumber;
	}

	public BankStatementResponseBean getBankStatement() {
		return bankStatement;
	}

	public void setBankStatement(BankStatementResponseBean bankStatement) {
		this.bankStatement = bankStatement;
	}
}
