package ind.automation.salesexpense.beans;

public class Expense {
	private String date;
	
	private String description;
	
	private Double amount;
	
	private String payee;
	
	private String account;
	
	private String reff;
	
	private String bank;
	
	private String toBePrinted;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getPayee() {
		return payee;
	}

	public void setPayee(String payee) {
		this.payee = payee;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getReff() {
		return reff;
	}

	public void setReff(String reff) {
		this.reff = reff;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getToBePrinted() {
		return toBePrinted;
	}

	public void setToBePrinted(String toBePrinted) {
		this.toBePrinted = toBePrinted;
	}
}
