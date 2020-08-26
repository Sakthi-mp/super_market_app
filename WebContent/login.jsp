<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Login</title>
	<style type="text/css">
		body{
			background-image: url("images/loginbackground.jpeg");
			background-repeat: no-repeat;
			background-size: 100%;
			margin: 0px;
			user-select : none;
		}

		.title1{
			font-family: 'Ubuntu', sans-serif;
			margin: 4% 0px 0px 70%;
			font-size: 3em;
			font-weight: 900;
			color: #28a8f4;
			display: none;
		}
		
		.selectCatagary{
			height:35vh;
			width:40%;
			margin: 8% 0px 0px 28%;
			background-color: #ffffffbf;
			border-radius: 10px;
		}
		
		.selectCatagary > div{
		    height: 38.5%;
		    width: 40%;
		    border-radius: 10px;
		    background-color: #000000d1;
		    float: left;
		    margin: 5% 0px 0px 6%;
		    text-align: center;
		    padding-top: 16%;
		    color:white;
		    font-family: 'Ubuntu', sans-serif;
			font-size: 30px;
			font-weight: 800;
		}
		
		.selectCatagary > div:hover{
			box-shadow: 0px 0px 25px 5px #000000d1;	
			transition-duration: .2s;	
		}
		.LogInPage{
			height: 60vh;
			width: 35%;
			box-shadow: 0px 0px 10px 3px white;
			border-radius: 10px;
			background-color: white;
			margin: 2% 0px 0px 55%;
			display: none;
		}

		.inputStyle{
			height: 30px;
			width: 70%;
			border: none;
			outline: none;
			border-bottom: 3.5px solid black;			
			border-radius: 2px;
			font-family: 'Ubuntu', sans-serif;
			font-size: 20px;
			font-weight: 800;
			margin: 10% 0px 0px 15%;
		}

		.inputStyle:focus{
				border-bottom: 3px solid green;
				transition-duration: .1s;
		}

		.submit{
			height: 65px;
			width: 180px;
			font-family: 'Ubuntu', sans-serif;
			font-size: 25px;
			font-weight: 700;	
			background-color: black;
			color: white;
			margin: 30% 0px 0px 15%;
			border: none;
			outline: none;
			border-radius: 12px;
		}

		.submit:hover{
			background-color: white;
			color: black;
			font-weight: 900;
			box-shadow: 0px 0px 30px 3px black;
			transition-duration: .3s;
		}
		.errorPage{
			height: 30vh;
			width:18%;
			background-color: white;
			border-radius: 10px;
			box-shadow: 0px 0px 20px 5px white;
			position:absolute;
			margin:11% 0px 0px 31%;
			display: none;
		}
		
		.ErrorHolder{
			list-style-type: disc;
			color: red;
			font-size: 20px;
			font-weight: 900;
			font-family: 'Ubuntu', sans-serif;
			line-height: 30px;			
		}

	</style>
</head>
<body>
	<h1 class="title1">Log In</h1>
	<div class="selectCatagary">
		<div onclick="showLoginForm(0);">ADMIN</div>
		<div onclick="showLoginForm(1);">EMPLOYEE</div>
	</div>
	
	<div class="errorPage">
		<ul class="ErrorHolder"></ul>
	</div>
	
	<div class="LogInPage"> </div>	
</body>
<script type="text/javascript">
	var loginDiv = document.getElementsByClassName("LogInPage")[0];
	var errorPage = document.getElementsByClassName("errorPage")[0];
	var whichCategary;
	var inputName = [["adminName","adminPassWord"],
		 			 ["employeeID","phoneNumber"]];
	
	var placeHolder = [["Enter Admin Name","Enter passWord"],
					   ["Enter Employee ID","Enter phoneNumber"]];
	
	function showLoginForm(Categary) {
		loginDiv.innerHTML = "";
		whichCategary = Categary;
		document.getElementsByClassName("title1")[0].style.display = " block";
		document.getElementsByClassName("selectCatagary")[0].style.display = "none";
		loginDiv.style.display = "block";
		
		for (var i = 0; i < inputName[whichCategary].length; i++) {
			let field = document.createElement("input");
			field.classList.add("inputStyle");		
			loginDiv.appendChild(field);
			document.getElementsByClassName("inputStyle")[i].name = inputName[whichCategary][i];
			document.getElementsByClassName("inputStyle")[i].placeholder = placeHolder[whichCategary][i];
		}
		
		if(Categary == 0){
			document.getElementsByClassName("inputStyle")[1].type = "password";
		}
		var loginButton = document.createElement("button");
		loginButton.classList.add("submit");
		loginDiv.appendChild(loginButton);
		document.getElementsByClassName("submit")[0].innerText = "Login";
		document.getElementsByClassName("submit")[0].onclick = function login() {
			loginToIndex();
		}
		
		var backButton = document.createElement("button");
		backButton.classList.add("submit");
		loginDiv.appendChild(backButton);
		document.getElementsByClassName("submit")[1].innerText = "Back";
		document.getElementsByClassName("submit")[1].onclick = function back() {
			BackToCatagery();
		}
	}
	
	function BackToCatagery() {
		document.getElementsByClassName("title1")[0].style.display = " none";
		loginDiv.style.display = "none";
		document.getElementsByClassName("selectCatagary")[0].style.display = "block";
		errorPage.style.display = "none";
	}
	
	function loginToIndex() {
		var dict={};
		var jsonObject;
		for (var i = 0; i < inputName[whichCategary].length; i++) {
			dict[inputName[whichCategary][i]] = document.getElementsByClassName("inputStyle")[i].value;
		}
		console.log(dict);
		
		var xhr = new XMLHttpRequest();		
		xhr.onreadystatechange = function() {
			if (this.readyState == 4 && this.status == 200) {
				
				var response = this.responseText;
				jsonObject = JSON.parse(response);
				console.log(jsonObject);
				if(jsonObject[0] == "success"){
					errorPage.style.display = "none";
					window.location = "http://192.168.43.126:8080/supermarketapp/index.jsp"
				}else{
					errorPage.style.display = "block";
					printFieldError(jsonObject);
				}				
			}
		};
		let jsonstring = JSON.stringify(dict);
		let sendstr = "formData=" + encodeURIComponent(jsonstring);
		let actionClass = (whichCategary == 0) ? "login_admin" : "login_employee";
		xhr.open("POST", "http://192.168.43.126:8080/supermarketapp/"+actionClass+".action?"+sendstr, true);
		xhr.send(sendstr);
	}
		
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