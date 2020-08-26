<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<title>Home page</title>

	<meta name='viewport' content='width=device-width, initial-scale=1'>
	<script src='https://kit.fontawesome.com/a076d05399.js'></script>
	<link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
	<link rel="stylesheet" href="index.css">
	

</head>

<body onload="loadingFunction();">
	<div class="blurDiv">	
	
		<button class="addCustomerButton" onclick="AddCustomerFromInvoiceForm();">Add customer</button>	
		
		<div  class="formDiv"></div>
		
		<div class="errorPage">
			<ul class="ErrorHolder"></ul>
		</div>
	</div>
	
	<div class="bodyDiv">
		<!-- header section -->
		<div class="header">
			<p class="headerTitle">Home page</p>
			<div class="profilePhoto" onclick="accountSettings();"></div>			
			<p class="userName" onclick="accountSettings();"></p>
		</div>
		<div class="accountDiv">
			<i class="material-icons accountCancelButton" onclick="hideAccountSettings();">cancel</i>
			<p class="showUserName">Username : <span id="name" style="color:black;">ijrghpiuwrgpuruh</span></p>
			<button class="logoutButton" style="width: 33%;" onclick="resetPassword();">Reset Password</button>
			<button class="logoutButton" onclick="logout();">LOG OUT</button>
		</div>

		<div class="optionHolder">
			<div onclick="fromOption(0);">Customer Details</div>
			<div onclick="fromOption(1);">Staff Details</div>
			<div onclick="fromOption(2);">Vendor Details</div>
			<div onclick="fromOption(3);">Purchase orders</div>
			<div onclick="fromOption(4);">Sales orders</div>
			<div onclick="fromOption(5);">Product</div>
			<div onclick="fromOption(6);">Invoices(Bill)</div>
			<div onclick="fromOption(7);">Payment</div>
		</div>

		<div class=" modifyTableSection">

			<i class='fas fa-backspace' id="backButton" style="color: black;font-size: 40px;margin: .8% 0px 0px .5%;position: absolute;" onclick="backToOptionHolder();"></i>
			<p class="optionName">Table describes</p>
			<button class="addUpdateDelete" onclick="add();"><i class="material-icons" style="margin: 1% 5% 0px 0px;font-size: 40px;font-weight: 900;"> add_circle_outline</i> ADD</button> 
			<button class="addUpdateDelete" onclick="update();"><i class="material-icons" style="margin: 1% 5% 0px 0px;font-size: 40px;font-weight: 900;">update</i> UPDATE</button>
			<button class="addUpdateDelete" onclick="deleteTable();"><i class="material-icons" style="margin: 1% 5% 0px 0px;font-size: 40px;font-weight: 900;">delete</i> DELETE</button>
		</div>

		<div class="tableTitle"></div>
		<p class="findFieldHolder">
			ID : <input name="findID" placeholder="Enter the ID" class="findIDStyle" oninput="findID();">
		</p>
		<div style="margin: 1% 0px 0px 50%;position: absolute;width:50%;display:none;" id="checkBoxHolder">
			<p onclick="checkBoxFunction();" style="border-radius: 10px;box-shadow : 0px 0px 8px 7px #c9cac9c4;height: 30px;width: 30px;color: black;font-family: 'Ubuntu', sans-serif;font-size: 18px;font-weight: 700;">
				<i class='fas fa-check ' id="tickSymbol" style=" color: white;margin: 2px 0px 0px 2px;font-size: 25px;"></i></p>
				<p style="color:#0199f5	;position: absolute;font-family: 'Ubuntu', sans-serif;font-size: 25px;font-weight: 700;margin: -5% 0px 0px 6%;" id="checkBoxLabel">
			</p>			
		</div>
		<div class="workSheet">
		</div>
	</div>
</body>
<script type="text/javascript" src="index.js"></script>
</html>