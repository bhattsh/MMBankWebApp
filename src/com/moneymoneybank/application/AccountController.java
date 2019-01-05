package com.moneymoneybank.application;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.moneymoney.account.CurrentAccount;
import com.moneymoney.account.SavingsAccount;
import com.moneymoney.account.service.CurrentAccountService;
import com.moneymoney.account.service.CurrentAccountServiceImpl;
import com.moneymoney.account.service.SavingsAccountService;
import com.moneymoney.account.service.SavingsAccountServiceImpl;
import com.moneymoney.account.util.DBUtil;
import com.moneymoney.exception.AccountNotFoundException;

/**
 * Servlet implementation class AccountController
 */
@WebServlet("*.mm")
public class AccountController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private SavingsAccountService savingsAccountService = new SavingsAccountServiceImpl();

	private CurrentAccountService currentAccountService = new CurrentAccountServiceImpl();

	private RequestDispatcher dispatcher;
	// To ask
	private boolean toSortIn = false;

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String path = request.getServletPath();
		
		int accountNumber=0;
		String accountType="";
		SavingsAccount savingsAccount = null;
		CurrentAccount currentAccount = null;
		
		switch(path)
		{
		case "/closeApp.mm":
			response.sendRedirect("closeAccountDetails.html");
		break;

		case "/deleteAccountDetails.mm":
			 accountNumber = Integer.parseInt(request
					.getParameter("accountNumber"));
			try {
				savingsAccountService.deleteAccount(accountNumber);
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		break;
		case "/create.mm":
			response.sendRedirect("createNewAccountForm.html");
			break;

		case "/createSavingAccount.mm":
			String name = request.getParameter("accountHolderName");
			String typeOfAccount = request.getParameter("accountType");
			double initialBalance = Double.parseDouble(request
					.getParameter("accountBalance"));
			boolean salaried = request.getParameter("salaried").equals("no") ? false
					: true;

			try {
				savingsAccountService.createNewAccount(name, initialBalance,
						salaried);
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		break;

		case "/getBalance.mm":
			response.sendRedirect("getBalanceForm.html");
			break;

		case "/getBalanceSubmission.mm":
			 accountNumber = Integer.parseInt(request
					.getParameter("accountNumber"));
			SavingsAccount account;
			try {
				account = savingsAccountService.getAccountById(accountNumber);
				System.out.println("Balance in your account is: "
						+ account.getBankAccount().getAccountBalance());
			} catch (ClassNotFoundException | SQLException
					| AccountNotFoundException e) {
				e.printStackTrace();
			}
			break;
			
		case "/withdrawRequest.mm":
			response.sendRedirect("withdrawRequestForm.html");
			break;

		case "/withdraw.mm":
			 accountNumber = Integer.parseInt(request
					.getParameter("accountNumber"));
			int amountToWithdraw = Integer.parseInt(request
					.getParameter("amount"));
			accountType = request.getParameter("accountType");

			try {
				if (accountType.equals("SA")) {
					savingsAccount = savingsAccountService
							.getAccountById(accountNumber);
					savingsAccountService.withdraw(savingsAccount,
							amountToWithdraw);
				} else {
					currentAccount = currentAccountService
							.getAccountById(accountNumber);
					currentAccountService.withdraw(currentAccount,
							amountToWithdraw);
				}
				DBUtil.commit();
				response.sendRedirect("success.html");

			} catch (ClassNotFoundException | SQLException
					| AccountNotFoundException e) {
				e.printStackTrace();
			}
			break;
		
		case "/depositRequest.mm":
			response.sendRedirect("depositRequestForm.html");
			break;

		case "/depositRequestForm.mm":
			accountNumber = Integer.parseInt(request
					.getParameter("accountNumber"));
			int amountToDeposit = Integer.parseInt(request
					.getParameter("amount"));
			accountType = request.getParameter("accountType");

			try {
				if (accountType.equals("SA")) {
					savingsAccount = savingsAccountService
							.getAccountById(accountNumber);
					savingsAccountService.deposit(savingsAccount,
							amountToDeposit);
				} else {
					currentAccount = currentAccountService
							.getAccountById(accountNumber);
					currentAccountService.deposit(currentAccount,
							amountToDeposit);
				}
				DBUtil.commit();
			} catch (ClassNotFoundException | SQLException
					| AccountNotFoundException e) {
				e.printStackTrace();
			}
			break;
		
		case "/transferMoney.mm":
			// int senderAccountNumber =
			// Integer.parseInt(request.getParameter("senderAccountNumber"));
			// int receiverAccountNumber =
			// Integer.parseInt(request.getParameter("receiverAccountNumber"));
			// int amountToTransfer =
			// Integer.parseInt(request.getParameter("amount"));
			// String senderAccountType = request.getParameter("accountType");

			// if(senderAccountType.equals("SA"))
			// {
			// SavingsAccount senderSavingsAccount =
			// savingsAccountService.getAccountById(senderAccountNumber);
			// SavingsAccount receiverSavingsAccount =
			// savingsAccountService.getAccountById(receiverAccountNumber);
			// savingsAccountService.fundTransfer(senderSavingsAccount,
			// receiverSavingsAccount, amountToTransfer);
			// }
			// else
			// {
			//
			// CurrentAccount senderCurrentAccount =
			// currentAccountService.getAccountById(senderAccountNumber);
			// CurrentAccount receiverCurrentAccount =
			// currentAccountService.getAccountById(receiverAccountNumber);
			// currentAccountService.fundTransfer(senderCurrentAccount,
			// receiverCurrentAccount, amountToTransfer);
			// }
			break;
			
		case "/moneyTransferRequest.mm":
			response.sendRedirect("moneyTransferForm.html");
			break;

		case "/getAll.mm":
			try {
				List<SavingsAccount> accounts = savingsAccountService
						.getAllSavingsAccount();
				request.setAttribute("accounts", accounts);
				dispatcher = request.getRequestDispatcher("AccountDetails.jsp");
				dispatcher.forward(request, response);
			} catch (ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
		case "/sortByName.mm":
			try {
				ArrayList<SavingsAccount> accounts = (ArrayList<SavingsAccount>) savingsAccountService
						.getAllSavingsAccount();
				toSortIn = !toSortIn;
				int sort = toSortIn == false ? 1 : -1;
				Collections.sort(accounts, new Comparator<SavingsAccount>() {
					@Override
					public int compare(SavingsAccount accountOne,
							SavingsAccount accountTwo) {
						return sort
								* (accountOne.getBankAccount()
										.getAccountHolderName()
										.compareToIgnoreCase(accountTwo
												.getBankAccount()
												.getAccountHolderName()));
					}
				});
				System.out.println(toSortIn);
				request.setAttribute("accounts", accounts);
				dispatcher = request.getRequestDispatcher("AccountDetails.jsp");
				dispatcher.forward(request, response);
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
			break;
			
		case "/updateRequest.mm":
			response.sendRedirect("getAccountForm.jsp");
			break;

		case "/getAccount.mm":
			 accountNumber = Integer.parseInt(request
					.getParameter("accountNumber"));			 
			try {
				try {
					 account = savingsAccountService
							.getAccountById(accountNumber);
					request.setAttribute("account", account);
					dispatcher = request
							.getRequestDispatcher("AccountDetails.jsp");
					dispatcher.forward(request, response);
				} catch (AccountNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		break;

		case"/updateAccount.mm":
			 accountNumber = Integer.parseInt(request
					.getParameter("accountNumber"));
			String newAccountHolderName = request.getParameter("newName");
			double accountBalance = Double.parseDouble(request
					.getParameter("accountBalance"));
			boolean newSalaried = request.getParameter("newSalaried").equals(
					"yes") ? true : false;
			 typeOfAccount = request.getParameter("accountType");
			Object odLimit = request.getParameter("typeOfAccount");
			try {

				if (typeOfAccount.equals("Saving")) {
					SavingsAccount savingAccountToUpdate = new SavingsAccount(
							accountNumber, newAccountHolderName,
							accountBalance, newSalaried);
					savingAccountToUpdate = savingsAccountService
							.updateAccount(savingAccountToUpdate);
					request.setAttribute("account", savingAccountToUpdate);
					dispatcher = request
							.getRequestDispatcher("AccountDetails.jsp");
					dispatcher.forward(request, response);
				} else {
					currentAccount = currentAccountService
							.getAccountById(accountNumber);
				}

			} catch (ClassNotFoundException | SQLException
					| AccountNotFoundException e) {
				e.printStackTrace();
			}
		break;
		
		default:
			System.out.println("something wents wrong");
		System.out.println(path);
		}
	}
}
