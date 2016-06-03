<?php
	$con = mysqli_connect("localhost" , "my_user", "my_password", "my_db");
	
	$name = $_POST["name"];
	$email = $_POST["email"];
	$username = $_POST["username"];
	$password = $_POST["password"];
	
	$insertquery = mysqli_prepare($con, "INSERT INTO contacts (Name, Email, Username, Password) VALUES (?,?,?,?) ");
	mysqli_stmt_param($insertquery , "ssss" , $name , $email , $username , $password);
	mysqli_stmt_execute($insertquery);
		
	mysqli_stmt_close($insertquery);
		
	mysqli_close($con);
?>