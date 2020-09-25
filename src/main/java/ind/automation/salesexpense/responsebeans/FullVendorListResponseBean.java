package ind.automation.salesexpense.responsebeans;

import java.util.ArrayList;

public class FullVendorListResponseBean {
	private ArrayList<VendorListResponseBean> vendorList;
	
	private ArrayList<String> accountList;

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
}
