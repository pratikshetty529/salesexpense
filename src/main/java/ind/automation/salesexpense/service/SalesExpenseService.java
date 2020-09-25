package ind.automation.salesexpense.service;

import ind.automation.salesexpense.beans.SalesExpense;
import ind.automation.salesexpense.responsebeans.SalesExpenseResponse;

public interface SalesExpenseService {
	SalesExpenseResponse generateSalesExpense(SalesExpense salesExpense);
}
