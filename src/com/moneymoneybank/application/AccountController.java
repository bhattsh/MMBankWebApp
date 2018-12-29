package com.moneymoneybank.application;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.moneymoney.account.service.SavingsAccountService;
import com.moneymoney.account.service.SavingsAccountServiceImpl;

/**
 * Servlet implementation class AccountController
 */
@WebServlet("*.mm")
public class AccountController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	 
	private  SavingsAccountService savingsAccountService=new SavingsAccountServiceImpl();
	
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
			
		System.out.println(path);
		
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
