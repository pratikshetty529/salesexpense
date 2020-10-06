package ind.automation.salesexpense.responsebeans;

import java.util.ArrayList;

public class VendorListResponseBean {
	private String name;
	
	private ArrayList<String> account;
	
	private ArrayList<Integer> count;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<String> getAccount() {
		return account;
	}

	public void setAccount(ArrayList<String> account) {
		this.account = account;
	}

	public ArrayList<Integer> getCount() {
		return count;
	}

	public void setCount(ArrayList<Integer> count) {
		this.count = count;
	}
}
