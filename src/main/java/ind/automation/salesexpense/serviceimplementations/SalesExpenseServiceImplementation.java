package ind.automation.salesexpense.serviceimplementations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ind.automation.salesexpense.beans.Expense;
import ind.automation.salesexpense.beans.Sales;
import ind.automation.salesexpense.beans.SalesExpense;
import ind.automation.salesexpense.responsebeans.BankStatementFinalBean;
import ind.automation.salesexpense.responsebeans.SalesExpenseResponse;
import ind.automation.salesexpense.service.SalesExpenseService;
import ind.automation.salesexpense.utilities.Parameters;

@Component
public class SalesExpenseServiceImplementation implements SalesExpenseService {

	@Autowired
	private Parameters parameters;

	@Override
	public SalesExpenseResponse generateSalesExpense(SalesExpense salesExpense) {
		SalesExpenseResponse response = new SalesExpenseResponse();
		ArrayList<BankStatementFinalBean> cleanedBankStatementList = salesExpense.getBankStatement()
				.getCleanedBankStatement();
		ArrayList<Sales> salesList = new ArrayList<Sales>();
		ArrayList<Expense> expenseList = new ArrayList<Expense>();
		ArrayList<String> salesKeyWordsList = parameters.getSalesKeywords();
		int salesReffNo = salesExpense.getSalesReffNumber();
		int expenseReffNo = salesExpense.getExpenseReffNumber();
		Collections.sort(cleanedBankStatementList, new Comparator<BankStatementFinalBean>() {
			public int compare(BankStatementFinalBean o1, BankStatementFinalBean o2) {
				return o1.getVendor().compareTo(o2.getVendor());
			}
		});
		for (BankStatementFinalBean bean : cleanedBankStatementList) {
			for (int i = 0; i < salesKeyWordsList.size(); i++) {
				if (bean.getDescription().toLowerCase().replaceAll("\\'", "").replace("`", "")
						.indexOf(salesKeyWordsList.get(i).toLowerCase().replaceAll("\\'", "").replace("`", "")) != -1) {
					Sales sales = new Sales();
					sales.setDate(bean.getDate());
					sales.setDescription(bean.getDescription());
					sales.setAmount(Math.abs(bean.getAmount()));
					sales.setPayee(bean.getVendor());
					sales.setAccount(bean.getAccount());
					sales.setReff("DEP" + salesReffNo);
					sales.setBank(salesExpense.getBankName());
					sales.setToBePrinted("False");
					salesList.add(sales);
					salesReffNo += 1;
				} else {
					Expense expense = new Expense();
					expense.setDate(bean.getDate());
					expense.setDescription(bean.getDescription());
					String descSplit[] = bean.getDescription().split(" ");
					if (descSplit[0].equalsIgnoreCase(parameters.getCheckCard())) {
						expense.setReff(String.valueOf(Integer.valueOf(descSplit[1])));
					} else {
						expense.setReff("EXP" + expenseReffNo);
						expenseReffNo += 1;
					}
					expense.setAmount(Math.abs(bean.getAmount()));
					expense.setPayee(bean.getVendor());
					expense.setAccount(bean.getAccount());
					expense.setBank(salesExpense.getBankName());
					expense.setToBePrinted("False");
					expenseList.add(expense);
				}
			}
		}
		response.setSales(salesList);
		response.setExpense(expenseList);
		return response;
	}
}