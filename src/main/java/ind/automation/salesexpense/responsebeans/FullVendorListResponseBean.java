package ind.automation.salesexpense.responsebeans;

import java.util.ArrayList;

public class FullVendorListResponseBean {
	private ArrayList<VendorListResponseBean> vendorList;
	
	private ArrayList<String> accountList;
	
	private ArrayList<String> fullAccountList;
	
	private ArrayList<String> errorMessage;

	public ArrayList<VendorListResponseBean> getVendorList() {
		return vendorList;
	}

	public void setVendorList(ArrayList<VendorListResponseBean> vendorList) {
		this.vendorList = vendorList;
	}

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

	public ArrayList<String> getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(ArrayList<String> errorMessage) {
		this.errorMessage = errorMessage;
	}
}
