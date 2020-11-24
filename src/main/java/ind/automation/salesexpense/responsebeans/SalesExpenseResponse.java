package ind.automation.salesexpense.responsebeans;

import java.util.ArrayList;
import ind.automation.salesexpense.beans.Expense;
import ind.automation.salesexpense.beans.Sales;

public class SalesExpenseResponse {
	
	private ArrayList<Sales> sales;
	
	private ArrayList<Expense> expense;

	public ArrayList<Sales> getSales() {
		return sales;
	}

	public void setSales(ArrayList<Sales> sales) {
		this.sales = sales;
	}

	public ArrayList<Expense> getExpense() {
		return expense;
	}

	public void setExpense(ArrayList<Expense> expense) {
		this.expense = expense;
	}
}
