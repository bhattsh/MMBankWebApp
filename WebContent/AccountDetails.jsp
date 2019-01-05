<%@ taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>

<form action ="updateAccount.mm">
	<table>
		<tr>
			<th>Account Number</th>
			<th><a href="sortByName.mm">Holder Name</a></th>
			<th>Account Balance</th>
			<th>Salary</th>
			<th>Over Draft Limit</th>
			<th>Type Of Account</th>
		</tr>
		<jstl:if test="${requestScope.account!=null}">
		
			<tr>
				<td><input type="text" name="accountNumber" value ="${requestScope.account.bankAccount.accountNumber}" readonly></td>
				<td><input type="text" name="newName" value ="${requestScope.account.bankAccount.accountHolderName}"></td>
				<td><input type="text" name="accountBalance" value ="${requestScope.account.bankAccount.accountBalance}" readonly></td>
				<td>
					<select name="newSalaried">
						<option selected>${requestScope.account.salary==true?"Yes":"No"}</option>		<!-- value in database will be seen selected -->
						<option>${requestScope.account.salary==true?"No":"Yes"}</option>				<!--  opposite to value in database will be shown as a option -->
					</select>
				</td>
				<td>${"N/A"}</td>
				<td><input type="text" name="accountType" value ="Saving" readonly></td>
			</tr>
				<input type = "submit" value = "update">	
		</jstl:if>
	
	
		
		<jstl:if test="${requestScope.accounts!=null}">
			<jstl:forEach var="account" items="${requestScope.accounts}">
				<tr>
					<td>${account.bankAccount.accountNumber}</td>
					<td>${account.bankAccount.accountHolderName }</td>
					<td>${account.bankAccount.accountBalance}</td>
					<td>${account.salary==true?"Yes":"No"}</td>
					<td>${"N/A"}</td>
					<td><input type="text" name ="typeOfAccount" value ='${"Savings"}' readonly></td>
				</tr>
			</jstl:forEach>
		</jstl:if>
	</table>
	<div>
		<jsp:include page="homeLink.html"></jsp:include>
	</div>
</form>	
</body>
</html>







