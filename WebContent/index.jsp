<%@page import="redis.clients.jedis.Jedis"%>
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
<% Jedis jedis = new Jedis("localhost");%>
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
			<p class="showUserName">Username : <span id="name" style="color:black;"></span></p>
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
<script type="text/javascript">
	var loginUserName;
	var whichUser;	
	var job_name;
	var optionHolder = document.getElementsByClassName('optionHolder')[0];
	var optionName = document.getElementsByClassName('optionName')[0];
	var modifyTableSection = document.getElementsByClassName('modifyTableSection')[0];
	var tableTitle = document.getElementsByClassName('tableTitle')[0];
	var blurDiv = document.getElementsByClassName("blurDiv")[0];
	var whichTable;
	var tempWhichTable = undefined;
	var workSheet = document.getElementsByClassName("workSheet")[0];
	var formDiv = document.getElementsByClassName("formDiv")[0];
	var errorPage = document.getElementsByClassName("errorPage")[0];
	var tableNames = ["customer","staff","vendor","purchase","sales","product","invoices","payment"];
	var jobNames = [];
	var customerID_product = [];
	var jsonTableData;
	
	//this method used to get data when the index.jsp file re-loaded or loaded
	function loadingFunction(){
		if(whichUser == "employee"){
			document.getElementsByClassName("optionHolder")[0].children[1].style.display="none";
			modifyTableSection.style.display = "none";
		}
		
		//this part get jobname from db
		var xhr = new XMLHttpRequest();
		xhr.open("GET", "http://192.168.43.126:8080/supermarketapp/onload_data_action.action", true);
		xhr.onreadystatechange = function() {
			if (this.readyState == 4 && this.status == 200) {
				
				var jsonArr = this.responseText;
				jobNames = JSON.parse(jsonArr);
			}
		};
		xhr.send();
		
		// this part get customerId and product name from db
		xhr = new XMLHttpRequest();
		xhr.open("GET", "http://192.168.43.126:8080/supermarketapp/onload_customerid_product_action.action", true);
		xhr.onreadystatechange = function() {
			if (this.readyState == 4 && this.status == 200) {
				
				var jsonArr = this.responseText;
				customerID_product = JSON.parse(jsonArr);
			}
		};
		xhr.send();
		
		//this part get login details from db
		<% String userName = jedis.get("userName"); 
		   String categary = jedis.get("categary"); 
		   String jobName = jedis.get("jobName");
		   String entryDetail = jedis.get("entryDetail"); %>
		xhr = new XMLHttpRequest();
		xhr.open("GET", "http://192.168.43.126:8080/supermarketapp/onload_login_details.action", true);
		xhr.onreadystatechange = function() {
			if (this.readyState == 4 && this.status == 200) {
				
				var jsonArr = this.responseText;
				jsonArr = JSON.parse(jsonArr);
				var entryDetail = "<%=entryDetail %>";
				if(entryDetail == "LOGOUT"){
					window.location="http://192.168.43.126:8080/supermarketapp/login.jsp";
				}
				whichUser = "<%=categary%>";
				job_name = "<%=jobName%>";
				loginUserName = "<%=userName%>";
				document.getElementsByClassName("userName")[0].innerText = loginUserName;
			}
		};
		xhr.send();
		modifyTableSection.style.display = "none";
	}
	
	//this method used to hide the option(add,update,delete) div
	function backToOptionHolder(){
		modifyTableSection.style.display = "none";
	}
	
	//this method used to get data when user enter anyone's id in the search box.
	function findID() {
		var jsonArr;
		var id = document.getElementsByClassName("findIDStyle")[0].value;
		var xhr = new XMLHttpRequest();
		xhr.open("POST", "http://192.168.43.126:8080/supermarketapp/find_"+tableNames[whichTable]+".action?ID="+id, true);
		xhr.onreadystatechange = function() {
			if (this.readyState == 4 && this.status == 200) {			
				var jsonArr = this.responseText;
				jsonArr = JSON.parse(jsonArr);
				
				
				if(jsonArr[0] == "success"){
					generateTable(jsonArr[1]);
				}else{
					workSheet.innerHTML = "";
					if(jsonArr[0] == "empty"){
						fromOption(whichTable);
					}else{
						var showErr = document.createElement("p");
						showErr.classList.add("ErrorOnWorkSheet");
						workSheet.appendChild(showErr);
						
						document.getElementsByClassName("ErrorOnWorkSheet")[0].style.display = "block";
						document.getElementsByClassName("ErrorOnWorkSheet")[0].innerText = jsonArr[0];
					}
				}
			}
		};
		xhr.send();
	}
	
	//this method used to get deleted table records when check box clicked.
	var click = true;
	function checkBoxFunction() {	
		if(click){
			document.getElementsByClassName("findFieldHolder")[0].style.display = "none";
			document.getElementById("tickSymbol").style.color = "red";
			var xhr = new XMLHttpRequest();
			xhr.open("GET", "http://192.168.43.126:8080/supermarketapp/deleted_table_record.action?tableName="+tableNames[whichTable], true);
			xhr.onreadystatechange = function() {
				if (this.readyState == 4 && this.status == 200) {				
					var jsonObject = this.responseText;
					jsonObject = JSON.parse(jsonObject);
					if(jsonObject.length < 1){
						workSheet.innerHTML = "";
						var showErr = document.createElement("p");
						showErr.classList.add("ErrorOnWorkSheet");
						workSheet.appendChild(showErr);
						
						document.getElementsByClassName("ErrorOnWorkSheet")[0].style.display = "block";
						document.getElementsByClassName("ErrorOnWorkSheet")[0].innerText = "Deleted Record is Empty ";
					}else{
						generateTable(jsonObject);
					}		
				}
			};
			xhr.send();
			click = false;
		}else{
			document.getElementsByClassName("findFieldHolder")[0].style.display = "block";
			document.getElementById("tickSymbol").style.color = "white";
			fromOption(whichTable);
			click = true;
			
		}	
	}
	
	//this method used to hide the profile settings div.
	function hideAccountSettings() {
		document.getElementsByClassName("accountDiv")[0].style.display = "none";
	}
	
	//this method used to show the profile settings div.
	function accountSettings() {
		document.getElementsByClassName("accountDiv")[0].style.display = "block";
		document.getElementsByClassName("logoutButton")[0].style.visibility= (whichUser == "admin") ?  "visible" : "hidden" ;
		document.getElementById("name").innerText = loginUserName;
	}
	
	//this method used to reset the admin password when resetpassword button is clicked.
	function resetPassword() {
		formDiv.innerHTML = "";
		hideAccountSettings();
		blurDiv.style.display = "block";
		formDiv.style.display = "block";
		errorPage.style.display = "none";
		var nameArr = ["oldPassword","newPassword","confirmPassword"];
		var placeHolderArr = ["Enter the old passWord","Enter the new Password","Re-enter the new Password"];
		for(let i=0;i < nameArr.length;i += 1){
			let field = document.createElement("input");
			field.classList.add("inputStyle");		
			formDiv.appendChild(field);
			document.getElementsByClassName("inputStyle")[i].name = nameArr[i];
			document.getElementsByClassName("inputStyle")[i].type = "password";
			document.getElementsByClassName("inputStyle")[i].placeholder = placeHolderArr[i];
		}
		
		let addButton = document.createElement("button");
		addButton.classList.add("submitButton");
		formDiv.appendChild(addButton);
		document.getElementsByClassName("submitButton")[0].innerText = "CHANGE";
		document.getElementsByClassName("submitButton")[0].onclick = function changePassword(){
			
			var dict={};
			for(let i = 0;i < nameArr.length;i += 1){
				dict[nameArr[i]] = document.getElementsByClassName("inputStyle")[i].value;
			}
			
			var xhr = new XMLHttpRequest();		
			xhr.onreadystatechange = function() {
				if (this.readyState == 4 && this.status == 200) {
					
					var response = this.responseText;
					var jsonObject = JSON.parse(response);
					if(jsonObject[0] == "success"){
						alert("Password Changed successfull !!!");
						blurDiv.style.display = "none";
						errorPage.style.display = "none";
					}else{
						errorPage.style.display = "block";
						printFieldError(jsonObject);
					}				
				}
			};
			let jsonstring = JSON.stringify(dict);
			let sendstr = "formData=" + encodeURIComponent(jsonstring);
			xhr.open("POST", "http://192.168.43.126:8080/supermarketapp/reset_password.action?"+sendstr, true);
			xhr.send(sendstr);
		};
		let backButton = document.createElement("button");
		backButton.classList.add("submitButton");
		formDiv.appendChild(backButton);
		document.getElementsByClassName("submitButton")[1].innerText = "BACK";
		document.getElementsByClassName("submitButton")[1].onclick = function backToAccount(){
			blurDiv.style.display = "none";
			errorPage.style.display = "none";
		}
	}
	
	//this method used to log out from index.jsp page when logout button clicked.
	function logout() {
		var xhr = new XMLHttpRequest();
		xhr.open("GET", "http://192.168.43.126:8080/supermarketapp/logout.action", true);
		xhr.onreadystatechange = function() {
			if (this.readyState == 4 && this.status == 200) {
				window.location="http://192.168.43.126:8080/supermarketapp/index.jsp";
			}
		};
		xhr.send();
	}
	
	//this method used to get show all data from a table when user click the side moulted button(customer deteils,staff,...).
	function fromOption(whichChild){
		workSheet.innerHTML = "";
		
		document.getElementsByClassName("findFieldHolder")[0].style.display = "block";
		document.getElementsByClassName("findIDStyle")[0].placeholder = "Enter "+tableNames[whichChild]+"ID";
		
		whichTable = whichChild;
		let buttonName = optionHolder.children[whichChild].innerText;
		modifyTableSection.style.display = "block";
		modifyTableSection.style.marginTop = (optionHolder.children[whichChild].offsetTop-98)+"px";
	
		optionName.innerText = buttonName;
		tableTitle.innerText = buttonName.split(" ")[0] +" Table";
		
		document.getElementById("tickSymbol").style.color = "white";
		if(whichUser == "employee"){
			modifyTableSection.style.display = "none";
			if(job_name == "bill counter" && (whichTable == 6 || whichTable == 7)){
				modifyTableSection.style.display = "block";
			}
		}
		//this function send http request and return specfied table name's data.
		
		getTableData();	
		
		if(whichTable == 0 || whichTable == 1 || whichTable == 2){
			document.getElementById("checkBoxHolder").style.display = "block";
			document.getElementById("checkBoxLabel").innerText = "Show Deleted "+tableNames[whichTable]+"";
		}else{
			document.getElementById("checkBoxHolder").style.display = "none";
		}
		
		if(whichChild == 4 || whichChild == 5){
			modifyTableSection.style.display = "none";
		}
		if(whichTable == 6 || whichTable == 3 || whichTable == 7){
			document.getElementsByClassName("addUpdateDelete")[2].style.visibility = "hidden";
			document.getElementsByClassName("addUpdateDelete")[0].style.visibility = "visible";
			if(whichTable == 7){
				document.getElementsByClassName("addUpdateDelete")[0].style.visibility = "hidden";
			}
		}else{
			document.getElementsByClassName("addUpdateDelete")[2].style.visibility = "visible";
			document.getElementsByClassName("addUpdateDelete")[0].style.visibility = "visible";
		}
	}
	
	//this method used to create select tag to hold job name.
	function getJobNames() {
		var job_name;
		let selectHolder = document.createElement("div");
		selectHolder.classList.add("selectStyle");
		formDiv.appendChild(selectHolder);
			var label = document.createElement("p");
			label.classList.add("label");
			formDiv.appendChild(label);
			document.getElementsByClassName("label")[0].innerText = "Job Division : ";
			
			var IDHolder = document.createElement("SELECT");
			IDHolder.setAttribute("id","mySelect");
			formDiv.appendChild(IDHolder);
			
			
			document.getElementById("mySelect").onchange = function putInputval(){
				job_name = document.getElementById("mySelect").value;
			}
			job_name = document.getElementById("mySelect").value;
			
			for(let i = 0;i < jobNames.length;i += 1){
				var data = jsonTableData[i];
				var option = document.createElement("option");
				option.setAttribute("value", (i+1));
						
			    var item = document.createTextNode(jobNames[i][i+1]);
			    option.appendChild(item);
			    document.getElementById("mySelect").appendChild(option);
			}
	}
	
	//this method used to hide button(add customer) except invoices.
	function AddCustomerFromInvoiceForm() {
		document.getElementsByClassName("addCustomerButton")[0].style.display = "none";
		tempWhichTable = whichTable;
		whichTable = 0;
		add();
	}
	
	//this method used to add a data to given table.
	function add(){
		errorPage.style.display = "none";
		var idName = ["customerID","employeeID","vendorID","purchaseID","","","billID"];
		var fieldNameForAddData = [["customerName","phoneNumber","EmailID"],
								   ["employeeName","phoneNumber","salary"],
								   ["vendorName","vendorProduct","phoneNumber","vendorEmailID"],
								   ["vendorID","productName","netAmount","quantity"],[],[],
								   ["quantity","discount"]];
		
		var fieldPlaceHolder = [["Enter customer Name","Enter customer's phoneNumber","Enter EmailID"],
								["Enter Employee Name","Enter Employee's phoneNumber","Enter Employee's Salary"],
								["Enter the vendor name","Enter the product","Enter vendor's phoneNumber","Enter vendor's Email id"],
								["Enter vendorID","Enter product name","Enter net amount per product","Enter quantity like eg:100 kg(or)litre(or)pieces"],[],[],
								["Enter the quantity like 1 kg or litre or pieces ","Enter the discount"]];
		var jsonObject;
		
		formDiv.innerHTML = "";
		blurDiv.style.display = "block";
		
		if(whichTable == 1){
			getJobNames();
		}
		
		if(whichTable == 6){
			document.getElementsByClassName("addCustomerButton")[0].style.display = "block"; 
			for(let x = 0;x < 2;x += 1){
				let selectHolder = document.createElement("div");
				selectHolder.classList.add("selectStyle");
				formDiv.appendChild(selectHolder);
					var label = document.createElement("p");
					label.classList.add("label");
					formDiv.appendChild(label);
					document.getElementsByClassName("label")[x].innerText = (x == 0) ? "CustomerID : " : "Product : ";
					document.getElementsByClassName("label")[x].style.marginTop += x+"%";
					
					var IDHolder = document.createElement("SELECT");
					IDHolder.setAttribute("id","mySelect"+x+"");
					formDiv.appendChild(IDHolder);
					document.getElementById("mySelect"+x+"").style.marginTop +=  x+"%";
					
					document.getElementById("mySelect"+x+"").onchange = function putInputval(){
						job_name = document.getElementById("mySelect"+x).value;
					}
					job_name = document.getElementById("mySelect"+x+"").value;
					
					var len;
					if(x == 0){
						len = customerID_product[x].length
					}else{
						len = Object.keys(customerID_product[1][0]).length
					}
					for(let i = 0;i < len;i += 1){
						var arr = customerID_product[x];
						var data = jsonTableData[i];
						var option = document.createElement("option");
						option.setAttribute("value", (x == 0) ? arr[i] : ""+(101+i)+"");
								
					    var item = document.createTextNode((x == 0) ? arr[i] : arr[0][""+(101+i)+""]);
					    option.appendChild(item);
					    document.getElementById("mySelect"+x+"").appendChild(option);
					}
			}
			
		}
		createInputField(fieldNameForAddData,fieldPlaceHolder);
		
			
		let addButton = document.createElement("button");
		addButton.classList.add("submitButton");
		formDiv.appendChild(addButton);
		document.getElementsByClassName("submitButton")[0].innerText = "ADD";
		document.getElementsByClassName("submitButton")[0].onclick = function addData() {
			errorPage.style.display = "block";
			
			var dict = {};
			var inputName = fieldNameForAddData[whichTable];
			if(whichTable == 1){
				dict["jobID"] = document.getElementById("mySelect").value;
			}
			if(whichTable == 6){
				dict["customerID"] = document.getElementById("mySelect0").value;
				dict["productID"] = document.getElementById("mySelect1").value;
			}
			for(let i = 0;i < inputName.length;i += 1){
				dict[inputName[i]] = document.getElementsByClassName("inputStyle")[i].value;
			}
			var xhr = new XMLHttpRequest();		
			xhr.onreadystatechange = function() {
				if (this.readyState == 4 && this.status == 200) {
					
					var response = this.responseText;
					jsonObject = JSON.parse(response);
					if(jsonObject[0] == "success"){
						document.getElementsByClassName("addCustomerButton")[0].style.display = "none";
						errorPage.style.display = "none";
						blurDiv.style.display = "none";
						alert("Data Added Successfully !!!");
						tempWhichTable = undefined;
						fromOption(whichTable);
						
					}else{
						errorPage.style.display = "block";
						printFieldError(jsonObject);
					}				
				}
			};
			let jsonstring = JSON.stringify(dict);
			let sendstr = "formData=" + encodeURIComponent(jsonstring);
			xhr.open("POST", "http://192.168.43.126:8080/supermarketapp/add_"+tableNames[whichTable]+".action?"+sendstr, true);
			xhr.send(sendstr);
		}
		
		createCancelButton()
	}
	
	//this method used to create another select tag to hold job name or vendorID.
	function createSelect2() {
		var selectLabel = ["","Job Division","","Vendor ID","","",""]
		var selectVal;
		let selectHolder = document.createElement("div");
		selectHolder.classList.add("selectStyle");
		formDiv.appendChild(selectHolder);
		
			var label = document.createElement("p");
			label.classList.add("label");
			formDiv.appendChild(label);
			document.getElementsByClassName("label")[1].innerText = selectLabel[whichTable]+" : ";
			document.getElementsByClassName("label")[1].style.marginTop=  "2%";
			
			
			var IDHolder = document.createElement("SELECT");
			IDHolder.setAttribute("id","mySelect1");
			formDiv.appendChild(IDHolder);
			document.getElementById("mySelect1").style.marginTop=  "2%";
			
			document.getElementById("mySelect1").onchange = function putInputval(){
				selectVal = document.getElementById("mySelect1").value;
			}
			selectVal = document.getElementById("mySelect1").value;
			
			for(let i = 0;i < jobNames.length;i += 1){
				var data = jsonTableData[i];
				var option = document.createElement("option");
				option.setAttribute("value", (i+1));
						
			    var item = document.createTextNode(jobNames[i][i+1]);
			    option.appendChild(item);
			    document.getElementById("mySelect1").appendChild(option);
			}
	}
	
	//this method used to update data to given table.
	function update(){	
		errorPage.style.display = "none";
		var idName = ["customerID","employeeID","vendorID","purchaseID","","","billID","paymentID"];
		blurDiv.style.display = "block";
		formDiv.innerHTML = "";
		var fieldNameForUpdate = [["customerName","phoneNumber","EmailID"],
								  ["employeeName","salary","phoneNumber"],
								  ["vendorName","phoneNumber","vendorEmailID"],
								  ["netAmount","quantity"],[],[],
								  ["quantity","discount"],
								  ["paymentDetail"]];
		
		var placeholderForUpdate = [["Enter the customer Name","Enter customers phoneNumber","Enter mail id"],
									["Enter the employee Name","Enter salary you want to change","Enter the customer's phoneNumber"],
									["Enter the vendor name","Enter vendor's phoneNumber","Enter vendor's Email id"],
									["Enter net payment for the product","Enter quantity like eg:100 kg(or)litre(or)pieces"],[],[],
									["Enter the quantity like 1 kg or litre or pieces","Enter discount value"],
									["Enter amount paid or not paid"]];
		
		let selectHolder = document.createElement("div");
		selectHolder.classList.add("selectStyle");
		formDiv.appendChild(selectHolder);
			var label = document.createElement("p");
			label.classList.add("label");
			formDiv.appendChild(label);
			document.getElementsByClassName("label")[0].innerText = idName[whichTable]+" : ";
			document.getElementsByClassName("label")[0].style.marginTop=  "0%";
			var IDHolder = document.createElement("SELECT");
			IDHolder.setAttribute("id","mySelect");
			formDiv.appendChild(IDHolder);
			document.getElementById("mySelect").style.marginTop=  "0%";
			
			document.getElementById("mySelect").onchange = function putInputval(){
				fillInputField(idName[whichTable],document.getElementById("mySelect").value);
			}
			for(let i = 0;i < jsonTableData.length;i += 1){
				var data = jsonTableData[i];
				var option = document.createElement("option");
				option.setAttribute("value", data[idName[whichTable]]);
						
			    var item = document.createTextNode(data[idName[whichTable]]);
			    option.appendChild(item);
			    document.getElementById("mySelect").appendChild(option);
			}
			
		if(whichTable == 1){
			createSelect2();
		} 
		
		createInputField(fieldNameForUpdate,placeholderForUpdate);	
		fillInputField(idName[whichTable],jsonTableData[0][idName[whichTable]]);
		
		let updateButton = document.createElement("button");
		updateButton.classList.add("submitButton");
		formDiv.appendChild(updateButton);
		document.getElementsByClassName("submitButton")[0].innerText = "UPDATE";
		document.getElementsByClassName("submitButton")[0].onclick = function updateRow() {
			var dict = {};
			var inputName = fieldNameForUpdate[whichTable];
			dict["ID"] = document.getElementById("mySelect").value;
			if(whichTable == 1){
				dict["jobId"] = document.getElementById("mySelect1").value;
			}
			for(let i = 0;i < inputName.length;i += 1){
				dict[inputName[i]] = document.getElementsByClassName("inputStyle")[i].value;
			}
	
			var xhr = new XMLHttpRequest();
			xhr.onreadystatechange = function() {
				if (this.readyState == 4 && this.status == 200) {	
					var response = this.responseText;
					var jsonObject = JSON.parse(response);
					if(jsonObject[0] == "success"){
						errorPage.style.display = "none";
						blurDiv.style.display = "none";				
						fromOption(whichTable);
						alert("Data Updated	");
					}else{
						errorPage.style.display = "block";
						printFieldError(jsonObject);
					}				
				}
			};
			let jsonstring = JSON.stringify(dict);
			let sendstr = "formData=" + encodeURIComponent(jsonstring);
			xhr.open("POST", "http://192.168.43.126:8080/supermarketapp/update_"+tableNames[whichTable]+".action?"+sendstr, true);
			xhr.send(sendstr);
		}
		createCancelButton()
	}
	
	//this method used to fill value to all input field only in update form.
	function fillInputField(idColumnName,id){
		for(let j = 0;j < jsonTableData.length;j += 1){
			if(jsonTableData[j][idColumnName] == id){
				var count = 0;
				for(let index = 0;index < document.getElementsByTagName("input").length-1;index += 1){
					var fieldName = document.getElementsByClassName("inputStyle")[index].name;
					document.getElementsByClassName("inputStyle")[index].value = jsonTableData[j][fieldName]
				}
			}
		}
	}
	
	//this method used to delete a record for a given id.
	function deleteTable(){
		errorPage.style.display = "none";
		var idName = ["customerID","employeeID","vendorID","purchaseID","","","billID"];
		var ID;
		blurDiv.style.display = "block";
		
		formDiv.innerHTML = "";
		formDiv.action = "delete_"+tableNames[whichTable]+".action";
		
		var fieldNameForDelete = [["customerID"]];
		
		let selectHolder = document.createElement("div");
		selectHolder.classList.add("selectStyle");
		formDiv.appendChild(selectHolder);
		
			var label = document.createElement("p");
			label.classList.add("label");
			formDiv.appendChild(label);
			document.getElementsByClassName("label")[0].innerText = idName[whichTable]+" : ";
					
			var IDHolder = document.createElement("SELECT");
			IDHolder.setAttribute("id","mySelect");
			formDiv.appendChild(IDHolder);
		
			for(let i = 0;i < jsonTableData.length;i += 1){
				var data = jsonTableData[i];
				var option = document.createElement("option");
				option.setAttribute("value", data[idName[whichTable]]);
						
			    var item = document.createTextNode(data[idName[whichTable]]);
			    option.appendChild(item);
			    document.getElementById("mySelect").appendChild(option);
			}
			
			
			document.getElementById("mySelect").onchange = function putInputval(){
				ID = document.getElementById("mySelect").value;
			}
			ID = document.getElementById("mySelect").value;
	
		let deleteButton = document.createElement("button");
		deleteButton.classList.add("submitButton");
		formDiv.appendChild(deleteButton);
		document.getElementsByClassName("submitButton")[0].innerText = "REMOVE";
		
		document.getElementsByClassName("submitButton")[0].onclick = function removeRow() {
			var xhr = new XMLHttpRequest();
			xhr.onreadystatechange = function() {
				if (this.readyState == 4 && this.status == 200) {
					
					var response = this.responseText;
					blurDiv.style.display = "none";
					alert("Data Removed");
					fromOption(whichTable);
				}
			};
			xhr.open("POST", "http://192.168.43.126:8080/supermarketapp/delete_"+tableNames[whichTable]+".action?ID="+ID, true);
			xhr.send();
		}	
		createCancelButton();
	}
	
	//this method used to get table data from database. 
	function getTableData() {
		var jsonObject;
		var xhr = new XMLHttpRequest();
		xhr.open("GET", "http://192.168.43.126:8080/supermarketapp/select_table_action?tableName="+tableNames[whichTable], true);
		xhr.onreadystatechange = function() {
			if (this.readyState == 4 && this.status == 200) {
				
				jsonObject = this.responseText;
				jsonObject = JSON.parse(jsonObject);
				generateTable(jsonObject);
			}
		};
		xhr.send();	
	}
	
	//this method used to create input field for add/update/delete form.
	function createInputField(FieldName,FieldPlaceholder){
		var inputName = FieldName[whichTable];
		var inputPlaceHolder = FieldPlaceholder[whichTable];
		
		for(let i=0;i < inputName.length;i += 1){
			let field = document.createElement("input");
			field.classList.add("inputStyle");		
			formDiv.appendChild(field);
			document.getElementsByClassName("inputStyle")[i].name = inputName[i];
			document.getElementsByClassName("inputStyle")[i].placeholder = inputPlaceHolder[i];
		}
	}
	
	//this method used to create cancel button.
	function createCancelButton() {
		var errorListHolder = document.getElementsByClassName('ErrorHolder')[0];	
		errorListHolder.innerHTML = "";
		let cancelButton = document.createElement("button");
		cancelButton.classList.add("submitButton");
		formDiv.appendChild(cancelButton);
		document.getElementsByClassName("submitButton")[1].innerText = "CANCEL";
		document.getElementsByClassName("submitButton")[1].onclick = function backToMainPage() {		
			document.getElementsByClassName("addCustomerButton")[0].style.display = "none";
			document.getElementsByClassName("errorPage")[0].inner
			blurDiv.style.display = "none";
			errorPage.style.display = "none";
			if(tempWhichTable != undefined){
				whichTable = tempWhichTable;
				tempWhichTable = undefined;
				add();
			}		
		}
	}
	
	
	//this method used to generate table 
	function generateTable(jsonObject){
		workSheet.innerHTML = "";
		jsonTableData = jsonObject;
		var table = document.createElement("TABLE");
		table.setAttribute("id","myTable");
		
		workSheet.appendChild(table);
		
		var header = document.createElement("TR");
		header.setAttribute("id","columnName");
		header.style.backgroundColor = "black";
		header.style.color = "white";
		table.appendChild(header);
			
		for(var columnName in jsonObject[0]){
			if(columnName != "leavingReport"){
				
				var tableData = document.createElement("TD");
				var textNode = document.createTextNode(columnName);
				if(columnName == "jobId"){textNode = document.createTextNode("jobName")}
				tableData.appendChild(textNode);
				header.appendChild(tableData);
			} 
		}
		
		for(let rowLength = 0;rowLength <= jsonObject.length;rowLength += 1){
			
			var innerJSON = jsonObject[rowLength];
			
			var tableRow = document.createElement("TR");
			table.appendChild(tableRow);
			
			for(var columnName in innerJSON){
				if(columnName != "leavingReport"){
					var tableData = document.createElement("TD");
					var textNode = document.createTextNode(innerJSON[columnName]);
					
					if(columnName == "jobId"){textNode = document.createTextNode(Object.values(jobNames[innerJSON[columnName]-1])[0]);}
					
					tableData.style.backgroundColor = "white";
					tableData.style.color = "black";
					tableData.style.fontSize = "20px";
					
					tableData.appendChild(textNode);
					tableRow.appendChild(tableData);
				}
			}			
		}
	}
	
	//this method used to print error message in form.
	function printFieldError(jsonArr) {
		var errorListHolder = document.getElementsByClassName('ErrorHolder')[0];
		errorListHolder.innerHTML = "";
		for(let i = 0;i < jsonArr.length;i += 1){
			var listItem = document.createElement('li');
			errorListHolder.appendChild(listItem);
			listItem.innerText = jsonArr[i];
		}
	}
</script>
</html>