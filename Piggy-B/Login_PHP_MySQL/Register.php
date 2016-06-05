<?php
	$con = mysqli_connect("mysql9.000webhost.com" , "a1150590_root", "123456a", "a1150590_root");
	
	$name = $_POST["name"];
	$phone = $_POST["phone"];
	$username = $_POST["username"];
	$password = $_POST["password"];
	
	$insertquery = mysqli_prepare($con, "INSERT INTO contacts (name, phone, username, password) VALUES (?,?,?,?) ");
	mysqli_stmt_bind_param($insertquery , "ssss" , $name , $phone , $username , $password);
	mysqli_stmt_execute($insertquery);
		
	mysqli_stmt_close($insertquery);
		
	mysqli_close($con);
?>