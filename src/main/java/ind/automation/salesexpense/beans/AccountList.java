package ind.automation.salesexpense.beans;

import java.util.ArrayList;

public class AccountList {
	private ArrayList<String> accountList;

	private ArrayList<String> fullAccountList;
	
	public ArrayList<String> getAccountList() {
		return accountList;
	}

	public void setAccountList(ArrayList<String> accountList) {
		this.accountList = accountList;
	}

	public ArrayList<String> getFullAccountList() {
		return fullAccountList;
	}

	public void setFullAccountList(ArrayList<String> fullAccountList) {
		this.fullAccountList = fullAccountList;
	}
}
