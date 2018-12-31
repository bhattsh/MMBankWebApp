package com.moneymoneybank.application;

import java.io.IOException;
import java.sql.SQLException;

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
	 
	private  SavingsAccountService savingsAccountService=new SavingsAccountServiceImpl();

	private CurrentAccountService currentAccountService = new CurrentAccountServiceImpl();
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String path = request.getServletPath();
		
		if(path.equals("/closeApp.mm"))
			response.sendRedirect("closeAccountDetails.html");
		
		else if(path.equals("/deleteAccountDetails.mm"))
		{
			int accountNumber = Integer.parseInt(request.getParameter("accountNumber"));
			try {
				savingsAccountService.deleteAccount(accountNumber);
			} catch (ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		else if(path.equals("/create.mm"))
			response.sendRedirect("createNewAccountForm.html");
		
		else if(path.equals("/createSavingAccount.mm"))
		{
			String name =  request.getParameter("accountHolderName");
			String typeOfAccount = request.getParameter("accountType");
			double initialBalance=  Double.parseDouble(request.getParameter("accountBalance"));
			boolean salaried = request.getParameter("salaried").equals("no") ? false : true;
			
			try {
				savingsAccountService.createNewAccount(name, initialBalance, salaried);
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		}
		
		else if(path.equals("/getBalance.mm"))
			response.sendRedirect("getBalanceForm.html");
		
		else if(path.equals("/getBalanceSubmission.mm"))
		{
			int accountNumber = Integer.parseInt(request.getParameter("accountNumber"));
			SavingsAccount account;
				try {
					account = savingsAccountService.getAccountById(accountNumber);
					System.out.println("Balance in your account is: "+account.getBankAccount().getAccountBalance());
				} catch (ClassNotFoundException | SQLException
						| AccountNotFoundException e) {
					e.printStackTrace();
				}
				
		}
		else if(path.equals("/withdrawRequest.mm"))
			response.sendRedirect("withdrawRequestForm.html");
		
		else if(path.equals("/withdraw.mm"))
		{
			int accountNumber = Integer.parseInt(request.getParameter("accountNumber"));
			int amountToWithdraw = Integer.parseInt(request.getParameter("amount"));
			String accountType = request.getParameter("accountType");
			
			SavingsAccount savingsAccount = null;
			CurrentAccount currentAccount = null;
			try {
				if(accountType.equals("SA"))
				{
				savingsAccount = savingsAccountService.getAccountById(accountNumber);
				savingsAccountService.withdraw(savingsAccount, amountToWithdraw);
				}
				else
				{
					currentAccount = currentAccountService.getAccountById(accountNumber);
					currentAccountService .withdraw(currentAccount, amountToWithdraw);
				}
				DBUtil.commit();
				response.sendRedirect("success.html");
				
			} catch (ClassNotFoundException | SQLException
					| AccountNotFoundException e) {
				e.printStackTrace();
			}
			
		}

		else if(path.equals("/depositRequest.mm"))
			response.sendRedirect("depositRequestForm.html");
		
		else if(path.equals("/depositRequestForm.mm"))
		{
			int accountNumber = Integer.parseInt(request.getParameter("accountNumber"));
			int amountToDeposit = Integer.parseInt(request.getParameter("amount"));
			String accountType = request.getParameter("accountType");
			
			SavingsAccount savingsAccount = null;
			CurrentAccount currentAccount = null;
			
			try {
				if(accountType.equals("SA"))
				{
					savingsAccount = savingsAccountService.getAccountById(accountNumber);
					savingsAccountService.deposit(savingsAccount, amountToDeposit);
				}
				else
				{
					currentAccount = currentAccountService.getAccountById(accountNumber);
					currentAccountService.deposit(currentAccount, amountToDeposit);
				}
				DBUtil.commit();
			}
			catch (ClassNotFoundException | SQLException
					| AccountNotFoundException e) {
				e.printStackTrace();
			}
			
		}
//TODO LATER		
		else if(path.equals("/transferMoney.mm"))		
		{
//			int senderAccountNumber = Integer.parseInt(request.getParameter("senderAccountNumber"));
//			int receiverAccountNumber = Integer.parseInt(request.getParameter("receiverAccountNumber"));
//			int amountToTransfer = Integer.parseInt(request.getParameter("amount"));
//			String senderAccountType = request.getParameter("accountType");
			
//			if(senderAccountType.equals("SA"))
//			{
//				SavingsAccount senderSavingsAccount = savingsAccountService.getAccountById(senderAccountNumber);
//				SavingsAccount receiverSavingsAccount = savingsAccountService.getAccountById(receiverAccountNumber);
//				savingsAccountService.fundTransfer(senderSavingsAccount, receiverSavingsAccount, amountToTransfer);
//			}
//			else
//			{
//
//				CurrentAccount senderCurrentAccount = currentAccountService.getAccountById(senderAccountNumber);
//				CurrentAccount receiverCurrentAccount = currentAccountService.getAccountById(receiverAccountNumber);
//				currentAccountService.fundTransfer(senderCurrentAccount, receiverCurrentAccount, amountToTransfer);
//		}
			
		}
		else if(path.equals("/moneyTransferRequest.mm"))
			response.sendRedirect("moneyTransferForm.html");
		System.out.println(path);
		
	}

}
